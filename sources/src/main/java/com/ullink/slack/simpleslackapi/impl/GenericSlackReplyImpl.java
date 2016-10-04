package com.ullink.slack.simpleslackapi.impl;

import com.ullink.slack.simpleslackapi.replies.GenericSlackReply;

class GenericSlackReplyImpl implements GenericSlackReply
{
    private String obj;

    public GenericSlackReplyImpl(String obj)
    {
        this.obj = obj;
    }

    @Override
    public String getPlainAnswer()
    {
        return obj;
    }

}
