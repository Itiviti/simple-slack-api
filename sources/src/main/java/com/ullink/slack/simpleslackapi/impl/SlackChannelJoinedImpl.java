package com.ullink.slack.simpleslackapi.impl;

import com.ullink.slack.simpleslackapi.SlackChannel;
import com.ullink.slack.simpleslackapi.events.SlackEventType;
import com.ullink.slack.simpleslackapi.events.SlackChannelJoined;

class SlackChannelJoinedImpl implements SlackChannelJoined {
    private SlackChannel slackChannel;

    SlackChannelJoinedImpl(SlackChannel slackChannel)
    {
        this.slackChannel = slackChannel;
    }

    @Override
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
