package com.ullink.slack.simpleslackapi.impl;

import java.io.IOException;
import java.io.InputStreamReader;
import java.net.ConnectException;
import java.net.Proxy;
import java.net.URI;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import javax.websocket.DeploymentException;
import javax.websocket.Endpoint;
import javax.websocket.EndpointConfig;
import javax.websocket.MessageHandler;
import javax.websocket.Session;
import org.apache.http.HttpHost;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
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
import com.ullink.slack.simpleslackapi.*;
import com.ullink.slack.simpleslackapi.events.*;
import com.ullink.slack.simpleslackapi.listeners.*;
import com.ullink.slack.simpleslackapi.impl.SlackChatConfiguration.Avatar;

class SlackWebSocketSessionImpl extends AbstractSlackSessionImpl implements SlackSession, MessageHandler.Whole<String>
{
    private static final String SLACK_API_HTTPS_ROOT      = "https://slack.com/api/";

    private static final String CHANNELS_LEAVE_COMMAND    = "channels.leave";

    private static final String CHANNELS_JOIN_COMMAND     = "channels.join";

    private static final String CHAT_POST_MESSAGE_COMMAND = "chat.postMessage";

    private static final String CHAT_DELETE_COMMAND       = "chat.delete";

    private static final String CHAT_UPDATE_COMMAND       = "chat.update";

    private static final String REACTIONS_ADD_COMMAND     = "reactions.add";
    
    private static final String INVITE_USER_COMMAND     = "users.admin.invite";

    public class EventDispatcher
    {

        void dispatch(SlackEvent event)
        {
            switch (event.getEventType())
            {
                case SLACK_CHANNEL_ARCHIVED:
                    dispatchImpl((SlackChannelArchived) event, channelArchiveListener);
                    break;
                case SLACK_CHANNEL_CREATED:
                    dispatchImpl((SlackChannelCreated) event, channelCreateListener);
                    break;
                case SLACK_CHANNEL_DELETED:
                    dispatchImpl((SlackChannelDeleted) event, channelDeleteListener);
                    break;
                case SLACK_CHANNEL_RENAMED:
                    dispatchImpl((SlackChannelRenamed) event, channelRenamedListener);
                    break;
                case SLACK_CHANNEL_UNARCHIVED:
                    dispatchImpl((SlackChannelUnarchived) event, channelUnarchiveListener);
                    break;
                case SLACK_GROUP_JOINED:
                    dispatchImpl((SlackGroupJoined) event, groupJoinedListener);
                    break;
                case SLACK_MESSAGE_DELETED:
                    dispatchImpl((SlackMessageDeleted) event, messageDeletedListener);
                    break;
                case SLACK_MESSAGE_POSTED:
                    dispatchImpl((SlackMessagePosted) event, messagePostedListener);
                    break;
                case SLACK_MESSAGE_UPDATED:
                    dispatchImpl((SlackMessageUpdated) event, messageUpdatedListener);
                    break;
                case SLACK_REPLY:
                    dispatchImpl((SlackReplyEvent) event, slackReplyListener);
                    break;
                case SLACK_CONNECTED:
                    dispatchImpl((SlackConnected) event, slackConnectedLinster);
                    break;
                case UNKNOWN:
                    throw new IllegalArgumentException("event not handled " + event);
            }
        }

