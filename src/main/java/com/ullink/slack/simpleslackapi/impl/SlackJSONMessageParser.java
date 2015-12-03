package com.ullink.slack.simpleslackapi.impl;

import com.ullink.slack.simpleslackapi.SlackChannel;
import com.ullink.slack.simpleslackapi.SlackFile;
import com.ullink.slack.simpleslackapi.SlackSession;
import com.ullink.slack.simpleslackapi.SlackUser;
import com.ullink.slack.simpleslackapi.events.*;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

import java.util.*;

class SlackJSONMessageParser
{
    public static enum SlackMessageSubType
    {
        CHANNEL_JOIN("channel_join"), MESSAGE_CHANGED("message_changed"), MESSAGE_DELETED("message_deleted"), BOT_MESSAGE("bot_message"), OTHER("-"), FILE_SHARE("file_share");

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
            return SlackEvent.UNKNOWN_EVENT;
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

    static SlackChannelHistory decodeChannelHistory(SlackSession slackSession, SlackChannel slackChannel, JSONObject jsonObject) {
        String latest = (String) jsonObject.get("latest");
        boolean hasMore = (Boolean) jsonObject.get("has_more");
        JSONArray messages = (JSONArray) jsonObject.get("messages");
        List<SlackEvent> historyMessages = new ArrayList<>(messages.size());
        for (Object message : messages) {
            SlackEvent slackEvent = extractMessageEvent(slackSession, (JSONObject) message);
            historyMessages.add(slackEvent);
        }
        return new SlackChannelHistoryImpl(slackChannel, historyMessages, latest, hasMore);
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
        return new SlackMessagePostedImpl(text, user, user, channel, ts,obj);
    }
    
    private final static String COMMENT_PLACEHOLDER = "> and commented:";
    
    private static SlackMessagePostedImpl parseMessagePublishedWithFile(JSONObject obj, SlackChannel channel, String ts, SlackSession slackSession)
    {
        /*
        Example of message with file
        {"type":"message","subtype":"file_share","text":"<@U024SEY12|ben> uploaded a file: <https://test-team.slack.com/files/ben/F0D34F799/1388485488946.png|1388485488946.png>","file":{"id":"F0D34F799","created":1445615453,"timestamp":1445615453,"name":"1388485488946.png","title":"1388485488946.png","mimetype":"image/png","filetype":"png","pretty_type":"PNG","user":"U024SEY12","editable":false,"size":467034,"mode":"hosted","is_external":false,"external_type":"","is_public":false,"public_url_shared":false,"display_as_bot":false,"username":"","url":"https://slack-files.com/files-pub/T01234567-F0D34F799-dfacea79ee/1388485488946.png","url_download":"https://slack-files.com/files-pub/T01234567-F0D34F799-dfacea79ee/download/1388485488946.png","url_private":"https://files.slack.com/files-pri/T01234567-F0D34F799/1388485488946.png","url_private_download":"https://files.slack.com/files-pri/T01234567-F0D34F799/download/1388485488946.png","thumb_64":"https://slack-files.com/files-tmb/T01234567-F0D34F799-c329abf88c/1388485488946_64.png","thumb_80":"https://slack-files.com/files-tmb/T01234567-F0D34F799-c329abf88c/1388485488946_80.png","thumb_360":"https://slack-files.com/files-tmb/T01234567-F0D34F799-c329abf88c/1388485488946_360.png","thumb_360_w":360,"thumb_360_h":244,"thumb_480":"https://slack-files.com/files-tmb/T01234567-F0D34F799-c329abf88c/1388485488946_480.png","thumb_480_w":480,"thumb_480_h":325,"thumb_160":"https://slack-files.com/files-tmb/T01234567-F0D34F799-c329abf88c/1388485488946_160.png","thumb_720":"https://slack-files.com/files-tmb/T01234567-F0D34F799-c329abf88c/1388485488946_720.png","thumb_720_w":720,"thumb_720_h":488,"image_exif_rotation":1,"original_w":797,"original_h":540,"permalink":"https://bentest1.slack.com/files/ben/F0D34F799/1388485488946.png","permalink_public":"https://slack-files.com/T01234567-F0D34F799-dfacea79ee","channels":[],"groups":[],"ims":[],"comments_count":0},"user":"U024SEY12","upload":true,"display_as_bot":false,"username":"<@U024SEY12|ben>","bot_id":null,"channel":"D0251JL9Y","ts":"1445615455.000002"}
        */
        SlackFile file = new SlackFile();
        if(obj.get("file")!=null){
            JSONObject rawFile = (JSONObject) obj.get("file");
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
            }catch(Exception e){
                //this properties will be null if something goes wrong
            }
            file.setPermalink((String) rawFile.get("permalink"));
            file.setPermalinkPublic((String) rawFile.get("permalink_public"));
        }
        
        String text = (String) obj.get("text");
        
        String comment = null;
        
        int idx = text.indexOf(COMMENT_PLACEHOLDER);
        
        if (idx != -1) {
            comment = text.substring(idx + COMMENT_PLACEHOLDER.length());
        }
        file.setComment(comment);
        
        String userId = (String) obj.get("user");
        
        SlackUser user = slackSession.findUserById(userId);
        
        return new SlackMessagePostedImpl(text, user, user, channel, ts,file,obj);
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
