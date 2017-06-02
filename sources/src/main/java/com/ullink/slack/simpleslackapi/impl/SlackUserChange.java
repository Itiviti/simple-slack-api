package com.ullink.slack.simpleslackapi.impl;

import com.ullink.slack.simpleslackapi.SlackUser;
import com.ullink.slack.simpleslackapi.events.SlackEventType;
import com.ullink.slack.simpleslackapi.events.SlackUserChangeEvent;

public class SlackUserChange implements SlackUserChangeEvent {

    private final SlackUser slackUser;

    public SlackUserChange(SlackUser slackUser) {
        this.slackUser = slackUser;
    }

    @Override
    public SlackUser getUser() {
        return slackUser;
    }

    @Override
    public SlackEventType getEventType() {
        return SlackEventType.SLACK_USER_CHANGE;
    }
}
