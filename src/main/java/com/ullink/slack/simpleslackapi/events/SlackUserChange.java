package com.ullink.slack.simpleslackapi.events;

import com.ullink.slack.simpleslackapi.SlackUser;

public interface SlackUserChange extends SlackEvent {

    SlackUser getUser();
}
