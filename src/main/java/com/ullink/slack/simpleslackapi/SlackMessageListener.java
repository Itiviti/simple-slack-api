package com.ullink.slack.simpleslackapi;

public interface SlackMessageListener
{
    void onSessionLoad(SlackSession session);

    void onMessage(SlackMessage message);
}
