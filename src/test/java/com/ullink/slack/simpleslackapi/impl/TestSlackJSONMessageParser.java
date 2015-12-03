package com.ullink.slack.simpleslackapi.impl;

import com.ullink.slack.simpleslackapi.*;
import com.ullink.slack.simpleslackapi.events.*;
import com.ullink.slack.simpleslackapi.replies.SlackChannelReply;
import org.assertj.core.api.Assertions;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.junit.Before;
import org.junit.Test;

import java.io.IOException;

public class TestSlackJSONMessageParser
{

    SlackSession                session;

    private static final String TEST_NEW_MESSAGE        = "{\"type\":\"message\",\"channel\":\"TESTCHANNEL1\",\"user\":\"TESTUSER1\",\"text\":\"Test text 1\",\"ts\":\"1413187521.000004\"}";
    private static final String TEST_DELETED_MESSAGE    = "{\"type\":\"message\",\"channel\":\"TESTCHANNEL1\",\"user\":\"TESTUSER1\",\"text\":\"Test text 1\",\"ts\":\"1413187521.000005\", \"subtype\": \"message_deleted\", \"deleted_ts\": \"1358878749.000002\"}";
    private static final String TEST_UPDATED_MESSAGE    = "{\"type\":\"message\",\"channel\":\"TESTCHANNEL1\",\"text\":\"Test text 1\",\"ts\":\"1358878755.001234\", \"subtype\": \"message_changed\", \"message\": {\"type:\" \"message\", \"user\": \"TESTUSER1\", \"text\": \"newtext\", \"ts\": \"1413187521.000005\", \"edited\": { \"user\": \"TESTUSER1\", \"ts\":\"1358878755.001234\"}}}";

    private static final String TEST_CHANNEL_CREATED    = "{\"type\":\"channel_created\",\"channel\": { \"id\": \"NEWCHANNEL\", \"name\": \"new channel\", \"creator\": \"TESTUSER1\"}}";
    private static final String TEST_CHANNEL_DELETED    = "{\"type\":\"channel_deleted\",\"channel\": \"TESTCHANNEL1\"}";

    private static final String TEST_CHANNEL_ARCHIVED   = "{\"type\":\"channel_archive\",\"channel\": \"TESTCHANNEL1\",\"user\":\"TESTUSER1\"}";
    private static final String TEST_CHANNEL_UNARCHIVED = "{\"type\":\"channel_unarchive\",\"channel\": \"TESTCHANNEL1\",\"user\":\"TESTUSER1\"}";

    private static final String TEST_GROUP_JOINED       = "{\"type\":\"group_joined\",\"channel\": { \"id\": \"NEWCHANNEL\", \"name\": \"new channel\", \"creator\": \"TESTUSER1\"}}";
    private static final String CHANNEL_HISTORY         = "{\"ok\": true,\"latest\": \"1358547726.000003\",\"messages\": [{\"type\": \"message\",\"ts\": \"1358546515.000008\",\"user\": \"U2147483896\",\"text\": \"Hello\"},{\"type\": \"message\",\"ts\": \"1358546515.000007\",\"user\": \"U2147483896\",\"text\": \"World\",\"is_starred\": true,},{\n\"type\": \"something_else\",\"ts\": \"1358546515.000007\",\"wibblr\": true}],\"has_more\": false}";

