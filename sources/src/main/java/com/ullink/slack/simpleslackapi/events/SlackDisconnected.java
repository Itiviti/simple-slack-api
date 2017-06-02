package com.ullink.slack.simpleslackapi.events;

import com.ullink.slack.simpleslackapi.SlackPersona;
import com.ullink.slack.simpleslackapi.events.SlackEvent;
import com.ullink.slack.simpleslackapi.events.SlackEventType;

public class SlackDisconnected implements SlackEvent {
    private SlackPersona persona;

    public SlackDisconnected(SlackPersona persona) {
        this.persona = persona;
    }

    public SlackPersona getDisconnectedPersona() {
        return persona;
    }

    @Override
    public SlackEventType getEventType() {
        return SlackEventType.SLACK_DISCONNECTED;
    }
}
