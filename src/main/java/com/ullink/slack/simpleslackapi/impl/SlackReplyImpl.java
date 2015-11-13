package com.ullink.slack.simpleslackapi.impl;

import com.ullink.slack.simpleslackapi.replies.SlackReply;

abstract class SlackReplyImpl implements SlackReply
{

    private boolean ok;

    SlackReplyImpl(boolean ok)
    {
        this.ok = ok;
    }

    @Override
    public boolean isOk()
    {
        return ok;
    }

}
