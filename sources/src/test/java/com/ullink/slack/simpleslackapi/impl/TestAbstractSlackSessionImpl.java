package com.ullink.slack.simpleslackapi.impl;

import com.ullink.slack.simpleslackapi.*;
import com.ullink.slack.simpleslackapi.blocks.Block;
import com.ullink.slack.simpleslackapi.events.SlackConnected;
import com.ullink.slack.simpleslackapi.listeners.SlackConnectedListener;
import com.ullink.slack.simpleslackapi.replies.*;
import org.junit.Test;

import java.io.InputStream;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

public class TestAbstractSlackSessionImpl
{

    private static class TestSlackSessionImpl extends AbstractSlackSessionImpl
    {
        @Override
        public void setPresence(SlackPresence presence) {
        }

        /**
         * connect function creates the preconditions required for testing slack session functions
         *
         * CS427 Issue link: https://github.com/Itiviti/simple-slack-api/issues/264
         */
        @Override
        public void connect()
        {
            channels.put("channelid1",new SlackChannel("channelid1", "testchannel1", "topicchannel1", "topicchannel1", false, false, false));
            channels.put("channelid2",new SlackChannel("channelid2", "testchannel2", "topicchannel2", "topicchannel2", false, false, false));
            channels.put("channelid3",new SlackChannel("channelid3", "testchannel3", "topicchannel3", "topicchannel3", false, false, false));
            channels.put("channelid4",new SlackChannel("channelid4", "testchannel4", "topicchannel4", "topicchannel4", false, false, false));
            channels.put("channelid5",new SlackChannel("channelid5", "testchannel5", "topicchannel5", "topicchannel5", false, false, false));

            users.put("userid1",SlackPersonaImpl.builder().id("userid1").userName("username1").profile(SlackProfileImpl.builder().presence(SlackPresence.ACTIVE).skype("testSkype").phone("testPhone").title("testTitle").email("username1@gmail.com").build()).timeZone("tz").timeZoneLabel("txLabel").build());
            users.put("userid2",SlackPersonaImpl.builder().id("userid2").userName("username2").profile(SlackProfileImpl.builder().presence(SlackPresence.ACTIVE).skype("testSkype").phone("testPhone").title("testTitle").email("username2@gmail.com").build()).timeZone("tz").timeZoneLabel("txLabel").build());
            users.put("userid3",SlackPersonaImpl.builder().id("userid3").userName("username3").profile(SlackProfileImpl.builder().presence(SlackPresence.ACTIVE).skype("testSkype").phone("testPhone").title("testTitle").email("username3@gmail.com").build()).timeZone("tz").timeZoneLabel("txLabel").build());
            users.put("userid4",SlackPersonaImpl.builder().id("userid4").userName("username4").profile(SlackProfileImpl.builder().presence(SlackPresence.ACTIVE).skype("testSkype").phone("testPhone").title("testTitle").email("username4@gmail.com").build()).timeZone("tz").timeZoneLabel("txLabel").build());
            users.put("userid5",SlackPersonaImpl.builder().id("userid5").userName("username5").profile(SlackProfileImpl.builder().presence(SlackPresence.ACTIVE).skype("testSkype").phone("testPhone").title("testTitle").email("username5@gmail.com").build()).timeZone("tz").timeZoneLabel("txLabel").build());
            users.put("botid1",SlackPersonaImpl.builder().bot(true).id("botid1").userName("botname1").profile(SlackProfileImpl.builder().realName("real bot name 1").presence(SlackPresence.ACTIVE).skype("testSkype").phone("testPhone").title("testTitle").build()).timeZone("tz").timeZoneLabel("txLabel").build());
            users.put("botid2",SlackPersonaImpl.builder().bot(true).id("botid2").userName("botname2").profile(SlackProfileImpl.builder().realName("real bot name 2").presence(SlackPresence.ACTIVE).skype("testSkype").phone("testPhone").title("testTitle").build()).timeZone("tz").timeZoneLabel("txLabel").build());
            users.put("botid3",SlackPersonaImpl.builder().bot(true).id("botid3").userName("botname3").profile(SlackProfileImpl.builder().realName("real bot name 3").presence(SlackPresence.ACTIVE).skype("testSkype").phone("testPhone").title("testTitle").build()).timeZone("tz").timeZoneLabel("txLabel").build());
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
        public SlackMessageHandle<SlackMessageReply> sendTyping(String channelId) {
            return null;
        }

        @Override
        public SlackPresence getPresence(SlackPersona persona) {
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
        public SlackMessageHandle<SlackMessageReply> deleteMessage(String timeStamp, String channelId) {
            return null;
        }

        @Override
        public SlackMessageHandle<SlackMessageReply> sendMessage(String channelId, SlackPreparedMessage preparedMessage, SlackChatConfiguration chatConfiguration) {
            return null;
        }

        @Override
        public SlackMessageHandle<SlackMessageReply> sendMessage(String channelId, SlackPreparedMessage preparedMessage) {
            return null;
        }

        @Override
        public SlackMessageHandle<SlackMessageReply> sendMessage(String channelId, String message, SlackAttachment attachment, SlackChatConfiguration chatConfiguration, boolean unfurl) {
            return null;
        }

        @Override
        public SlackMessageHandle<SlackMessageReply> sendMessage(String channelId, String message, SlackAttachment attachment, SlackChatConfiguration chatConfiguration) {
            return null;
        }

        @Override
        public SlackMessageHandle<SlackMessageReply> sendMessage(String channelId, String message, SlackAttachment attachment, boolean unfurl) {
            return null;
        }

        @Override
        public SlackMessageHandle<SlackMessageReply> sendMessage(String channelId, String message, SlackAttachment attachment) {
            return null;
        }

        @Override
        public SlackMessageHandle<SlackMessageReply> sendMessage(String channelId, String message, boolean unfurl) {
            return null;
        }

        @Override
        public SlackMessageHandle<SlackMessageReply> sendMessage(String channelId, String message) {
            return null;
        }

        @Override
        public SlackMessageHandle<SlackMessageReply> sendEphemeralMessage(SlackChannel channel, SlackUser user, SlackPreparedMessage preparedMessage, SlackChatConfiguration chatConfiguration) {
            return null;
        }

        @Override
        public SlackMessageHandle<SlackMessageReply> sendFile(SlackChannel channel, InputStream data, String fileName)
        {
            return null;
        }

        @Override
        public SlackMessageHandle<SlackMessageReply> sendFile(SlackChannel channel, InputStream data, String fileName, String title, String initialComment)
        {
            return null;
        }

        @Override
        public SlackMessageHandle<SlackMessageReply> sendEphemeralMessage(String channelId, String userName, SlackPreparedMessage preparedMessage, SlackChatConfiguration chatConfiguration) {
            return null;
        }

        @Override
        public SlackMessageHandle<SlackMessageReply> sendEphemeralMessage(String channelId, String userName, SlackPreparedMessage preparedMessage) {
            return null;
        }

        @Override
        public SlackMessageHandle<SlackMessageReply> sendEphemeralMessage(String channelId, String userName, String message, SlackAttachment attachment, SlackChatConfiguration chatConfiguration, boolean unfurl) {
            return null;
        }

        @Override
        public SlackMessageHandle<SlackMessageReply> sendEphemeralMessage(String channelId, String userName, String message, SlackAttachment attachment, SlackChatConfiguration chatConfiguration) {
            return null;
        }

        @Override
        public SlackMessageHandle<SlackMessageReply> sendEphemeralMessage(String channelId, String userName, String message, SlackAttachment attachment, boolean unfurl) {
            return null;
        }

        @Override
        public SlackMessageHandle<SlackMessageReply> sendEphemeralMessage(String channelId, String userName, String message, SlackAttachment attachment) {
            return null;
        }

        @Override
        public SlackMessageHandle<SlackMessageReply> sendEphemeralMessage(String channelId, String userName, String message, boolean unfurl) {
            return null;
        }

        @Override
        public SlackMessageHandle<SlackMessageReply> sendEphemeralMessage(String channelId, String userName, String message) {
            return null;
        }

        @Override
        public SlackMessageHandle<SlackMessageReply> sendFile(String channelId, InputStream data, String fileName) {
            return null;
        }

        @Override
        public SlackMessageHandle<SlackMessageReply> sendFile(String channelId, InputStream data, String fileName, String title, String initialComment) {
            return null;
        }

        @Override
        public SlackMessageHandle<SlackMessageReply> sendFileToUser(SlackUser user, InputStream data, String fileName)
        {
            return null;
        }

        @Override
        public SlackMessageHandle<SlackMessageReply> sendFileToUser(String userName, InputStream data, String fileName)
        {
            return null;
        }

        @Override
        public SlackMessageHandle<SlackMessageReply> sendMessageToUser(SlackUser user, SlackPreparedMessage preparedMessage) {
            return null;
        }

        @Override
        public SlackMessageHandle<SlackMessageReply> sendMessageToUser(String userName, SlackPreparedMessage preparedMessage) {
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
        public SlackMessageHandle updateMessage(String timeStamp, SlackChannel channel, String message, SlackAttachment[] attachments, List<Block> blocks)
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
        public SlackMessageHandle<SlackChannelReply> inviteToChannel(String channelId, String userName) {
            return null;
        }

        @Override
        public SlackMessageHandle<SlackChannelReply> leaveChannel(String channelId) {
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
        public SlackMessageHandle<SlackMessageReply> updateMessage(String timeStamp, String channelId, String message) {
            return null;
        }

        @Override
        public SlackMessageHandle<SlackMessageReply> updateMessage(String timeStamp, String channelId, String message, SlackAttachment[] attachments) {
            return null;
        }

        @Override
        public SlackMessageHandle<SlackMessageReply> updateMessage(String timeStamp, String channelId, String message, SlackAttachment[] attachments, List<Block> blocks) {
            return null;
        }

        @Override
        public SlackMessageHandle<SlackMessageReply> sendMessageOverWebSocket(String channelId, String message) {
            return null;
        }

        @Override
        public SlackMessageHandle<SlackMessageReply> addReactionToMessage(String channelId, String messageTimeStamp, String emojiCode) {
            return null;
        }

        @Override
        public SlackMessageHandle<SlackMessageReply> removeReactionFromMessage(String channelId, String messageTimeStamp, String emojiCode) {
            return null;
        }

        @Override
        public SlackMessageHandle<SlackChannelReply> setChannelTopic(String channelId, String topic) {
            return null;
        }

        @Override
        public SlackMessageHandle<SlackChannelReply> inviteToChannel(String channelId, SlackUser user) {
            return null;
        }

        @Override
        public SlackMessageHandle<ParsedSlackReply> archiveChannel(String channelId) {
            return null;
        }

        @Override
        public SlackMessageHandle<ParsedSlackReply> unarchiveChannel(String channelId) {
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

    /**
     * Test the method findUserByEmail() for an existing bot
     *
     * CS427 Issue link: https://github.com/Itiviti/simple-slack-api/issues/264
     */
    @Test
    public void testFindUserByEmail_ExistingBot()
    {
        TestSlackSessionImpl slackSession = new TestSlackSessionImpl();

        slackSession.connect();

        // case-sensitive
        SlackUser slackUser = slackSession.findUserByEmail("username1@gmail.com");
        assertThat(slackUser).isNotNull();
        assertThat(slackUser.getId()).isEqualTo("userid1");

        // case-insensitive
        slackUser = slackSession.findUserByEmail("UsernaMe1@gmail.com");
        assertThat(slackUser).isNotNull();
        assertThat(slackUser.getId()).isEqualTo("userid1");
    }

    /**
     * Test the method findUserByEmail() for a missing bot
     *
     * CS427 Issue link: https://github.com/Itiviti/simple-slack-api/issues/264
     */
    @Test
    public void testFindUserByEmail_MissingBot()
    {
        TestSlackSessionImpl slackSession = new TestSlackSessionImpl();

        slackSession.connect();

        assertThat(slackSession.findUserByEmail("unknownuser@gmail.com")).isNull();
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
