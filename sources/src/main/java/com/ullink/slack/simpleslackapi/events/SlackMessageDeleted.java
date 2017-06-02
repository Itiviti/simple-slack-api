package com.ullink.slack.simpleslackapi.events;

import com.ullink.slack.simpleslackapi.SlackChannel;

public class SlackMessageDeleted implements SlackEvent {
    private final SlackChannel channel;
    private final String       messageTimestamp;
    private final String       deleteTimestamp;

    public SlackMessageDeleted(SlackChannel channel, String messageTimestamp, String deleteTimestamp) {
        this.channel = channel;
        this.messageTimestamp = messageTimestamp;
        this.deleteTimestamp = deleteTimestamp;
    }

    public SlackChannel getChannel()
    {
        return channel;
    }

    public String getMessageTimestamp()
    {
        return messageTimestamp;
    }

    public String getTimeStamp()
    {
        return deleteTimestamp;
    }

    @Override
    public SlackEventType getEventType()
    {
        return SlackEventType.SLACK_MESSAGE_DELETED;
    }

}
