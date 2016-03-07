package com.ullink.slack.simpleslackapi.replies;

import org.json.simple.JSONObject;

public interface GenericSlackReply extends SlackReply
{
    JSONObject getPlainAnswer();
}
