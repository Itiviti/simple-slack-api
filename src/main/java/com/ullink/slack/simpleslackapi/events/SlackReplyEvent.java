package com.ullink.slack.simpleslackapi.events;

import org.json.simple.JSONObject;

public interface SlackReplyEvent extends SlackEvent
{
    boolean isOk();
    long getReplyTo();
    String getTimestamp();
    JSONObject getPlainAnswer();
}
