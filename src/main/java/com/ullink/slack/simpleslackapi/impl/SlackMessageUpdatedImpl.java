package com.ullink.slack.simpleslackapi.impl;

import com.ullink.slack.simpleslackapi.SlackChannel;
import com.ullink.slack.simpleslackapi.events.SlackEventType;
import com.ullink.slack.simpleslackapi.events.SlackMessageUpdated;

class SlackMessageUpdatedImpl implements SlackMessageUpdated
{
    private final SlackChannel channel;
    private final String       messageTimestamp;
    private final String       editionTimestamp;
    private final String       newMessage;

    SlackMessageUpdatedImpl(SlackChannel channel, String messageTimestamp, String editionTimestamp, String newMessage)
    {
        this.channel = channel;
        this.messageTimestamp = messageTimestamp;
        this.editionTimestamp = editionTimestamp;
        this.newMessage = newMessage;
    }

    @Override
    public SlackChannel getChannel()
    {
        return channel;
    }

    @Override
    public String getMessageTimestamp()
    {
        return messageTimestamp;
    }

    @Override
    public String getTimeStamp()
    {
        return editionTimestamp;
    }

    @Override
    public String getNewMessage()
    {
        return newMessage;
    }

    @Override
    public SlackEventType getEventType()
    {
        return SlackEventType.SLACK_MESSAGE_UPDATED;
    }


}
