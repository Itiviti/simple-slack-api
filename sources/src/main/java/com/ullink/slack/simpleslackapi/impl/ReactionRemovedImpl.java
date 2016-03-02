package com.ullink.slack.simpleslackapi.impl;

import com.ullink.slack.simpleslackapi.SlackChannel;
import com.ullink.slack.simpleslackapi.SlackUser;
import com.ullink.slack.simpleslackapi.events.ReactionRemoved;
import com.ullink.slack.simpleslackapi.events.SlackEventType;

public class ReactionRemovedImpl implements ReactionRemoved{
    private final String emojiName;
    private final SlackChannel channel;
    private final SlackUser user;
    private final String messageID;
    private final String fileID;
    private final String fileCommentID;
  
    public ReactionRemovedImpl(String emojiName, SlackUser user, SlackChannel channel, String messageID, String fileID, String fileCommentID) {
        this.emojiName = emojiName;
        this.user = user;
        this.channel = channel;
        this.messageID = messageID;
        this.fileID = fileID;
        this.fileCommentID = fileCommentID;
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
    public SlackEventType getEventType() {
        return SlackEventType.REACTION_REMOVED;
    }
}
