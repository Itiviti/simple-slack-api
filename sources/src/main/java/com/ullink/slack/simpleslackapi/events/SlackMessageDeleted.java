package com.ullink.slack.simpleslackapi.events;

import com.ullink.slack.simpleslackapi.SlackChannel;

public interface SlackMessageDeleted extends SlackMessageEvent
{
    SlackChannel getChannel();
    String getMessageTimestamp();
}
