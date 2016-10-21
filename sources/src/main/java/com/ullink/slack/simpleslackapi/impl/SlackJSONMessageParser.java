package com.ullink.slack.simpleslackapi.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.ullink.slack.simpleslackapi.SlackAttachment;
import com.ullink.slack.simpleslackapi.SlackIntegration;
import com.ullink.slack.simpleslackapi.SlackPersona;
import com.ullink.slack.simpleslackapi.events.*;
import com.ullink.slack.simpleslackapi.SlackChannel;
import com.ullink.slack.simpleslackapi.SlackFile;
import com.ullink.slack.simpleslackapi.SlackSession;
import com.ullink.slack.simpleslackapi.SlackUser;

class SlackJSONMessageParser {

    public static enum SlackMessageSubType
    {
        CHANNEL_JOIN("channel_join"), CHANNEL_LEAVE("channel_leave"), MESSAGE_CHANGED("message_changed"), MESSAGE_DELETED("message_deleted"), OTHER("-"), FILE_SHARE("file_share");

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

    static SlackEvent decode(SlackSession slackSession, JsonObject obj) {
        if (obj.get("type") == null) {
            return SlackEvent.UNKNOWN_EVENT;
        }
        String type = GsonHelper.getStringOrNull(obj.get("type"));
        EventType eventType = EventType.getByCode(type);
        switch (eventType) {
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
            case CHANNEL_JOINED:
                return extractChannelJoinedEvent(slackSession, obj);
            case CHANNEL_LEFT:
                return extractChannelLeftEvent(slackSession, obj);
            case GROUP_JOINED:
                return extractGroupJoinedEvent(slackSession, obj);
            case REACTION_ADDED:
                return extractReactionAddedEvent(slackSession, obj);
            case REACTION_REMOVED:
                return extractReactionRemovedEvent(slackSession, obj);
            case USER_CHANGE:
                return extractUserChangeEvent(obj);
            case TEAM_JOIN:
                return extractTeamJoinEvent(obj);
            case PRESENCE_CHANGE:
                return extractPresenceChangeEvent(slackSession, obj);
            case PIN_ADDED:
                return extractPinAddedEvent(slackSession, obj);
            case PIN_REMOVED:
                return extractPinRemovedEvent(slackSession, obj);
            case USER_TYPING:
                return extractUserTypingEvent(slackSession, obj);
            default:
                return SlackEvent.UNKNOWN_EVENT;
        }
    }

    private static SlackChannelJoined extractChannelJoinedEvent(SlackSession slackSession, JsonObject obj)
    {
        JsonObject channelJSONObject = obj.get("channel").getAsJsonObject();
        SlackChannel slackChannel = parseChannelDescription(channelJSONObject);
        return new SlackChannelJoinedImpl(slackChannel);
    }

    private static SlackChannelLeft extractChannelLeftEvent(SlackSession slackSession, JsonObject obj)
    {
        String channelId = GsonHelper.getStringOrNull(obj.get("channel"));
        SlackChannel slackChannel = slackSession.findChannelById(channelId);
        return new SlackChannelLeftImpl(slackChannel);
    }

    private static SlackGroupJoined extractGroupJoinedEvent(SlackSession slackSession, JsonObject obj)
    {
        JsonObject channelJSONObject = obj.get("channel").getAsJsonObject();
        SlackChannel slackChannel = parseChannelDescription(channelJSONObject);
        return new SlackGroupJoinedImpl(slackChannel);
    }

    private static SlackChannelRenamed extractChannelRenamedEvent(SlackSession slackSession, JsonObject obj)
    {
        String channelId = GsonHelper.getStringOrNull(obj.get("channel"));
        String newName = GsonHelper.getStringOrNull(obj.get("name"));
        return new SlackChannelRenamedImpl(slackSession.findChannelById(channelId), newName);
    }

    private static SlackChannelDeleted extractChannelDeletedEvent(SlackSession slackSession, JsonObject obj)
    {
        String channelId = GsonHelper.getStringOrNull(obj.get("channel"));
        return new SlackChannelDeletedImpl(slackSession.findChannelById(channelId));
    }

    private static SlackChannelUnarchived extractChannelUnarchiveEvent(SlackSession slackSession, JsonObject obj)
    {
        String channelId = GsonHelper.getStringOrNull(obj.get("channel"));
        String userId = GsonHelper.getStringOrNull(obj.get("user"));
        return new SlackChannelUnarchivedImpl(slackSession.findChannelById(channelId), slackSession.findUserById(userId));
    }

