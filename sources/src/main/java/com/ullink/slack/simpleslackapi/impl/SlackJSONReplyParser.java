package com.ullink.slack.simpleslackapi.impl;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.ullink.slack.simpleslackapi.replies.*;
import com.ullink.slack.simpleslackapi.SlackSession;

class SlackJSONReplyParser
{
    static ParsedSlackReply decode(JsonObject obj, SlackSession session)
    {
        Boolean ok = obj.get("ok").getAsBoolean();
        String error = null;
        if (obj.get("error") != null)
        {
            error = obj.get("error").getAsString();
        }
        if (obj.get("presence") != null)
        {
            return new SlackUserPresenceReply(ok, error,"active".equals(obj.get("presence").getAsString()));
        }

        if (isMpim(obj) || isIm(obj) || isChannel(obj) || isGroup(obj)) {
            return buildSlackChannelReply(ok,error,obj,session);
        }

        if(isMessageReply(obj)) {
            String timestamp = GsonHelper.getStringOrNull(obj.get("ts"));
            return new SlackMessageReply(ok, error, GsonHelper.getLongOrDefaultValue(obj.get("reply_to"),-1l), timestamp);
        }

        if (isEmojiReply(obj)) {
            String timestamp = GsonHelper.getStringOrNull(obj.get("cache_ts"));
            return new EmojiSlackReply(ok, error, SlackJSONMessageParser.extractEmojisFromMessageJSON(obj.get("emoji").getAsJsonObject()), timestamp);
        }

        if (ok == null) {
            //smelly reply
            ok = Boolean.FALSE;
        }
        return new SlackReplyImpl(ok,error);
    }

    private static SlackChannelReply buildSlackChannelReply(Boolean ok, String error, JsonObject obj, SlackSession session) {
        if (obj.get("id") != null) {
            return new SlackChannelReply(ok,error, session.findChannelById(obj.get("id").getAsString()));
        }

        JsonElement channelObj = obj.get("channel");
        if (channelObj == null) {
            channelObj = obj.get("group");
        }

        String id = channelObj.getAsJsonObject().get("id").getAsString();
        return new SlackChannelReply(ok,error, session.findChannelById(id));
    }

    private static boolean isMessageReply(JsonObject obj)
    {
        return obj.get("ts") != null;
    }
    
    private static boolean isMpim(JsonObject obj) {
        return GsonHelper.getBooleanOrDefaultValue(obj.get("is_mpim"),false);
    }

    private static boolean isIm(JsonObject obj) {
        return GsonHelper.getBooleanOrDefaultValue(obj.get("is_im"),false);
    }

    private static boolean isChannel(JsonObject obj) {
        JsonElement channel = obj.get("channel");
        return channel != null && channel.isJsonObject();
    }

    private static boolean isGroup(JsonObject obj)
    {
        if (obj.get("is_group") != null) {
            return obj.get("is_group").getAsBoolean();
        }
        JsonElement group = obj.get("group");
        return group != null && group.isJsonObject();
    }

    private static boolean isEmojiReply(JsonObject obj) {
        JsonElement emoji = obj.get("emoji");
        return emoji != null && emoji.isJsonObject();
    }

}