    @Before
    public void setup()
    {
        session = new AbstractSlackSessionImpl()
        {
            @Override
            public void connect()
            {
                SlackUser user1 = new SlackUserImpl("TESTUSER1", "test user 1", "", "", false, false, false, false, false, false, false, "tz", "tzLabel", new Integer(0));
                SlackUser user2 = new SlackUserImpl("TESTUSER2", "test user 2", "", "", false, false, false, false, false, false, false, "tz", "tzLabel", new Integer(0));
                SlackUser user3 = new SlackUserImpl("TESTUSER3", "test user 3", "", "", false, false, false, false, false, false, false, "tz", "tzLabel", new Integer(0));
                users.put(user1.getId(), user1);
                users.put(user2.getId(), user2);
                users.put(user3.getId(), user3);

                SlackChannel channel1 = new SlackChannelImpl("TESTCHANNEL1", "testchannel1", null, null, false);
                SlackChannel channel2 = new SlackChannelImpl("TESTCHANNEL2", "testchannel2", null, null, false);
                SlackChannel channel3 = new SlackChannelImpl("TESTCHANNEL3", "testchannel3", null, null, false);
                channels.put(channel1.getId(), channel1);
                channels.put(channel2.getId(), channel2);
                channels.put(channel3.getId(), channel3);
            }

            @Override
            public void disconnect()
            {
            }

            @Override
            public SlackMessageHandle sendMessage(SlackChannel channel, String message, SlackAttachment attachment, SlackChatConfiguration chatConfiguration)
            {
                throw new UnsupportedOperationException();
            }

            @Override
            public SlackMessageHandle sendMessageOverWebSocket(SlackChannel channel, String message, SlackAttachment attachment)
            {
                throw new UnsupportedOperationException();
            }

            @Override
            public SlackPersona.SlackPresence getPresence(SlackPersona persona)
            {
                return null;
            }

            @Override
            public SlackMessageHandle deleteMessage(String timeStamp, SlackChannel channel)
            {
                return null;
            }

            @Override
            public SlackMessageHandle updateMessage(String timeStamp, SlackChannel channel, String message)
            {
                return null;
            }

            @Override
            public SlackMessageHandle addReactionToMessage(SlackChannel channel, String messageTimeStamp, String emojiCode)
            {
                return null;
            }

            @Override
            public SlackMessageHandle joinChannel(String channelName)
            {
                return null;
            }

            @Override
            public SlackMessageHandle leaveChannel(SlackChannel channel)
            {
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
            public SlackMessageHandle inviteUser(String email, String firstName, boolean setActive) 
            {
                return null;
            }

            @Override
            public boolean isConnected() {
                return true;
            }

        };
        try
        {
            session.connect();
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }
    }

    @Test
    public void shouldParseChannelHistory() throws Exception {
        JSONParser parser = new JSONParser();
        JSONObject object = (JSONObject) parser.parse(CHANNEL_HISTORY);

        SlackChannelImpl slackChannel = new SlackChannelImpl("id", "name", "topic", "purpose", true);
        SlackChannelHistory history = SlackJSONMessageParser.decodeChannelHistory(session, slackChannel, object);

        Assertions.assertThat(history.getChannelEvents()).hasSize(3);
        Assertions.assertThat(history.getChannel()).isEqualTo(slackChannel);
        Assertions.assertThat(history.getLatest()).isEqualTo("1358547726.000003");
        Assertions.assertThat(history.hasMore()).isFalse();
    }

    @Test
    public void testParsingNewMessage() throws Exception
    {
        JSONParser parser = new JSONParser();
        JSONObject object = (JSONObject) parser.parse(TEST_NEW_MESSAGE);
        SlackEvent event = SlackJSONMessageParser.decode(session, object);
        Assertions.assertThat(event).isInstanceOf(SlackMessagePosted.class);
        SlackMessagePosted slackMessage = (SlackMessagePosted) event;
        Assertions.assertThat(slackMessage.getSender().getId()).isEqualTo("TESTUSER1");
        Assertions.assertThat(slackMessage.getChannel().getId()).isEqualTo("TESTCHANNEL1");
        Assertions.assertThat(slackMessage.getMessageContent()).isEqualTo("Test text 1");
        Assertions.assertThat(slackMessage.getTimeStamp()).isEqualTo("1413187521.000004");
    }

    @Test
    public void testParsingMessageDeleted() throws Exception
    {
        JSONParser parser = new JSONParser();
        JSONObject object = (JSONObject) parser.parse(TEST_DELETED_MESSAGE);
        SlackEvent event = SlackJSONMessageParser.decode(session, object);
        Assertions.assertThat(event).isInstanceOf(SlackMessageDeleted.class);
        SlackMessageDeleted slackMessageDeleted = (SlackMessageDeleted) event;
        Assertions.assertThat(slackMessageDeleted.getMessageTimestamp()).isEqualTo("1358878749.000002");
        Assertions.assertThat(slackMessageDeleted.getTimeStamp()).isEqualTo("1413187521.000005");
        Assertions.assertThat(slackMessageDeleted.getChannel().getId()).isEqualTo("TESTCHANNEL1");
    }

    @Test
    public void testParsingMessageChanged() throws Exception
    {
        JSONParser parser = new JSONParser();
        JSONObject object = (JSONObject) parser.parse(TEST_UPDATED_MESSAGE);
        SlackEvent event = SlackJSONMessageParser.decode(session, object);
        Assertions.assertThat(event).isInstanceOf(SlackMessageUpdatedImpl.class);
        SlackMessageUpdatedImpl slackMessageUpdated = (SlackMessageUpdatedImpl) event;
        Assertions.assertThat(slackMessageUpdated.getMessageTimestamp()).isEqualTo("1413187521.000005");
        Assertions.assertThat(slackMessageUpdated.getTimeStamp()).isEqualTo("1358878755.001234");
        Assertions.assertThat(slackMessageUpdated.getChannel().getId()).isEqualTo("TESTCHANNEL1");
        Assertions.assertThat(slackMessageUpdated.getNewMessage()).isEqualTo("newtext");
    }

    @Test
    public void testChannelCreated() throws Exception
    {
        JSONParser parser = new JSONParser();
        JSONObject object = (JSONObject) parser.parse(TEST_CHANNEL_CREATED);
        SlackEvent event = SlackJSONMessageParser.decode(session, object);
        Assertions.assertThat(event).isInstanceOf(SlackChannelCreated.class);
        SlackChannelCreated slackChannelCreated = (SlackChannelCreated) event;
        Assertions.assertThat(slackChannelCreated.getCreator().getId()).isEqualTo("TESTUSER1");
        Assertions.assertThat(slackChannelCreated.getSlackChannel().getName()).isEqualTo("new channel");
        Assertions.assertThat(slackChannelCreated.getSlackChannel().getId()).isEqualTo("NEWCHANNEL");
    }

    @Test
    public void testChannelDeleted() throws Exception
    {
        JSONParser parser = new JSONParser();
        JSONObject object = (JSONObject) parser.parse(TEST_CHANNEL_DELETED);
        SlackEvent event = SlackJSONMessageParser.decode(session, object);
        Assertions.assertThat(event).isInstanceOf(SlackChannelDeleted.class);
        SlackChannelDeleted slackChannelDeleted = (SlackChannelDeleted) event;
        Assertions.assertThat(slackChannelDeleted.getSlackChannel().getId()).isEqualTo("TESTCHANNEL1");
    }

    @Test
    public void testChannelArchived() throws Exception
    {
        JSONParser parser = new JSONParser();
        JSONObject object = (JSONObject) parser.parse(TEST_CHANNEL_ARCHIVED);
        SlackEvent event = SlackJSONMessageParser.decode(session, object);
        Assertions.assertThat(event).isInstanceOf(SlackChannelArchived.class);
        SlackChannelArchived slackChannelArchived = (SlackChannelArchived) event;
        Assertions.assertThat(slackChannelArchived.getSlackChannel().getId()).isEqualTo("TESTCHANNEL1");
        Assertions.assertThat(slackChannelArchived.getUser().getId()).isEqualTo("TESTUSER1");
    }

    @Test
    public void testChannelUnarchived() throws Exception
    {
        JSONParser parser = new JSONParser();
        JSONObject object = (JSONObject) parser.parse(TEST_CHANNEL_UNARCHIVED);
        SlackEvent event = SlackJSONMessageParser.decode(session, object);
        Assertions.assertThat(event).isInstanceOf(SlackChannelUnarchived.class);
        SlackChannelUnarchived slackChannelUnarchived = (SlackChannelUnarchived) event;
        Assertions.assertThat(slackChannelUnarchived.getSlackChannel().getId()).isEqualTo("TESTCHANNEL1");
        Assertions.assertThat(slackChannelUnarchived.getUser().getId()).isEqualTo("TESTUSER1");
    }

    @Test
    public void testGroupJoined() throws Exception
    {
        JSONParser parser = new JSONParser();
        JSONObject object = (JSONObject) parser.parse(TEST_GROUP_JOINED);
        SlackEvent event = SlackJSONMessageParser.decode(session, object);
        Assertions.assertThat(event).isInstanceOf(SlackGroupJoined.class);
        SlackGroupJoined slackGroupJoined = (SlackGroupJoined) event;
        Assertions.assertThat(slackGroupJoined.getSlackChannel().getId()).isEqualTo("NEWCHANNEL");
        Assertions.assertThat(slackGroupJoined.getSlackChannel().getName()).isEqualTo("new channel");
    }

}
