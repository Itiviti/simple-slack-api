package com.ullink.slack.simpleslackapi.impl;

import java.util.HashMap;
import java.util.Map;
import org.json.simple.JSONObject;
import com.ullink.slack.simpleslackapi.SlackBot;
import com.ullink.slack.simpleslackapi.SlackChannel;
import com.ullink.slack.simpleslackapi.SlackSession;
import com.ullink.slack.simpleslackapi.SlackUser;
import com.ullink.slack.simpleslackapi.events.EventType;
import com.ullink.slack.simpleslackapi.events.SlackChannelArchived;
import com.ullink.slack.simpleslackapi.events.SlackChannelCreated;
import com.ullink.slack.simpleslackapi.events.SlackChannelDeleted;
import com.ullink.slack.simpleslackapi.events.SlackChannelRenamed;
import com.ullink.slack.simpleslackapi.events.SlackChannelUnarchived;
import com.ullink.slack.simpleslackapi.events.SlackEvent;
import com.ullink.slack.simpleslackapi.events.SlackGroupJoined;

class SlackJSONMessageParser
{
    public static enum SlackMessageSubType
    {
        CHANNEL_JOIN("channel_join"), MESSAGE_CHANGED("message_changed"), MESSAGE_DELETED("message_deleted"), BOT_MESSAGE("bot_message"), OTHER("-");

        private static final Map<String, SlackMessageSubType> CODE_MAP = new HashMap<>();

        static
        {
            for (SlackMessageSubType enumValue : SlackMessageSubType.values())
            {
                CODE_MAP.put(enumValue.getCode(), enumValue);
            }
        }

        String                                                code;

        public static SlackMessageSubType getByCode(String code)
        {
            SlackMessageSubType toReturn = CODE_MAP.get(code);
            if (toReturn == null)
            {
                return OTHER;
            }
            return toReturn;
        }

        SlackMessageSubType(String code)
        {
            this.code = code;
        }

        public String getCode()
        {
            return code;
        }
    }

    static SlackEvent decode(SlackSession slackSession, JSONObject obj)
    {
        String type = (String) obj.get("type");
        if (type == null)
        {
            return parseSlackReply(obj);
        }
        EventType eventType = EventType.getByCode(type);
        switch (eventType)
        {
            case MESSAGE:
                return extractMessageEvent(slackSession, obj);
            case CHANNEL_CREATED:
                return extractChannelCreatedEvent(slackSession, obj);
            case CHANNEL_ARCHIVE:
                return extractChannelArchiveEvent(slackSession, obj);
            case CHANNEL_DELETED:
                return extractChannelDeletedEvent(slackSession, obj);
            case CHANNEL_RENAME:
                return extractChannelRenamedEvent(slackSession, obj);
            case CHANNEL_UNARCHIVE:
                return extractChannelUnarchiveEvent(slackSession, obj);
            case GROUP_JOINED:
                return extractGroupJoinedEvent(slackSession, obj);
            default:
                return SlackEvent.UNKNOWN_EVENT;
        }
    }

    private static SlackGroupJoined extractGroupJoinedEvent(SlackSession slackSession, JSONObject obj)
    {
        JSONObject channelJSONObject = (JSONObject) obj.get("channel");
        SlackChannel slackChannel = parseChannelDescription(channelJSONObject);
        return new SlackGroupJoinedImpl(slackChannel);
    }

    private static SlackChannelRenamed extractChannelRenamedEvent(SlackSession slackSession, JSONObject obj)
    {
        String channelId = (String) obj.get("channel");
        String newName = (String) obj.get("name");
        return new SlackChannelRenamedImpl(slackSession.findChannelById(channelId), newName);
    }

    private static SlackChannelDeleted extractChannelDeletedEvent(SlackSession slackSession, JSONObject obj)
    {
        String channelId = (String) obj.get("channel");
        return new SlackChannelDeletedImpl(slackSession.findChannelById(channelId));
    }

    private static SlackChannelUnarchived extractChannelUnarchiveEvent(SlackSession slackSession, JSONObject obj)
    {
        String channelId = (String) obj.get("channel");
        String userId = (String) obj.get("user");
        return new SlackChannelUnarchivedImpl(slackSession.findChannelById(channelId), slackSession.findUserById(userId));
    }

