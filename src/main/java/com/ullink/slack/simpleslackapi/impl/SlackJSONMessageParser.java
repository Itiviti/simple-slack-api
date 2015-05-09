package com.ullink.slack.simpleslackapi.impl;

import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import com.ullink.slack.simpleslackapi.SlackBot;
import com.ullink.slack.simpleslackapi.SlackChannel;
import com.ullink.slack.simpleslackapi.SlackMessage;
import com.ullink.slack.simpleslackapi.SlackSession;
import com.ullink.slack.simpleslackapi.SlackUser;

class SlackJSONMessageParser
{

    static SlackMessage decode(SlackSession slackSession, JSONObject obj)
    {
        String channelId = (String) obj.get("channel");
        String userId = (String) obj.get("user");
        String botId = (String) obj.get("bot_id");
        SlackChannel channel = null;
        if (channelId != null)
        {
            if (channelId.startsWith("D"))
            {
                // direct messaging, on the fly channel creation
                channel = new SlackChannelImpl(channelId, userId != null ? userId : botId, "", "", true);
            }
            else
            {
                channel = slackSession.findChannelById(channelId);
            }
        }
        SlackUser user = userId != null ? slackSession.findUserById(userId) : null;
        SlackBot bot = botId != null ? slackSession.findBotById(botId) : null;

        String text = (String) obj.get("text");
        String subtype = (String) obj.get("subtype");
        return new SlackMessageImpl(text, bot, user, channel, SlackMessage.SlackMessageSubType.getByCode(subtype));
    }

}
