package com.ullink.slack.simpleslackapi.events;

import com.ullink.slack.simpleslackapi.SlackPersona;
import com.ullink.slack.simpleslackapi.events.SlackEvent;
import com.ullink.slack.simpleslackapi.events.SlackEventType;

public class SlackConnected implements SlackEvent {
    private SlackPersona slackConnectedPersona;

    public SlackConnected(SlackPersona slackConnectedPersona)
    {
        this.slackConnectedPersona = slackConnectedPersona;
    }

    public SlackPersona getConnectedPersona()
    {
        return slackConnectedPersona;
    }

    @Override
    public SlackEventType getEventType()
    {
        return SlackEventType.SLACK_CONNECTED;
    }
}
