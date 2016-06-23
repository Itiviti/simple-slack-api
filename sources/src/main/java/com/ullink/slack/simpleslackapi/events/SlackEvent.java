package com.ullink.slack.simpleslackapi.events;

public interface SlackEvent {

    SlackEvent UNKNOWN_EVENT = new SlackEvent()
    {

        @Override
        public SlackEventType getEventType()
        {
            return SlackEventType.UNKNOWN;
        }
    };
    
    SlackEventType getEventType();
}
