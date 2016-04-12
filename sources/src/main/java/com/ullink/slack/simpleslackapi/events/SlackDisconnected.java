package com.ullink.slack.simpleslackapi.events;

import com.ullink.slack.simpleslackapi.SlackPersona;

public interface SlackDisconnected extends SlackEvent {

    SlackPersona getDisconnectedPersona();
}
