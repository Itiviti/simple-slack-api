package com.ullink.slack.simpleslackapi.events;

public interface SlackChannelRenamed extends SlackChannelEvent
{
    String getNewName();
}
