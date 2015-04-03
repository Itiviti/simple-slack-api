package com.ullink.slack.simpleslackapi.impl;

import com.ullink.slack.simpleslackapi.SlackChannel;
import com.ullink.slack.simpleslackapi.SlackChannelCreated;

public class SlackChannelCreatedImpl implements SlackChannelCreated
{
    private SlackChannel slackChannel;

    SlackChannelCreatedImpl(SlackChannel slackChannel)
    {
        this.slackChannel = slackChannel;
    }

    @Override
    public SlackChannel getSlackChannel()
    {
        return slackChannel;
    }

}
