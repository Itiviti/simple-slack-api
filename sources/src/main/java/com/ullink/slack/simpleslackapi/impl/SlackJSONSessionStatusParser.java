package com.ullink.slack.simpleslackapi.impl;

import com.ullink.slack.simpleslackapi.*;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashMap;
import java.util.Map;

class SlackJSONSessionStatusParser {
    private static final Logger             LOGGER       = LoggerFactory.getLogger(SlackJSONSessionStatusParser.class);

    private Map<String, SlackChannel>       channels     = new HashMap<>();
    private Map<String, SlackUser>          users        = new HashMap<>();
    private Map<String, SlackIntegration>   integrations = new HashMap<>();

    private SlackPersona              sessionPersona;

    private SlackTeam                 team;

    private String                    webSocketURL;

    private String                    toParse;

    private String                    error;

    SlackJSONSessionStatusParser(String toParse)
    {
        this.toParse = toParse;
    }

    Map<String, SlackChannel> getChannels()
    {
        return channels;
    }

    Map<String, SlackUser> getUsers()
    {
        return users;
    }

    Map<String,SlackIntegration> getIntegrations() {
        return integrations;
    }

    public String getWebSocketURL()
    {
        return webSocketURL;
    }

    public String getError()
    {
        return error;
    }
    
    void parse() throws ParseException {
        LOGGER.debug("parsing session status : " + toParse);
        JSONParser parser = new JSONParser();
        JSONObject jsonResponse = (JSONObject) parser.parse(toParse);
        Boolean ok = (Boolean)jsonResponse.get("ok");
        if (Boolean.FALSE.equals(ok)) {
            error = (String)jsonResponse.get("error");
            return;
        }
        JSONArray usersJson = (JSONArray) jsonResponse.get("users");

        for (Object jsonObject : usersJson)
        {
            JSONObject jsonUser = (JSONObject) jsonObject;
            SlackUser slackUser = SlackJSONParsingUtils.buildSlackUser(jsonUser);
            LOGGER.debug("slack user found : " + slackUser.getId());
            users.put(slackUser.getId(), slackUser);
        }

        JSONArray integrationsJson = (JSONArray) jsonResponse.get("bots");
        if (integrationsJson != null) {
            for (Object jsonObject : integrationsJson)
            {
                JSONObject jsonIntegration = (JSONObject) jsonObject;
                SlackUser slackUser = SlackJSONParsingUtils.buildSlackUser(jsonIntegration);
                SlackIntegration slackIntegration = SlackJSONParsingUtils.buildSlackIntegration(jsonIntegration);
                LOGGER.debug("slack integration found : " + slackIntegration.getId());
                integrations.put(slackIntegration.getId(), slackIntegration);
                users.put(slackUser.getId(), slackUser);
            }
        }

        JSONArray channelsJson = (JSONArray) jsonResponse.get("channels");

        for (Object jsonObject : channelsJson)
        {
            JSONObject jsonChannel = (JSONObject) jsonObject;
            SlackChannelImpl channel = SlackJSONParsingUtils.buildSlackChannel(jsonChannel, users);
            LOGGER.debug("slack public channel found : " + channel.getId());
            channels.put(channel.getId(), channel);
        }

        JSONArray groupsJson = (JSONArray) jsonResponse.get("groups");

        for (Object jsonObject : groupsJson)
        {
            JSONObject jsonChannel = (JSONObject) jsonObject;
            SlackChannelImpl channel = SlackJSONParsingUtils.buildSlackChannel(jsonChannel, users);
            LOGGER.debug("slack private group found : " + channel.getId());
            channels.put(channel.getId(), channel);
        }

        JSONArray imsJson = (JSONArray) jsonResponse.get("ims");

        for (Object jsonObject : imsJson)
        {
            JSONObject jsonChannel = (JSONObject) jsonObject;
            SlackChannelImpl channel = SlackJSONParsingUtils.buildSlackImChannel(jsonChannel, users);
            LOGGER.debug("slack im channel found : " + channel.getId());
            channels.put(channel.getId(), channel);
        }

        JSONObject selfJson = (JSONObject) jsonResponse.get("self");
        sessionPersona = SlackJSONParsingUtils.buildSlackUser(selfJson);

        JSONObject teamJson = (JSONObject) jsonResponse.get("team");
        team = SlackJSONParsingUtils.buildSlackTeam(teamJson);

        webSocketURL = (String) jsonResponse.get("url");

    }

    public SlackPersona getSessionPersona()
    {
        return sessionPersona;
    }

    public SlackTeam getTeam()
    {
        return team;
    }
}
