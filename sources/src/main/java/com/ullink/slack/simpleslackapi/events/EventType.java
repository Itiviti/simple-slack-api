package com.ullink.slack.simpleslackapi.events;

import java.util.HashMap;
import java.util.Map;

public enum EventType
{
    MESSAGE("message"),
    CHANNEL_CREATED("channel_created"),
    CHANNEL_DELETED("channel_deleted"),
    CHANNEL_RENAME("channel_rename"),
    CHANNEL_ARCHIVE("channel_archive"),
    CHANNEL_UNARCHIVE("channel_unarchive"),
    CHANNEL_JOINED("channel_joined"),
    CHANNEL_LEFT("channel_left"),
    GROUP_JOINED("group_joined"),
    REACTION_ADDED("reaction_added"),
    REACTION_REMOVED("reaction_removed"),
    USER_CHANGE("user_change"),
    TEAM_JOIN("team_join"),
    PRESENCE_CHANGE("presence_change"),
    PIN_ADDED("pin_added"),
    PIN_REMOVED("pin_removed"),
    USER_TYPING("user_typing"),
    HELLO("hello"),
    OTHER("-");

    private static final Map<String, EventType> CODE_MAP = new HashMap<>();

    static
    {
        for (EventType enumValue : EventType.values())
        {
            CODE_MAP.put(enumValue.getCode(), enumValue);
        }
    }

    private String                              code;

    public static EventType getByCode(String code)
    {
        EventType toReturn = CODE_MAP.get(code);
        if (toReturn == null)
        {
            return OTHER;
        }
        return toReturn;
    }

    EventType(String code)
    {
        this.code = code;
    }

    public String getCode()
    {
        return code;
    }

}
