package com.ullink.slack.simpleslackapi.events;

import com.ullink.slack.simpleslackapi.entity.SlackUser;
import com.ullink.slack.simpleslackapi.entity.SlackChannel;
import lombok.Data;

@Data
public class SlackChannelArchived implements SlackEvent {
    private final SlackChannel slackChannel;
    private final SlackUser slackuser;

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
