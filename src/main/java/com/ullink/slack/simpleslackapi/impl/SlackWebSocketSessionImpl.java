package com.ullink.slack.simpleslackapi.impl;

import java.io.IOException;
import java.io.InputStreamReader;
import java.net.ConnectException;
import java.net.Proxy;
import java.net.URI;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.TimeUnit;
import javax.websocket.DeploymentException;
import javax.websocket.Endpoint;
import javax.websocket.EndpointConfig;
import javax.websocket.MessageHandler;
import javax.websocket.Session;
import org.apache.http.HttpHost;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.impl.conn.DefaultProxyRoutePlanner;
import org.apache.http.message.BasicNameValuePair;
import org.glassfish.tyrus.client.ClientManager;
import org.glassfish.tyrus.client.ClientProperties;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import com.google.common.io.CharStreams;
import com.ullink.slack.simpleslackapi.SlackAttachment;
import com.ullink.slack.simpleslackapi.SlackChannel;
import com.ullink.slack.simpleslackapi.SlackMessage;
import com.ullink.slack.simpleslackapi.SlackMessageHandle;
import com.ullink.slack.simpleslackapi.SlackMessageListener;
import com.ullink.slack.simpleslackapi.SlackSession;
import com.ullink.slack.simpleslackapi.events.SlackChannelArchived;
import com.ullink.slack.simpleslackapi.events.SlackChannelCreated;
import com.ullink.slack.simpleslackapi.events.SlackChannelDeleted;
import com.ullink.slack.simpleslackapi.events.SlackChannelUnarchived;
import com.ullink.slack.simpleslackapi.events.SlackEvent;
import com.ullink.slack.simpleslackapi.events.SlackGroupJoined;
import com.ullink.slack.simpleslackapi.events.SlackMessageDeleted;
import com.ullink.slack.simpleslackapi.events.SlackMessageUpdated;
import com.ullink.slack.simpleslackapi.events.SlackReplyEvent;
import com.ullink.slack.simpleslackapi.impl.SlackChatConfiguration.Avatar;
import com.ullink.slack.simpleslackapi.listeners.SlackChannelArchiveListener;
import com.ullink.slack.simpleslackapi.listeners.SlackChannelCreateListener;
import com.ullink.slack.simpleslackapi.listeners.SlackChannelDeleteListener;
import com.ullink.slack.simpleslackapi.listeners.SlackChannelUnarchiveListener;
import com.ullink.slack.simpleslackapi.listeners.SlackMessageDeletedListener;
import com.ullink.slack.simpleslackapi.listeners.SlackMessageSentListener;
import com.ullink.slack.simpleslackapi.listeners.SlackMessageUpdatedListener;

class SlackWebSocketSessionImpl extends AbstractSlackSessionImpl implements SlackSession, MessageHandler.Whole<String>
{
    public class EventDispatcher
    {

        void dispatch(SlackEvent event)
        {
            throw new IllegalArgumentException("unsupported event type : " + event.getClass());
        }

        void dispatch(SlackMessage event)
        {
            for (SlackMessageListener listener : oldMessageListeners)
            {
                listener.onMessage(event);
            }
            for (SlackMessageSentListener listener : messageSentListeners)
            {
                listener.onEvent(event, SlackWebSocketSessionImpl.this);
            }
        }

        void dispatch(SlackMessageDeleted event)
        {
            for (SlackMessageDeletedListener listener : messageDeletedListeners)
            {
                listener.onEvent(event, SlackWebSocketSessionImpl.this);
            }
        }

        void dispatch(SlackMessageUpdated event)
        {
            for (SlackMessageUpdatedListener listener : messageUpdatedListeners)
            {
                listener.onEvent(event, SlackWebSocketSessionImpl.this);
            }
        }

        void dispatch(SlackChannelArchived event)
        {
            for (SlackChannelArchiveListener listener : channelArchivedListener)
            {
                listener.onEvent(event, SlackWebSocketSessionImpl.this);
            }
        }

        void dispatch(SlackChannelUnarchived event)
        {
            for (SlackChannelUnarchiveListener listener : channelUnarchivedListener)
            {
                listener.onEvent(event, SlackWebSocketSessionImpl.this);
            }
        }

        void dispatch(SlackChannelCreated event)
        {
            for (SlackChannelCreateListener listener : channelCreatedListener)
            {
                listener.onEvent(event, SlackWebSocketSessionImpl.this);
            }
        }

