package com.ullink.slack.simpleslackapi.impl;

import java.util.Map;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.ullink.slack.simpleslackapi.*;

class SlackJSONParsingUtils {

    private SlackJSONParsingUtils() {
        // Helper class
    }

    static SlackUser buildSlackUser(JsonObject jsonUser)
    {
        String id = GsonHelper.getStringOrNull(jsonUser.get("id"));
        String name = GsonHelper.getStringOrNull(jsonUser.get("name"));
        String realName = GsonHelper.getStringOrNull(jsonUser.get("real_name"));
        String tz = GsonHelper.getStringOrNull(jsonUser.get("tz"));
        String tzLabel = GsonHelper.getStringOrNull(jsonUser.get("tz_label"));
        JsonElement element = jsonUser.get("tz_offset");
        Integer tzOffset = null;
        if (element != null)
        {
            tzOffset = element.getAsInt();
        }
        Boolean deleted = GsonHelper.ifNullFalse(jsonUser.get("deleted"));
        Boolean admin = GsonHelper.ifNullFalse(jsonUser.get("is_admin"));
        Boolean owner = GsonHelper.ifNullFalse(jsonUser.get("is_owner"));
        Boolean primaryOwner = GsonHelper.ifNullFalse(jsonUser.get("is_primary_owner"));
        Boolean restricted = GsonHelper.ifNullFalse(jsonUser.get("is_restricted"));
        Boolean ultraRestricted = GsonHelper.ifNullFalse(jsonUser.get("is_ultra_restricted"));
        Boolean bot = GsonHelper.ifNullFalse(jsonUser.get("is_bot"));
        JsonObject profileJSON = GsonHelper.getJsonObjectOrNull(jsonUser.get("profile"));
        String email = "";
        String skype = "";
        String title = "";
        String phone = "";
        String presence = "";
        String statusTxt = "";
        String statusEmoji = "";
        if (profileJSON !=null && !profileJSON.isJsonNull())
        {
            email = GsonHelper.getStringOrNull(profileJSON.get("email"));
            skype = GsonHelper.getStringOrNull(profileJSON.get("skype"));
            title = GsonHelper.getStringOrNull(profileJSON.get("title"));
            phone = GsonHelper.getStringOrNull(profileJSON.get("phone"));
            presence = GsonHelper.getStringOrNull(profileJSON.get("presence"));
            statusTxt = GsonHelper.getStringOrNull(profileJSON.get("status_text"));
            statusEmoji = GsonHelper.getStringOrNull(profileJSON.get("status_emoji"));
        }
        SlackPresence slackPresence = SlackPresence.UNKNOWN;
        if ("active".equals(presence))
        {
            slackPresence = SlackPresence.ACTIVE;
        }
        if ("away".equals(presence))
        {
            slackPresence = SlackPresence.AWAY;
        }
        SlackStatus slackStatus = (new SlackStatus())
                .setEmoji(statusEmoji)
                .setText(statusTxt);
        SlackProfileImpl profile = SlackProfileImpl.builder()
            .presence(slackPresence)
            .realName(realName)
            .email(email)
            .phone(phone)
            .skype(skype)
            .status(slackStatus)
            .title(title)
           .build();

        return SlackPersonaImpl.builder()
            .profile(profile)
            .id(id)
            .admin(admin)
            .bot(bot)
            .deleted(deleted)
            .primaryOwner(primaryOwner)
            .owner(owner)
            .restricted(restricted)
            .timeZone(tz)
            .timeZoneLabel(tzLabel)
            .timeZoneOffset(tzOffset)
            .ultraRestricted(ultraRestricted)
            .userName(name)
            .build();
    }

    static SlackChannel buildSlackChannel(JsonObject jsonChannel, Map<String, SlackUser> knownUsersById) {
        String id =  GsonHelper.getStringOrNull(jsonChannel.get("id"));
        String name = GsonHelper.getStringOrNull(jsonChannel.get("name"));

        String topic = null;
        if (jsonChannel.has("topic")) {
            topic = GsonHelper.getStringOrNull(jsonChannel.get("topic").getAsJsonObject().get("value"));
        }

        String purpose = null;
        if (jsonChannel.has("purpose")) {
            purpose = GsonHelper.getStringOrNull(jsonChannel.get("purpose").getAsJsonObject().get("value"));
        }

        boolean isMember = false;
        if (jsonChannel.has("is_member")) {
            isMember = jsonChannel.get("is_member").getAsBoolean();
        }

        boolean isArchived = false;
        if (jsonChannel.has("is_archived")) {
            isArchived = jsonChannel.get("is_archived").getAsBoolean();
        }

        SlackChannel toReturn = new SlackChannel(id, name, topic, purpose, false, isMember, isArchived);
        JsonArray membersJson = GsonHelper.getJsonArrayOrNull(jsonChannel.get("members"));
        if (membersJson != null) {
            for (JsonElement jsonMembersObject : membersJson) {
                String memberId = jsonMembersObject.getAsString();
                SlackUser user = knownUsersById.get(memberId);
                toReturn.addUser(user);
            }
        }
        return toReturn;
    }

    static SlackChannel buildSlackImChannel(JsonObject jsonChannel, Map<String, SlackUser> knownUsersById)
    {
        String id = GsonHelper.getStringOrNull(jsonChannel.get("id"));
        SlackChannel toReturn = new SlackChannel(id, null, null, null, true, false, false);
        String memberId = GsonHelper.getStringOrNull(jsonChannel.get("user"));
        SlackUser user = knownUsersById.get(memberId);
        toReturn.addUser(user);
        return toReturn;
    }

    static SlackTeam buildSlackTeam(JsonObject jsonTeam)
    {
        String id = GsonHelper.getStringOrNull(jsonTeam.get("id"));
        String name = GsonHelper.getStringOrNull(jsonTeam.get("name"));
        String domain = GsonHelper.getStringOrNull(jsonTeam.get("domain"));
        return new SlackTeam(id, name, domain);
    }

    static SlackIntegration buildSlackIntegration(JsonObject jsonIntegration) {
        String id = GsonHelper.getStringOrNull(jsonIntegration.get("id"));
        String name = GsonHelper.getStringOrNull(jsonIntegration.get("name"));
        boolean deleted = GsonHelper.getBooleanOrDefaultValue(jsonIntegration.get("deleted"),false);
        return new SlackIntegrationImpl(id, name, deleted);
    }
}
