package com.ullink.slack.simpleslackapi.impl;

import java.util.HashMap;
import java.util.Map;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.ullink.slack.simpleslackapi.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


class SlackJSONSessionStatusParser {
    private static final Logger             LOGGER       = LoggerFactory.getLogger(SlackJSONSessionStatusParser.class);

    private final Map<String, SlackChannel>     channels     = new HashMap<>();
    private final Map<String, SlackUser>        users        = new HashMap<>();
    private final Map<String, SlackIntegration> integrations = new HashMap<>();

    private SlackPersona sessionPersona;

    private SlackTeam team;

    private String                    webSocketURL;

    private final String toParse;

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
    
    void parse()
    {
        LOGGER.debug("parsing session status : " + toParse);
        JsonParser parser = new JsonParser();
        JsonObject jsonResponse = parser.parse(toParse).getAsJsonObject();
        Boolean ok = jsonResponse.get("ok").getAsBoolean();
        if (Boolean.FALSE.equals(ok)) {
            error = jsonResponse.get("error").getAsString();
            return;
        }
        JsonArray usersJson = jsonResponse.get("users").getAsJsonArray();

        for (JsonElement jsonObject : usersJson)
        {
            JsonObject jsonUser = jsonObject.getAsJsonObject();
            SlackUser slackUser = SlackJSONParsingUtils.buildSlackUser(jsonUser);
            LOGGER.debug("slack user found : " + slackUser.getId());
            users.put(slackUser.getId(), slackUser);
        }

        if (jsonResponse.get("bots") != null) {
            JsonArray integrationsJson = jsonResponse.get("bots").getAsJsonArray();
            for (JsonElement jsonElement : integrationsJson)
            {
                JsonObject jsonIntegration = jsonElement.getAsJsonObject();
                SlackIntegration slackIntegration = SlackJSONParsingUtils.buildSlackIntegration(jsonIntegration);
                LOGGER.debug("slack integration found : " + slackIntegration.getId());
                integrations.put(slackIntegration.getId(), slackIntegration);
            }
        }

        JsonArray channelsJson = jsonResponse.get("channels").getAsJsonArray();

        for (JsonElement jsonObject : channelsJson)
        {
            JsonObject jsonChannel = jsonObject.getAsJsonObject();
            SlackChannel channel = SlackJSONParsingUtils.buildSlackChannel(jsonChannel);
            LOGGER.debug("slack public channel found : " + channel.getId());
            channels.put(channel.getId(), channel);
        }

        if (jsonResponse.get("groups") != null)
        {
            JsonArray groupsJson = jsonResponse.get("groups").getAsJsonArray();
            for (JsonElement jsonObject : groupsJson)
            {
                JsonObject jsonChannel = jsonObject.getAsJsonObject();
                SlackChannel channel = SlackJSONParsingUtils.buildSlackChannel(jsonChannel);
                LOGGER.debug("slack private group found : " + channel.getId());
                channels.put(channel.getId(), channel);
            }
        }

        if (jsonResponse.get("ims") != null)
        {
            JsonArray imsJson = jsonResponse.get("ims").getAsJsonArray();

            for (JsonElement jsonObject : imsJson)
            {
                JsonObject jsonChannel = jsonObject.getAsJsonObject();
                SlackChannel channel = SlackJSONParsingUtils.buildSlackImChannel(jsonChannel, users);
                LOGGER.debug("slack im channel found : " + channel.getId());
                channels.put(channel.getId(), channel);
            }
        }


        JsonObject selfJson = jsonResponse.get("self").getAsJsonObject();
        sessionPersona = SlackJSONParsingUtils.buildSlackUser(selfJson);

        JsonObject teamJson = jsonResponse.get("team").getAsJsonObject();
        team = SlackJSONParsingUtils.buildSlackTeam(teamJson);

        webSocketURL = jsonResponse.get("url").getAsString();

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
