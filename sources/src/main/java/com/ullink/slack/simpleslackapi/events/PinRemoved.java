package com.ullink.slack.simpleslackapi.events;

import com.ullink.slack.simpleslackapi.SlackChannel;
import com.ullink.slack.simpleslackapi.SlackFile;
import com.ullink.slack.simpleslackapi.SlackUser;
import lombok.Data;

@Data
public class PinRemoved implements SlackEvent {

    private final SlackUser sender;
    private final SlackChannel channel;
    private final String timestamp;
    private final SlackFile file;
    private final String message;

    @Override
    public SlackEventType getEventType() {
        return SlackEventType.PIN_REMOVED;
    }

}
