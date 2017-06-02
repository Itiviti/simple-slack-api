package com.ullink.slack.simpleslackapi.events;

import com.ullink.slack.simpleslackapi.SlackChannel;

public class SlackChannelJoined implements SlackEvent {
    private SlackChannel slackChannel;

    public SlackChannelJoined(SlackChannel slackChannel)
    {
        this.slackChannel = slackChannel;
    }

    public SlackChannel getSlackChannel()
    {
        return slackChannel;
    }

    void setSlackChannel(SlackChannel slackChannel)
    {
        this.slackChannel = slackChannel;
    }

    @Override
    public SlackEventType getEventType()
    {
        return SlackEventType.SLACK_CHANNEL_JOINED;
    }

}
