package com.ullink.slack.simpleslackapi.events;

import com.ullink.slack.simpleslackapi.SlackUser;
import com.ullink.slack.simpleslackapi.SlackChannel;
import lombok.Data;

@Data
public class ReactionRemoved implements SlackEvent {
    private final String emojiName;
    private final SlackUser user;
    private final SlackUser itemUser;
    private final SlackChannel channel;
    private final String messageID;
    private final String fileID;
    private final String fileCommentID;
    private final String timestamp;
    
    @Override
    public SlackEventType getEventType() {
        return SlackEventType.REACTION_REMOVED;
    }
}
