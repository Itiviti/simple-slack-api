package com.ullink.slack.simpleslackapi.events;

public class Hello implements SlackEvent {
    @Override
    public SlackEventType getEventType() {
        return SlackEventType.HELLO;
    }
}