        void dispatch(SlackChannelDeleted event)
        {
            for (SlackChannelDeleteListener listener : channelDeletedListener)
            {
                listener.onEvent(event, SlackWebSocketSessionImpl.this);
            }
        }

        void dispatch(SlackReplyEvent event)
        {
            SlackMessageHandleImpl handle = pendingMessageMap.get(event.getReplyTo());
            handle.setSlackReply(event);
            pendingMessageMap.remove(event.getReplyTo());
        }

        void dispatch(SlackGroupJoined event)
        {
            SlackChannel channel = event.getSlackChannel();
            if (channel != null)
            {
                channels.put(channel.getId(), channel);
            }
        }

    }

    private static final Logger               LOGGER                     = LoggerFactory.getLogger(SlackWebSocketSessionImpl.class);

    private static final String               SLACK_HTTPS_AUTH_URL       = "https://slack.com/api/rtm.start?token=";

    private Session                           websocketSession;
    private String                            authToken;
    private String                            proxyAddress;
    private int                               proxyPort                  = -1;
    HttpHost                                  proxyHost;
    private long                              lastPingSent               = 0;
    private volatile long                     lastPingAck                = 0;

    private long                              messageId                  = 0;

    private long                              lastConnectionTime         = -1;

    private boolean                           reconnectOnDisconnection;

    private Map<Long, SlackMessageHandleImpl> pendingMessageMap          = new ConcurrentHashMap<Long, SlackMessageHandleImpl>();

    private Thread                            connectionMonitoringThread = null;
    private EventDispatcher                   dispatcher                 = new EventDispatcher();

    SlackWebSocketSessionImpl(String authToken, Proxy.Type proxyType, String proxyAddress, int proxyPort, boolean reconnectOnDisconnection)
    {
        this.authToken = authToken;
        this.proxyAddress = proxyAddress;
        this.proxyPort = proxyPort;
        this.proxyHost = new HttpHost(proxyAddress, proxyPort);
        this.reconnectOnDisconnection = reconnectOnDisconnection;
    }

    SlackWebSocketSessionImpl(String authToken, boolean reconnectOnDisconnection)
    {
        this.authToken = authToken;
        this.reconnectOnDisconnection = reconnectOnDisconnection;
    }

    @Override
    public void connect() throws IOException
    {
        long currentTime = System.nanoTime();
        while (lastConnectionTime >= 0 && currentTime - lastConnectionTime < TimeUnit.SECONDS.toNanos(30))
        {
            LOGGER.warn("Previous connection was made less than 30s ago, waiting 10s before trying to connect");
            try
            {
                Thread.sleep(10000);
                currentTime = System.nanoTime();
            }
            catch (InterruptedException e)
            {
                // TODO: handle this case
            }
        }
        LOGGER.info("connecting to slack");
        lastConnectionTime = currentTime;
        HttpClient httpClient = getHttpClient();
        HttpGet request = new HttpGet(SLACK_HTTPS_AUTH_URL + authToken);
        HttpResponse response;
        response = httpClient.execute(request);
        LOGGER.debug(response.getStatusLine().toString());
        String jsonResponse = CharStreams.toString(new InputStreamReader(response.getEntity().getContent()));
        SlackJSONSessionStatusParser sessionParser = new SlackJSONSessionStatusParser(jsonResponse);
        try
        {
            sessionParser.parse();
        }
        catch (ParseException e1)
        {
            LOGGER.error(e1.toString());
        }
        if (sessionParser.getError() != null)
        {
            LOGGER.error("Error during authentication : " + sessionParser.getError());
            throw new ConnectException(sessionParser.getError());
        }
        users = sessionParser.getUsers();
        bots = sessionParser.getBots();
        channels = sessionParser.getChannels();
        LOGGER.info(users.size() + " users found on this session");
        LOGGER.info(bots.size() + " bots found on this session");
        LOGGER.info(channels.size() + " channels found on this session");
        String wssurl = sessionParser.getWebSocketURL();

        LOGGER.debug("retrieved websocket URL : " + wssurl);
        ClientManager client = ClientManager.createClient();
        client.getProperties().put(ClientProperties.LOG_HTTP_UPGRADE, true);
        if (proxyAddress != null)
        {
            client.getProperties().put(ClientProperties.PROXY_URI, "http://" + proxyAddress + ":" + proxyPort);
        }
        final MessageHandler handler = this;
        LOGGER.debug("initiating connection to websocket");
        try
        {
            websocketSession = client.connectToServer(new Endpoint()
            {
                @Override
                public void onOpen(Session session, EndpointConfig config)
                {
                    session.addMessageHandler(handler);
                }

            }, URI.create(wssurl));
        }
        catch (DeploymentException e)
        {
            LOGGER.error(e.toString());
        }
        for (SlackMessageListener slackMessageListener : oldMessageListeners)
        {
            slackMessageListener.onSessionLoad(this);
        }
        if (websocketSession != null)
        {
            LOGGER.debug("websocket connection established");
            LOGGER.info("slack session ready");
        }
        if (connectionMonitoringThread == null)
        {
            LOGGER.debug("starting connection monitoring");
            startConnectionMonitoring();
        }
    }

