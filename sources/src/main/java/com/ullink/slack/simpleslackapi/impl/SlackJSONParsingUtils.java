package com.ullink.slack.simpleslackapi.impl;

import java.util.Map;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import com.ullink.slack.simpleslackapi.SlackTeam;
import com.ullink.slack.simpleslackapi.SlackUser;

class SlackJSONParsingUtils
{

    private SlackJSONParsingUtils()
    {
        // Helper class
    }

    static final SlackUser buildSlackUser(JSONObject jsonUser)
    {
        String id = (String) jsonUser.get("id");
        String name = (String) jsonUser.get("name");
        String realName = (String) jsonUser.get("real_name");
        String tz = (String) jsonUser.get("tz");
        String tzLabel = (String) jsonUser.get("tz_label");
        Long tzOffset = ((Long) jsonUser.get("tz_offset"));
        Boolean deleted = ifNullFalse(jsonUser, "deleted");
        Boolean admin = ifNullFalse(jsonUser, "is_admin");
        Boolean owner = ifNullFalse(jsonUser, "is_owner");
        Boolean primaryOwner = ifNullFalse(jsonUser, "is_primary_owner");
        Boolean restricted = ifNullFalse(jsonUser, "is_restricted");
        Boolean ultraRestricted = ifNullFalse(jsonUser, "is_ultra_restricted");
        Boolean bot = ifNullFalse(jsonUser, "is_bot");
        JSONObject profileJSON = (JSONObject) jsonUser.get("profile");
        String email = "";
        if (profileJSON != null)
        {
            email = (String) profileJSON.get("email");
        }
        return new SlackUserImpl(id, name, realName, email, deleted, admin, owner, primaryOwner, restricted, ultraRestricted, bot, tz, tzLabel, tzOffset == null ? null : new Integer(tzOffset.intValue()));
    }

    private static Boolean ifNullFalse(JSONObject jsonUser, String field)
    {
        Boolean deleted = (Boolean) jsonUser.get(field);
        if (deleted == null)
        {
            deleted = false;
        }
        return deleted;
    }

    static final SlackChannelImpl buildSlackChannel(JSONObject jsonChannel, Map<String, SlackUser> knownUsersById)
    {
        String id = (String) jsonChannel.get("id");
        String name = (String) jsonChannel.get("name");
        String topic = null; // TODO
        String purpose = null; // TODO
        SlackChannelImpl toReturn = new SlackChannelImpl(id, name, topic, purpose, false);
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

    static final SlackChannelImpl buildSlackImChannel(JSONObject jsonChannel, Map<String, SlackUser> knownUsersById)
    {
        String id = (String) jsonChannel.get("id");
        SlackChannelImpl toReturn = new SlackChannelImpl(id, null, null, null, true);
        String memberId = (String) jsonChannel.get("user");
        SlackUser user = knownUsersById.get(memberId);
        toReturn.addUser(user);
        return toReturn;
    }

    static final SlackTeam buildSlackTeam(JSONObject jsonTeam)
    {
        String id = (String) jsonTeam.get("id");
        String name = (String) jsonTeam.get("name");
        String domain = (String) jsonTeam.get("domain");
        return new SlackTeamImpl(id, name, domain);
    }
}
