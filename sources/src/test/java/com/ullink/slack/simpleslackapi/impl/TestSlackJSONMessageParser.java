package com.ullink.slack.simpleslackapi.impl;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.ullink.slack.simpleslackapi.*;
import com.ullink.slack.simpleslackapi.events.*;
import com.ullink.slack.simpleslackapi.events.userchange.SlackTeamJoin;
import com.ullink.slack.simpleslackapi.events.userchange.SlackUserChange;
import com.ullink.slack.simpleslackapi.replies.*;
import org.assertj.core.api.Assertions;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.io.IOException;
import java.util.List;
import java.util.Map;

public class TestSlackJSONMessageParser {

    SlackSession session;

    private static final String TEST_NEW_MESSAGE = "{\"type\":\"message\",\"channel\":\"TESTCHANNEL1\",\"user\":\"TESTUSER1\",\"text\":\"Test text 1\",\"ts\":\"1413187521.000004\"}";
    private static final String TEST_NEW_MESSAGE_FROM_INTEGRATION = "{\"type\":\"message\",\"channel\":\"TESTCHANNEL1\",\"bot_id\":\"TESTINTEGRATION1\",\"text\":\"Test text 1\",\"ts\":\"1413187521.000004\"}";
    private static final String TEST_DELETED_MESSAGE = "{\"type\":\"message\",\"channel\":\"TESTCHANNEL1\",\"user\":\"TESTUSER1\",\"text\":\"Test text 1\",\"ts\":\"1413187521.000005\", \"subtype\": \"message_deleted\", \"deleted_ts\": \"1358878749.000002\"}";
    private static final String TEST_UPDATED_MESSAGE = "{\"type\":\"message\",\"channel\":\"TESTCHANNEL1\",\"text\":\"Test text 1\",\"ts\":\"1358878755.001234\", \"subtype\": \"message_changed\", \"message\": {\"type\": \"message\", \"user\": \"TESTUSER1\", \"text\": \"newtext\", \"ts\": \"1413187521.000005\", \"edited\": { \"user\": \"TESTUSER1\", \"ts\":\"1358878755.001234\"}}}";

    private static final String TEST_CHANNEL_CREATED = "{\"type\":\"channel_created\",\"channel\": { \"id\": \"NEWCHANNEL\", \"name\": \"new channel\", \"creator\": \"TESTUSER1\"}}";
    private static final String TEST_CHANNEL_DELETED = "{\"type\":\"channel_deleted\",\"channel\": \"TESTCHANNEL1\"}";

    private static final String TEST_CHANNEL_ARCHIVED = "{\"type\":\"channel_archive\",\"channel\": \"TESTCHANNEL1\",\"user\":\"TESTUSER1\"}";
    private static final String TEST_CHANNEL_UNARCHIVED = "{\"type\":\"channel_unarchive\",\"channel\": \"TESTCHANNEL1\",\"user\":\"TESTUSER1\"}";

    private static final String NEW_CHANNEL = "\"channel\": { \"id\": \"NEWCHANNEL\", \"name\": \"new channel\", \"creator\": \"TESTUSER1\", \"topic\": {\"value\": \"To have something new\"}, \"purpose\": {\"value\": \"This channel so new it aint even old yet\"}}";
    private static final String TEST_GROUP_JOINED = "{\"type\":\"group_joined\"," + NEW_CHANNEL + "}";

    private static final String TEST_REACTION = " \"reaction\":\"thumbsup\", \"item\": {\"channel\":\"NEWCHANNEL\",\"ts\":\"1360782804.083113\"}";
    private static final String TEST_REACTION_ADDED = "{\"type\":\"reaction_added\", " + TEST_REACTION + ", \"user\":\"TESTUSER1\",\"item_user\":\"TESTUSER2\"}";
    private static final String TEST_REACTION_REMOVED = "{\"type\":\"reaction_removed\", " + TEST_REACTION + ",\"user\":\"TESTUSER1\",\"item_user\":\"TESTUSER2\"}";

    private static final String TEST_USER_CHANGE = "{\"type\": \"user_change\",\"user\": {\"id\": \"TESTUSER1\", \"name\": \"test user 1\"}}";

    private static final String TEST_TEAM_JOIN = "{\"type\": \"team_join\",\"user\": {\"id\": \"TESTUSER1\", \"name\": \"test user 1\"}}";

