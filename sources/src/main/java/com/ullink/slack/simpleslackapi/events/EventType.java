//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by FernFlower decompiler)
//

package com.ullink.slack.simpleslackapi.events;

import java.util.HashMap;
import java.util.Map;

public enum EventType {
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
    MEMBER_JOINED_CHANNEL("member_joined_channel"),
    OTHER("-");

    private static final Map<String, EventType> CODE_MAP = new HashMap();
    private String code;

    public static EventType getByCode(String code) {
        EventType toReturn = (EventType)CODE_MAP.get(code);
        return toReturn == null ? OTHER : toReturn;
    }

    private EventType(String code) {
        this.code = code;
    }

    public String getCode() {
        return this.code;
    }

    static {
        EventType[] var0 = values();
        int var1 = var0.length;

        for(int var2 = 0; var2 < var1; ++var2) {
            EventType enumValue = var0[var2];
            CODE_MAP.put(enumValue.getCode(), enumValue);
        }

    }
}