    private static SlackChannelArchived extractChannelArchiveEvent(SlackSession slackSession, JsonObject obj)
    {
        String channelId = GsonHelper.getStringOrNull(obj.get("channel"));
        String userId = GsonHelper.getStringOrNull(obj.get("user"));
        return new SlackChannelArchivedImpl(slackSession.findChannelById(channelId), slackSession.findUserById(userId));
    }

    private static SlackChannelCreated extractChannelCreatedEvent(SlackSession slackSession, JsonObject obj)
    {
        JsonObject channelJSONObject = obj.get("channel").getAsJsonObject();
        SlackChannel channel = parseChannelDescription(channelJSONObject);
        String creatorId = GsonHelper.getStringOrNull(channelJSONObject.get("creator"));
        SlackUser user = slackSession.findUserById(creatorId);
        return new SlackChannelCreatedImpl(channel, user);
    }

    private static SlackEvent extractMessageEvent(SlackSession slackSession, JsonObject obj)
    {
        String channelId = GsonHelper.getStringOrNull(obj.get("channel"));
        SlackChannel channel = getChannel(slackSession, channelId);

        String ts = GsonHelper.getStringOrNull(obj.get("ts"));
        SlackMessageSubType subType = SlackMessageSubType.getByCode(GsonHelper.getStringOrNull(obj.get("subtype")));
        switch (subType)
        {
            case MESSAGE_CHANGED:
                return parseMessageUpdated(obj, channel, ts);
            case MESSAGE_DELETED:
                return parseMessageDeleted(obj, channel, ts);
            case FILE_SHARE:
                return parseMessagePublishedWithFile(obj, channel, ts, slackSession);
            default:
                return parseMessagePublished(obj, channel, ts, slackSession);
        }
    }

    private static SlackChannel getChannel(SlackSession slackSession, String channelId)
    {
        if (channelId != null)
        {
            if (channelId.startsWith("D"))
            {
                // direct messaging, on the fly channel creation
                return new SlackChannelImpl(channelId, channelId, "", "", true, false, false);
            }
            else
            {
                return slackSession.findChannelById(channelId);
            }
        }
        return null;
    }

    private static SlackMessageUpdatedImpl parseMessageUpdated(JsonObject obj, SlackChannel channel, String ts)
    {
        JsonObject message = obj.get("message").getAsJsonObject();
        String text = GsonHelper.getStringOrNull(message.get("text"));
        String messageTs = GsonHelper.getStringOrNull(message.get("ts"));
        SlackMessageUpdatedImpl toto = new SlackMessageUpdatedImpl(channel, messageTs, ts, text);
        ArrayList<SlackAttachment> attachments = extractAttachmentsFromMessageJSON(message);
        toto.setAttachments(attachments);
        return toto;
    }

    private static SlackMessageDeletedImpl parseMessageDeleted(JsonObject obj, SlackChannel channel, String ts)
    {
        String deletedTs = GsonHelper.getStringOrNull(obj.get("deleted_ts"));
        return new SlackMessageDeletedImpl(channel, deletedTs, ts);
    }

    private static SlackMessagePostedImpl parseBotMessage(JsonObject obj, SlackChannel channel, String ts, SlackSession slackSession)
    {
        String text = GsonHelper.getStringOrNull(obj.get("text"));
        String subtype =  GsonHelper.getStringOrNull(obj.get("subtype"));
        String botId = GsonHelper.getStringOrNull(obj.get("bot_id"));
        SlackUser user = slackSession.findUserById(botId);
        return new SlackMessagePostedImpl(text, user, user, channel, ts, SlackMessagePosted.MessageSubType.fromCode(subtype));
    }

    private static SlackMessagePostedImpl parseMessagePublished(JsonObject obj, SlackChannel channel, String ts, SlackSession slackSession) {
        String text = GsonHelper.getStringOrNull(obj.get("text"));
        String userId = GsonHelper.getStringOrNull(obj.get("user"));
        if (userId == null) {
            userId = GsonHelper.getStringOrNull(obj.get("bot_id"));
        }
        String subtype = GsonHelper.getStringOrNull(obj.get("subtype"));
        SlackUser user = slackSession.findUserById(userId);
        if (user == null) {

            SlackIntegration integration = slackSession.findIntegrationById(userId);
            if (integration == null) {
                throw new IllegalStateException("unknown user id: " + userId);
            }
            user = new SlackIntegrationUser(integration);

        }
        Map<String, Integer> reacs = extractReactionsFromMessageJSON(obj);
        ArrayList<SlackAttachment> attachments = extractAttachmentsFromMessageJSON(obj);
        SlackMessagePostedImpl message = new SlackMessagePostedImpl(text, null, user, channel, ts, null, obj.toString(), SlackMessagePosted.MessageSubType.fromCode(subtype));
        message.setReactions(reacs);
        message.setAttachments(attachments);
        return message;
    }

