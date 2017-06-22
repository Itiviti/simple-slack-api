package com.ullink.slack.simpleslackapi.events.userchange;

import com.ullink.slack.simpleslackapi.SlackUser;
import com.ullink.slack.simpleslackapi.events.SlackEvent;

public interface SlackUserChangeEvent extends SlackEvent {

    SlackUser getUser();
}
