package com.ullink.slack.simpleslackapi.impl;

import com.ullink.slack.simpleslackapi.SlackChannel;
import com.ullink.slack.simpleslackapi.events.ReactionRemoved;
import com.ullink.slack.simpleslackapi.events.SlackEventType;

public class ReactionRemovedImpl implements ReactionRemoved{
    private final String emojiName;
    private final String messageID;
    private final SlackChannel channel;

    public ReactionRemovedImpl(String emojiName, String messageID, SlackChannel channel) {
        this.emojiName = emojiName;
        this.messageID = messageID;
        this.channel = channel;
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
    public SlackEventType getEventType() {
        return SlackEventType.REACTION_REMOVED;
    }
}