    private static final String TEST_ATTACHMENT = "{\"type\":\"message\",\"channel\":\"TESTCHANNEL1\",\"user\":\"TESTUSER1\",\"text\":\"Test text 1\",\"ts\":\"1413187521.000004\", \"attachments\": [{\"fallback\": \"Required plain-text summary of the attachment.\", \"color\": \"#36a64f\", \"pretext\": \"Optional text that appears above the attachment block\", \"author_name\": \"Bobby Tables\", \"author_link\": \"http://flickr.com/bobby/\", \"author_icon\": \"http://flickr.com/icons/bobby.jpg\", \"title\": \"Slack API Documentation\", \"title_link\": \"https://api.slack.com/\", \"text\": \"Optional text that appears within the attachment\", \"fields\": [ { \"title\": \"Priority\", \"value\": \"High\", \"short\": false } ], \"image_url\": \"http://my-website.com/path/to/image.jpg\", \"thumb_url\": \"http://example.com/path/to/thumb.png\", \"footer\": \"Slack API\", \"footer_icon\": \"https://platform.slack-edge.com/img/default_application_icon.png\", \"ts\": 123456789}]}";

    private static final String TEST_USER_TYPING = "{\"type\":\"user_typing\",\"channel\":\"TESTCHANNEL1\",\"user\":\"TESTUSER3\"}";

