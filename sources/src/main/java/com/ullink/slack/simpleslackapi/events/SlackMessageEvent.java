package com.ullink.slack.simpleslackapi.events;

public interface SlackMessageEvent extends SlackEvent {
    String getTimeStamp();
}
