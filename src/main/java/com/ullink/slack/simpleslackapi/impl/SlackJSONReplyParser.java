package com.ullink.slack.simpleslackapi.impl;

import com.ullink.slack.simpleslackapi.SlackSession;
import com.ullink.slack.simpleslackapi.replies.SlackChannelReply;
import com.ullink.slack.simpleslackapi.replies.SlackReply;
import org.json.simple.JSONObject;

class SlackJSONReplyParser
{
    static SlackReply decode(JSONObject obj, SlackSession session)
    {
        Boolean ok = (Boolean) obj.get("ok");
        String presence = (String)obj.get("presence");
        if (presence != null) {
            return new SlackUserPresenceReplyImpl(ok, "active".equals(presence));
        }
        if (isMpim(obj) || isIm(obj) || isChannel(obj) || isGroup(obj)) {
            return buildSlackChannelReply(ok,obj,session);
        }
        // that's a message reply
        Long replyTo = (Long) obj.get("reply_to");
        String timestamp = (String) obj.get("ts");
        return new SlackMessageReplyImpl(ok, replyTo != null ? replyTo : -1, timestamp);
    }

    private static SlackChannelReply buildSlackChannelReply(Boolean ok, JSONObject obj, SlackSession session) {
        String id = (String)obj.get("id");
        if (id != null) {
            return new SlackChannelReplyImpl(ok, session.findChannelById(id));
        }
        JSONObject channelObj = (JSONObject) obj.get("channel");
        id = (String)channelObj.get("id");
        return new SlackChannelReplyImpl(ok, session.findChannelById(id));
    }

    private static boolean isMpim(JSONObject obj)
    {
        Boolean isMpim = (Boolean)obj.get("is_mpim");
        return isMpim != null && isMpim.equals(Boolean.TRUE);
    }

    private static boolean isIm(JSONObject obj)
    {
        Boolean isIm = (Boolean)obj.get("is_im");
        return isIm != null && isIm.equals(Boolean.TRUE);
    }

    private static boolean isChannel(JSONObject obj)
    {
        Object channel = obj.get("channel");
        return channel != null && channel instanceof JSONObject;
    }

    private static boolean isGroup(JSONObject obj)
    {
        Boolean isGroup = (Boolean)obj.get("is_group");
        return isGroup != null && isGroup.equals(Boolean.TRUE);
    }

}
