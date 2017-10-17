package com.ullink.slack.simpleslackapi.impl;

import com.ullink.slack.simpleslackapi.*;
import com.ullink.slack.simpleslackapi.events.SlackConnected;
import com.ullink.slack.simpleslackapi.listeners.SlackConnectedListener;
import com.ullink.slack.simpleslackapi.replies.*;
import org.junit.Test;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

public class TestAbstractSlackSessionImpl
{

    private class TestSlackSessionImpl extends AbstractSlackSessionImpl
    {
        @Override
        public void setPresence(SlackPersona.SlackPresence presence) {
        }
        @Override
        public void connect()
        {
            channels.put("channelid1",new SlackChannel("channelid1", "testchannel1", "topicchannel1", "topicchannel1", false, false, false));
            channels.put("channelid2",new SlackChannel("channelid2", "testchannel2", "topicchannel2", "topicchannel2", false, false, false));
            channels.put("channelid3",new SlackChannel("channelid3", "testchannel3", "topicchannel3", "topicchannel3", false, false, false));
            channels.put("channelid4",new SlackChannel("channelid4", "testchannel4", "topicchannel4", "topicchannel4", false, false, false));
            channels.put("channelid5",new SlackChannel("channelid5", "testchannel5", "topicchannel5", "topicchannel5", false, false, false));

            users.put("userid1",new SlackUserImpl("userid1", "username1", "realname1","userid1@my.mail", "testSkype", "testPhone", "testTitle", false,false,false,false,false,false, false,"tz","tzLabel",new Integer(0), SlackPersona.SlackPresence.ACTIVE));
            users.put("userid2",new SlackUserImpl("userid2", "username2", "realname2","userid2@my.mail", "testSkype", "testPhone", "testTitle", false,false,false,false,false,false, false,"tz","tzLabel",new Integer(0), SlackPersona.SlackPresence.ACTIVE));
            users.put("userid3",new SlackUserImpl("userid3", "username3", "realname3","userid3@my.mail", "testSkype", "testPhone", "testTitle", true,false,false,false,false,false, false,"tz","tzLabel",new Integer(0), SlackPersona.SlackPresence.ACTIVE));
            users.put("userid4",new SlackUserImpl("userid4", "username4", "realname4","userid4@my.mail", "testSkype", "testPhone", "testTitle", false,false,false,false,false,false, false,"tz","tzLabel",new Integer(0), SlackPersona.SlackPresence.ACTIVE));
            users.put("userid5",new SlackUserImpl("userid5", "username5", "realname4","userid5@my.mail", "testSkype", "testPhone", "testTitle", true,false,false,false,false,false, false,"tz","tzLabel",new Integer(0), SlackPersona.SlackPresence.ACTIVE));

            users.put("botid1",new SlackUserImpl("botid1", "botname1", "real bot name 1", null, "testSkype", "testPhone", "testTitle", false,false,false,false,false,false,true,"tz","tzLabel",new Integer(0), SlackPersona.SlackPresence.ACTIVE));
            users.put("botid2",new SlackUserImpl("botid2", "botname2", "real bot name 2", null, "testSkype", "testPhone", "testTitle", false,false,false,false,false,false,true,"tz","tzLabel",new Integer(0), SlackPersona.SlackPresence.ACTIVE));
            users.put("botid3",new SlackUserImpl("botid3", "botname3", "real bot name 3", null, "testSkype", "testPhone", "testTitle", true,false,false,false,false,false,true,"tz","tzLabel",new Integer(0), SlackPersona.SlackPresence.ACTIVE));
        }

        @Override
        public void disconnect()
        {
        }

        @Override
        public SlackMessageHandle sendMessageOverWebSocket(SlackChannel channel, String message)
        {
            return null;
        }

        @Override
        public SlackMessageHandle<SlackMessageReply> sendTyping(SlackChannel channel) {
           return null;
        }

        @Override
        public SlackPersona.SlackPresence getPresence(SlackPersona persona) {
            return null;
        }

        @Override
        public SlackMessageHandle deleteMessage(String timeStamp, SlackChannel channel)
        {
            return null;
        }

        @Override
        public SlackMessageHandle<SlackMessageReply> sendMessage(SlackChannel channel, SlackPreparedMessage preparedMessage, SlackChatConfiguration chatConfiguration) {
            return null;
        }

