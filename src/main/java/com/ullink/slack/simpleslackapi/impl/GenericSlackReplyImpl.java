package com.ullink.slack.simpleslackapi.impl;

import org.json.simple.JSONObject;
import com.ullink.slack.simpleslackapi.replies.GenericSlackReply;

class GenericSlackReplyImpl implements GenericSlackReply
{
    private JSONObject obj;

    public GenericSlackReplyImpl(JSONObject obj)
    {
        this.obj = obj;
    }

    @Override
    public JSONObject getPlainAnswer()
    {
        return obj;
    }

}
