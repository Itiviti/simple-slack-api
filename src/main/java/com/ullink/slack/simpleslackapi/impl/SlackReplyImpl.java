package com.ullink.slack.simpleslackapi.impl;

import com.ullink.slack.simpleslackapi.events.SlackEventType;
import com.ullink.slack.simpleslackapi.events.SlackReplyEvent;
import org.json.simple.JSONObject;

class SlackReplyImpl implements SlackReplyEvent
{

    private boolean ok;
    private long replyTo;
    private String timestamp;
    private JSONObject plainAnswer;

    SlackReplyImpl(boolean ok, long replyTo, String timestamp, JSONObject plain)
    {
        this.ok = ok;
        this.replyTo = replyTo;
        this.timestamp = timestamp;
        this.plainAnswer = plain;
    }

    @Override
    public boolean isOk()
    {
        return ok;
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

    @Override
    public SlackEventType getEventType()
    {
        return SlackEventType.SLACK_REPLY;
    }

    @Override
    public JSONObject getPlainAnswer() {
        return plainAnswer;
    }

}
