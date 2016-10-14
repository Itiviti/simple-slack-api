package com.ullink.slack.simpleslackapi.events;

import com.ullink.slack.simpleslackapi.SlackUser;

public interface SlackUserChangeEvent extends SlackEvent {

    SlackUser getUser();
}
