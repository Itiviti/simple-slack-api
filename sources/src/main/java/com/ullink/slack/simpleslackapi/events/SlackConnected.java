package com.ullink.slack.simpleslackapi.events;

import com.ullink.slack.simpleslackapi.SlackPersona;
import com.ullink.slack.simpleslackapi.events.SlackEvent;
import com.ullink.slack.simpleslackapi.events.SlackEventType;
import lombok.Data;

@Data
public class SlackConnected implements SlackEvent {
    private final SlackPersona slackConnectedPersona;

    @Override
    public SlackEventType getEventType() {
        return SlackEventType.SLACK_CONNECTED;
    }
}
