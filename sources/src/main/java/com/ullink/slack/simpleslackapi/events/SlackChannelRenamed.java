package com.ullink.slack.simpleslackapi.events;

import com.ullink.slack.simpleslackapi.SlackChannel;
import lombok.Data;

@Data
public class SlackChannelRenamed implements SlackEvent {
    private final SlackChannel slackChannel;
    private final String newName;

    @Override
    public SlackEventType getEventType() {
        return SlackEventType.SLACK_CHANNEL_RENAMED;
    }

}
