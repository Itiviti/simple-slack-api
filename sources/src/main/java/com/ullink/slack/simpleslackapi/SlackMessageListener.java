package com.ullink.slack.simpleslackapi;

import com.ullink.slack.simpleslackapi.entity.SlackMessage;
import com.ullink.slack.simpleslackapi.entity.SlackSession;

@Deprecated
public interface SlackMessageListener {
    void onSessionLoad(SlackSession session);
    void onMessage(SlackMessage message);
}
