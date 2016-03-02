package com.ullink.slack.simpleslackapi.events;

import com.ullink.slack.simpleslackapi.SlackChannel;

public interface SlackMessageUpdated extends SlackMessageEvent
{
    SlackChannel getChannel();
    String getMessageTimestamp();
    String getNewMessage();
}
