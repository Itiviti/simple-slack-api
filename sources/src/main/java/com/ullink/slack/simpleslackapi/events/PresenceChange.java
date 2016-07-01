package com.ullink.slack.simpleslackapi.events;

import com.ullink.slack.simpleslackapi.SlackPersona.SlackPresence;

public interface PresenceChange extends SlackEvent {

    String getUserId();
    SlackPresence getPresence();
}
