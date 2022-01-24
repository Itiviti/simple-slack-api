package com.ullink.slack.simpleslackapi.events;

import com.ullink.slack.simpleslackapi.SlackChannel;
import com.ullink.slack.simpleslackapi.SlackUser;
import lombok.NonNull;


public class SlackMemberJoined implements SlackEvent {

    @NonNull
    private SlackUser user;
    @NonNull
    private SlackChannel slackChannel;


    public SlackEventType getEventType() {
        return SlackEventType.SLACK_MEMBER_JOINED;
    }

    public SlackMemberJoined(SlackUser user, SlackChannel slackChannel) {
        if (slackChannel == null) {
            throw new NullPointerException("slackChannel is marked non-null but is null");
        } else {
            this.slackChannel = slackChannel;
            this.user = user;
        }
    }

    public SlackChannel getChannel() {
        return this.slackChannel;
    }

    public SlackUser getUser() {
        return this.user;
    }

    public void setChannel(SlackChannel slackChannel) {
        this.slackChannel = slackChannel;
    }

    public void setUser(SlackUser user) {
        this.user=user;
    }

    public String toString() {
        return "A new Slack Member Joined" + this.slackChannel + ", user=" + this.user;
    }
}
