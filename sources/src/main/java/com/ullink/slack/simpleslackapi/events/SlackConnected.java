package com.ullink.slack.simpleslackapi.events;

import com.ullink.slack.simpleslackapi.entity.SlackPersona;
import lombok.Data;

@Data
public class SlackConnected implements SlackEvent {
    private final SlackPersona slackConnectedPersona;

    @Override
    public SlackEventType getEventType() {
        return SlackEventType.SLACK_CONNECTED;
    }
}
