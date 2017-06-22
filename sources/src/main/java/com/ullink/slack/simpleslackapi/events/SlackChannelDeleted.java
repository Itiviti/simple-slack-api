package com.ullink.slack.simpleslackapi.events;

import com.ullink.slack.simpleslackapi.SlackChannel;
import lombok.Data;

@Data
public class SlackChannelDeleted implements SlackEvent {
    private final SlackChannel slackChannel;

    @Override
    public SlackEventType getEventType() {
        return SlackEventType.SLACK_CHANNEL_DELETED;
    }
}
