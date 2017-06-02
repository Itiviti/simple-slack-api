package com.ullink.slack.simpleslackapi.events;

import com.ullink.slack.simpleslackapi.SlackPersona;
import com.ullink.slack.simpleslackapi.events.SlackEvent;
import com.ullink.slack.simpleslackapi.events.SlackEventType;

public class PresenceChange implements SlackEvent {
    private final String userId;
    private final SlackPersona.SlackPresence presence;

    public PresenceChange(String userId, SlackPersona.SlackPresence presence) {
        this.userId = userId;
        this.presence = presence;
    }

    public String getUserId() {
        return userId;
    }

    public SlackPersona.SlackPresence getPresence() {
        return presence;
    }

    @Override
    public SlackEventType getEventType() {
        return SlackEventType.PRESENCE_CHANGE;
    }
}
