package com.ullink.slack.simpleslackapi.events;

import com.ullink.slack.simpleslackapi.SlackUser;
import com.ullink.slack.simpleslackapi.SlackChannel;

public class SlackChannelCreated implements SlackEvent {
    private SlackChannel slackChannel;
    private SlackUser slackuser;
    
    public SlackChannelCreated(SlackChannel slackChannel, SlackUser slackuser)
    {
        this.slackChannel = slackChannel;
        this.slackuser = slackuser;
    }

    public SlackChannel getSlackChannel()
    {
        return slackChannel;
    }

    public SlackUser getCreator()
    {
        return slackuser;
    }

    @Override
    public SlackEventType getEventType()
    {
        return SlackEventType.SLACK_CHANNEL_CREATED;
    }

}
