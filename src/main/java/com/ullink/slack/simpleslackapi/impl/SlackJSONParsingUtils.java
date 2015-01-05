package com.ullink.slack.simpleslackapi.impl;

import java.util.Map;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import com.ullink.slack.simpleslackapi.SlackUser;

class SlackJSONParsingUtils
{

    private SlackJSONParsingUtils()
    {
        // Helper class
    }

    static final SlackUserImpl buildSlackUser(JSONObject jsonUser)
    {
        String id = (String) jsonUser.get("id");
        String name = (String) jsonUser.get("name");
        String realName = (String) jsonUser.get("real_name");
        Boolean deleted = (Boolean) jsonUser.get("deleted");
        JSONObject profileJSON = (JSONObject) jsonUser.get("profile");
        String email = (String) profileJSON.get("email");
        return new SlackUserImpl(id, name, realName, email, deleted);
    }

    static final SlackBotImpl buildSlackBot(JSONObject jsonBot)
    {
        String id = (String) jsonBot.get("id");
        String name = (String) jsonBot.get("name");
        Boolean deleted = (Boolean) jsonBot.get("deleted");
        return new SlackBotImpl(id, name, deleted);
    }

    static final SlackChannelImpl buildSlackChannel(JSONObject jsonChannel, Map<String, SlackUser> knownUsersById)
    {
        String id = (String) jsonChannel.get("id");
        String name = (String) jsonChannel.get("name");
        System.out.println(name);
        String topic = null; // TODO
        String purpose = null; // TODO
        SlackChannelImpl toReturn = new SlackChannelImpl(id, name, topic, purpose);
        JSONArray membersJson = (JSONArray) jsonChannel.get("members");
        if (membersJson != null)
        {
            for (Object jsonMembersObject : membersJson)
            {
                String memberId = (String) jsonMembersObject;
                SlackUser user = knownUsersById.get(memberId);
                toReturn.addUser(user);
            }
        }
        return toReturn;
    }

}
