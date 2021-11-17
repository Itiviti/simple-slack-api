//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by FernFlower decompiler)
//

package com.ullink.slack.simpleslackapi;

import com.ullink.slack.simpleslackapi.blocks.Block;
import com.ullink.slack.simpleslackapi.listeners.*;
import com.ullink.slack.simpleslackapi.replies.EmojiSlackReply;
import com.ullink.slack.simpleslackapi.replies.GenericSlackReply;
import com.ullink.slack.simpleslackapi.replies.ParsedSlackReply;
import com.ullink.slack.simpleslackapi.replies.SlackChannelReply;
import com.ullink.slack.simpleslackapi.replies.SlackMessageReply;
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

    SlackChannel findChannelByName(String var1);

    SlackChannel findChannelById(String var1);

    SlackIntegration findIntegrationById(String var1);

    SlackUser findUserById(String var1);

    SlackUser findUserByUserName(String var1);

    SlackUser findUserByEmail(String var1);

    SlackPersona sessionPersona();

    SlackMessageHandle<EmojiSlackReply> listEmoji();

    void refetchUsers();

    /** @deprecated */
    @Deprecated
    SlackBot findBotById(String var1);

    SlackMessageHandle<ParsedSlackReply> inviteUser(String var1, String var2, boolean var3);

    void connect() throws IOException;

    void disconnect() throws IOException;

    SlackMessageHandle<SlackMessageReply> deleteMessage(String var1, SlackChannel var2);

    SlackMessageHandle<SlackMessageReply> sendMessage(SlackChannel var1, SlackPreparedMessage var2, SlackChatConfiguration var3);

    SlackMessageHandle<SlackMessageReply> sendMessage(SlackChannel var1, SlackPreparedMessage var2);

    SlackMessageHandle<SlackMessageReply> sendMessage(SlackChannel var1, String var2, SlackAttachment var3, SlackChatConfiguration var4, boolean var5);

    SlackMessageHandle<SlackMessageReply> sendMessage(SlackChannel var1, String var2, SlackAttachment var3, SlackChatConfiguration var4);

    SlackMessageHandle<SlackMessageReply> sendMessage(SlackChannel var1, String var2, SlackAttachment var3, boolean var4);

    SlackMessageHandle<SlackMessageReply> sendMessage(SlackChannel var1, String var2, SlackAttachment var3);

    SlackMessageHandle<SlackMessageReply> sendMessage(SlackChannel var1, String var2, boolean var3);

    SlackMessageHandle<SlackMessageReply> sendMessage(SlackChannel var1, String var2);

    SlackMessageHandle<SlackMessageReply> deleteMessage(String var1, String var2);

    SlackMessageHandle<SlackMessageReply> sendMessage(String var1, SlackPreparedMessage var2, SlackChatConfiguration var3);

    SlackMessageHandle<SlackMessageReply> sendMessage(String var1, SlackPreparedMessage var2);

    SlackMessageHandle<SlackMessageReply> sendMessage(String var1, String var2, SlackAttachment var3, SlackChatConfiguration var4, boolean var5);

    SlackMessageHandle<SlackMessageReply> sendMessage(String var1, String var2, SlackAttachment var3, SlackChatConfiguration var4);

    SlackMessageHandle<SlackMessageReply> sendMessage(String var1, String var2, SlackAttachment var3, boolean var4);

    SlackMessageHandle<SlackMessageReply> sendMessage(String var1, String var2, SlackAttachment var3);

    SlackMessageHandle<SlackMessageReply> sendMessage(String var1, String var2, boolean var3);

    SlackMessageHandle<SlackMessageReply> sendMessage(String var1, String var2);

    SlackMessageHandle<SlackMessageReply> sendEphemeralMessage(SlackChannel var1, SlackUser var2, SlackPreparedMessage var3, SlackChatConfiguration var4);

    SlackMessageHandle<SlackMessageReply> sendEphemeralMessage(SlackChannel var1, SlackUser var2, SlackPreparedMessage var3);

    SlackMessageHandle<SlackMessageReply> sendEphemeralMessage(SlackChannel var1, SlackUser var2, String var3, SlackAttachment var4, SlackChatConfiguration var5, boolean var6);

    SlackMessageHandle<SlackMessageReply> sendEphemeralMessage(SlackChannel var1, SlackUser var2, String var3, SlackAttachment var4, SlackChatConfiguration var5);

    SlackMessageHandle<SlackMessageReply> sendEphemeralMessage(SlackChannel var1, SlackUser var2, String var3, SlackAttachment var4, boolean var5);

    SlackMessageHandle<SlackMessageReply> sendEphemeralMessage(SlackChannel var1, SlackUser var2, String var3, SlackAttachment var4);

    SlackMessageHandle<SlackMessageReply> sendEphemeralMessage(SlackChannel var1, SlackUser var2, String var3, boolean var4);

    SlackMessageHandle<SlackMessageReply> sendEphemeralMessage(SlackChannel var1, SlackUser var2, String var3);

    SlackMessageHandle<SlackMessageReply> sendFile(SlackChannel var1, InputStream var2, String var3);

    SlackMessageHandle<SlackMessageReply> sendFile(SlackChannel var1, InputStream var2, String var3, String var4, String var5);

    SlackMessageHandle<SlackMessageReply> sendEphemeralMessage(String var1, String var2, SlackPreparedMessage var3, SlackChatConfiguration var4);

    SlackMessageHandle<SlackMessageReply> sendEphemeralMessage(String var1, String var2, SlackPreparedMessage var3);

    SlackMessageHandle<SlackMessageReply> sendEphemeralMessage(String var1, String var2, String var3, SlackAttachment var4, SlackChatConfiguration var5, boolean var6);

    SlackMessageHandle<SlackMessageReply> sendEphemeralMessage(String var1, String var2, String var3, SlackAttachment var4, SlackChatConfiguration var5);

    SlackMessageHandle<SlackMessageReply> sendEphemeralMessage(String var1, String var2, String var3, SlackAttachment var4, boolean var5);

    SlackMessageHandle<SlackMessageReply> sendEphemeralMessage(String var1, String var2, String var3, SlackAttachment var4);

    SlackMessageHandle<SlackMessageReply> sendEphemeralMessage(String var1, String var2, String var3, boolean var4);

    SlackMessageHandle<SlackMessageReply> sendEphemeralMessage(String var1, String var2, String var3);

    SlackMessageHandle<SlackMessageReply> sendFile(String var1, InputStream var2, String var3);

    SlackMessageHandle<SlackMessageReply> sendFile(String var1, InputStream var2, String var3, String var4, String var5);

    SlackMessageHandle<SlackMessageReply> sendFileToUser(SlackUser var1, InputStream var2, String var3);

    SlackMessageHandle<SlackMessageReply> sendFileToUser(String var1, InputStream var2, String var3);

    SlackMessageHandle<SlackMessageReply> sendMessageToUser(SlackUser var1, SlackPreparedMessage var2);

    SlackMessageHandle<SlackMessageReply> sendMessageToUser(String var1, SlackPreparedMessage var2);

    SlackMessageHandle<SlackMessageReply> sendMessageToUser(SlackUser var1, String var2, SlackAttachment var3);

    SlackMessageHandle<SlackMessageReply> sendMessageToUser(String var1, String var2, SlackAttachment var3);

    SlackMessageHandle<SlackMessageReply> updateMessage(String var1, SlackChannel var2, String var3);

    SlackMessageHandle<SlackMessageReply> updateMessage(String var1, SlackChannel var2, String var3, SlackAttachment[] var4);

    SlackMessageHandle<SlackMessageReply> updateMessage(String var1, SlackChannel var2, String var3, SlackAttachment[] var4, List<Block> var5);

    SlackMessageHandle<SlackMessageReply> sendMessageOverWebSocket(SlackChannel var1, String var2);

    SlackMessageHandle<SlackMessageReply> addReactionToMessage(SlackChannel var1, String var2, String var3);

    SlackMessageHandle<SlackMessageReply> removeReactionFromMessage(SlackChannel var1, String var2, String var3);

    SlackMessageHandle<SlackChannelReply> setChannelTopic(SlackChannel var1, String var2);

    SlackMessageHandle<SlackChannelReply> joinChannel(String var1);

    SlackMessageHandle<SlackChannelReply> leaveChannel(SlackChannel var1);

    SlackMessageHandle<SlackChannelReply> inviteToChannel(SlackChannel var1, SlackUser var2);

    SlackMessageHandle<SlackChannelReply> inviteToChannel(String var1, String var2);

    SlackMessageHandle<SlackChannelReply> leaveChannel(String var1);

    SlackMessageHandle<ParsedSlackReply> archiveChannel(SlackChannel var1);

    SlackMessageHandle<ParsedSlackReply> unarchiveChannel(SlackChannel var1);

    SlackMessageHandle<SlackMessageReply> updateMessage(String var1, String var2, String var3);

    SlackMessageHandle<SlackMessageReply> updateMessage(String var1, String var2, String var3, SlackAttachment[] var4);

    SlackMessageHandle<SlackMessageReply> updateMessage(String var1, String var2, String var3, SlackAttachment[] var4, List<Block> var5);

    SlackMessageHandle<SlackMessageReply> sendMessageOverWebSocket(String var1, String var2);

    SlackMessageHandle<SlackMessageReply> addReactionToMessage(String var1, String var2, String var3);

    SlackMessageHandle<SlackMessageReply> removeReactionFromMessage(String var1, String var2, String var3);

    SlackMessageHandle<SlackChannelReply> setChannelTopic(String var1, String var2);

    SlackMessageHandle<SlackChannelReply> inviteToChannel(String var1, SlackUser var2);

    SlackMessageHandle<ParsedSlackReply> archiveChannel(String var1);

    SlackMessageHandle<ParsedSlackReply> unarchiveChannel(String var1);

    SlackMessageHandle<SlackChannelReply> openDirectMessageChannel(SlackUser var1);

    SlackMessageHandle<SlackChannelReply> openMultipartyDirectMessageChannel(SlackUser... var1);

    SlackMessageHandle<SlackMessageReply> sendTyping(SlackChannel var1);

    SlackMessageHandle<SlackMessageReply> sendTyping(String var1);

    SlackPresence getPresence(SlackPersona var1);

    void setPresence(SlackPresence var1);

    SlackMessageHandle<GenericSlackReply> postGenericSlackCommand(Map<String, String> var1, String var2);

    void addChannelArchivedListener(SlackChannelArchivedListener var1);

    void removeChannelArchivedListener(SlackChannelArchivedListener var1);

    void addChannelCreatedListener(SlackChannelCreatedListener var1);

    void removeChannelCreatedListener(SlackChannelCreatedListener var1);

    void addChannelDeletedListener(SlackChannelDeletedListener var1);

    void removeChannelDeletedListener(SlackChannelDeletedListener var1);

    void addChannelRenamedListener(SlackChannelRenamedListener var1);

    void removeChannelRenamedListener(SlackChannelRenamedListener var1);

    void addChannelUnarchivedListener(SlackChannelUnarchivedListener var1);

    void removeChannelUnarchivedListener(SlackChannelUnarchivedListener var1);

    void addMessageDeletedListener(SlackMessageDeletedListener var1);

    void removeMessageDeletedListener(SlackMessageDeletedListener var1);

    void addMessagePostedListener(SlackMessagePostedListener var1);

    void removeMessagePostedListener(SlackMessagePostedListener var1);

    void addMessageUpdatedListener(SlackMessageUpdatedListener var1);

    void removeMessageUpdatedListener(SlackMessageUpdatedListener var1);

    void addChannelJoinedListener(SlackChannelJoinedListener var1);

    void removeChannelJoinedListener(SlackChannelJoinedListener var1);

    void addChannelLeftListener(SlackChannelLeftListener var1);

    void removeChannelLeftListener(SlackChannelLeftListener var1);

    void addGroupJoinedListener(SlackGroupJoinedListener var1);

    void removeGroupJoinedListener(SlackGroupJoinedListener var1);

    void addUserTypingListener(UserTypingListener var1);

    void removeUserTypingListener(UserTypingListener var1);

    void addSlackConnectedListener(SlackConnectedListener var1);

    void removeSlackConnectedListener(SlackConnectedListener var1);

    void addSlackDisconnectedListener(SlackDisconnectedListener var1);

    void removeSlackDisconnectedListener(SlackDisconnectedListener var1);

    boolean isConnected();

    void addReactionAddedListener(ReactionAddedListener var1);

    void removeReactionAddedListener(ReactionAddedListener var1);

    void addReactionRemovedListener(ReactionRemovedListener var1);

    void removeReactionRemovedListener(ReactionRemovedListener var1);

    void addSlackUserChangeListener(SlackUserChangeListener var1);

    void removeSlackUserChangeListener(SlackUserChangeListener var1);

    void addSlackTeamJoinListener(SlackTeamJoinListener var1);

    void removeSlackTeamJoinListener(SlackTeamJoinListener var1);

    void addPinAddedListener(PinAddedListener var1);

    void removePinAddedListener(PinAddedListener var1);

    void addPresenceChangeListener(PresenceChangeListener var1);

    void removePresenceChangeListener(PresenceChangeListener var1);

    void addPinRemovedListener(PinRemovedListener var1);

    void removePinRemovedListener(PinRemovedListener var1);

    void addSlackMemberJoinedListener(SlackMemberJoinedListener var1);

    void removeSlackMemberJoinedListener(SlackMemberJoinedListener var1);

    long getHeartbeat();
}
