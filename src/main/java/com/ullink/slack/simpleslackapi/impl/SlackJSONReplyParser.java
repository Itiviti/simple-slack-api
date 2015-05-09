package com.ullink.slack.simpleslackapi.impl;

import org.json.simple.JSONObject;

class SlackJSONReplyParser
{
    static SlackReplyImpl decode(JSONObject obj)
    {
        Boolean ok = (Boolean) obj.get("ok");
        Long replyTo = (Long) obj.get("reply_to");
        String timestamp = (String) obj.get("ts");
        return new SlackReplyImpl(ok, replyTo != null ? replyTo : -1, timestamp);
    }
}
