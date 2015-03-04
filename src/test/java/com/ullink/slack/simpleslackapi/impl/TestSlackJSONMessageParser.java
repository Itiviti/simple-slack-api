package com.ullink.slack.simpleslackapi.impl;

import com.ullink.slack.simpleslackapi.SlackAttachment;
import com.ullink.slack.simpleslackapi.SlackChannel;
import com.ullink.slack.simpleslackapi.SlackMessage;
import com.ullink.slack.simpleslackapi.SlackMessageHandle;
import com.ullink.slack.simpleslackapi.SlackReply;
import com.ullink.slack.simpleslackapi.SlackSession;
import com.ullink.slack.simpleslackapi.SlackUser;
import org.assertj.core.api.Assertions;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import org.junit.Before;
import org.junit.Test;

public class TestSlackJSONMessageParser
{

    SlackSession session;

    private static final String TEST_MESSAGE = "{\"type\":\"message\",\"channel\":\"TESTCHANNEL1\",\"user\":\"TESTUSER1\",\"text\":\"Test text 1\",\"ts\":\"1413187521.000004\"}";

    @Before
    public void setup()
    {
        session = new AbstractSlackSessionImpl()
        {
            @Override
            public void connect()
            {
                SlackUser user1 = new SlackUserImpl("TESTUSER1", "test user 1", "","", false);
                SlackUser user2 = new SlackUserImpl("TESTUSER2", "test user 2", "","", false);
                SlackUser user3 = new SlackUserImpl("TESTUSER3", "test user 3", "","", false);
                users.put(user1.getId(), user1);
                users.put(user2.getId(), user2);
                users.put(user3.getId(), user3);

                SlackChannel channel1 = new SlackChannelImpl("TESTCHANNEL1", "testchannel1", null, null);
                SlackChannel channel2 = new SlackChannelImpl("TESTCHANNEL2", "testchannel2", null, null);
                SlackChannel channel3 = new SlackChannelImpl("TESTCHANNEL3", "testchannel3", null, null);
                channels.put(channel1.getId(), channel1);
                channels.put(channel2.getId(), channel2);
                channels.put(channel3.getId(), channel3);
            }

            @Override
            public SlackMessageHandle sendMessage(SlackChannel channel, String message, SlackAttachment attachment, String username, String iconURL)
            {
                throw new UnsupportedOperationException();
            }

            @Override
            public SlackMessageHandle sendMessageOverWebSocket(SlackChannel channel, String message, SlackAttachment attachment)
            {
                throw new UnsupportedOperationException();
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

        };
        session.connect();
    }

    @Test
    public void testParsingMessage() throws Exception
    {
        JSONParser parser = new JSONParser();
        JSONObject object = (JSONObject) parser.parse(TEST_MESSAGE);
        SlackMessage message = SlackJSONMessageParser.decode(session, object);
        Assertions.assertThat(message.getSender().getId()).isEqualTo("TESTUSER1");
        Assertions.assertThat(message.getChannel().getId()).isEqualTo("TESTCHANNEL1");
        Assertions.assertThat(message.getMessageContent()).isEqualTo("Test text 1");
    }
}