    private void startConnectionMonitoring()
    {
        connectionMonitoringThread = new Thread()
        {
            @Override
            public void run()
            {
                LOGGER.debug("monitoring thread started");
                while (true)
                {
                    try
                    {
                        if (lastPingSent != lastPingAck)
                        {
                            // disconnection happened
                            LOGGER.warn("Connection lost...");
                            websocketSession.close();
                            lastPingSent = 0;
                            lastPingAck = 0;
                            if (reconnectOnDisconnection)
                            {
                                connect();
                                continue;
                            }
                        }
                        else
                        {
                            lastPingSent = getNextMessageId();
                            LOGGER.debug("sending ping " + lastPingSent);
                            websocketSession.getBasicRemote().sendText("{\"type\":\"ping\",\"id\":" + lastPingSent + "}");
                        }
                        Thread.sleep(30000);
                    }
                    catch (InterruptedException e)
                    {
                        break;
                    }
                    catch (IOException e)
                    {
                        LOGGER.error("unexpected exception on monitoring thread ", e);
                    }
                }
                LOGGER.debug("monitoring thread stopped");
            }
        };
        connectionMonitoringThread.start();
    }

    @Override
    public SlackMessageHandle sendMessage(SlackChannel channel, String message, SlackAttachment attachment, SlackChatConfiguration chatConfiguration)
    {
        SlackMessageHandleImpl handle = new SlackMessageHandleImpl(getNextMessageId());
        HttpClient client = getHttpClient();
        HttpPost request = new HttpPost("https://slack.com/api/chat.postMessage");
        List<NameValuePair> nameValuePairList = new ArrayList<>();
        nameValuePairList.add(new BasicNameValuePair("token", authToken));
        nameValuePairList.add(new BasicNameValuePair("channel", channel.getId()));
        if (chatConfiguration.asUser)
        {
            nameValuePairList.add(new BasicNameValuePair("as_user", "true"));
        }
        nameValuePairList.add(new BasicNameValuePair("text", message));
        if (chatConfiguration.avatar == Avatar.ICON_URL)
        {
            nameValuePairList.add(new BasicNameValuePair("icon_url", chatConfiguration.avatarDescription));
        }
        if (chatConfiguration.avatar == Avatar.EMOJI)
        {
            nameValuePairList.add(new BasicNameValuePair("icon_emoji", chatConfiguration.avatarDescription));
        }
        if (chatConfiguration.userName != null)
        {
            nameValuePairList.add(new BasicNameValuePair("username", chatConfiguration.userName));
        }
        if (attachment != null)
        {
            nameValuePairList.add(new BasicNameValuePair("attachments", SlackJSONAttachmentFormatter.encodeAttachments(attachment).toString()));
        }
        try
        {
            request.setEntity(new UrlEncodedFormEntity(nameValuePairList, "UTF-8"));
            HttpResponse response = client.execute(request);
            String jsonResponse = CharStreams.toString(new InputStreamReader(response.getEntity().getContent()));
            LOGGER.debug("PostMessage return: " + jsonResponse);
            SlackReplyImpl reply = SlackJSONReplyParser.decode(parseObject(jsonResponse));
            handle.setSlackReply(reply);
        }
        catch (Exception e)
        {
            // TODO : improve exception handling
            e.printStackTrace();
        }
        return handle;
    }