    private static SlackChannelArchived extractChannelArchiveEvent(SlackSession slackSession, JSONObject obj)
    {
        String channelId = (String) obj.get("channel");
        String userId = (String) obj.get("user");
        return new SlackChannelArchivedImpl(slackSession.findChannelById(channelId), slackSession.findUserById(userId));
    }

    private static SlackChannelCreated extractChannelCreatedEvent(SlackSession slackSession, JSONObject obj)
    {
        JSONObject channelJSONObject = (JSONObject) obj.get("channel");
        SlackChannel channel = parseChannelDescription(channelJSONObject);
        String creatorId = (String) channelJSONObject.get("creator");
        SlackUser user = slackSession.findUserById(creatorId);
        return new SlackChannelCreatedImpl(channel, user);
    }

    private static SlackEvent extractMessageEvent(SlackSession slackSession, JSONObject obj)
    {
        String channelId = (String) obj.get("channel");
        SlackChannel channel = getChannel(slackSession, channelId);

        String ts = (String) obj.get("ts");
        SlackMessageSubType subType = SlackMessageSubType.getByCode((String) obj.get("subtype"));
        switch (subType)
        {
            case MESSAGE_CHANGED:
                return parseMessageUpdated(obj, channel, ts);
            case MESSAGE_DELETED:
                return parseMessageDeleted(obj, channel, ts);
            case BOT_MESSAGE:
                return parseBotMessage(obj, channel, ts, slackSession);
            default:
                return parseMessagePublished(obj, channel, ts, slackSession);
        }
    }

    private static SlackEvent parseSlackReply(JSONObject obj)
    {
        Boolean ok = (Boolean) obj.get("ok");
        Long replyTo = (Long) obj.get("reply_to");
        String timestamp = (String) obj.get("ts");
        return new SlackReplyImpl(ok, replyTo != null ? replyTo : -1, timestamp);
    }

    private static SlackChannel getChannel(SlackSession slackSession, String channelId)
    {
        if (channelId != null)
        {
            if (channelId.startsWith("D"))
            {
                // direct messaging, on the fly channel creation
                return new SlackChannelImpl(channelId, channelId, "", "", true);
            }
            else
            {
                return slackSession.findChannelById(channelId);
            }
        }
        return null;
    }

    private static SlackMessageUpdatedImpl parseMessageUpdated(JSONObject obj, SlackChannel channel, String ts)
    {
        JSONObject message = (JSONObject) obj.get("message");
        String text = (String) message.get("text");
        String messageTs = (String) message.get("ts");
        SlackMessageUpdatedImpl toto = new SlackMessageUpdatedImpl(channel, messageTs, ts, text);
        return toto;
    }

    private static SlackMessageDeletedImpl parseMessageDeleted(JSONObject obj, SlackChannel channel, String ts)
    {
        String deletedTs = (String) obj.get("deleted_ts");
        return new SlackMessageDeletedImpl(channel, deletedTs, ts);
    }

    private static SlackMessagePostedImpl parseBotMessage(JSONObject obj, SlackChannel channel, String ts, SlackSession slackSession)
    {
        String text = (String) obj.get("text");
        String botId = (String) obj.get("bot_id");
        SlackUser user = slackSession.findUserById(botId);
        return new SlackMessagePostedImpl(text, user, user, channel, ts);
    }

    private static SlackMessagePostedImpl parseMessagePublished(JSONObject obj, SlackChannel channel, String ts, SlackSession slackSession)
    {
        String text = (String) obj.get("text");
        String userId = (String) obj.get("user");
        SlackUser user = slackSession.findUserById(userId);
        return new SlackMessagePostedImpl(text, user, user, channel, ts);
    }

    private static SlackChannel parseChannelDescription(JSONObject channelJSONObject)
    {
        String id = (String) channelJSONObject.get("id");
        String name = (String) channelJSONObject.get("name");
        String topic = null; // TODO
        String purpose = null; // TODO
        return new SlackChannelImpl(id, name, topic, purpose, true);
    }

}
