package com.ullink.slack.simpleslackapi.events;

import com.ullink.slack.simpleslackapi.entity.SlackPersona;
import lombok.Data;

@Data
public class PresenceChange implements SlackEvent {
    private final String userId;
    private final SlackPersona.SlackPresence presence;

    @Override
    public SlackEventType getEventType() {
        return SlackEventType.PRESENCE_CHANGE;
    }
}
