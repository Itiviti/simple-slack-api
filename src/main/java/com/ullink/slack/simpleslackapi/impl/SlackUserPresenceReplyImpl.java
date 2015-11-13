package com.ullink.slack.simpleslackapi.impl;

import com.ullink.slack.simpleslackapi.replies.SlackUserPresenceReply;

public class SlackUserPresenceReplyImpl extends SlackReplyImpl implements SlackUserPresenceReply
{
    private boolean active;

    SlackUserPresenceReplyImpl(boolean ok, boolean active)
    {
        super(ok);
        this.active = active;
    }

    @Override
    public boolean isActive()
    {
        return active;
    }
}
