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
    GROUP_JOINED("group_joined"),
    REACTION_ADDED("reaction_added"),
    REACTION_REMOVED("reaction_removed"),
    USER_CHANGE("user_change"),
    PIN_ADDED("pin_added"),
    PIN_REMOVED("pin_removed"),
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