        @Override
        public SlackMessageHandle<SlackMessageReply> sendEphemeralMessage(SlackChannel channel, SlackUser user, SlackPreparedMessage preparedMessage, SlackChatConfiguration chatConfiguration) {
            return null;
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
        public SlackMessageHandle updateMessage(String timeStamp, SlackChannel channel, String message)
        {
            return null;
        }

        @Override
        public SlackMessageHandle updateMessage(String timeStamp, SlackChannel channel, String message, SlackAttachment[] attachments)
        {
            return null;
        }

        @Override
        public SlackMessageHandle addReactionToMessage(SlackChannel channel, String messageTimeStamp, String emojiCode)
        {
            return null;
        }

        @Override public SlackMessageHandle<SlackMessageReply> removeReactionFromMessage(SlackChannel channel, String messageTimeStamp, String emojiCode)
        {
            return null;
        }

        @Override
        public SlackMessageHandle joinChannel(String channelName)
        {
            return null;
        }

        @Override
        public SlackMessageHandle<SlackChannelReply> setChannelTopic(SlackChannel channel, String topic) {
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

        // Helper method with access to abstract class properties.
        public boolean isListening(SlackConnectedListener expectedListener) 
        {
          return slackConnectedListener.contains(expectedListener);
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
        public SlackMessageHandle<ParsedSlackReply> archiveChannel(SlackChannel channel)
        {
          return null;
        }

        @Override public SlackMessageHandle<ParsedSlackReply> unarchiveChannel(SlackChannel channel)
        {
            return null;
        }

        @Override
        public SlackMessageHandle<GenericSlackReply> postGenericSlackCommand(java.util.Map<String,String> params, String command) {
            throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
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
        public long getHeartbeat() {
            return 0;
        }

    }

    @Test
    public void testFindChannelByName_ExistingChannel()
    {
        TestSlackSessionImpl slackSession = new TestSlackSessionImpl();

        slackSession.connect();

        assertThat(slackSession.findChannelByName("testchannel1")).isNotNull();
        assertThat(slackSession.findChannelByName("testchannel1").getId()).isEqualTo("channelid1");
    }

    @Test
    public void testFindChannelByName_MissingChannel()
    {
        TestSlackSessionImpl slackSession = new TestSlackSessionImpl();

        slackSession.connect();

        assertThat(slackSession.findChannelByName("unknownChannel")).isNull();
    }

    @Test
    public void testFindChannelById_ExistingChannel()
    {
        TestSlackSessionImpl slackSession = new TestSlackSessionImpl();

        slackSession.connect();

        assertThat(slackSession.findChannelById("channelid1")).isNotNull();
        assertThat(slackSession.findChannelById("channelid1").getName()).isEqualTo("testchannel1");
    }

    @Test
    public void testFindChannelById_MissingChannel()
    {
        TestSlackSessionImpl slackSession = new TestSlackSessionImpl();

        slackSession.connect();

        assertThat(slackSession.findChannelByName("unknownChannel")).isNull();
    }

    @Test
    public void testFindBotById_ExistingBot()
    {
        TestSlackSessionImpl slackSession = new TestSlackSessionImpl();

        slackSession.connect();

        assertThat(slackSession.findBotById("botid1")).isNotNull();
        assertThat(slackSession.findBotById("botid1").getUserName()).isEqualTo("botname1");
    }

    @Test
    public void testFindBotById_MissingBot()
    {
        TestSlackSessionImpl slackSession = new TestSlackSessionImpl();

        slackSession.connect();

        assertThat(slackSession.findBotById("unknownbot")).isNull();
    }

    @Test
    public void testFindUserById_ExistingBot()
    {
        TestSlackSessionImpl slackSession = new TestSlackSessionImpl();

        slackSession.connect();

        assertThat(slackSession.findUserById("userid1")).isNotNull();
        assertThat(slackSession.findUserById("userid1").getUserName()).isEqualTo("username1");
    }

    @Test
    public void testFindUserById_MissingBot()
    {
        TestSlackSessionImpl slackSession = new TestSlackSessionImpl();

        slackSession.connect();

        assertThat(slackSession.findBotById("unknownuser")).isNull();
    }

    @Test
    public void testFindUserByUserName_ExistingBot()
    {
        TestSlackSessionImpl slackSession = new TestSlackSessionImpl();

        slackSession.connect();

        assertThat(slackSession.findUserByUserName("username1")).isNotNull();
        assertThat(slackSession.findUserByUserName("username1").getId()).isEqualTo("userid1");
    }

    @Test
    public void testFindUserByUserName_MissingBot()
    {
        TestSlackSessionImpl slackSession = new TestSlackSessionImpl();

        slackSession.connect();

        assertThat(slackSession.findUserByUserName("unknownuser")).isNull();
    }

    @Test
    public void testAddConnectedListener() {
        SlackConnectedListener listener = new SlackConnectedListener() {
          @Override
          public void onEvent(SlackConnected event, SlackSession session) {
          }
        };

        TestSlackSessionImpl slackSession = new TestSlackSessionImpl();
        slackSession.addSlackConnectedListener(listener);

        assertThat(slackSession.isListening(listener)).isTrue();
    }

    @Test
    public void testRemoveConnectedListener() {
        SlackConnectedListener listener = new SlackConnectedListener() {
          @Override
          public void onEvent(SlackConnected event, SlackSession session) {
          }
        };

        TestSlackSessionImpl slackSession = new TestSlackSessionImpl();
        slackSession.addSlackConnectedListener(listener);
        slackSession.removeSlackConnectedListener(listener);

        assertThat(slackSession.isListening(listener)).isFalse();
    }
}
