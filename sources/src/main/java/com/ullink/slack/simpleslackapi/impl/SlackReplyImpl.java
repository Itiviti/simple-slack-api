package com.ullink.slack.simpleslackapi.impl;

import com.ullink.slack.simpleslackapi.replies.ParsedSlackReply;

class SlackReplyImpl implements ParsedSlackReply
{

    private boolean ok;
    private String error;

    SlackReplyImpl(boolean ok, String error)
    {
        this.ok = ok;
        this.error = error;
    }

    @Override
    public boolean isOk()
    {
        return ok;
    }

    @Override
    public String getErrorMessage()
    {
        return error;
    }
}
