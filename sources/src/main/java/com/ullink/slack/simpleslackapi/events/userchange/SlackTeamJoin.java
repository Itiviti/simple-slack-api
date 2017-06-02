package com.ullink.slack.simpleslackapi.events.userchange;

import com.ullink.slack.simpleslackapi.SlackUser;
import com.ullink.slack.simpleslackapi.events.SlackEventType;

public class SlackTeamJoin implements SlackUserChangeEvent {

    private final SlackUser slackUser;

    public SlackTeamJoin(SlackUser slackUser) {
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
