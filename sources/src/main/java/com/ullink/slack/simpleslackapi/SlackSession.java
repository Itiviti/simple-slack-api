package com.ullink.slack.simpleslackapi;

import com.ullink.slack.simpleslackapi.blocks.Block;
import com.ullink.slack.simpleslackapi.listeners.*;
import com.ullink.slack.simpleslackapi.replies.*;

import java.io.IOException;
import java.io.InputStream;
import java.util.Collection;
import java.util.List;
import java.util.Map;

public interface SlackSession {

    SlackTeam getTeam();

    Collection<SlackChannel> getChannels();

    Collection<SlackUser> getUsers();

    Collection<SlackBot> getBots();

    Collection<SlackIntegration> getIntegrations();

    SlackChannel findChannelByName(String channelName);

    SlackChannel findChannelById(String channelId);

    SlackIntegration findIntegrationById(String integrationId);

    SlackUser findUserById(String userId);

    SlackUser findUserByUserName(String userName);

    SlackUser findUserByEmail(String userMail);

    SlackPersona sessionPersona();

    SlackMessageHandle<EmojiSlackReply> listEmoji();

    void refetchUsers();

    @Deprecated
    SlackBot findBotById(String botId);
    

    SlackMessageHandle<ParsedSlackReply> inviteUser(String email, String firstName, boolean setActive);

    void connect() throws IOException;

    void disconnect() throws IOException;

    SlackMessageHandle<SlackMessageReply> deleteMessage(String timeStamp, SlackChannel channel);

    SlackMessageHandle<SlackMessageReply> sendMessage(SlackChannel channel, SlackPreparedMessage preparedMessage, SlackChatConfiguration chatConfiguration);

    SlackMessageHandle<SlackMessageReply> sendMessage(SlackChannel channel, SlackPreparedMessage preparedMessage);

    SlackMessageHandle<SlackMessageReply> sendMessage(SlackChannel channel, String message, SlackAttachment attachment, SlackChatConfiguration chatConfiguration, boolean unfurl);

    SlackMessageHandle<SlackMessageReply> sendMessage(SlackChannel channel, String message, SlackAttachment attachment, SlackChatConfiguration chatConfiguration);

    SlackMessageHandle<SlackMessageReply> sendMessage(SlackChannel channel, String message, SlackAttachment attachment, boolean unfurl);

    SlackMessageHandle<SlackMessageReply> sendMessage(SlackChannel channel, String message, SlackAttachment attachment);

    SlackMessageHandle<SlackMessageReply> sendMessage(SlackChannel channel, String message, boolean unfurl);

    SlackMessageHandle<SlackMessageReply> sendMessage(SlackChannel channel, String message);

    SlackMessageHandle<SlackMessageReply> deleteMessage(String timeStamp, String channelId);

    SlackMessageHandle<SlackMessageReply> sendMessage(String channelId, SlackPreparedMessage preparedMessage, SlackChatConfiguration chatConfiguration);

    SlackMessageHandle<SlackMessageReply> sendMessage(String channelId, SlackPreparedMessage preparedMessage);

    SlackMessageHandle<SlackMessageReply> sendMessage(String channelId, String message, SlackAttachment attachment, SlackChatConfiguration chatConfiguration, boolean unfurl);

    SlackMessageHandle<SlackMessageReply> sendMessage(String channelId, String message, SlackAttachment attachment, SlackChatConfiguration chatConfiguration);

    SlackMessageHandle<SlackMessageReply> sendMessage(String channelId, String message, SlackAttachment attachment, boolean unfurl);

    SlackMessageHandle<SlackMessageReply> sendMessage(String channelId, String message, SlackAttachment attachment);

    SlackMessageHandle<SlackMessageReply> sendMessage(String channelId, String message, boolean unfurl);

    SlackMessageHandle<SlackMessageReply> sendMessage(String channelId, String message);

    SlackMessageHandle<SlackMessageReply> sendEphemeralMessage(SlackChannel channel, SlackUser user, SlackPreparedMessage preparedMessage, SlackChatConfiguration chatConfiguration);

    SlackMessageHandle<SlackMessageReply> sendEphemeralMessage(SlackChannel channel, SlackUser user, SlackPreparedMessage preparedMessage);

    SlackMessageHandle<SlackMessageReply> sendEphemeralMessage(SlackChannel channel, SlackUser user, String message, SlackAttachment attachment, SlackChatConfiguration chatConfiguration, boolean unfurl);

    SlackMessageHandle<SlackMessageReply> sendEphemeralMessage(SlackChannel channel, SlackUser user, String message, SlackAttachment attachment, SlackChatConfiguration chatConfiguration);

