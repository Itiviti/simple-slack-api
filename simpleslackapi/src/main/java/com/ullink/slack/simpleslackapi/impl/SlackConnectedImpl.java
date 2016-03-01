package com.ullink.slack.simpleslackapi.impl;

import com.ullink.slack.simpleslackapi.SlackPersona;
import com.ullink.slack.simpleslackapi.events.SlackConnected;
import com.ullink.slack.simpleslackapi.events.SlackEventType;

class SlackConnectedImpl implements SlackConnected
{
    private SlackPersona slackConnectedPersona;

    SlackConnectedImpl(SlackPersona slackConnectedPersona)
    {
        this.slackConnectedPersona = slackConnectedPersona;
    }

    @Override
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