    private final static String COMMENT_PLACEHOLDER = "> and commented:";
    

     private static void parseSlackFileFromRaw(JsonObject rawFile, SlackFile file) {
        file.setId(GsonHelper.getStringOrNull(rawFile.get("id")));
        file.setName(GsonHelper.getStringOrNull(rawFile.get("name")));
        file.setTitle(GsonHelper.getStringOrNull(rawFile.get("title")));
        file.setMimetype(GsonHelper.getStringOrNull(rawFile.get("mimetype")));
        file.setFiletype(GsonHelper.getStringOrNull(rawFile.get("filetype")));
        file.setUrl(GsonHelper.getStringOrNull(rawFile.get("url")));
        file.setUrlDownload(GsonHelper.getStringOrNull(rawFile.get("url_download")));
        file.setUrlPrivate(GsonHelper.getStringOrNull(rawFile.get("url_private")));
        file.setUrlPrivateDownload(GsonHelper.getStringOrNull(rawFile.get("url_private_download")));
        file.setThumb64(GsonHelper.getStringOrNull(rawFile.get("thumb_64")));
        file.setThumb80(GsonHelper.getStringOrNull(rawFile.get("thumb_80")));
        file.setThumb160(GsonHelper.getStringOrNull(rawFile.get("thumb_160")));
        file.setThumb360(GsonHelper.getStringOrNull(rawFile.get("thumb_360")));
        file.setThumb480(GsonHelper.getStringOrNull(rawFile.get("thumb_480")));
        file.setThumb720(GsonHelper.getStringOrNull(rawFile.get("thumb_720")));
        try{
            file.setOriginalH(GsonHelper.getLongOrNull(rawFile.get("original_h")));
            file.setOriginalW(GsonHelper.getLongOrNull(rawFile.get("original_w")));
            file.setImageExifRotation(GsonHelper.getLongOrNull(rawFile.get("image_exif_rotation")));
        } catch(Exception e){
            //this properties will be null if something goes wrong
        }
        file.setPermalink(GsonHelper.getStringOrNull(rawFile.get("permalink")));
        file.setPermalinkPublic(GsonHelper.getStringOrNull(rawFile.get("permalink_public")));
    }
  
    private static SlackMessagePostedImpl parseMessagePublishedWithFile(JsonObject obj, SlackChannel channel, String ts, SlackSession slackSession)
    {
        SlackFile file = new SlackFile();
        if (obj.get("file")!=null) {
            JsonObject rawFile = obj.get("file").getAsJsonObject();
	        parseSlackFileFromRaw(rawFile, file);
        }
        
        String text = GsonHelper.getStringOrNull(obj.get("text"));
        String subtype = GsonHelper.getStringOrNull(obj.get("subtype"));

        String comment = null;
        
        int idx = text.indexOf(COMMENT_PLACEHOLDER);
        
        if (idx != -1) {
            comment = text.substring(idx + COMMENT_PLACEHOLDER.length());
        }
        file.setComment(comment);
        
        String userId = GsonHelper.getStringOrNull(obj.get("user"));
        
        SlackUser user = slackSession.findUserById(userId);
        
        return new SlackMessagePostedImpl(text, user, user, channel, ts,file,obj.toString(), SlackMessagePosted.MessageSubType.fromCode(subtype));
    }

    private static SlackChannel parseChannelDescription(JsonObject channelJSONObject) {
        String id = GsonHelper.getStringOrNull(channelJSONObject.get("id"));
        String name = GsonHelper.getStringOrNull(channelJSONObject.get("name"));
        String topic = null;
        String purpose = null;
        if (channelJSONObject.has("topic")) {
            topic = GsonHelper.getStringOrNull(channelJSONObject.get("topic").getAsJsonObject().get("value"));
        }
        if (channelJSONObject.has("purpose")) {
            purpose = GsonHelper.getStringOrNull((channelJSONObject.get("purpose").getAsJsonObject().get("value")));
        }
        boolean isArchived = GsonHelper.getBooleanOrDefaultValue(channelJSONObject.get("is_archived"), false);
        return new SlackChannelImpl(id, name, topic, purpose, id.startsWith("D"),false, isArchived);
    }