    SlackMessageHandle<SlackMessageReply> sendEphemeralMessage(SlackChannel channel, SlackUser user, String message, SlackAttachment attachment, boolean unfurl);

    SlackMessageHandle<SlackMessageReply> sendEphemeralMessage(SlackChannel channel, SlackUser user, String message, SlackAttachment attachment);

    SlackMessageHandle<SlackMessageReply> sendEphemeralMessage(SlackChannel channel, SlackUser user, String message, boolean unfurl);

    SlackMessageHandle<SlackMessageReply> sendEphemeralMessage(SlackChannel channel, SlackUser user, String message);

    SlackMessageHandle<SlackMessageReply> sendFile(SlackChannel channel, InputStream data, String fileName);

    SlackMessageHandle<SlackMessageReply> sendFile(SlackChannel channel, InputStream data, String fileName, String title, String initialComment);

    SlackMessageHandle<SlackMessageReply> sendEphemeralMessage(String channelId, String userName, SlackPreparedMessage preparedMessage, SlackChatConfiguration chatConfiguration);

    SlackMessageHandle<SlackMessageReply> sendEphemeralMessage(String channelId, String userName, SlackPreparedMessage preparedMessage);

    SlackMessageHandle<SlackMessageReply> sendEphemeralMessage(String channelId, String userName, String message, SlackAttachment attachment, SlackChatConfiguration chatConfiguration, boolean unfurl);

    SlackMessageHandle<SlackMessageReply> sendEphemeralMessage(String channelId, String userName, String message, SlackAttachment attachment, SlackChatConfiguration chatConfiguration);

    SlackMessageHandle<SlackMessageReply> sendEphemeralMessage(String channelId, String userName, String message, SlackAttachment attachment, boolean unfurl);

    SlackMessageHandle<SlackMessageReply> sendEphemeralMessage(String channelId, String userName, String message, SlackAttachment attachment);

    SlackMessageHandle<SlackMessageReply> sendEphemeralMessage(String channelId, String userName, String message, boolean unfurl);

    SlackMessageHandle<SlackMessageReply> sendEphemeralMessage(String channelId, String userName, String message);

    SlackMessageHandle<SlackMessageReply> sendFile(String channelId, InputStream data, String fileName);

    SlackMessageHandle<SlackMessageReply> sendFile(String channelId, InputStream data, String fileName, String title, String initialComment);

    SlackMessageHandle<SlackMessageReply> sendFileToUser(SlackUser user, InputStream data, String fileName);

    SlackMessageHandle<SlackMessageReply> sendFileToUser(String userName, InputStream data, String fileName);

    SlackMessageHandle<SlackMessageReply> sendMessageToUser(SlackUser user, SlackPreparedMessage preparedMessage);

    SlackMessageHandle<SlackMessageReply> sendMessageToUser(String userName, SlackPreparedMessage preparedMessage);

    SlackMessageHandle<SlackMessageReply> sendMessageToUser(SlackUser user, String message, SlackAttachment attachment);
    
    SlackMessageHandle<SlackMessageReply> sendMessageToUser(String userName, String message, SlackAttachment attachment);

    SlackMessageHandle<SlackMessageReply> updateMessage(String timeStamp, SlackChannel channel, String message);

    SlackMessageHandle<SlackMessageReply> updateMessage(String timeStamp, SlackChannel channel, String message, SlackAttachment[] attachments);

    SlackMessageHandle<SlackMessageReply> updateMessage(String timeStamp, SlackChannel channel, String message, SlackAttachment[] attachments, List<Block> blocks);

    SlackMessageHandle<SlackMessageReply> sendMessageOverWebSocket(SlackChannel channel, String message);

    SlackMessageHandle<SlackMessageReply> addReactionToMessage(SlackChannel channel, String messageTimeStamp, String emojiCode);

    SlackMessageHandle<SlackMessageReply> removeReactionFromMessage(SlackChannel channel, String messageTimeStamp, String emojiCode);

    SlackMessageHandle<SlackChannelReply> setChannelTopic(SlackChannel channel, String topic);

    SlackMessageHandle<SlackChannelReply> joinChannel(String channelName);

    SlackMessageHandle<SlackChannelReply> leaveChannel(SlackChannel channel);
    
    SlackMessageHandle<SlackChannelReply> inviteToChannel(SlackChannel channel, SlackUser user);

    SlackMessageHandle<SlackChannelReply> inviteToChannel(String channelId, String userName);

    SlackMessageHandle<SlackChannelReply> leaveChannel(String channelId);

