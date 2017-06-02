package com.ullink.slack.simpleslackapi.events;

import com.ullink.slack.simpleslackapi.SlackChannel;
import lombok.Data;
import lombok.NonNull;

@Data
public class SlackChannelLeft implements SlackEvent {
    @NonNull
    private SlackChannel slackChannel;

    @Override
    public SlackEventType getEventType()
    {
        return SlackEventType.SLACK_CHANNEL_LEFT;
    }

}
