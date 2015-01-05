package com.ullink.slack.simpleslackapi.impl;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.InetSocketAddress;
import java.net.Proxy;
import java.net.URI;
import java.net.URL;
import java.net.URLEncoder;
import java.util.concurrent.TimeUnit;
import javax.net.ssl.HttpsURLConnection;
import javax.websocket.Endpoint;
import javax.websocket.EndpointConfig;
import javax.websocket.MessageHandler;
import javax.websocket.Session;
import org.glassfish.tyrus.client.ClientManager;
import org.glassfish.tyrus.client.ClientProperties;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import com.ullink.slack.simpleslackapi.SlackAttachment;
import com.ullink.slack.simpleslackapi.SlackChannel;
import com.ullink.slack.simpleslackapi.SlackGroupJoined;
import com.ullink.slack.simpleslackapi.SlackMessage;
import com.ullink.slack.simpleslackapi.SlackMessageListener;
import com.ullink.slack.simpleslackapi.SlackSession;

class SlackWebSocketSessionImpl extends AbstractSlackSessionImpl implements SlackSession, MessageHandler.Whole<String>
{

    private static final Logger LOGGER                     = LoggerFactory.getLogger(SlackWebSocketSessionImpl.class);

    private static final String SLACK_HTTPS_AUTH_URL       = "https://slack.com/api/rtm.start?token=";

    private Session             websocketSession;
    private String              authToken;
    private Proxy.Type          proxyType;
    private String              proxyAddress;
    private int                 proxyPort                  = -1;
    private Proxy               outputProxy;
    private int                 lastPingSent               = 0;
    private volatile int        lastPingAck                = 0;

    private long                messageId                  = 0;

    private long                lastConnectionTime         = -1;

    private boolean             reconnectOnDisconnection;

    private Thread              connectionMonitoringThread = null;

    SlackWebSocketSessionImpl(String authToken, Proxy.Type proxyType, String proxyAddress, int proxyPort, boolean reconnectOnDisconnection)
    {
        this.authToken = authToken;
        this.proxyType = proxyType;
        this.proxyAddress = proxyAddress;
        this.proxyPort = proxyPort;
        outputProxy = new Proxy(proxyType, InetSocketAddress.createUnresolved(proxyAddress, proxyPort));
        this.reconnectOnDisconnection = reconnectOnDisconnection;
    }

    SlackWebSocketSessionImpl(String authToken, boolean reconnectOnDisconnection)
    {
        this.authToken = authToken;
        this.reconnectOnDisconnection = reconnectOnDisconnection;
    }

    @Override
    public void connect()
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
        try
        {
            URL url = new URL(SLACK_HTTPS_AUTH_URL + authToken);
            HttpsURLConnection con;
            if (outputProxy != null)
            {
                con = (HttpsURLConnection) url.openConnection(outputProxy);
            }
            else
            {
                con = (HttpsURLConnection) url.openConnection();
            }
            StringBuilder strBuilder = new StringBuilder();
            BufferedReader br = new BufferedReader(new InputStreamReader(con.getInputStream()));
            String input;

            while ((input = br.readLine()) != null)
            {
                strBuilder.append(input);
            }
            br.close();
            String json = strBuilder.toString();
            SlackJSONSessionStatusParser sessionParser = new SlackJSONSessionStatusParser(json);
            sessionParser.parse();
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
            websocketSession = client.connectToServer(new Endpoint()
            {
                @Override
                public void onOpen(Session session, EndpointConfig config)
                {
                    session.addMessageHandler(handler);
                }

            }, URI.create(wssurl));
            for (SlackMessageListener slackMessageListener : messageListeners)
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
        catch (Exception e)
        {
            // TODO : improve exception handling
            e.printStackTrace();
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
                            lastPingSent++;
                            if (lastPingSent > 9999)
                            {
                                lastPingSent = 0;
                            }
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
    public void sendMessage(SlackChannel channel, String message, SlackAttachment attachment, String userName, String iconURL)
    {
        try
        {
            String encodedMessage = URLEncoder.encode(message, "UTF-8");
            String urlValue = "https://slack.com/api/chat.postMessage?token=" + authToken;
            if (iconURL != null)
            {
                urlValue += "&icon_url=" + URLEncoder.encode(iconURL, "UTF-8");
            }
            if (userName != null)
            {
                urlValue += "&username=" + URLEncoder.encode(userName, "UTF-8");
            }
            urlValue += "&channel=" + channel.getId() + "&text=" + encodedMessage;
            if (attachment != null)
            {
                String encodedAttachments = JSONArray.toJSONString(SlackJSONAttachmentFormatter.encodeAttachments(attachment));
                urlValue += "&attachments=" + URLEncoder.encode(encodedAttachments, "UTF-8");
            }
            URL sendMessageURL = new URL(urlValue);

            HttpsURLConnection httpsConnection;
            if (outputProxy != null)
            {
                httpsConnection = (HttpsURLConnection) sendMessageURL.openConnection(outputProxy);
            }
            else
            {
                httpsConnection = (HttpsURLConnection) sendMessageURL.openConnection();
            }
            StringBuilder strBuilder = new StringBuilder();
            BufferedReader br = new BufferedReader(new InputStreamReader(httpsConnection.getInputStream()));
            String input;

            while ((input = br.readLine()) != null)
            {
                strBuilder.append(input);
            }
            LOGGER.debug("PostMessage return: " + strBuilder.toString());
            br.close();
        }
        catch (Exception e)
        {
            // TODO : improve exception handling
            e.printStackTrace();
        }

    }

    @Override
    public void sendMessageOverWebSocket(SlackChannel channel, String message, SlackAttachment attachment)
    {
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
    }

    private long getMessageId()
    {
        return messageId++;
    }

    @Override
    public void onMessage(String message)
    {
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

            String type = (String) object.get("type");
            if ("message".equals(type))
            {
                SlackMessage slackMessage = SlackJSONMessageParser.decode(this, object);
                if (slackMessage != null)
                {
                    for (SlackMessageListener slackMessageListener : messageListeners)
                    {
                        slackMessageListener.onMessage(slackMessage);
                    }
                }
            }
            else if ("group_joined".equals(type))
            {
                SlackGroupJoined groupJoined = parseGroupJoined(object);
                if (groupJoined != null)
                {
                    SlackChannel channel = groupJoined.getSlackChannel();
                    if (channel != null)
                    {
                        channels.put(channel.getId(), channel);
                    }
                }
            }
        }
    }

    private SlackGroupJoined parseGroupJoined(JSONObject object)
    {
        JSONObject channel = (JSONObject) object.get("channel");
        SlackChannel slackChannel = SlackJSONParsingUtils.buildSlackChannel(channel, users);
        return new SlackGroupJoinedImpl(slackChannel);
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

    private SlackMessage decodeMessage(String json)
    {
        SlackJSONMessageParser messageParser = new SlackJSONMessageParser(json, this);
        try
        {
            messageParser.parse();
            return messageParser.getSlackMessage();
        }
        catch (ParseException e)
        {
            e.printStackTrace();
        }
        return null;
    }
}
