package com.ullink.slack.simpleslackapi.impl;

import com.ullink.slack.simpleslackapi.SlackChannel;
import com.ullink.slack.simpleslackapi.SlackUser;
import com.ullink.slack.simpleslackapi.events.ReactionAdded;
import com.ullink.slack.simpleslackapi.events.SlackEventType;

public class ReactionAddedImpl implements ReactionAdded {

    private final String emojiName;
    private final String messageID;
    private final SlackChannel channel;
    private final SlackUser user;
    private final String fileID;
    private final String fileCommentID;
  
  public ReactionAddedImpl(String emojiName, SlackUser user, SlackChannel channel, String messageID, String fileID, String fileCommentID) {
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
        return SlackEventType.REACTION_ADDED;
    }

}
