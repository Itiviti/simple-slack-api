package com.ullink.slack.simpleslackapi.impl;

import com.ullink.slack.simpleslackapi.SlackPersona;
import com.ullink.slack.simpleslackapi.events.SlackDisconnected;
import com.ullink.slack.simpleslackapi.events.SlackEventType;

public class SlackDisconnectedImpl implements SlackDisconnected {
    private SlackPersona persona;

    public SlackDisconnectedImpl(SlackPersona persona) {
        this.persona = persona;
    }

    @Override
    public SlackPersona getDisconnectedPersona() {
        return persona;
    }

    @Override
    public SlackEventType getEventType() {
        return SlackEventType.SLACK_DISCONNECTED;
    }
}
