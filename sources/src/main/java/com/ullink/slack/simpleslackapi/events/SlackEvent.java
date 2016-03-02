package com.ullink.slack.simpleslackapi.events;

public interface SlackEvent
{
    public static final SlackEvent UNKNOWN_EVENT = new SlackEvent()
    {

        @Override
        public SlackEventType getEventType()
        {
            return SlackEventType.UNKNOWN;
        }
    };
    
    SlackEventType getEventType();
}
