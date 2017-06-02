package com.ullink.slack.simpleslackapi.events;

import com.ullink.slack.simpleslackapi.*;
import com.ullink.slack.simpleslackapi.SlackChannel;
import lombok.Data;
import lombok.NonNull;

import java.util.ArrayList;
import java.util.Map;

//TODO: even bother with NonNull in here?
@Data
public class SlackMessagePosted implements SlackEvent {
    @NonNull
    private String messageContent;
    @NonNull
    private SlackBot bot;
    @NonNull
    private SlackUser user;
    @NonNull
    private SlackChannel channel;
    @NonNull
    private String timestamp;
    @NonNull
    private MessageSubType msgSubType;
    private String threadTimestamp;
    private SlackFile slackFile;
    private String jsonSource;
    private Map<String, Integer> reactions;
    private ArrayList<SlackAttachment> attachments;

    public SlackMessagePosted(String messageContent, SlackBot bot, SlackUser user, SlackChannel channel, String timestamp, MessageSubType msgSubType) {
        this.channel = channel;
        this.messageContent = messageContent;
        this.user = user;
        this.bot = bot;
        this.timestamp = timestamp;
        this.msgSubType = msgSubType;
    }

    public SlackMessagePosted(String messageContent, SlackBot bot, SlackUser user, SlackChannel channel, String timestamp, SlackFile slackFile, String jsonSource, MessageSubType msgSubType, String threadTimestamp) {
        this.channel = channel;
        this.messageContent = messageContent;
        this.user = user;
        this.bot = bot;
        this.timestamp = timestamp;
        this.jsonSource = jsonSource;
        this.slackFile = slackFile;
        this.msgSubType = msgSubType;
        this.threadTimestamp = threadTimestamp;
    }

    @Override
    public String toString() {
        return "SlackMessagePosted{" + "messageContent=" + messageContent + ", user=" + user + ", bot=" + bot + ", channel=" + channel + ", timestamp=" + timestamp + ", reactions=" + reactions + '}';
    }

    public String getJsonSource() {
        return jsonSource;
    }

    public SlackFile getSlackFile() {
        return slackFile;
    }


    public String getMessageContent() {
        return messageContent;
    }

    public SlackUser getSender() {
        return user;
    }

    public SlackBot getBot() {
        return bot;
    }

    public SlackChannel getChannel() {
        return channel;
    }

    public String getTimeStamp() {
        return timestamp;
    }

    @Override
    public SlackEventType getEventType() {
        return SlackEventType.SLACK_MESSAGE_POSTED;
    }

    public Map<String, Integer> getReactions() {
        return reactions;
    }

    public void setReactions(Map<String, Integer> reactions) {
        this.reactions = reactions;
    }

    public void setAttachments(ArrayList<SlackAttachment> attachments) {
        this.attachments = attachments;
    }

    public int getTotalCountOfReactions() {
        return reactions == null ? 0 : reactions.size();
    }

    public MessageSubType getMessageSubType() {
        return msgSubType;
    }

    public String getTimestamp() {
        return timestamp;
    }

    public ArrayList<SlackAttachment> getAttachments() {
        return attachments;
    }

    public String getThreadTimestamp() {
        return threadTimestamp;
    }

    public enum MessageSubType {
        BOT_MESSAGE("bot_message"),
        CHANNEL_ARCHIVE("channel_archive"),
        CHANNEL_JOIN("channel_join"),
        CHANNEL_LEAVE("channel_leave"),
        CHANNEL_LEFT("channel_left"),
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
        MESSAGE_REPLIED("message_replied"),
        UNKNOWN("");

        String code;

        MessageSubType(String code) {
            this.code = code;
        }

        static public final MessageSubType fromCode(String code) {
            for (MessageSubType subType : SlackMessagePosted.MessageSubType.values()) {
                if (subType.code.equals(code)) {
                    return subType;
                }
            }
            return UNKNOWN;
        }
    }
}

