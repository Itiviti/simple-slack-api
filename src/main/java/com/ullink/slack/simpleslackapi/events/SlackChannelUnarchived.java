package com.ullink.slack.simpleslackapi.events;

import com.ullink.slack.simpleslackapi.SlackUser;

public interface SlackChannelUnarchived extends SlackChannelEvent
{
    SlackUser getUser();
}
