package com.ullink.slack.simpleslackapi.events;

import com.ullink.slack.simpleslackapi.SlackUser;

public interface SlackChannelCreated extends SlackChannelEvent
{
    SlackUser getCreator();
}
