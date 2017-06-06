package com.ullink.slack.simpleslackapi.events;

import com.ullink.slack.simpleslackapi.SlackAttachment;
import com.ullink.slack.simpleslackapi.SlackChannel;

import java.util.ArrayList;

public class SlackMessageUpdated implements SlackEvent {
    private final SlackChannel channel;
    private final String       messageTimestamp;
    private final String       editionTimestamp;
    private final String       newMessage;
    private ArrayList<SlackAttachment> attachments;

    public SlackMessageUpdated(SlackChannel channel, String messageTimestamp, String editionTimestamp, String newMessage) {
        this.channel = channel;
        this.messageTimestamp = messageTimestamp;
        this.editionTimestamp = editionTimestamp;
        this.newMessage = newMessage;
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
        return editionTimestamp;
    }

    public String getNewMessage()
    {
        return newMessage;
    }

    @Override
    public SlackEventType getEventType()
    {
        return SlackEventType.SLACK_MESSAGE_UPDATED;
    }

    public ArrayList<SlackAttachment> getAttachments() { return attachments; }

    public void setAttachments(ArrayList<SlackAttachment> attachments) {
        this.attachments = attachments;
    }
}
