package com.ullink.slack.simpleslackapi.impl;

import com.ullink.slack.simpleslackapi.SlackChannel;
import com.ullink.slack.simpleslackapi.SlackMessage;
import org.junit.Test;
import static org.assertj.core.api.Assertions.*;

import java.util.List;

public class TestAbstractSlackSessionImpl
{

    private class TestSlackSessionImpl extends AbstractSlackSessionImpl
    {

        List<SlackMessage> messageSent;

        @Override
        public void connect()
        {
            channels.add(new SlackChannelImpl("channelid1", "testchannel1", "topicchannel1", "topicchannel1"));
            channels.add(new SlackChannelImpl("channelid2", "testchannel2", "topicchannel2", "topicchannel2"));
            channels.add(new SlackChannelImpl("channelid3", "testchannel3", "topicchannel3", "topicchannel3"));
            channels.add(new SlackChannelImpl("channelid4", "testchannel4", "topicchannel4", "topicchannel4"));
            channels.add(new SlackChannelImpl("channelid5", "testchannel5", "topicchannel5", "topicchannel5"));

            users.add(new SlackUserImpl("userid1", "username1", "realname1", false));
            users.add(new SlackUserImpl("userid2", "username2", "realname2", false));
            users.add(new SlackUserImpl("userid3", "username3", "realname3", true));
            users.add(new SlackUserImpl("userid4", "username4", "realname4", false));
            users.add(new SlackUserImpl("userid5", "username5", "realname4", true));

            bots.add(new SlackBotImpl("botid1", "botname1", false));
            bots.add(new SlackBotImpl("botid2", "botname2", false));
            bots.add(new SlackBotImpl("botid3", "botname2", true));

        }

        @Override
        public void sendMessage(SlackChannel channel, String message, String username, String iconURL)
        {

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
        assertThat(slackSession.findBotById("botid1").getName()).isEqualTo("botname1");
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

}
