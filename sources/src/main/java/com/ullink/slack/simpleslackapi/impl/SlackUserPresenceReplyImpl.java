package com.ullink.slack.simpleslackapi.impl;

import com.ullink.slack.simpleslackapi.replies.SlackUserPresenceReply;

public class SlackUserPresenceReplyImpl extends SlackReplyImpl implements SlackUserPresenceReply
{
    private boolean active;

    SlackUserPresenceReplyImpl(boolean ok, String error, boolean active)
    {
        super(ok, error);
        this.active = active;
    }

    @Override
    public boolean isActive()
    {
        return active;
    }
}
