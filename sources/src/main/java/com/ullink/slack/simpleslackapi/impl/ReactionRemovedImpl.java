package com.ullink.slack.simpleslackapi.impl;

import com.ullink.slack.simpleslackapi.SlackChannel;
import com.ullink.slack.simpleslackapi.SlackUser;
import com.ullink.slack.simpleslackapi.events.ReactionRemoved;
import com.ullink.slack.simpleslackapi.events.SlackEventType;

public class ReactionRemovedImpl implements ReactionRemoved{
    private final String emojiName;
    private final SlackChannel channel;
    private final SlackUser user;
    private final SlackUser itemUser;
    private final String messageID;
    private final String fileID;
    private final String fileCommentID;
    private final String timestamp;

    public ReactionRemovedImpl(String emojiName, SlackUser user, SlackUser itemUser, SlackChannel channel, String messageID, String fileID, String fileCommentID, String timestamp) {
        this.emojiName = emojiName;
        this.user = user;
        this.itemUser = itemUser;
        this.channel = channel;
        this.messageID = messageID;
        this.fileID = fileID;
        this.fileCommentID = fileCommentID;
        this.timestamp = timestamp;
    }

    @Override
    public String getEmojiName() {
        return emojiName;
    }

    @Override
    public SlackChannel getChannel() {
        return channel;
    }

    @Override
    public String getMessageID() {
        return messageID;
    }

    @Override
    public String getFileID() {
        return fileID;
    }

    @Override
    public String getFileCommentID() {
        return fileCommentID;
    }

    @Override
    public SlackUser getUser() {
        return user;
    }

    @Override
    public SlackUser getItemUser() {
        return itemUser;
    }

    @Override
    public SlackEventType getEventType() {
        return SlackEventType.REACTION_REMOVED;
    }
    
	@Override
	public String getTimestamp() {
		return timestamp;
	}
}
