package com.ullink.slack.simpleslackapi;

@Deprecated
public interface SlackMessageListener
{
    void onSessionLoad(SlackSession session);

    void onMessage(SlackMessage message);
}
