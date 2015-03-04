package com.ullink.slack.simpleslackapi.impl;

import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import com.ullink.slack.simpleslackapi.SlackMessage;
import com.ullink.slack.simpleslackapi.SlackReply;
import com.ullink.slack.simpleslackapi.SlackSession;

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
