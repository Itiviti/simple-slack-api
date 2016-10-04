package com.ullink.slack.simpleslackapi.impl;

import com.ullink.slack.simpleslackapi.SlackAttachment;
import java.util.ArrayList;
import java.util.Map;

import com.ullink.slack.simpleslackapi.SlackBot;
import com.ullink.slack.simpleslackapi.SlackChannel;
import com.ullink.slack.simpleslackapi.SlackFile;
import com.ullink.slack.simpleslackapi.SlackUser;
import com.ullink.slack.simpleslackapi.events.SlackEventType;
import com.ullink.slack.simpleslackapi.events.SlackMessagePosted;

class SlackMessagePostedImpl implements SlackMessagePosted {
    private String       messageContent;
    private SlackUser    user;
    private SlackBot     bot;
    private SlackChannel channel;
    private String       timestamp;
    private SlackFile    slackFile;
    private String   jsonSource;
    private MessageSubType msgSubType;
    private Map<String, Integer> reactions;
    private ArrayList<SlackAttachment> attachments;
    
    SlackMessagePostedImpl(String messageContent, SlackBot bot, SlackUser user, SlackChannel channel, String timestamp, MessageSubType msgSubType)
    {
        this.channel = channel;
        this.messageContent = messageContent;
        this.user = user;
        this.bot = bot;
        this.timestamp = timestamp;
        this.msgSubType = msgSubType;
    }

    SlackMessagePostedImpl(String messageContent, SlackBot bot, SlackUser user, SlackChannel channel, String timestamp, SlackFile slackFile, String jsonSource, MessageSubType msgSubType)
    {
        this.channel = channel;
        this.messageContent = messageContent;
        this.user = user;
        this.bot = bot;
        this.timestamp = timestamp;
        this.jsonSource = jsonSource;
        this.slackFile = slackFile;
        this.msgSubType = msgSubType;
    }

    @Override
    public String toString() {
        return "SlackMessagePostedImpl{" + "messageContent=" + messageContent + ", user=" + user + ", bot=" + bot + ", channel=" + channel + ", timestamp=" + timestamp + ", reactions=" + reactions + '}';
    }

    @Override
    public String getJsonSource() {
        return jsonSource;
    }

    @Override
    public SlackFile getSlackFile() {
        return slackFile;
    }
    

    @Override
    public String getMessageContent() {
        return messageContent;
    }

    @Override
    public SlackUser getSender() {
        return user;
    }

    @Override
    public SlackBot getBot() {
        return bot;
    }

    @Override
    public SlackChannel getChannel() {
        return channel;
    }

    @Override
    public String getTimeStamp() {
        return timestamp;
    }

    @Override
    public SlackEventType getEventType() {
        return SlackEventType.SLACK_MESSAGE_POSTED;
    }

    @Override
    public Map<String, Integer> getReactions() {
        return reactions;
    }

    public void setReactions(Map<String, Integer> reactions) {
        this.reactions = reactions;
    }

    public void setAttachments(ArrayList<SlackAttachment> attachments) {
        this.attachments = attachments;
    }

    @Override
    public int getTotalCountOfReactions() {
        return reactions == null ? 0 : reactions.size();
    }

    @Override
    public MessageSubType getMessageSubType()
    {
        return msgSubType;
    }

    @Override
    public String getTimestamp() {
        return timestamp;
    }

    @Override public ArrayList<SlackAttachment> getAttachments() { return attachments; }
}