    SlackMessageHandle<ParsedSlackReply> archiveChannel(SlackChannel channel);

    SlackMessageHandle<ParsedSlackReply> unarchiveChannel(SlackChannel channel);

    SlackMessageHandle<SlackMessageReply> updateMessage(String timeStamp, String channelId, String message);

    SlackMessageHandle<SlackMessageReply> updateMessage(String timeStamp, String channelId, String message, SlackAttachment[] attachments);

    SlackMessageHandle<SlackMessageReply> updateMessage(String timeStamp, String channelId, String message, SlackAttachment[] attachments, List<Block> blocks);

    SlackMessageHandle<SlackMessageReply> sendMessageOverWebSocket(String channelId, String message);

    SlackMessageHandle<SlackMessageReply> addReactionToMessage(String channelId, String messageTimeStamp, String emojiCode);

    SlackMessageHandle<SlackMessageReply> removeReactionFromMessage(String channelId, String messageTimeStamp, String emojiCode);

    SlackMessageHandle<SlackChannelReply> setChannelTopic(String channelId, String topic);

    SlackMessageHandle<SlackChannelReply> inviteToChannel(String channelId, SlackUser user);

    SlackMessageHandle<ParsedSlackReply> archiveChannel(String channelId);

    SlackMessageHandle<ParsedSlackReply> unarchiveChannel(String channelId);

    /**
     * SlackChannelReply type does not work for subsequent operations, change it to GenericSlackReply type
     * @param user: current slack user
     * @return the replies in current channel
     */
    SlackMessageHandle<GenericSlackReply> openDirectMessageChannel(SlackUser user);

    SlackMessageHandle<SlackChannelReply> openMultipartyDirectMessageChannel(SlackUser... users);

    SlackMessageHandle<SlackMessageReply> sendTyping(SlackChannel channel);

    SlackMessageHandle<SlackMessageReply> sendTyping(String channelId);

    SlackPresence getPresence(SlackPersona persona);

    void setPresence(SlackPresence presence);

    SlackMessageHandle<GenericSlackReply> postGenericSlackCommand(Map<String, String> params, String command);

    void addChannelArchivedListener(SlackChannelArchivedListener listener);

    void removeChannelArchivedListener(SlackChannelArchivedListener listener);

    void addChannelCreatedListener(SlackChannelCreatedListener listener);

    void removeChannelCreatedListener(SlackChannelCreatedListener listener);

    void addChannelDeletedListener(SlackChannelDeletedListener listener);

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

    void addChannelJoinedListener(SlackChannelJoinedListener listener);

    void removeChannelJoinedListener(SlackChannelJoinedListener listener);

    void addChannelLeftListener(SlackChannelLeftListener listener);

    void removeChannelLeftListener(SlackChannelLeftListener listener);

    void addGroupJoinedListener(SlackGroupJoinedListener listener);

    void removeGroupJoinedListener(SlackGroupJoinedListener listener);

    void addUserTypingListener(UserTypingListener listener);

    void removeUserTypingListener(UserTypingListener listener);


    /*
     * Subscribe to events related to the actions to the slack
     * server. At this time a set of status information is exchanged that
     * is useful to implementing bots.
     * 
     * For example, the current user that is connecting.
     * knowing your own user id will help you stop answering your own
     * questions.
     */
    void addSlackConnectedListener(SlackConnectedListener listener);
    
    void removeSlackConnectedListener(SlackConnectedListener listener);

    void addSlackDisconnectedListener(SlackDisconnectedListener listener);

    void removeSlackDisconnectedListener(SlackDisconnectedListener listener);

    /**
     * 
     * @return true if actions is open
     */
    boolean isConnected();
    
    void addReactionAddedListener(ReactionAddedListener listener);
    
    void removeReactionAddedListener(ReactionAddedListener listener);
    
    void addReactionRemovedListener(ReactionRemovedListener listener);
    
    void removeReactionRemovedListener(ReactionRemovedListener listener);

    void addSlackUserChangeListener(SlackUserChangeListener listener);

    void removeSlackUserChangeListener(SlackUserChangeListener listener);

    void addSlackTeamJoinListener(SlackTeamJoinListener listener);

    void removeSlackTeamJoinListener(SlackTeamJoinListener listener);

    void addPinAddedListener(PinAddedListener listener);

    void removePinAddedListener(PinAddedListener listener);

    void addPresenceChangeListener(PresenceChangeListener listener);

    void removePresenceChangeListener(PresenceChangeListener listener);

    void addPinRemovedListener(PinRemovedListener listener);
  
    void removePinRemovedListener(PinRemovedListener listener);

    long getHeartbeat();
}
