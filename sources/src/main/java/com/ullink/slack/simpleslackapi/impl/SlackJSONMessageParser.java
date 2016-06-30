package com.ullink.slack.simpleslackapi.impl;

import java.util.HashMap;
import java.util.Map;

import com.ullink.slack.simpleslackapi.events.*;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
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

    static SlackEvent decode(SlackSession slackSession, JSONObject obj) {
        String type = (String) obj.get("type");
        if (type == null) {
            return SlackEvent.UNKNOWN_EVENT;
        }
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
                return extractUserChangeEvent(slackSession, obj);
            case PIN_ADDED:
                return extractPinAddedEvent(slackSession, obj);
            case PIN_REMOVED:
                return extractPinRemovedEvent(slackSession, obj);
            default:
                return SlackEvent.UNKNOWN_EVENT;
        }
    }
    
    
    private static SlackChannelJoined extractChannelJoinedEvent(SlackSession slackSession, JSONObject obj)
    {
        JSONObject channelJSONObject = (JSONObject) obj.get("channel");
        SlackChannel slackChannel = parseChannelDescription(channelJSONObject);
        return new SlackChannelJoinedImpl(slackChannel);
    }

    private static SlackChannelLeft extractChannelLeftEvent(SlackSession slackSession, JSONObject obj)
    {
        String channelId = (String) obj.get("channel");
        SlackChannel slackChannel = slackSession.findChannelById(channelId);
        return new SlackChannelLeftImpl(slackChannel);
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

    private static SlackMessagePostedImpl parseMessagePublished(JSONObject obj, SlackChannel channel, String ts, SlackSession slackSession) {
        String text = (String) obj.get("text");
        String userId = (String) obj.get("user");
        if (userId == null) {
            userId = (String) obj.get("bot_id");
        }
        String subtype = (String) obj.get("subtype");
        SlackUser user = slackSession.findUserById(userId);
        Map<String, Integer> reacs = extractReactionsFromMessageJSON(obj);
        SlackMessagePostedImpl message = new SlackMessagePostedImpl(text, null, user, channel, ts, null, obj, SlackMessagePosted.MessageSubType.fromCode(subtype));
        message.setReactions(reacs);
        return message;
    }

    private final static String COMMENT_PLACEHOLDER = "> and commented:";
    

     private static void parseSlackFileFromRaw(JSONObject rawFile, SlackFile file) {
        file.setId((String) rawFile.get("id"));
        file.setName((String) rawFile.get("name"));
        file.setTitle((String) rawFile.get("title"));
        file.setMimetype((String) rawFile.get("mimetype"));
        file.setFiletype((String) rawFile.get("filetype"));
        file.setUrl((String) rawFile.get("url"));
        file.setUrlDownload((String) rawFile.get("url_download"));
        file.setUrlPrivate((String) rawFile.get("url_private"));
        file.setUrlPrivateDownload((String) rawFile.get("url_private_download"));
        file.setThumb64((String) rawFile.get("thumb_64"));
        file.setThumb80((String) rawFile.get("thumb_80"));
        file.setThumb160((String) rawFile.get("thumb_160"));
        file.setThumb360((String) rawFile.get("thumb_360"));
        file.setThumb480((String) rawFile.get("thumb_480"));
        file.setThumb720((String) rawFile.get("thumb_720"));
        try{
            file.setOriginalH((Long) rawFile.get("original_h"));
            file.setOriginalW((Long) rawFile.get("original_w"));
            file.setImageExifRotation((Long) rawFile.get("image_exif_rotation"));
        } catch(Exception e){
            //this properties will be null if something goes wrong
        }
        file.setPermalink((String) rawFile.get("permalink"));
        file.setPermalinkPublic((String) rawFile.get("permalink_public"));
    }
  
    private static SlackMessagePostedImpl parseMessagePublishedWithFile(JSONObject obj, SlackChannel channel, String ts, SlackSession slackSession)
    {
        SlackFile file = new SlackFile();
        if (obj.get("file")!=null){
            JSONObject rawFile = (JSONObject) obj.get("file");
	        parseSlackFileFromRaw(rawFile, file);
        }
        
        String text = (String) obj.get("text");
        String subtype = (String) obj.get("subtype");

        String comment = null;
        
        int idx = text.indexOf(COMMENT_PLACEHOLDER);
        
        if (idx != -1) {
            comment = text.substring(idx + COMMENT_PLACEHOLDER.length());
        }
        file.setComment(comment);
        
        String userId = (String) obj.get("user");
        
        SlackUser user = slackSession.findUserById(userId);
        
        return new SlackMessagePostedImpl(text, user, user, channel, ts,file,obj, SlackMessagePosted.MessageSubType.fromCode(subtype));
    }

    private static SlackChannel parseChannelDescription(JSONObject channelJSONObject) {
        String id = (String) channelJSONObject.get("id");
        String name = (String) channelJSONObject.get("name");
        String topic = (String)((Map)channelJSONObject.get("topic")).get("value");
        String purpose = (String) ((Map) channelJSONObject.get("purpose")).get("value");
        return new SlackChannelImpl(id, name, topic, purpose, id.startsWith("D"));
    }


    private static ReactionAdded extractReactionAddedEvent(SlackSession slackSession, JSONObject obj) {
        JSONObject item = (JSONObject) obj.get("item");
        String emojiName = (String) obj.get("reaction");
        String messageId = (String) item.get("ts");	
        String fileId = (String) item.get("file");
        String fileCommentId = (String) item.get("file_comment");
        String channelId = (String) item.get("channel");
        SlackChannel channel = (channelId != null) ? slackSession.findChannelById(channelId) : null;
        SlackUser user = slackSession.findUserById((String) obj.get("user"));
        return new ReactionAddedImpl(emojiName, user, channel, messageId, fileId, fileCommentId);
    }

    private static SlackUserChange extractUserChangeEvent(SlackSession slackSession, JSONObject obj) {
        JSONObject user = (JSONObject) obj.get("user");
        SlackUser slackUser = SlackJSONParsingUtils.buildSlackUser(user);
        return new SlackUserChangeImpl(slackUser);
    }

    private static ReactionRemoved extractReactionRemovedEvent(SlackSession slackSession, JSONObject obj) {
        JSONObject item = (JSONObject) obj.get("item");
        String emojiName = (String) obj.get("reaction");
        String messageId = (String) item.get("ts");	
        String fileId = (String) item.get("file");
        String fileCommentId = (String) item.get("file_comment");
        String channelId = (String) item.get("channel");
        SlackChannel channel = (channelId != null) ? slackSession.findChannelById(channelId) : null;
        SlackUser user = slackSession.findUserById((String) obj.get("user"));
        return new ReactionRemovedImpl(emojiName, user, channel, messageId, fileId, fileCommentId);
    }

    private static PinRemoved extractPinRemovedEvent(SlackSession slackSession, JSONObject obj) {
        String senderId = (String) obj.get("user");
        SlackUser sender = slackSession.findUserById(senderId);

        String channelId = (String) obj.get("channel_id");
        SlackChannel channel = slackSession.findChannelById(channelId);

        JSONObject item = (JSONObject) obj.get("item");
        String messageType = (String) item.get("type");
        SlackFile file = null;
        String message = null;
        if ("file".equals(messageType)) {
            file = new SlackFile();
            parseSlackFileFromRaw((JSONObject) item.get("file"), file);
        } else if ("message".equals(messageType)) {
            JSONObject messageObj = (JSONObject) item.get("message");
            message = (String) messageObj.get("text");
        }
        String timestamp = (String) obj.get("event_ts");
        return new PinRemovedImpl(sender, channel, timestamp, file, message);
    }

    private static PinAdded extractPinAddedEvent(SlackSession slackSession, JSONObject obj) {
        String senderId = (String) obj.get("user");
        SlackUser sender = slackSession.findUserById(senderId);

        String channelId = (String) obj.get("channel_id");
        SlackChannel channel = slackSession.findChannelById(channelId);

        JSONObject item = (JSONObject) obj.get("item");
        String messageType = (String) item.get("type");
        SlackFile file = null;
        String message = null;
        if ("file".equals(messageType)) {
            file = new SlackFile();
            parseSlackFileFromRaw((JSONObject) item.get("file"), file);
        } else if ("message".equals(messageType)) {
            JSONObject messageObj = (JSONObject) item.get("message");
            message = (String) messageObj.get("text");
        }
        String timestamp = (String) obj.get("event_ts");

        return new PinAddedImpl(sender, channel, timestamp, file, message);
    }

    private static Map<String, Integer> extractReactionsFromMessageJSON(JSONObject obj) {
        Map<String, Integer> reacs = new HashMap<>();
        JSONArray rawReactions = (JSONArray) obj.get("reactions");
        if (rawReactions != null) {
            for (Object rawReaction : rawReactions) {
                JSONObject reaction = (JSONObject) rawReaction;
                String emojiCode = reaction.get("name").toString();
                Integer count = Integer.valueOf(reaction.get("count").toString());
                reacs.put(emojiCode, count);
            }
        }
        return reacs;
    }

    public static Map<String, String> extractEmojisFromMessageJSON(JSONObject object) {
        Map<String, String> emojis = new HashMap<>();

        for (Object o : object.entrySet()) {
            Map.Entry entry = (Map.Entry) o;
            emojis.put(entry.getKey().toString(), entry.getValue().toString());
        }

        return emojis;
    }
}


