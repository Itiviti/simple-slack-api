package com.ullink.slack.simpleslackapi.impl;

import java.util.Map;
import org.json.simple.JSONObject;
import com.ullink.slack.simpleslackapi.SlackBot;
import com.ullink.slack.simpleslackapi.SlackChannel;
import com.ullink.slack.simpleslackapi.SlackFile;
import com.ullink.slack.simpleslackapi.SlackUser;
import com.ullink.slack.simpleslackapi.events.SlackEventType;
import com.ullink.slack.simpleslackapi.events.SlackMessagePosted;

class SlackMessagePostedImpl implements SlackMessagePosted
{
    private String       messageContent;
    private SlackUser    user;
    private SlackBot     bot;
    private SlackChannel channel;
    private String       timestamp;
    private SlackFile    slackFile;
    private JSONObject   jsonSource;
    private Map<String, Integer> reactions;
    
    SlackMessagePostedImpl(String messageContent, SlackBot bot, SlackUser user, SlackChannel channel, String timestamp)
    {
        this.channel = channel;
        this.messageContent = messageContent;
        this.user = user;
        this.bot = bot;
        this.timestamp = timestamp;
    }
    
    SlackMessagePostedImpl(String messageContent, SlackBot bot, SlackUser user, SlackChannel channel, String timestamp, JSONObject jsonSource)
    {
        this.channel = channel;
        this.messageContent = messageContent;
        this.user = user;
        this.bot = bot;
        this.timestamp = timestamp;
        this.jsonSource = jsonSource;
    }
    
    SlackMessagePostedImpl(String messageContent, SlackBot bot, SlackUser user, SlackChannel channel, String timestamp, SlackFile slackFile, JSONObject jsonSource)
    {
        this.channel = channel;
        this.messageContent = messageContent;
        this.user = user;
        this.bot = bot;
        this.timestamp = timestamp;
        this.jsonSource = jsonSource;
        this.slackFile = slackFile;
    }

    @Override
    public String toString() {
        return "SlackMessagePostedImpl{" + "messageContent=" + messageContent + ", user=" + user + ", bot=" + bot + ", channel=" + channel + ", timestamp=" + timestamp + ", reactions=" + reactions + '}';
    }

    @Override
    public JSONObject getJsonSource() {
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

    @Override
    public int getTotalCountOfReactions() {
        int count = 0;
        for (Integer tmpCount : reactions.values()) {
            count += tmpCount;
        }
        return count;
    }

    @Override
    public String getTimestamp() {
        return timestamp;
    }

}

