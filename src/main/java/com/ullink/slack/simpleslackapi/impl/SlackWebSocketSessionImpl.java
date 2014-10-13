package com.ullink.slack.simpleslackapi.impl;

import com.ullink.slack.simpleslackapi.SlackBot;
import com.ullink.slack.simpleslackapi.SlackChannel;
import com.ullink.slack.simpleslackapi.SlackMessage;
import com.ullink.slack.simpleslackapi.SlackMessageListener;
import com.ullink.slack.simpleslackapi.SlackSession;
import com.ullink.slack.simpleslackapi.SlackUser;
import org.glassfish.tyrus.client.ClientManager;
import org.glassfish.tyrus.client.ClientProperties;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import javax.net.ssl.HttpsURLConnection;
import javax.websocket.Endpoint;
import javax.websocket.EndpointConfig;
import javax.websocket.MessageHandler;
import javax.websocket.Session;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.InetSocketAddress;
import java.net.Proxy;
import java.net.URI;
import java.net.URL;
import java.net.URLEncoder;

class SlackWebSocketSessionImpl extends AbstractSlackSessionImpl implements SlackSession, MessageHandler.Whole<String>
{

    private static final String SLACK_HTTPS_AUTH_URL = "https://slack.com/api/rtm.start?token=";

    private Session websocketSession;
    private String authToken;
    private Proxy.Type proxyType;
    private String proxyAddress;
    private int proxyPort = -1;
    private Proxy outputProxy;

    SlackWebSocketSessionImpl(String authToken, Proxy.Type proxyType, String proxyAddress, int proxyPort)
    {
        this.authToken = authToken;
        this.proxyType = proxyType;
        this.proxyAddress = proxyAddress;
        this.proxyPort = proxyPort;
        outputProxy = new Proxy(proxyType, InetSocketAddress.createUnresolved(proxyAddress, proxyPort));
    }

    SlackWebSocketSessionImpl(String authToken)
    {
        this.authToken = authToken;
    }


    @Override
    public void connect()
    {
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
            BufferedReader br =
                    new BufferedReader(
                            new InputStreamReader(con.getInputStream()));
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
            String wssurl = sessionParser.getWebSocketURL();

            System.out.println("URL = " + wssurl);

            ClientManager client = ClientManager.createClient();
            client.getProperties().put(ClientProperties.LOG_HTTP_UPGRADE, true);
            if (proxyAddress != null)
            {
                client.getProperties().put(ClientProperties.PROXY_URI, "http://" + proxyAddress + ":" + proxyPort);
            }
            final MessageHandler handler = this;
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
        } catch (Exception e)
        {
            //TODO : improve exception handling
            e.printStackTrace();
        }

    }

    @Override
    public void sendMessage(SlackChannel channel, String message, String userName, String iconURL)
    {
        try
        {
            String encodedMessage = URLEncoder.encode(message, "UTF-8");
            String encodedIcon = URLEncoder.encode(iconURL, "UTF-8");
            String encodedUserName = URLEncoder.encode(userName, "UTF-8");
            URL sendMessageURL = new URL("https://slack.com/api/chat.postMessage?token=" + authToken + "&icon_url=" + encodedIcon + "&username=" + encodedUserName + "&channel=" + channel.getId() + "&text=" + encodedMessage);
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
            BufferedReader br =
                    new BufferedReader(
                            new InputStreamReader(httpsConnection.getInputStream()));
            String input;

            while ((input = br.readLine()) != null)
            {
                strBuilder.append(input);
            }
            br.close();
        } catch (Exception e)
        {
            //TODO : improve exception handling
            e.printStackTrace();
        }

    }

    @Override
    public void onMessage(String message)
    {
        SlackMessage slackMessage = decodeMessage(message);
        if (slackMessage != null)
        {
            for (SlackMessageListener slackMessageListener : messageListeners)
            {
                slackMessageListener.onMessage(slackMessage);
            }
        }
    }


    private SlackMessage decodeMessage(String json)
    {
        SlackJSONMessageParser messageParser = new SlackJSONMessageParser(json,this);
        try
        {
            messageParser.parse();
            return messageParser.getSlackMessage();
        } catch (ParseException e)
        {
            e.printStackTrace();
        }
        return null;
    }
}
