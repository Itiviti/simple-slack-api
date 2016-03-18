package com.ullink.slack.simpleslackapi.events;

import java.util.Map;
import org.json.simple.JSONObject;
import com.ullink.slack.simpleslackapi.SlackBot;
import com.ullink.slack.simpleslackapi.SlackChannel;
import com.ullink.slack.simpleslackapi.SlackFile;
import com.ullink.slack.simpleslackapi.SlackUser;

public interface SlackMessagePosted extends SlackMessageEvent
{
    enum MessageSubType {
        BOT_MESSAGE("bot_message"),
        CHANNEL_ARCHIVE("channel_archive"),
        CHANNEL_JOIN("channel_join"),
        CHANNEL_LEAVE("channel_leave"),
        CHANNEL_NAME("channel_name"),
        CHANNEL_PURPOSE("channel_purpose"),
        CHANNEL_TOPIC("channel_topic"),
        CHANNEL_UNARCHIVE("channel_unarchive"),
        FILE_COMMENT("file_comment"),
        FILE_MENTION("file_mention"),
        FILE_SHARE("file_share"),
        GROUP_JOIN("group_join"),
        GROUP_LEAVE("group_leave"),
        GROUP_NAME("group_name"),
        GROUP_PURPOSE("group_purpose"),
        GROUP_TOPIC("group_topic"),
        GROUP_UNARCHIVE("group_unarchive"),
        ME_MESSAGE("me_message"),
        MESSAGE_CHANGED("message_changed"),
        MESSAGE_DELETED("message_deleted"),
        PINNED_ITEM("pinned_item"),
        UNPINNED_ITEM("unpinned_item"),
        UNKNOWN("");

        String code;

        MessageSubType(String code) {
            this.code = code;
        }

        static public final MessageSubType fromCode(String code) {
            for (MessageSubType subType : MessageSubType.values()) {
                if (subType.code.equals(code)) {
                    return subType;
                }
            }
            return UNKNOWN;
        }
    }

    String getMessageContent();

    SlackUser getSender();

    @Deprecated
    SlackBot getBot();

    SlackChannel getChannel();
    
    SlackFile getSlackFile();
    
    JSONObject getJsonSource();
    
    String getTimestamp();
    
    Map<String, Integer> getReactions();
    
    int getTotalCountOfReactions();
    MessageSubType getMessageSubType();



}
