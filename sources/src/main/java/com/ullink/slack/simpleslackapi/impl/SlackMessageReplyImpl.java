package com.ullink.slack.simpleslackapi.impl;

import org.json.simple.JSONObject;
import com.ullink.slack.simpleslackapi.replies.SlackMessageReply;

public class SlackMessageReplyImpl extends SlackReplyImpl implements SlackMessageReply
{
    private long replyTo;
    private String timestamp;

    SlackMessageReplyImpl(boolean ok, String error, JSONObject obj, long replyTo, String timestamp)
    {
        super(ok, error);
        this.replyTo = replyTo;
        this.timestamp = timestamp;
    }

    @Override
    public long getReplyTo()
    {
        return replyTo;
    }

    @Override
    public String getTimestamp()
    {
        return timestamp;
    }
}
