package com.ullink.slack.simpleslackapi.impl;

import com.ullink.slack.simpleslackapi.SlackPersona;
import com.ullink.slack.simpleslackapi.events.PresenceChange;
import com.ullink.slack.simpleslackapi.events.SlackEventType;

public class PresenceChangeImpl implements PresenceChange {
    private final String userId;
    private final SlackPersona.SlackPresence presence;

    PresenceChangeImpl(String userId, SlackPersona.SlackPresence presence) {
        this.userId = userId;
        this.presence = presence;
    }

    @Override
    public String getUserId() {
        return userId;
    }

    @Override
    public SlackPersona.SlackPresence getPresence() {
        return presence;
    }

    @Override
    public SlackEventType getEventType() {
        return SlackEventType.PRESENCE_CHANGE;
    }
}
