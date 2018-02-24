package com.ullink.slack.simpleslackapi.events;

import com.ullink.slack.simpleslackapi.entity.SlackPersona;
import lombok.Data;

@Data
public class SlackDisconnected implements SlackEvent {
    private final SlackPersona persona;

    @Override
    public SlackEventType getEventType() {
        return SlackEventType.SLACK_DISCONNECTED;
    }
}
