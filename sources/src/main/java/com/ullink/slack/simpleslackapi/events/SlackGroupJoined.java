package com.ullink.slack.simpleslackapi.events;

import com.ullink.slack.simpleslackapi.SlackChannel;
import lombok.Data;
import lombok.NonNull;

@Data
public class SlackGroupJoined implements SlackEvent {
    @NonNull
    private SlackChannel slackChannel;

    @Override
    public SlackEventType getEventType() {
        return SlackEventType.SLACK_GROUP_JOINED;
    }

}
