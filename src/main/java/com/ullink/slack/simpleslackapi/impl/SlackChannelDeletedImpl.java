package com.ullink.slack.simpleslackapi.impl;

import com.ullink.slack.simpleslackapi.SlackChannel;
import com.ullink.slack.simpleslackapi.events.SlackChannelDeleted;

public class SlackChannelDeletedImpl implements SlackChannelDeleted
{
    private SlackChannel slackChannel;

    SlackChannelDeletedImpl(SlackChannel slackChannel)
    {
        this.slackChannel = slackChannel;
    }

    @Override
    public SlackChannel getSlackChannel()
    {
        return slackChannel;
    }

}