    @Override
    public SlackMessageHandle deleteMessage(String timeStamp, SlackChannel channel)
    {
        SlackMessageHandleImpl handle = new SlackMessageHandleImpl(getNextMessageId());
        HttpClient client = getHttpClient();
        HttpPost request = new HttpPost("https://slack.com/api/chat.delete");
        List<NameValuePair> nameValuePairList = new ArrayList<>();
        nameValuePairList.add(new BasicNameValuePair("token", authToken));
        nameValuePairList.add(new BasicNameValuePair("channel", channel.getId()));
        nameValuePairList.add(new BasicNameValuePair("ts", timeStamp));
        try
        {
            request.setEntity(new UrlEncodedFormEntity(nameValuePairList, "UTF-8"));
            HttpResponse response = client.execute(request);
            String jsonResponse = CharStreams.toString(new InputStreamReader(response.getEntity().getContent()));
            LOGGER.debug("PostMessage return: " + jsonResponse);
            SlackReplyImpl reply = SlackJSONReplyParser.decode(parseObject(jsonResponse));
            handle.setSlackReply(reply);
        }
        catch (Exception e)
        {
            // TODO : improve exception handling
            e.printStackTrace();
        }
        return handle;
    }

    @Override
    public SlackMessageHandle updateMessage(String timeStamp, SlackChannel channel, String message)
    {
        SlackMessageHandleImpl handle = new SlackMessageHandleImpl(getNextMessageId());
        HttpClient client = getHttpClient();
        HttpPost request = new HttpPost("https://slack.com/api/chat.update");
        List<NameValuePair> nameValuePairList = new ArrayList<>();
        nameValuePairList.add(new BasicNameValuePair("token", authToken));
        nameValuePairList.add(new BasicNameValuePair("ts", timeStamp));
        nameValuePairList.add(new BasicNameValuePair("channel", channel.getId()));
        nameValuePairList.add(new BasicNameValuePair("text", message));
        try
        {
            request.setEntity(new UrlEncodedFormEntity(nameValuePairList, "UTF-8"));
            HttpResponse response = client.execute(request);
            String jsonResponse = CharStreams.toString(new InputStreamReader(response.getEntity().getContent()));
            LOGGER.debug("PostMessage return: " + jsonResponse);
            SlackReplyImpl reply = SlackJSONReplyParser.decode(parseObject(jsonResponse));
            handle.setSlackReply(reply);
        }
        catch (Exception e)
        {
            // TODO : improve exception handling
            e.printStackTrace();
        }
        return handle;
    }

    private HttpClient getHttpClient()
    {
        HttpClient client = null;
        if (proxyHost != null)
        {
            client = HttpClientBuilder.create().setRoutePlanner(new DefaultProxyRoutePlanner(proxyHost)).build();
        }
        else
        {
            client = HttpClientBuilder.create().build();
        }
        return client;
    }

    @Override
    public SlackMessageHandle sendMessageOverWebSocket(SlackChannel channel, String message, SlackAttachment attachment)
    {
        SlackMessageHandleImpl handle = new SlackMessageHandleImpl(getNextMessageId());
        try
        {
            JSONObject messageJSON = new JSONObject();
            messageJSON.put("type", "message");
            messageJSON.put("channel", channel.getId());
            messageJSON.put("text", message);
            if (attachment != null)
            {
                messageJSON.put("attachments", SlackJSONAttachmentFormatter.encodeAttachments(attachment));
            }
            websocketSession.getBasicRemote().sendText(messageJSON.toJSONString());
        }
        catch (Exception e)
        {
            // TODO : improve exception handling
            e.printStackTrace();
        }
        return handle;
    }

    private synchronized long getNextMessageId()
    {
        return messageId++;
    }

    @Override
    public void onMessage(String message)
    {
        LOGGER.debug("receiving from websocket " + message);
        if (message.contains("{\"type\":\"pong\",\"reply_to\""))
        {
            int rightBracketIdx = message.indexOf('}');
            String toParse = message.substring(26, rightBracketIdx);
            lastPingAck = Integer.parseInt(toParse);
            LOGGER.debug("pong received " + lastPingAck);
        }
        else
        {
            JSONObject object = parseObject(message);
            SlackEvent slackEvent = SlackJSONMessageParser.decode(this, object);
            dispatcher.dispatch(slackEvent);
        }
    }

    private JSONObject parseObject(String json)
    {
        JSONParser parser = new JSONParser();
        try
        {
            JSONObject object = (JSONObject) parser.parse(json);
            return object;
        }
        catch (ParseException e)
        {
            e.printStackTrace();
            return null;
        }
    }

}
