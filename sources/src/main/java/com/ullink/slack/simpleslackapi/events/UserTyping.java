package com.ullink.slack.simpleslackapi.events;

import com.ullink.slack.simpleslackapi.SlackChannel;
import com.ullink.slack.simpleslackapi.SlackUser;

public interface UserTyping extends SlackEvent {
    SlackChannel getChannel();
    SlackUser getUser();
}
