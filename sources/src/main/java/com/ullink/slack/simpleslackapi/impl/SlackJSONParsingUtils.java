package com.ullink.slack.simpleslackapi.impl;

import com.ullink.slack.simpleslackapi.SlackIntegration;
import com.ullink.slack.simpleslackapi.SlackPersona;
import com.ullink.slack.simpleslackapi.SlackTeam;
import com.ullink.slack.simpleslackapi.SlackUser;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

import java.util.Map;

class SlackJSONParsingUtils {

    private SlackJSONParsingUtils() {
        // Helper class
    }

    static final SlackUser buildSlackUser(JSONObject jsonUser)
    {
        String id = (String) jsonUser.get("id"); //userSkype, userTitle, userPhone
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
        String skype = "";
        String title = "";
        String phone = "";
        if (profileJSON != null)
        {
            email = (String) profileJSON.get("email");
            skype = (String) profileJSON.get("skype");
            title = (String) profileJSON.get("title");
            phone = (String) profileJSON.get("phone");
        }

        String presence = (String) jsonUser.get("presence");
        SlackPersona.SlackPresence slackPresence = SlackPersona.SlackPresence.UNKNOWN;
        if ("active".equals(presence))
        {
            slackPresence = SlackPersona.SlackPresence.ACTIVE;
        }
        if ("away".equals(presence))
        {
            slackPresence = SlackPersona.SlackPresence.AWAY;
        }
        return new SlackUserImpl(id, name, realName, email, skype, title, phone, deleted, admin, owner, primaryOwner, restricted, ultraRestricted, bot, tz, tzLabel, tzOffset == null ? null : new Integer(tzOffset.intValue()), slackPresence);
    }

    private static Boolean ifNullFalse(JSONObject jsonUser, String field) {
        Boolean deleted = (Boolean) jsonUser.get(field);
        if (deleted == null) {
            deleted = false;
        }
        return deleted;
    }

    static final SlackChannelImpl buildSlackChannel(JSONObject jsonChannel, Map<String, SlackUser> knownUsersById) {
        String id = (String) jsonChannel.get("id");
        String name = (String) jsonChannel.get("name");

        String topic = null;
        if (jsonChannel.containsKey("topic")) {
            JSONObject jsonTopic = (JSONObject)jsonChannel.get("topic");
            topic = (String)jsonTopic.get("value");
        }

        String purpose = null;
        if (jsonChannel.containsKey("purpose")) {
            JSONObject jsonPurpose = (JSONObject)jsonChannel.get("purpose");
            purpose = (String) jsonPurpose.get("value");
        }

        boolean isMember = false;
        if (jsonChannel.containsKey("is_member")) {
            isMember = (boolean)jsonChannel.get("is_member");
        }

        SlackChannelImpl toReturn = new SlackChannelImpl(id, name, topic, purpose, false, isMember);
        JSONArray membersJson = (JSONArray) jsonChannel.get("members");
        if (membersJson != null) {
            for (Object jsonMembersObject : membersJson) {
                String memberId = (String) jsonMembersObject;
                SlackUser user = knownUsersById.get(memberId);
                toReturn.addUser(user);
            }
        }
        return toReturn;
    }

    static final SlackChannelImpl buildSlackImChannel(JSONObject jsonChannel, Map<String, SlackUser> knownUsersById) {
        String id = (String) jsonChannel.get("id");
        SlackChannelImpl toReturn = new SlackChannelImpl(id, null, null, null, true, false);
        String memberId = (String) jsonChannel.get("user");
        SlackUser user = knownUsersById.get(memberId);
        toReturn.addUser(user);
        return toReturn;
    }

    static final SlackTeam buildSlackTeam(JSONObject jsonTeam) {
        String id = (String) jsonTeam.get("id");
        String name = (String) jsonTeam.get("name");
        String domain = (String) jsonTeam.get("domain");
        return new SlackTeamImpl(id, name, domain);
    }

    static final SlackIntegration buildSlackIntegration(JSONObject jsonIntegration) {
        String id = (String) jsonIntegration.get("id");
        String name = (String) jsonIntegration.get("name");
        boolean deleted = ifNullFalse(jsonIntegration, "deleted");
        return new SlackIntegrationImpl(id, name, deleted);
    }
}