    private static ReactionAdded extractReactionAddedEvent(SlackSession slackSession, JsonObject obj) {
        JsonObject item = obj.get("item").getAsJsonObject();
        String emojiName = GsonHelper.getStringOrNull(obj.get("reaction"));
        String messageId = GsonHelper.getStringOrNull(item.get("ts"));
        String fileId = GsonHelper.getStringOrNull(item.get("file"));
        String fileCommentId = GsonHelper.getStringOrNull(item.get("file_comment"));
        String channelId = GsonHelper.getStringOrNull(item.get("channel"));
        SlackChannel channel = (channelId != null) ? slackSession.findChannelById(channelId) : null;
        SlackUser user = slackSession.findUserById(GsonHelper.getStringOrNull(obj.get("user")));
        return new ReactionAddedImpl(emojiName, user, channel, messageId, fileId, fileCommentId);
    }

    private static SlackUserChange extractUserChangeEvent(JsonObject obj) {
        JsonObject user = obj.get("user").getAsJsonObject();
        SlackUser slackUser = SlackJSONParsingUtils.buildSlackUser(user);
        return new SlackUserChangeImpl(slackUser);
    }

    private static SlackTeamJoin extractTeamJoinEvent(JsonObject obj) {
        JsonObject user = obj.get("user").getAsJsonObject();
        SlackUser slackUser = SlackJSONParsingUtils.buildSlackUser(user);
        return new SlackTeamJoinImpl(slackUser);
    }

    private static PresenceChange extractPresenceChangeEvent(SlackSession slackSession, JsonObject obj) {
        String userId = GsonHelper.getStringOrNull(obj.get("user"));
        String presence = GsonHelper.getStringOrNull(obj.get("presence"));
        SlackPersona.SlackPresence value = SlackPersona.SlackPresence.UNKNOWN;
        if ("active".equals(presence)) {
            value = SlackPersona.SlackPresence.ACTIVE;
        } else if ("away".equals(presence)) {
            value = SlackPersona.SlackPresence.AWAY;
        }
        return new PresenceChangeImpl(userId, value);
    }

    private static ReactionRemoved extractReactionRemovedEvent(SlackSession slackSession, JsonObject obj) {
        JsonObject item = (JsonObject) obj.get("item");
        String emojiName = GsonHelper.getStringOrNull(obj.get("reaction"));
        String messageId = GsonHelper.getStringOrNull(item.get("ts"));
        String fileId = GsonHelper.getStringOrNull(item.get("file"));
        String fileCommentId = GsonHelper.getStringOrNull(item.get("file_comment"));
        String channelId = GsonHelper.getStringOrNull(item.get("channel"));
        SlackChannel channel = (channelId != null) ? slackSession.findChannelById(channelId) : null;
        SlackUser user = slackSession.findUserById(GsonHelper.getStringOrNull(obj.get("user")));
        return new ReactionRemovedImpl(emojiName, user, channel, messageId, fileId, fileCommentId);
    }

    private static UserTyping extractUserTypingEvent(SlackSession slackSession, JsonObject obj) {
        String channelId = GsonHelper.getStringOrNull(obj.get("channel"));
        String userId = GsonHelper.getStringOrNull(obj.get("user"));

        SlackChannel slackChannel = slackSession.findChannelById(channelId);
        SlackUser slackUser = slackSession.findUserById(userId);

        return new UserTypingImpl(slackChannel, slackUser, SlackEventType.USER_TYPING);
    }

    private static PinRemoved extractPinRemovedEvent(SlackSession slackSession, JsonObject obj) {
        String senderId = GsonHelper.getStringOrNull(obj.get("user"));
        SlackUser sender = slackSession.findUserById(senderId);

        String channelId = GsonHelper.getStringOrNull(obj.get("channel_id"));
	    SlackChannel channel = slackSession.findChannelById(channelId);

        JsonObject item = obj.get("item").getAsJsonObject();
	    String messageType = GsonHelper.getStringOrNull(item.get("type"));
	    SlackFile file = null;
	    String message = null;
	    if ("file".equals(messageType)) {
	        file = new SlackFile();
	        parseSlackFileFromRaw(item.get("file").getAsJsonObject(), file);
        } else if ("message".equals(messageType)) {
            JsonObject messageObj = item.get("message").getAsJsonObject();
	        message = GsonHelper.getStringOrNull(messageObj.get("text"));
	    }
	    String timestamp = GsonHelper.getStringOrNull(obj.get("event_ts"));
        return new PinRemovedImpl(sender, channel, timestamp, file, message);
    }