    @Before
    public void setup() {
        session = new AbstractSlackSessionImpl() {

            @Override
            public long getHeartbeat() {
                return 0;
            }

            @Override
            public void setPresence(SlackPersona.SlackPresence presence) {};

            @Override
            public void connect() {
                SlackUser user1 = new SlackUserImpl("TESTUSER1", "test user 1", "", "", "testSkype", "testPhone", "testTitle", false, false, false, false, false, false, false, "tz", "tzLabel", new Integer(0), SlackPersona.SlackPresence.ACTIVE);
                SlackUser user2 = new SlackUserImpl("TESTUSER2", "test user 2", "", "", "testSkype", "testPhone", "testTitle", false, false, false, false, false, false, false, "tz", "tzLabel", new Integer(0), SlackPersona.SlackPresence.ACTIVE);
                SlackUser user3 = new SlackUserImpl("TESTUSER3", "test user 3", "", "", "testSkype", "testPhone", "testTitle", false, false, false, false, false, false, false, "tz", "tzLabel", new Integer(0), SlackPersona.SlackPresence.ACTIVE);

                users.put(user1.getId(), user1);
                users.put(user2.getId(), user2);
                users.put(user3.getId(), user3);

                SlackIntegration integration = new SlackIntegrationImpl("TESTINTEGRATION1","integration 1",false);

                integrations.put(integration.getId(),integration);

                SlackChannel channel1 = new SlackChannel("TESTCHANNEL1", "testchannel1", null, null, false, false, false);
                SlackChannel channel2 = new SlackChannel("TESTCHANNEL2", "testchannel2", null, null, false, false, false);
                SlackChannel channel3 = new SlackChannel("TESTCHANNEL3", "testchannel3", null, null, false, false, false);
                SlackChannel channel4 = new SlackChannel("NEWCHANNEL", "new channel", "To have something new", "This channel so new it aint even old yet", false, false, false);
                channels.put(channel1.getId(), channel1);
                channels.put(channel2.getId(), channel2);
                channels.put(channel3.getId(), channel3);
                channels.put(channel4.getId(), channel4);
            }

            @Override
            public void disconnect() {
            }

            @Override
            public SlackMessageHandle sendMessageOverWebSocket(SlackChannel channel, String message) {
                throw new UnsupportedOperationException();
            }

            @Override
            public SlackMessageHandle<SlackMessageReply> sendTyping(SlackChannel channel) {
                throw new UnsupportedOperationException();
            }

            @Override
            public SlackPersona.SlackPresence getPresence(SlackPersona persona) {
                return null;
            }

            @Override
            public SlackMessageHandle deleteMessage(String timeStamp, SlackChannel channel) {
                return null;
            }

            @Override
            public SlackMessageHandle<SlackMessageReply> sendMessage(SlackChannel channel, SlackPreparedMessage preparedMessage, SlackChatConfiguration chatConfiguration) {
                throw new UnsupportedOperationException();
            }

            @Override
            public SlackMessageHandle<SlackMessageReply> sendEphemeralMessage(SlackChannel channel, SlackUser user, SlackPreparedMessage preparedMessage, SlackChatConfiguration chatConfiguration) {
                throw new UnsupportedOperationException();
            }

            @Override
            public SlackMessageHandle<SlackMessageReply> sendFile(SlackChannel channel, byte[] data, String fileName)
            {
                return null;
            }

            @Override
            public SlackMessageHandle<SlackMessageReply> sendFile(SlackChannel channel, byte[] data, String fileName, String title, String initialComment)
            {
                return null;
            }

            @Override
            public SlackMessageHandle<SlackMessageReply> sendFileToUser(SlackUser user, byte[] data, String fileName)
            {
                return null;
            }

            @Override
            public SlackMessageHandle<SlackMessageReply> sendFileToUser(String userName, byte[] data, String fileName)
            {
                return null;
            }

            @Override
            public SlackMessageHandle<SlackMessageReply> sendMessageToUser(SlackUser user, SlackPreparedMessage preparedMessage) {
                return null;
            }

            @Override
            public SlackMessageHandle updateMessage(String timeStamp, SlackChannel channel, String message) {
                return null;
            }

            @Override
            public SlackMessageHandle updateMessage(String timeStamp, SlackChannel channel, String message, SlackAttachment[] attachments) {
                return null;
            }

            @Override
            public SlackMessageHandle addReactionToMessage(SlackChannel channel, String messageTimeStamp, String emojiCode) {
                return null;
            }

            @Override public SlackMessageHandle<SlackMessageReply> removeReactionFromMessage(SlackChannel channel, String messageTimeStamp, String emojiCode)
            {
                return null;
            }

            @Override
            public SlackMessageHandle joinChannel(String channelName) {
                return null;
            }

            @Override
            public SlackMessageHandle<SlackChannelReply> setChannelTopic(SlackChannel channel, String topic) {
                return null;
            }

            @Override
            public SlackMessageHandle leaveChannel(SlackChannel channel) {
                return null;
            }

            @Override
            public SlackMessageHandle<SlackChannelReply> openDirectMessageChannel(SlackUser user)
            {
                return null;
            }

            @Override
            public SlackMessageHandle<SlackChannelReply> openMultipartyDirectMessageChannel(SlackUser... users)
            {
                return null;
            }

            @Override
            public SlackMessageHandle<EmojiSlackReply> listEmoji() {
                return null;
            }

            @Override
            public void refetchUsers() {}

            @Override
            public SlackMessageHandle inviteUser(String email, String firstName, boolean setActive)
            {
                return null;
            }

            @Override
            public SlackMessageHandle<ParsedSlackReply> inviteUser(String email, List<SlackChannel> channels, String firstName, String lastName, boolean resend, boolean restricted, boolean setActive) {
                return null;
            }

            @Override
            public boolean isConnected() {
                return true;
            }

            @Override
            public SlackMessageHandle<SlackChannelReply> inviteToChannel(SlackChannel channel, SlackUser user) {
              return null;
            }

            @Override
            public SlackMessageHandle sendMessageToUser(SlackUser user, String message, SlackAttachment attachment) {
                throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
            }

            @Override
            public SlackMessageHandle sendMessageToUser(String userName, String message, SlackAttachment attachment) {
                throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
            }

            @Override
            public SlackMessageHandle<GenericSlackReply> postGenericSlackCommand(Map<String, String> params, String command)
            {
                return null;
            }

            @Override
            public SlackMessageHandle<ParsedSlackReply> archiveChannel(SlackChannel channel)
            {
                return null;
            }

            @Override public SlackMessageHandle<ParsedSlackReply> unarchiveChannel(SlackChannel channel)
            {
                return null;
            }

        };
        try {
            session.connect();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void testParsingNewMessage() throws Exception {
        JsonParser parser = new JsonParser();
        JsonObject object = parser.parse(TEST_NEW_MESSAGE).getAsJsonObject();
        SlackEvent event = SlackJSONMessageParser.decode(session, object);
        Assertions.assertThat(event).isInstanceOf(SlackMessagePosted.class);
        SlackMessagePosted slackMessage = (SlackMessagePosted) event;
        Assertions.assertThat(slackMessage.getSender().getId()).isEqualTo("TESTUSER1");
        Assertions.assertThat(slackMessage.getChannel().getId()).isEqualTo("TESTCHANNEL1");
        Assertions.assertThat(slackMessage.getMessageContent()).isEqualTo("Test text 1");
        Assertions.assertThat(slackMessage.getTimeStamp()).isEqualTo("1413187521.000004");
    }

    @Test
    public void testParsingNewMessageFromIntegration() throws Exception {
        JsonParser parser = new JsonParser();
        JsonObject object = parser.parse(TEST_NEW_MESSAGE_FROM_INTEGRATION).getAsJsonObject();
        SlackEvent event = SlackJSONMessageParser.decode(session, object);
        Assertions.assertThat(event).isInstanceOf(SlackMessagePosted.class);
        SlackMessagePosted slackMessage = (SlackMessagePosted) event;
        Assertions.assertThat(slackMessage.getSender().getId()).isEqualTo("TESTINTEGRATION1");
        Assertions.assertThat(slackMessage.getChannel().getId()).isEqualTo("TESTCHANNEL1");
        Assertions.assertThat(slackMessage.getMessageContent()).isEqualTo("Test text 1");
        Assertions.assertThat(slackMessage.getTimeStamp()).isEqualTo("1413187521.000004");
    }

    @Test
    public void testParsingMessageDeleted() throws Exception {
        JsonParser parser = new JsonParser();
        JsonObject object = parser.parse(TEST_DELETED_MESSAGE).getAsJsonObject();
        SlackEvent event = SlackJSONMessageParser.decode(session, object);
        Assertions.assertThat(event).isInstanceOf(SlackMessageDeleted.class);
        SlackMessageDeleted slackMessageDeleted = (SlackMessageDeleted) event;
        Assertions.assertThat(slackMessageDeleted.getMessageTimestamp()).isEqualTo("1358878749.000002");
        Assertions.assertThat(slackMessageDeleted.getTimeStamp()).isEqualTo("1413187521.000005");
        Assertions.assertThat(slackMessageDeleted.getChannel().getId()).isEqualTo("TESTCHANNEL1");
    }

    @Test
    public void testParsingMessageChanged() throws Exception {
        JsonParser parser = new JsonParser();
        JsonObject object = parser.parse(TEST_UPDATED_MESSAGE).getAsJsonObject();
        SlackEvent event = SlackJSONMessageParser.decode(session, object);
        Assertions.assertThat(event).isInstanceOf(SlackMessageUpdated.class);
        SlackMessageUpdated slackMessageUpdated = (SlackMessageUpdated) event;
        Assertions.assertThat(slackMessageUpdated.getMessageTimestamp()).isEqualTo("1413187521.000005");
        Assertions.assertThat(slackMessageUpdated.getTimeStamp()).isEqualTo("1358878755.001234");
        Assertions.assertThat(slackMessageUpdated.getChannel().getId()).isEqualTo("TESTCHANNEL1");
        Assertions.assertThat(slackMessageUpdated.getNewMessage()).isEqualTo("newtext");
    }

    @Test
    public void testChannelCreated() throws Exception {
        JsonParser parser = new JsonParser();
        JsonObject object = parser.parse(TEST_CHANNEL_CREATED).getAsJsonObject();
        SlackEvent event = SlackJSONMessageParser.decode(session, object);
        Assertions.assertThat(event).isInstanceOf(SlackChannelCreated.class);
        SlackChannelCreated slackChannelCreated = (SlackChannelCreated) event;
        Assertions.assertThat(slackChannelCreated.getCreator().getId()).isEqualTo("TESTUSER1");
        Assertions.assertThat(slackChannelCreated.getSlackChannel().getName()).isEqualTo("new channel");
        Assertions.assertThat(slackChannelCreated.getSlackChannel().getId()).isEqualTo("NEWCHANNEL");
        Assertions.assertThat(slackChannelCreated.getSlackChannel().getTopic()).isNull();
        Assertions.assertThat(slackChannelCreated.getSlackChannel().getPurpose()).isNull();
        Assertions.assertThat(slackChannelCreated.getSlackChannel().isArchived()).isEqualTo(false);
    }

    @Test
    public void testChannelDeleted() throws Exception {
        JsonParser parser = new JsonParser();
        JsonObject object = parser.parse(TEST_CHANNEL_DELETED).getAsJsonObject();
        SlackEvent event = SlackJSONMessageParser.decode(session, object);
        Assertions.assertThat(event).isInstanceOf(SlackChannelDeleted.class);
        SlackChannelDeleted slackChannelDeleted = (SlackChannelDeleted) event;
        Assertions.assertThat(slackChannelDeleted.getSlackChannel().getId()).isEqualTo("TESTCHANNEL1");
    }

    @Test
    public void testChannelArchived() throws Exception {
        JsonParser parser = new JsonParser();
        JsonObject object = parser.parse(TEST_CHANNEL_ARCHIVED).getAsJsonObject();
        SlackEvent event = SlackJSONMessageParser.decode(session, object);
        Assertions.assertThat(event).isInstanceOf(SlackChannelArchived.class);
        SlackChannelArchived slackChannelArchived = (SlackChannelArchived) event;
        Assertions.assertThat(slackChannelArchived.getSlackChannel().getId()).isEqualTo("TESTCHANNEL1");
        Assertions.assertThat(slackChannelArchived.getUser().getId()).isEqualTo("TESTUSER1");
    }

    @Test
    public void testChannelUnarchived() throws Exception {
        JsonParser parser = new JsonParser();
        JsonObject object = parser.parse(TEST_CHANNEL_UNARCHIVED).getAsJsonObject();
        SlackEvent event = SlackJSONMessageParser.decode(session, object);
        Assertions.assertThat(event).isInstanceOf(SlackChannelUnarchived.class);
        SlackChannelUnarchived slackChannelUnarchived = (SlackChannelUnarchived) event;
        Assertions.assertThat(slackChannelUnarchived.getSlackChannel().getId()).isEqualTo("TESTCHANNEL1");
        Assertions.assertThat(slackChannelUnarchived.getUser().getId()).isEqualTo("TESTUSER1");
    }

    @Test
    public void testGroupJoined() throws Exception {
        JsonParser parser = new JsonParser();
        JsonObject object = parser.parse(TEST_GROUP_JOINED).getAsJsonObject();
        SlackEvent event = SlackJSONMessageParser.decode(session, object);
        Assertions.assertThat(event).isInstanceOf(SlackGroupJoined.class);
        SlackGroupJoined slackGroupJoined = (SlackGroupJoined) event;
        Assertions.assertThat(slackGroupJoined.getSlackChannel().getId()).isEqualTo("NEWCHANNEL");
        Assertions.assertThat(slackGroupJoined.getSlackChannel().getName()).isEqualTo("new channel");
        Assertions.assertThat(slackGroupJoined.getSlackChannel().getTopic()).isEqualTo("To have something new");
        Assertions.assertThat(slackGroupJoined.getSlackChannel().getPurpose()).isEqualTo("This channel so new it aint even old yet");
        Assertions.assertThat(slackGroupJoined.getSlackChannel().isArchived()).isEqualTo(false);
    }

    @Test
    public void shouldParseReactionAddedEvent() {
        JsonParser parser = new JsonParser();
        JsonObject object = parser.parse(TEST_REACTION_ADDED).getAsJsonObject();
        SlackEvent event = SlackJSONMessageParser.decode(session, object);
        Assertions.assertThat(event).isInstanceOf(ReactionAdded.class);
        ReactionAdded reacAdded = (ReactionAdded) event;
        Assert.assertTrue(reacAdded.getEmojiName().equals("thumbsup"));
        Assert.assertTrue(reacAdded.getMessageID().equals("1360782804.083113"));
        shouldValidateNewChannelsValues(reacAdded.getChannel());
    }

    @Test
    public void shouldParseReactionRemovedEvent() {
        JsonParser parser = new JsonParser();
        JsonObject object = parser.parse(TEST_REACTION_REMOVED).getAsJsonObject();
        SlackEvent event = SlackJSONMessageParser.decode(session, object);
        Assertions.assertThat(event).isInstanceOf(ReactionRemoved.class);
        ReactionRemoved reacRemoved = (ReactionRemoved) event;
        Assert.assertTrue(reacRemoved.getEmojiName().equals("thumbsup"));
        Assert.assertTrue(reacRemoved.getMessageID().equals("1360782804.083113"));
        shouldValidateNewChannelsValues(reacRemoved.getChannel());

    }

    private void shouldValidateNewChannelsValues(SlackChannel channel) {
        Assert.assertTrue(channel.getId().equals("NEWCHANNEL"));
        Assert.assertTrue(channel.getName().equals("new channel"));
        Assert.assertTrue(channel.getPurpose().equals("This channel so new it aint even old yet"));
        Assert.assertTrue(channel.getTopic().equals("To have something new"));
        Assertions.assertThat(channel.isArchived()).isEqualTo(false);
    }

    @Test
    public void testUserChange() {
        JsonParser parser = new JsonParser();
        JsonObject object = parser.parse(TEST_USER_CHANGE).getAsJsonObject();
        SlackEvent event = SlackJSONMessageParser.decode(session, object);
        Assertions.assertThat(event).isInstanceOf(SlackUserChange.class);
        SlackUserChange slackUserChange = (SlackUserChange)event;
        SlackUser user = slackUserChange.getUser();
        Assertions.assertThat(user).isNotNull();
        Assertions.assertThat(user.getId()).isEqualTo("TESTUSER1");
        Assertions.assertThat(session.findUserById("TESTUSER1").getUserName()).isEqualTo(user.getUserName());
    }

    @Test
    public void testTeamJoin() {
        JsonParser parser = new JsonParser();
        JsonObject object = parser.parse(TEST_TEAM_JOIN).getAsJsonObject();
        SlackEvent event = SlackJSONMessageParser.decode(session, object);
        Assertions.assertThat(event).isInstanceOf(SlackTeamJoin.class);
        SlackTeamJoin slackTeamJoin = (SlackTeamJoin)event;
        SlackUser user = slackTeamJoin.getUser();
        Assertions.assertThat(user).isNotNull();
        Assertions.assertThat(user.getId()).isEqualTo("TESTUSER1");
        Assertions.assertThat(session.findUserById("TESTUSER1").getUserName()).isEqualTo(user.getUserName());
    }

    @Test
    public void testAttachment() {
        JsonParser parser = new JsonParser();
        JsonElement object = parser.parse(TEST_ATTACHMENT);
        SlackEvent event = SlackJSONMessageParser.decode(session, object.getAsJsonObject());
        Assertions.assertThat(event).isInstanceOf(SlackMessagePosted.class);
        SlackMessagePosted slackMessage = (SlackMessagePosted) event;
        Assertions.assertThat(slackMessage.getAttachments()).isNotNull();
        Assertions.assertThat(slackMessage.getAttachments().size() == 1);

        SlackAttachment attachment = slackMessage.getAttachments().get(0);

        Assertions.assertThat(attachment.getFallback()).isEqualTo("Required plain-text summary of the attachment.");
        Assertions.assertThat(attachment.getColor()).isEqualTo("#36a64f");
        Assertions.assertThat(attachment.getPretext()).isEqualTo("Optional text that appears above the attachment block");
        Assertions.assertThat(attachment.getAuthorName()).isEqualTo("Bobby Tables");
        Assertions.assertThat(attachment.getAuthorLink()).isEqualTo("http://flickr.com/bobby/");
        Assertions.assertThat(attachment.getAuthorIcon()).isEqualTo("http://flickr.com/icons/bobby.jpg");
        Assertions.assertThat(attachment.getTitle()).isEqualTo("Slack API Documentation");
        Assertions.assertThat(attachment.getTitleLink()).isEqualTo("https://api.slack.com/");
        Assertions.assertThat(attachment.getText()).isEqualTo("Optional text that appears within the attachment");
        Assertions.assertThat(attachment.getThumbUrl()).isEqualTo("http://example.com/path/to/thumb.png");
        Assertions.assertThat(attachment.getFooter()).isEqualTo("Slack API");
        Assertions.assertThat(attachment.getFooterIcon()).isEqualTo("https://platform.slack-edge.com/img/default_application_icon.png");

        Assertions.assertThat(attachment.getFields().size()).isEqualTo(1);

        SlackField field = attachment.getFields().get(0);

        Assertions.assertThat(field.getTitle()).isEqualTo("Priority");
        Assertions.assertThat(field.getValue()).isEqualTo("High");
        Assertions.assertThat(field.isShort()).isEqualTo(false);
    }

    @Test
    public void testUserTyping() {
        JsonParser jsonParser = new JsonParser();
        JsonElement object = jsonParser.parse(TEST_USER_TYPING);
        SlackEvent slackEvent = SlackJSONMessageParser.decode(session, object.getAsJsonObject());

        Assertions.assertThat(slackEvent).isInstanceOf(UserTyping.class);

        UserTyping userTyping = (UserTyping) slackEvent;

        Assertions.assertThat(userTyping.getChannel().getId()).isEqualTo("TESTCHANNEL1");
        Assertions.assertThat(userTyping.getChannel().isArchived()).isEqualTo(false);
        Assertions.assertThat(userTyping.getUser().getId()).isEqualTo("TESTUSER3");
    }
}
