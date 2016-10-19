package com.ullink.slack.simpleslackapi.impl;

import com.ullink.slack.simpleslackapi.SlackUser;
import com.ullink.slack.simpleslackapi.events.SlackEventType;
import com.ullink.slack.simpleslackapi.events.SlackTeamJoin;

public class SlackTeamJoinImpl implements SlackTeamJoin {

    private final SlackUser slackUser;

    SlackTeamJoinImpl(SlackUser slackUser) {
        this.slackUser = slackUser;
    }

    @Override
    public SlackUser getUser() {
        return slackUser;
    }

    @Override
    public SlackEventType getEventType() {
        return SlackEventType.SLACK_TEAM_JOIN;
    }
}
