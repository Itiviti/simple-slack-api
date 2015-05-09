package com.ullink.slack.simpleslackapi.impl;

import com.ullink.slack.simpleslackapi.SlackBot;
import com.ullink.slack.simpleslackapi.SlackChannel;
import com.ullink.slack.simpleslackapi.SlackMessage;
import com.ullink.slack.simpleslackapi.SlackUser;

class SlackMessageImpl implements SlackMessage
{
    private String              messageContent;
    private SlackUser           user;
    private SlackBot            bot;
    private SlackChannel        channel;
    private SlackMessageSubType subType;

    SlackMessageImpl(String messageContent, SlackBot bot, SlackUser user, SlackChannel channel, SlackMessageSubType subType)
    {
        this.channel = channel;
        this.messageContent = messageContent;
        this.user = user;
        this.bot = bot;
        this.subType = subType;
    }

    @Override
    public String getMessageContent()
    {
        return messageContent;
    }

    @Override
    public SlackUser getSender()
    {
        return user;
    }

    @Override
    public SlackBot getBot()
    {
        return bot;
    }

    @Override
    public SlackChannel getChannel()
    {
        return channel;
    }

    @Override
    public SlackMessageSubType getSubType()
    {
        return subType;
    }

    @Override
    public String toString() {
        return "SlackMessageImpl{" +
                "messageContent='" + messageContent + '\'' +
                ", user=" + user +
                ", bot=" + bot +
                ", channel=" + channel +
                ", subType=" + subType +
                '}';
    }
}
