package com.ullink.slack.simpleslackapi;

import com.ullink.slack.simpleslackapi.events.SlackChannelHistory;
import com.ullink.slack.simpleslackapi.impl.SlackChatConfiguration;
import com.ullink.slack.simpleslackapi.listeners.*;
import com.ullink.slack.simpleslackapi.replies.SlackChannelReply;
import com.ullink.slack.simpleslackapi.replies.SlackMessageReply;

import java.io.IOException;
import java.util.Collection;

public interface SlackSession
{

    Collection<SlackChannel> getChannels();

    Collection<SlackUser> getUsers();

    Collection<SlackBot> getBots();

    SlackChannel findChannelByName(String channelName);

    SlackChannel findChannelById(String channelId);

    SlackUser findUserById(String userId);

    SlackUser findUserByUserName(String userName);

    SlackUser findUserByEmail(String userMail);

    SlackPersona sessionPersona();

    @Deprecated
    SlackBot findBotById(String botId);
    
    SlackMessageHandle inviteUser(String email, String firstName, boolean setActive);

    void connect() throws IOException;

    void disconnect() throws IOException;

    SlackMessageHandle<SlackMessageReply> deleteMessage(String timeStamp, SlackChannel channel);

    SlackMessageHandle<SlackMessageReply> sendMessage(SlackChannel channel, String message, SlackAttachment attachment, SlackChatConfiguration chatConfiguration);

    SlackMessageHandle<SlackMessageReply> sendMessage(SlackChannel channel, String message, SlackAttachment attachment);
    
    SlackMessageHandle<SlackMessageReply> updateMessage(String timeStamp, SlackChannel channel, String message);

    SlackMessageHandle<SlackMessageReply> sendMessageOverWebSocket(SlackChannel channel, String message, SlackAttachment attachment);

    SlackMessageHandle<SlackMessageReply> addReactionToMessage(SlackChannel channel, String messageTimeStamp, String emojiCode);

    SlackMessageHandle<SlackChannelReply> joinChannel(String channelName);

    SlackMessageHandle<SlackChannelReply> leaveChannel(SlackChannel channel);

    SlackMessageHandle<SlackChannelReply> openDirectMessageChannel(SlackUser user);

    SlackMessageHandle<SlackChannelReply> openMultipartyDirectMessageChannel(SlackUser... users);

    SlackPersona.SlackPresence getPresence(SlackPersona persona);

    void addchannelArchivedListener(SlackChannelArchivedListener listener);

    void removeChannelArchivedListener(SlackChannelArchivedListener listener);

    void addchannelCreatedListener(SlackChannelCreatedListener listener);

    void removeChannelCreatedListener(SlackChannelCreatedListener listener);

    void addchannelDeletedListener(SlackChannelDeletedListener listener);

    void removeChannelDeletedListener(SlackChannelDeletedListener listener);

    void addChannelRenamedListener(SlackChannelRenamedListener listener);

    void removeChannelRenamedListener(SlackChannelRenamedListener listener);

    void addChannelUnarchivedListener(SlackChannelUnarchivedListener listener);

    void removeChannelUnarchivedListener(SlackChannelUnarchivedListener listener);

    void addMessageDeletedListener(SlackMessageDeletedListener listener);

    void removeMessageDeletedListener(SlackMessageDeletedListener listener);

    void addMessagePostedListener(SlackMessagePostedListener listener);

    void removeMessagePostedListener(SlackMessagePostedListener listener);

    void addMessageUpdatedListener(SlackMessageUpdatedListener listener);

    void removeMessageUpdatedListener(SlackMessageUpdatedListener listener);

    void addGroupJoinedListener(SlackGroupJoinedListener listener);

    void removeGroupJoinedListener(SlackGroupJoinedListener listener);

    /*
     * Subscribe to events related to the connection to the slack
     * server. At this time a set of status information is exchanged that
     * is useful to implementing bots.
     * 
     * For example, the current user that is connecting.
     * knowing your own user id will help you stop answering your own
     * questions.
     */
    void addSlackConnectedListener(SlackConnectedListener listner);
    
    void removeSlackConnectedListener(SlackConnectedListener listener);

    /**
     * 
     * @return true if connection is open
     */
    boolean isConnected();

    /**
     * Fetches history messages from channel
     *
     * @param slackChannel
     * @param oldest - timestamp (epoch seconds) of oldest message. Optional, if null will be ignored
     * @param latest - timestamp (epoch seconds) of latest message. Optional, if null will be ignored
     *
     * @return history with collection of channel events, flag if there more messages, timestamp.
     */

    SlackChannelHistory fetchChannelHistory(SlackChannel slackChannel, String oldest, String latest);
}