    private static PinAdded extractPinAddedEvent(SlackSession slackSession, JsonObject obj) {
        String senderId = GsonHelper.getStringOrNull(obj.get("user"));
        SlackUser sender = slackSession.findUserById(senderId);

        String channelId = GsonHelper.getStringOrNull(obj.get("channel_id"));
	    SlackChannel channel = slackSession.findChannelById(channelId);

        JsonObject item = (JsonObject) obj.get("item");
	    String messageType = GsonHelper.getStringOrNull(item.get("type"));
	    SlackFile file = null;
	    String message = null;
	    if ("file".equals(messageType)) {
	        file = new SlackFile();
	        parseSlackFileFromRaw(item.get("file").getAsJsonObject(), file);
	    } else if ("message".equals(messageType)) {
            JsonObject messageObj = item.get("message").getAsJsonObject();
	        message = GsonHelper.getStringOrNull(messageObj.get("text"));
	    }
	    String timestamp = GsonHelper.getStringOrNull(obj.get("event_ts"));

        return new PinAddedImpl(sender, channel, timestamp, file, message);       
    }

    private static Map<String, Integer> extractReactionsFromMessageJSON(JsonObject obj) {
        Map<String, Integer> reacs = new HashMap<>();
        JsonArray rawReactions = GsonHelper.getJsonArrayOrNull(obj.get("reactions"));
        if (rawReactions != null) {
            for (JsonElement element : rawReactions) {
                JsonObject reaction = element.getAsJsonObject();
                String emojiCode = reaction.get("name").toString();
                Integer count = reaction.get("count").getAsInt();
                reacs.put(emojiCode, count);
            }
        }
        return reacs;
    }

    public static Map<String, String> extractEmojisFromMessageJSON(JsonObject emojiObject) {
        Map<String, String> emojis = new HashMap<>();
        for (Map.Entry<String,JsonElement> entry : emojiObject.entrySet()) {
            emojis.put(entry.getKey().toString(), entry.getValue().getAsString());
        }
        return emojis;
    }

    private static ArrayList<SlackAttachment> extractAttachmentsFromMessageJSON(JsonObject object){
        if(object.get("attachments") == null) return new ArrayList<>();

        ArrayList<SlackAttachment> attachments = new ArrayList<>();

        for(JsonElement o : object.get("attachments").getAsJsonArray()){
            JsonObject obj = o.getAsJsonObject();
            SlackAttachment slackAttachment = new SlackAttachment();

            slackAttachment.setFallback(GsonHelper.getStringOrNull(obj.get("fallback")));
            slackAttachment.setColor(GsonHelper.getStringOrNull(obj.get("color")));
            slackAttachment.setPretext(GsonHelper.getStringOrNull(obj.get("pretext")));
            slackAttachment.setAuthorName(GsonHelper.getStringOrNull(obj.get("author_name")));
            slackAttachment.setAuthorLink(GsonHelper.getStringOrNull(obj.get("author_link")));
            slackAttachment.setAuthorIcon(GsonHelper.getStringOrNull(obj.get("author_icon")));
            slackAttachment.setTitle(GsonHelper.getStringOrNull(obj.get("title")));
            slackAttachment.setTitleLink(GsonHelper.getStringOrNull(obj.get("title_link")));
            slackAttachment.setText(GsonHelper.getStringOrNull(obj.get("text")));
            slackAttachment.setThumbUrl(GsonHelper.getStringOrNull(obj.get("thumb_url")));
            slackAttachment.setImageUrl(GsonHelper.getStringOrNull(obj.get("image_url")));
            slackAttachment.setFooter(GsonHelper.getStringOrNull(obj.get("footer")));
            slackAttachment.setFooterIcon(GsonHelper.getStringOrNull(obj.get("footer_icon")));

            if(obj.get("fields") != null) {
                for (JsonElement fieldElement : obj.get("fields").getAsJsonArray()) {
                    JsonObject field = fieldElement.getAsJsonObject();
                    slackAttachment.addField(GsonHelper.getStringOrNull(field.get("title")), GsonHelper.getStringOrNull(field.get("value")),
                            GsonHelper.getBooleanOrDefaultValue(field.get("short"),false));
                }
            }

            attachments.add(slackAttachment);
        }

        return attachments;
    }
}


