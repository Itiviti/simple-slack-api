package com.ullink.slack.simpleslackapi.events;

import com.ullink.slack.simpleslackapi.SlackUser;
import com.ullink.slack.simpleslackapi.SlackChannel;

public class SlackChannelArchived implements SlackEvent {
    private SlackChannel slackChannel;
    private SlackUser slackuser;

    public SlackChannelArchived(SlackChannel slackChannel, SlackUser slackuser)
    {
        this.slackChannel = slackChannel;
        this.slackuser = slackuser;
    }

    public SlackChannel getSlackChannel()
    {
        return slackChannel;
    }

    public SlackUser getUser()
    {
        return slackuser;
    }

    @Override
    public SlackEventType getEventType()
    {
        return SlackEventType.SLACK_CHANNEL_ARCHIVED;
    }

}
