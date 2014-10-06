package com.ullink.slack.simpleslackapi.impl;

import com.ullink.slack.simpleslackapi.SlackBot;
import com.ullink.slack.simpleslackapi.SlackChannel;
import com.ullink.slack.simpleslackapi.SlackMessage;
import com.ullink.slack.simpleslackapi.SlackMessageListener;
import com.ullink.slack.simpleslackapi.SlackSession;
import com.ullink.slack.simpleslackapi.SlackUser;
import org.glassfish.tyrus.client.ClientManager;
import org.glassfish.tyrus.client.ClientProperties;
import org.json.simple.JSONArray;
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
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

class SlackWebSocketSessionImpl extends AbstractSlackSessionImpl implements SlackSession, MessageHandler.Whole<String>
{

    private Session websocketSession;
    private String authToken;
    private Proxy.Type proxyType;
    private String proxyAddress;
    private int proxyPort;
    private Proxy outputProxy;

    SlackWebSocketSessionImpl(String authToken, Proxy.Type proxyType, String proxyAddress, int proxyPort)
    {
        this.authToken = authToken;
        this.proxyType = proxyType;
        this.proxyAddress = proxyAddress;
        this.proxyPort = proxyPort;
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
            URL url = new URL("https://slack.com/api/rtm.start?token=" + authToken);
            outputProxy = new Proxy(proxyType, InetSocketAddress.createUnresolved(proxyAddress, proxyPort));
            HttpsURLConnection con = (HttpsURLConnection) url.openConnection(outputProxy);
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
            System.out.println("JSON : " + json);
            JSONParser parser = new JSONParser();
            JSONObject jsonResponse = (JSONObject) parser.parse(json);

            JSONArray usersJson = (JSONArray) jsonResponse.get("users");

            for (Object jsonObject : usersJson)
            {
                JSONObject jsonUser = (JSONObject) jsonObject;
                String id = (String) jsonUser.get("id");
                String name = (String) jsonUser.get("name");
                String realName = (String) jsonUser.get("real_name");
                Boolean deleted = (Boolean) jsonUser.get("deleted");
                users.add(new SlackUserImpl(id, name, realName, deleted));
            }

            JSONArray botsJson = (JSONArray) jsonResponse.get("bots");

            for (Object jsonObject : botsJson)
            {
                JSONObject jsonBot = (JSONObject) jsonObject;
                String id = (String) jsonBot.get("id");
                String name = (String) jsonBot.get("name");
                Boolean deleted = (Boolean) jsonBot.get("deleted");
                bots.add(new SlackBotImpl(id, name, deleted));
            }

            JSONArray channelsJson = (JSONArray) jsonResponse.get("channels");

            for (Object jsonObject : channelsJson)
            {
                JSONObject jsonChannel = (JSONObject) jsonObject;
                String id = (String) jsonChannel.get("id");
                String name = (String) jsonChannel.get("name");
                System.out.println(name);
                String topic = null; // TODO
                String purpose = null;  // TODO
                SlackChannelImpl channel = new SlackChannelImpl(id, name, topic, purpose);
                JSONArray membersJson = (JSONArray) jsonChannel.get("members");
                if (membersJson != null)
                {
                    for (Object jsonMembersObject : membersJson)
                    {
                        String memberId = (String) jsonMembersObject;
                        SlackUser user = findUserById(memberId);
                        channel.addUser(user);
                    }
                }
                channels.add(channel);
            }

            JSONArray groupsJson = (JSONArray) jsonResponse.get("groups");

            for (Object jsonObject : groupsJson)
            {
                JSONObject jsonChannel = (JSONObject) jsonObject;
                String id = (String) jsonChannel.get("id");
                String name = (String) jsonChannel.get("name");
                System.out.println(name);
                String topic = null; // TODO
                String purpose = null;  // TODO
                SlackChannelImpl channel = new SlackChannelImpl(id, name, topic, purpose);
                JSONArray membersJson = (JSONArray) jsonChannel.get("members");
                if (membersJson != null)
                {
                    for (Object jsonMembersObject : membersJson)
                    {
                        String memberId = (String) jsonMembersObject;
                        SlackUser user = findUserById(memberId);
                        channel.addUser(user);
                    }
                }
                channels.add(channel);
            }

            String wssurl = (String) jsonResponse.get("url");

            System.out.println("URL = " + wssurl);

            ClientManager client = ClientManager.createClient();
            client.getProperties().put(ClientProperties.LOG_HTTP_UPGRADE, true);
            client.getProperties().put(ClientProperties.PROXY_URI, "http://" + proxyAddress + ":" + proxyPort);
            final MessageHandler handler = this;
            websocketSession = client.connectToServer(new Endpoint()
            {
                @Override
                public void onOpen(Session session, EndpointConfig config)
                {
                    System.out.println("opening");
                    session.addMessageHandler(handler);
                }

            }, URI.create(wssurl));
            System.out.println("connected");
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
        JSONParser parser = new JSONParser();
        JSONObject obj = null;

        try
        {
            obj = (JSONObject) parser.parse(json);
        } catch (ParseException e)
        {
            e.printStackTrace();
        }

        String messageType = (String) obj.get("type");
        if (!"message".equals(messageType))
        {
            return null;
        }

        String channelId = (String) obj.get("channel");
        String userId = (String) obj.get("user");
        String botId = (String) obj.get("bot_id");

        SlackChannel channel = channelId != null ? findChannelById(channelId) : null;
        SlackUser user = userId != null ? findUserById(userId) : null;
        SlackBot bot = botId != null ? findBotById(botId) : null;

        String text = (String) obj.get("text");
        String subtype = (String) obj.get("subtype");
        return new SlackMessageImpl(text, bot, user, channel, SlackMessage.SlackMessageSubType.getByCode(subtype));
    }
}
