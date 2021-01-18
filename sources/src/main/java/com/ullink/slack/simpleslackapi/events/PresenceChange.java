package com.ullink.slack.simpleslackapi.events;

import com.ullink.slack.simpleslackapi.SlackPresence;
import lombok.Data;

@Data
public class PresenceChange implements SlackEvent {
    private final String userId;
    private final SlackPresence presence;

    @Override
    public SlackEventType getEventType() {
        return SlackEventType.PRESENCE_CHANGE;
    }
}
