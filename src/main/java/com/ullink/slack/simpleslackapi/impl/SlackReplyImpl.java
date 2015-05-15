package com.ullink.slack.simpleslackapi.impl;

import com.ullink.slack.simpleslackapi.events.SlackEventType;
import com.ullink.slack.simpleslackapi.events.SlackReplyEvent;

class SlackReplyImpl implements SlackReplyEvent
{

    private boolean ok;
    private long replyTo;
    private String timestamp;

    SlackReplyImpl(boolean ok, long replyTo, String timestamp)
    {
        this.ok = ok;
        this.replyTo = replyTo;
        this.timestamp = timestamp;
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

}