        private <E extends SlackEvent, L extends SlackEventListener<E>> void dispatchImpl(E event, List<L> listeners)
        {
            for (L listener : listeners)
            {
                listener.onEvent(event, SlackWebSocketSessionImpl.this);
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
    private boolean                           wantDisconnect             = false;

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
        wantDisconnect = false;
        connectImpl();
        LOGGER.debug("starting connection monitoring");
        startConnectionMonitoring();
    }

    @Override
    public void disconnect()
    {
        wantDisconnect = true;
        LOGGER.debug("Disconnecting from the Slack server");
        disconnectImpl();
        stopConnectionMonitoring();
    }

    private void connectImpl() throws IOException, ClientProtocolException, ConnectException
    {
        LOGGER.info("connecting to slack");
        lastPingSent = 0;
        lastPingAck = 0;
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
        channels = sessionParser.getChannels();
        sessionPersona = sessionParser.getSessionPersona();
        LOGGER.info(users.size() + " users found on this session");
        LOGGER.info(channels.size() + " channels found on this session");
        String wssurl = sessionParser.getWebSocketURL();

        LOGGER.debug("retrieved websocket URL : " + wssurl);
        ClientManager client = ClientManager.createClient();
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
        if (websocketSession != null)
        {
            SlackConnectedImpl slackConnectedImpl = new SlackConnectedImpl(sessionPersona);
            dispatcher.dispatch(slackConnectedImpl);
            LOGGER.debug("websocket connection established");
            LOGGER.info("slack session ready");
        }
    }

    private void disconnectImpl()
    {
        if (websocketSession != null)
        {
            try
            {
                websocketSession.close();
            }
            catch (IOException ex)
            {
                // ignored.
            }
            finally
            {
                websocketSession = null;
            }
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
                        // heart beat of 30s (should be configurable in the future)
                        Thread.sleep(30000);

                        // disconnect() was called.
                        if (wantDisconnect)
                            this.interrupt();

                        if (lastPingSent != lastPingAck || websocketSession == null)
                        {
                            // disconnection happened
                            LOGGER.warn("Connection lost...");
                            try
                            {
                                if (websocketSession != null)
                                {
                                    websocketSession.close();
                                }
                            }
                            catch (IOException e)
                            {
                                LOGGER.error("exception while trying to close the websocket ", e);
                            }
                            websocketSession = null;
                            if (reconnectOnDisconnection)
                            {
                                connectImpl();
                                continue;
                            }
                            else
                            {
                                this.interrupt();
                            }
                        }
                        else
                        {
                            lastPingSent = getNextMessageId();
                            LOGGER.debug("sending ping " + lastPingSent);
                            try
                            {
                                if (websocketSession.isOpen())
                                {
                                    websocketSession.getBasicRemote().sendText("{\"type\":\"ping\",\"id\":" + lastPingSent + "}");
                                }
                                else if (reconnectOnDisconnection)
                                {
                                    connectImpl();
                                }
                            }
                            catch (IllegalStateException e)
                            {
                                // websocketSession might be closed in this case
                                if (reconnectOnDisconnection)
                                {
                                    connectImpl();
                                }
                            }
                        }
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

        if (!wantDisconnect)
            connectionMonitoringThread.start();
    }

    private void stopConnectionMonitoring()
    {
        if (connectionMonitoringThread != null)
        {
            while (true)
            {
                try
                {
                    connectionMonitoringThread.interrupt();
                    connectionMonitoringThread.join();
                    break;
                }
                catch (InterruptedException ex)
                {
                    // ouch - let's try again!
                }
            }
        }
    }

    @Override
    public SlackMessageHandle sendMessage(SlackChannel channel, String message, SlackAttachment attachment, SlackChatConfiguration chatConfiguration)
    {
        SlackMessageHandleImpl handle = new SlackMessageHandleImpl(getNextMessageId());
        Map<String, String> arguments = new HashMap<>();
        arguments.put("token", authToken);
        arguments.put("channel", channel.getId());
        arguments.put("text", message);
        if (chatConfiguration.asUser)
        {
            arguments.put("as_user", "true");
        }
        if (chatConfiguration.avatar == Avatar.ICON_URL)
        {
            arguments.put("icon_url", chatConfiguration.avatarDescription);
        }
        if (chatConfiguration.avatar == Avatar.EMOJI)
        {
            arguments.put("icon_emoji", chatConfiguration.avatarDescription);
        }
        if (chatConfiguration.userName != null)
        {
            arguments.put("username", chatConfiguration.userName);
        }
        if (attachment != null)
        {
            arguments.put("attachments", SlackJSONAttachmentFormatter.encodeAttachments(attachment).toString());
        }

        postSlackCommand(arguments, CHAT_POST_MESSAGE_COMMAND, handle);
        return handle;
    }

    @Override
    public SlackMessageHandle deleteMessage(String timeStamp, SlackChannel channel)
    {
        SlackMessageHandleImpl handle = new SlackMessageHandleImpl(getNextMessageId());
        Map<String, String> arguments = new HashMap<>();
        arguments.put("token", authToken);
        arguments.put("channel", channel.getId());
        arguments.put("ts", timeStamp);
        postSlackCommand(arguments, CHAT_DELETE_COMMAND, handle);
        return handle;
    }

    @Override
    public SlackMessageHandle updateMessage(String timeStamp, SlackChannel channel, String message)
    {
        SlackMessageHandleImpl handle = new SlackMessageHandleImpl(getNextMessageId());
        Map<String, String> arguments = new HashMap<>();
        arguments.put("token", authToken);
        arguments.put("ts", timeStamp);
        arguments.put("channel", channel.getId());
        arguments.put("text", message);
        postSlackCommand(arguments, CHAT_UPDATE_COMMAND, handle);
        return handle;
    }

    @Override
    public SlackMessageHandle addReactionToMessage(SlackChannel channel, String messageTimeStamp, String emojiCode)
    {
        SlackMessageHandleImpl handle = new SlackMessageHandleImpl(getNextMessageId());
        Map<String, String> arguments = new HashMap<>();
        arguments.put("token", authToken);
        arguments.put("channel", channel.getId());
        arguments.put("timestamp", messageTimeStamp);
        arguments.put("name", emojiCode);
        postSlackCommand(arguments, REACTIONS_ADD_COMMAND, handle);
        return handle;
    }

    @Override
    public SlackMessageHandle joinChannel(String channelName)
    {
        SlackMessageHandleImpl handle = new SlackMessageHandleImpl(getNextMessageId());
        Map<String, String> arguments = new HashMap<>();
        arguments.put("token", authToken);
        arguments.put("name", channelName);
        postSlackCommand(arguments, CHANNELS_JOIN_COMMAND, handle);
        return handle;
    }

    @Override
    public SlackMessageHandle leaveChannel(SlackChannel channel)
    {
        SlackMessageHandleImpl handle = new SlackMessageHandleImpl(getNextMessageId());
        Map<String, String> arguments = new HashMap<>();
        arguments.put("token", authToken);
        arguments.put("channel", channel.getId());
        postSlackCommand(arguments, CHANNELS_LEAVE_COMMAND, handle);
        return handle;
    }

    private void postSlackCommand(Map<String, String> params, String command, SlackMessageHandleImpl handle)
    {
        HttpClient client = getHttpClient();
        HttpPost request = new HttpPost(SLACK_API_HTTPS_ROOT + command);
        List<NameValuePair> nameValuePairList = new ArrayList<>();
        for (Map.Entry<String, String> arg : params.entrySet())
        {
            nameValuePairList.add(new BasicNameValuePair(arg.getKey(), arg.getValue()));
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

    @Override
    public SlackPersona.SlackPresence getPresence(SlackPersona persona)
    {
        HttpClient client = getHttpClient();
        HttpPost request = new HttpPost("https://slack.com/api/users.getPresence");
        List<NameValuePair> nameValuePairList = new ArrayList<>();
        nameValuePairList.add(new BasicNameValuePair("token", authToken));
        nameValuePairList.add(new BasicNameValuePair("user", persona.getId()));
        try
        {
            request.setEntity(new UrlEncodedFormEntity(nameValuePairList, "UTF-8"));
            HttpResponse response = client.execute(request);
            String jsonResponse = CharStreams.toString(new InputStreamReader(response.getEntity().getContent()));
            LOGGER.debug("PostMessage return: " + jsonResponse);
            JSONObject resultObject = parseObject(jsonResponse);

            SlackReplyImpl reply = SlackJSONReplyParser.decode(resultObject);
            if (!reply.isOk())
            {
                return SlackPersona.SlackPresence.UNKNOWN;
            }
            String presence = (String) resultObject.get("presence");

            if ("active".equals(presence))
            {
                return SlackPersona.SlackPresence.ACTIVE;
            }
            if ("away".equals(presence))
            {
                return SlackPersona.SlackPresence.AWAY;
            }
        }
        catch (Exception e)
        {
            // TODO : improve exception handling
            e.printStackTrace();
        }
        return SlackPersona.SlackPresence.UNKNOWN;
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
            if (slackEvent instanceof SlackChannelCreated)
            {
                SlackChannelCreated slackChannelCreated = (SlackChannelCreated) slackEvent;
                channels.put(slackChannelCreated.getSlackChannel().getId(), slackChannelCreated.getSlackChannel());
            }
            if (slackEvent instanceof SlackGroupJoined)
            {
                SlackGroupJoined slackGroupJoined = (SlackGroupJoined) slackEvent;
                channels.put(slackGroupJoined.getSlackChannel().getId(), slackGroupJoined.getSlackChannel());
            }
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

    @Override
    public SlackMessageHandle inviteUser(String email, String firstName, boolean setActive) {

        SlackMessageHandleImpl handle = new SlackMessageHandleImpl(getNextMessageId());
        Map<String, String> arguments = new HashMap<>();
        arguments.put("token", authToken);
        arguments.put("email", email);
        arguments.put("first_name", firstName);
        arguments.put("set_active", ""+setActive);
        postSlackCommand(arguments, INVITE_USER_COMMAND, handle);
        return handle;
    }

}
