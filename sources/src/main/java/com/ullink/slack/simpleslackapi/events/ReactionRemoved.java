package com.ullink.slack.simpleslackapi.events;

import com.ullink.slack.simpleslackapi.SlackUser;
import com.ullink.slack.simpleslackapi.SlackChannel;

public class ReactionRemoved implements SlackEvent {
    private final String emojiName;
    private final SlackChannel channel;
    private final SlackUser user;
    private final SlackUser itemUser;
    private final String messageID;
    private final String fileID;
    private final String fileCommentID;
    private final String timestamp;

    public ReactionRemoved(String emojiName, SlackUser user, SlackUser itemUser, SlackChannel channel, String messageID, String fileID, String fileCommentID, String timestamp) {
        this.emojiName = emojiName;
        this.user = user;
        this.itemUser = itemUser;
        this.channel = channel;
        this.messageID = messageID;
        this.fileID = fileID;
        this.fileCommentID = fileCommentID;
        this.timestamp = timestamp;
    }

    public String getEmojiName() {
        return emojiName;
    }

    public SlackChannel getChannel() {
        return channel;
    }

    public String getMessageID() {
        return messageID;
    }

    public String getFileID() {
        return fileID;
    }

    public String getFileCommentID() {
        return fileCommentID;
    }

    public SlackUser getUser() {
        return user;
    }

    public SlackUser getItemUser() {
        return itemUser;
    }

    @Override
    public SlackEventType getEventType() {
        return SlackEventType.REACTION_REMOVED;
    }
    
	public String getTimestamp() {
		return timestamp;
	}
}
