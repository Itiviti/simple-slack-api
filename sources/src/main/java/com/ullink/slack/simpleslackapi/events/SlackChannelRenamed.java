package com.ullink.slack.simpleslackapi.events;

import com.ullink.slack.simpleslackapi.SlackChannel;

public class SlackChannelRenamed implements SlackEvent {
    private SlackChannel slackChannel;
    private String       newName;

    public SlackChannelRenamed(SlackChannel slackChannel, String newName)
    {
        this.slackChannel = slackChannel;
        this.newName = newName;
    }

    public SlackChannel getSlackChannel()
    {
        return slackChannel;
    }

    public String getNewName()
    {
        return newName;
    }

    @Override
    public SlackEventType getEventType()
    {
        return SlackEventType.SLACK_CHANNEL_RENAMED;
    }

}
