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

public class SlackSessionWrapper implements SlackSession {
    protected final SlackSession delegate;

    public SlackSessionWrapper(SlackSession delegate) {
        this.delegate = delegate;
    }

    public SlackTeam getTeam() {
        return this.delegate.getTeam();
    }

    public Collection<SlackChannel> getChannels() {
        return this.delegate.getChannels();
    }

    public Collection<SlackUser> getUsers() {
        return this.delegate.getUsers();
    }

    public Collection<SlackBot> getBots() {
        return this.delegate.getBots();
    }

    public Collection<SlackIntegration> getIntegrations() {
        return this.delegate.getIntegrations();
    }

    public SlackChannel findChannelByName(String channelName) {
        return this.delegate.findChannelByName(channelName);
    }

    public SlackChannel findChannelById(String channelId) {
        return this.delegate.findChannelById(channelId);
    }

    public SlackIntegration findIntegrationById(String integrationId) {
        return this.delegate.findIntegrationById(integrationId);
    }

    public SlackUser findUserById(String userId) {
        return this.delegate.findUserById(userId);
    }

    public SlackUser findUserByUserName(String userName) {
        return this.delegate.findUserByUserName(userName);
    }

    public SlackUser findUserByEmail(String userMail) {
        return this.delegate.findUserByEmail(userMail);
    }

    public SlackPersona sessionPersona() {
        return this.delegate.sessionPersona();
    }

    public SlackMessageHandle<EmojiSlackReply> listEmoji() {
        return this.delegate.listEmoji();
    }

    public void refetchUsers() {
        this.delegate.refetchUsers();
    }

    public SlackBot findBotById(String botId) {
        return this.delegate.findBotById(botId);
    }

    public SlackMessageHandle<ParsedSlackReply> inviteUser(String email, String firstName, boolean setActive) {
        return this.delegate.inviteUser(email, firstName, setActive);
    }

    public void connect() throws IOException {
        this.delegate.connect();
    }

    public void disconnect() throws IOException {
        this.delegate.disconnect();
    }

    public SlackMessageHandle<SlackMessageReply> deleteMessage(String timeStamp, SlackChannel channel) {
        return this.delegate.deleteMessage(timeStamp, channel);
    }

    public SlackMessageHandle<SlackMessageReply> sendMessage(SlackChannel channel, SlackPreparedMessage preparedMessage, SlackChatConfiguration chatConfiguration) {
        return this.delegate.sendMessage(channel, preparedMessage, chatConfiguration);
    }

    public SlackMessageHandle<SlackMessageReply> sendMessage(SlackChannel channel, SlackPreparedMessage preparedMessage) {
        return this.delegate.sendMessage(channel, preparedMessage);
    }

    public SlackMessageHandle<SlackMessageReply> sendMessage(SlackChannel channel, String message, SlackAttachment attachment, SlackChatConfiguration chatConfiguration, boolean unfurl) {
        return this.delegate.sendMessage(channel, message, attachment, chatConfiguration, unfurl);
    }

    public SlackMessageHandle<SlackMessageReply> sendMessage(SlackChannel channel, String message, SlackAttachment attachment, SlackChatConfiguration chatConfiguration) {
        return this.delegate.sendMessage(channel, message, attachment, chatConfiguration);
    }

    public SlackMessageHandle<SlackMessageReply> sendMessage(SlackChannel channel, String message, SlackAttachment attachment, boolean unfurl) {
        return this.delegate.sendMessage(channel, message, attachment, unfurl);
    }

    public SlackMessageHandle<SlackMessageReply> sendMessage(SlackChannel channel, String message, SlackAttachment attachment) {
        return this.delegate.sendMessage(channel, message, attachment);
    }

    public SlackMessageHandle<SlackMessageReply> sendMessage(SlackChannel channel, String message, boolean unfurl) {
        return this.delegate.sendMessage(channel, message, unfurl);
    }

    public SlackMessageHandle<SlackMessageReply> sendMessage(SlackChannel channel, String message) {
        return this.delegate.sendMessage(channel, message);
    }

    public SlackMessageHandle<SlackMessageReply> deleteMessage(String timeStamp, String channelId) {
        return this.delegate.deleteMessage(timeStamp, channelId);
    }

    public SlackMessageHandle<SlackMessageReply> sendMessage(String channelId, SlackPreparedMessage preparedMessage, SlackChatConfiguration chatConfiguration) {
        return this.delegate.sendMessage(channelId, preparedMessage, chatConfiguration);
    }

    public SlackMessageHandle<SlackMessageReply> sendMessage(String channelId, SlackPreparedMessage preparedMessage) {
        return this.delegate.sendMessage(channelId, preparedMessage);
    }

    public SlackMessageHandle<SlackMessageReply> sendMessage(String channelId, String message, SlackAttachment attachment, SlackChatConfiguration chatConfiguration, boolean unfurl) {
        return this.delegate.sendMessage(channelId, message, attachment, chatConfiguration, unfurl);
    }

    public SlackMessageHandle<SlackMessageReply> sendMessage(String channelId, String message, SlackAttachment attachment, SlackChatConfiguration chatConfiguration) {
        return this.delegate.sendMessage(channelId, message, attachment, chatConfiguration);
    }

    public SlackMessageHandle<SlackMessageReply> sendMessage(String channelId, String message, SlackAttachment attachment, boolean unfurl) {
        return this.delegate.sendMessage(channelId, message, attachment, unfurl);
    }

    public SlackMessageHandle<SlackMessageReply> sendMessage(String channelId, String message, SlackAttachment attachment) {
        return this.delegate.sendMessage(channelId, message, attachment);
    }

    public SlackMessageHandle<SlackMessageReply> sendMessage(String channelId, String message, boolean unfurl) {
        return this.delegate.sendMessage(channelId, message, unfurl);
    }

    public SlackMessageHandle<SlackMessageReply> sendMessage(String channelId, String message) {
        return this.delegate.sendMessage(channelId, message);
    }

    public SlackMessageHandle<SlackMessageReply> sendEphemeralMessage(SlackChannel channel, SlackUser user, SlackPreparedMessage preparedMessage, SlackChatConfiguration chatConfiguration) {
        return this.delegate.sendEphemeralMessage(channel, user, preparedMessage, chatConfiguration);
    }

    public SlackMessageHandle<SlackMessageReply> sendEphemeralMessage(SlackChannel channel, SlackUser user, SlackPreparedMessage preparedMessage) {
        return this.delegate.sendEphemeralMessage(channel, user, preparedMessage);
    }

    public SlackMessageHandle<SlackMessageReply> sendEphemeralMessage(SlackChannel channel, SlackUser user, String message, SlackAttachment attachment, SlackChatConfiguration chatConfiguration, boolean unfurl) {
        return this.delegate.sendEphemeralMessage(channel, user, message, attachment, chatConfiguration, unfurl);
    }

    public SlackMessageHandle<SlackMessageReply> sendEphemeralMessage(SlackChannel channel, SlackUser user, String message, SlackAttachment attachment, SlackChatConfiguration chatConfiguration) {
        return this.delegate.sendEphemeralMessage(channel, user, message, attachment, chatConfiguration);
    }

    public SlackMessageHandle<SlackMessageReply> sendEphemeralMessage(SlackChannel channel, SlackUser user, String message, SlackAttachment attachment, boolean unfurl) {
        return this.delegate.sendEphemeralMessage(channel, user, message, attachment, unfurl);
    }

    public SlackMessageHandle<SlackMessageReply> sendEphemeralMessage(SlackChannel channel, SlackUser user, String message, SlackAttachment attachment) {
        return this.delegate.sendEphemeralMessage(channel, user, message, attachment);
    }

    public SlackMessageHandle<SlackMessageReply> sendEphemeralMessage(SlackChannel channel, SlackUser user, String message, boolean unfurl) {
        return this.delegate.sendEphemeralMessage(channel, user, message, unfurl);
    }

    public SlackMessageHandle<SlackMessageReply> sendEphemeralMessage(SlackChannel channel, SlackUser user, String message) {
        return this.delegate.sendEphemeralMessage(channel, user, message);
    }

    public SlackMessageHandle<SlackMessageReply> sendFile(SlackChannel channel, InputStream data, String fileName) {
        return this.delegate.sendFile(channel, data, fileName);
    }

    public SlackMessageHandle<SlackMessageReply> sendFile(SlackChannel channel, InputStream data, String fileName, String title, String initialComment) {
        return this.delegate.sendFile(channel, data, fileName, title, initialComment);
    }

    public SlackMessageHandle<SlackMessageReply> sendEphemeralMessage(String channelId, String userName, SlackPreparedMessage preparedMessage, SlackChatConfiguration chatConfiguration) {
        return this.delegate.sendEphemeralMessage(channelId, userName, preparedMessage, chatConfiguration);
    }

    public SlackMessageHandle<SlackMessageReply> sendEphemeralMessage(String channelId, String userName, SlackPreparedMessage preparedMessage) {
        return this.delegate.sendEphemeralMessage(channelId, userName, preparedMessage);
    }

    public SlackMessageHandle<SlackMessageReply> sendEphemeralMessage(String channelId, String userName, String message, SlackAttachment attachment, SlackChatConfiguration chatConfiguration, boolean unfurl) {
        return this.delegate.sendEphemeralMessage(channelId, userName, message, attachment, chatConfiguration, unfurl);
    }

    public SlackMessageHandle<SlackMessageReply> sendEphemeralMessage(String channelId, String userName, String message, SlackAttachment attachment, SlackChatConfiguration chatConfiguration) {
        return this.delegate.sendEphemeralMessage(channelId, userName, message, attachment, chatConfiguration);
    }

    public SlackMessageHandle<SlackMessageReply> sendEphemeralMessage(String channelId, String userName, String message, SlackAttachment attachment, boolean unfurl) {
        return this.delegate.sendEphemeralMessage(channelId, userName, message, attachment, unfurl);
    }

    public SlackMessageHandle<SlackMessageReply> sendEphemeralMessage(String channelId, String userName, String message, SlackAttachment attachment) {
        return this.delegate.sendEphemeralMessage(channelId, userName, message, attachment);
    }

    public SlackMessageHandle<SlackMessageReply> sendEphemeralMessage(String channelId, String userName, String message, boolean unfurl) {
        return this.delegate.sendEphemeralMessage(channelId, userName, message, unfurl);
    }

    public SlackMessageHandle<SlackMessageReply> sendEphemeralMessage(String channelId, String userName, String message) {
        return this.delegate.sendEphemeralMessage(channelId, userName, message);
    }

    public SlackMessageHandle<SlackMessageReply> sendFile(String channelId, InputStream data, String fileName) {
        return this.delegate.sendFile(channelId, data, fileName);
    }

    public SlackMessageHandle<SlackMessageReply> sendFile(String channelId, InputStream data, String fileName, String title, String initialComment) {
        return this.delegate.sendFile(channelId, data, fileName, title, initialComment);
    }

    public SlackMessageHandle<SlackMessageReply> sendFileToUser(SlackUser user, InputStream data, String fileName) {
        return this.delegate.sendFileToUser(user, data, fileName);
    }

    public SlackMessageHandle<SlackMessageReply> sendFileToUser(String userName, InputStream data, String fileName) {
        return this.delegate.sendFileToUser(userName, data, fileName);
    }

    public SlackMessageHandle<SlackMessageReply> sendMessageToUser(SlackUser user, SlackPreparedMessage preparedMessage) {
        return this.delegate.sendMessageToUser(user, preparedMessage);
    }

    public SlackMessageHandle<SlackMessageReply> sendMessageToUser(String userName, SlackPreparedMessage preparedMessage) {
        return this.delegate.sendMessageToUser(userName, preparedMessage);
    }

    public SlackMessageHandle<SlackMessageReply> sendMessageToUser(SlackUser user, String message, SlackAttachment attachment) {
        return this.delegate.sendMessageToUser(user, message, attachment);
    }

    public SlackMessageHandle<SlackMessageReply> sendMessageToUser(String userName, String message, SlackAttachment attachment) {
        return this.delegate.sendMessageToUser(userName, message, attachment);
    }

    public SlackMessageHandle<SlackMessageReply> updateMessage(String timeStamp, SlackChannel channel, String message) {
        return this.delegate.updateMessage(timeStamp, channel, message);
    }

    public SlackMessageHandle<SlackMessageReply> updateMessage(String timeStamp, SlackChannel channel, String message, SlackAttachment[] attachments) {
        return this.delegate.updateMessage(timeStamp, channel, message, attachments);
    }

    public SlackMessageHandle<SlackMessageReply> updateMessage(String timeStamp, SlackChannel channel, String message, SlackAttachment[] attachments, List<Block> blocks) {
        return this.delegate.updateMessage(timeStamp, channel, message, attachments, blocks);
    }

    public SlackMessageHandle<SlackMessageReply> sendMessageOverWebSocket(SlackChannel channel, String message) {
        return this.delegate.sendMessageOverWebSocket(channel, message);
    }

    public SlackMessageHandle<SlackMessageReply> addReactionToMessage(SlackChannel channel, String messageTimeStamp, String emojiCode) {
        return this.delegate.addReactionToMessage(channel, messageTimeStamp, emojiCode);
    }

    public SlackMessageHandle<SlackMessageReply> removeReactionFromMessage(SlackChannel channel, String messageTimeStamp, String emojiCode) {
        return this.delegate.removeReactionFromMessage(channel, messageTimeStamp, emojiCode);
    }

    public SlackMessageHandle<SlackChannelReply> setChannelTopic(SlackChannel channel, String topic) {
        return this.delegate.setChannelTopic(channel, topic);
    }

    public SlackMessageHandle<SlackChannelReply> joinChannel(String channelName) {
        return this.delegate.joinChannel(channelName);
    }

    public SlackMessageHandle<SlackChannelReply> leaveChannel(SlackChannel channel) {
        return this.delegate.leaveChannel(channel);
    }

    public SlackMessageHandle<SlackChannelReply> inviteToChannel(SlackChannel channel, SlackUser user) {
        return this.delegate.inviteToChannel(channel, user);
    }

    public SlackMessageHandle<SlackChannelReply> inviteToChannel(String channelId, String userName) {
        return this.delegate.inviteToChannel(channelId, userName);
    }

    public SlackMessageHandle<SlackChannelReply> leaveChannel(String channelId) {
        return this.delegate.leaveChannel(channelId);
    }

    public SlackMessageHandle<ParsedSlackReply> archiveChannel(SlackChannel channel) {
        return this.delegate.archiveChannel(channel);
    }

    public SlackMessageHandle<ParsedSlackReply> unarchiveChannel(SlackChannel channel) {
        return this.delegate.unarchiveChannel(channel);
    }

    public SlackMessageHandle<SlackMessageReply> updateMessage(String timeStamp, String channelId, String message) {
        return this.delegate.updateMessage(timeStamp, channelId, message);
    }

    public SlackMessageHandle<SlackMessageReply> updateMessage(String timeStamp, String channelId, String message, SlackAttachment[] attachments) {
        return this.delegate.updateMessage(timeStamp, channelId, message, attachments);
    }

    public SlackMessageHandle<SlackMessageReply> updateMessage(String timeStamp, String channelId, String message, SlackAttachment[] attachments, List<Block> blocks) {
        return this.delegate.updateMessage(timeStamp, channelId, message, attachments, blocks);
    }

    public SlackMessageHandle<SlackMessageReply> sendMessageOverWebSocket(String channelId, String message) {
        return this.delegate.sendMessageOverWebSocket(channelId, message);
    }

    public SlackMessageHandle<SlackMessageReply> addReactionToMessage(String channelId, String messageTimeStamp, String emojiCode) {
        return this.delegate.addReactionToMessage(channelId, messageTimeStamp, emojiCode);
    }

    public SlackMessageHandle<SlackMessageReply> removeReactionFromMessage(String channelId, String messageTimeStamp, String emojiCode) {
        return this.delegate.removeReactionFromMessage(channelId, messageTimeStamp, emojiCode);
    }

    public SlackMessageHandle<SlackChannelReply> setChannelTopic(String channelId, String topic) {
        return this.delegate.setChannelTopic(channelId, topic);
    }

    public SlackMessageHandle<SlackChannelReply> inviteToChannel(String channelId, SlackUser user) {
        return this.delegate.inviteToChannel(channelId, user);
    }

    public SlackMessageHandle<ParsedSlackReply> archiveChannel(String channelId) {
        return this.delegate.archiveChannel(channelId);
    }

    public SlackMessageHandle<ParsedSlackReply> unarchiveChannel(String channelId) {
        return this.delegate.unarchiveChannel(channelId);
    }

    public SlackMessageHandle<SlackChannelReply> openDirectMessageChannel(SlackUser user) {
        return this.delegate.openDirectMessageChannel(user);
    }

    public SlackMessageHandle<SlackChannelReply> openMultipartyDirectMessageChannel(SlackUser... users) {
        return this.delegate.openMultipartyDirectMessageChannel(users);
    }

    public SlackMessageHandle<SlackMessageReply> sendTyping(SlackChannel channel) {
        return this.delegate.sendTyping(channel);
    }

    public SlackMessageHandle<SlackMessageReply> sendTyping(String channelId) {
        return this.delegate.sendTyping(channelId);
    }

    public SlackPresence getPresence(SlackPersona persona) {
        return this.delegate.getPresence(persona);
    }

    public void setPresence(SlackPresence presence) {
        this.delegate.setPresence(presence);
    }

    public SlackMessageHandle<GenericSlackReply> postGenericSlackCommand(Map<String, String> params, String command) {
        return this.delegate.postGenericSlackCommand(params, command);
    }

    public void addChannelArchivedListener(SlackChannelArchivedListener listener) {
        this.delegate.addChannelArchivedListener(listener);
    }

    public void removeChannelArchivedListener(SlackChannelArchivedListener listener) {
        this.delegate.removeChannelArchivedListener(listener);
    }

    public void addChannelCreatedListener(SlackChannelCreatedListener listener) {
        this.delegate.addChannelCreatedListener(listener);
    }

    public void removeChannelCreatedListener(SlackChannelCreatedListener listener) {
        this.delegate.removeChannelCreatedListener(listener);
    }

    public void addChannelDeletedListener(SlackChannelDeletedListener listener) {
        this.delegate.addChannelDeletedListener(listener);
    }

    public void removeChannelDeletedListener(SlackChannelDeletedListener listener) {
        this.delegate.removeChannelDeletedListener(listener);
    }

    public void addChannelRenamedListener(SlackChannelRenamedListener listener) {
        this.delegate.addChannelRenamedListener(listener);
    }

    public void removeChannelRenamedListener(SlackChannelRenamedListener listener) {
        this.delegate.removeChannelRenamedListener(listener);
    }

    public void addChannelUnarchivedListener(SlackChannelUnarchivedListener listener) {
        this.delegate.addChannelUnarchivedListener(listener);
    }

    public void removeChannelUnarchivedListener(SlackChannelUnarchivedListener listener) {
        this.delegate.removeChannelUnarchivedListener(listener);
    }

    public void addMessageDeletedListener(SlackMessageDeletedListener listener) {
        this.delegate.addMessageDeletedListener(listener);
    }

    public void removeMessageDeletedListener(SlackMessageDeletedListener listener) {
        this.delegate.removeMessageDeletedListener(listener);
    }

    public void addMessagePostedListener(SlackMessagePostedListener listener) {
        this.delegate.addMessagePostedListener(listener);
    }

    public void removeMessagePostedListener(SlackMessagePostedListener listener) {
        this.delegate.removeMessagePostedListener(listener);
    }

    public void addMessageUpdatedListener(SlackMessageUpdatedListener listener) {
        this.delegate.addMessageUpdatedListener(listener);
    }

    public void removeMessageUpdatedListener(SlackMessageUpdatedListener listener) {
        this.delegate.removeMessageUpdatedListener(listener);
    }

    public void addChannelJoinedListener(SlackChannelJoinedListener listener) {
        this.delegate.addChannelJoinedListener(listener);
    }

    public void removeChannelJoinedListener(SlackChannelJoinedListener listener) {
        this.delegate.removeChannelJoinedListener(listener);
    }

    public void addChannelLeftListener(SlackChannelLeftListener listener) {
        this.delegate.addChannelLeftListener(listener);
    }

    public void removeChannelLeftListener(SlackChannelLeftListener listener) {
        this.delegate.removeChannelLeftListener(listener);
    }

    public void addGroupJoinedListener(SlackGroupJoinedListener listener) {
        this.delegate.addGroupJoinedListener(listener);
    }

    public void removeGroupJoinedListener(SlackGroupJoinedListener listener) {
        this.delegate.removeGroupJoinedListener(listener);
    }

    public void addUserTypingListener(UserTypingListener listener) {
        this.delegate.addUserTypingListener(listener);
    }

    public void removeUserTypingListener(UserTypingListener listener) {
        this.delegate.removeUserTypingListener(listener);
    }

    public void addSlackConnectedListener(SlackConnectedListener listener) {
        this.delegate.addSlackConnectedListener(listener);
    }

    public void removeSlackConnectedListener(SlackConnectedListener listener) {
        this.delegate.removeSlackConnectedListener(listener);
    }

    public void addSlackDisconnectedListener(SlackDisconnectedListener listener) {
        this.delegate.addSlackDisconnectedListener(listener);
    }

    public void removeSlackDisconnectedListener(SlackDisconnectedListener listener) {
        this.delegate.removeSlackDisconnectedListener(listener);
    }

    public boolean isConnected() {
        return this.delegate.isConnected();
    }

    public void addReactionAddedListener(ReactionAddedListener listener) {
        this.delegate.addReactionAddedListener(listener);
    }

    public void removeReactionAddedListener(ReactionAddedListener listener) {
        this.delegate.removeReactionAddedListener(listener);
    }

    public void addReactionRemovedListener(ReactionRemovedListener listener) {
        this.delegate.addReactionRemovedListener(listener);
    }

    public void removeReactionRemovedListener(ReactionRemovedListener listener) {
        this.delegate.removeReactionRemovedListener(listener);
    }

    public void addSlackUserChangeListener(SlackUserChangeListener listener) {
        this.delegate.addSlackUserChangeListener(listener);
    }

    public void removeSlackUserChangeListener(SlackUserChangeListener listener) {
        this.delegate.removeSlackUserChangeListener(listener);
    }

    public void addSlackTeamJoinListener(SlackTeamJoinListener listener) {
        this.delegate.addSlackTeamJoinListener(listener);
    }

    public void removeSlackTeamJoinListener(SlackTeamJoinListener listener) {
        this.delegate.removeSlackTeamJoinListener(listener);
    }

    public void addPinAddedListener(PinAddedListener listener) {
        this.delegate.addPinAddedListener(listener);
    }

    public void removePinAddedListener(PinAddedListener listener) {
        this.delegate.removePinAddedListener(listener);
    }

    public void addPresenceChangeListener(PresenceChangeListener listener) {
        this.delegate.addPresenceChangeListener(listener);
    }

    public void removePresenceChangeListener(PresenceChangeListener listener) {
        this.delegate.removePresenceChangeListener(listener);
    }

    public void addPinRemovedListener(PinRemovedListener listener) {
        this.delegate.addPinRemovedListener(listener);
    }

    public void removePinRemovedListener(PinRemovedListener listener) {
        this.delegate.removePinRemovedListener(listener);
    }

    public void addSlackMemberJoinedListener(SlackMemberJoinedListener listener) {
        this.delegate.addSlackMemberJoinedListener(listener);
    }

    public void removeSlackMemberJoinedListener(SlackMemberJoinedListener listener) {
        this.delegate.removeSlackMemberJoinedListener(listener);
    }


    public long getHeartbeat() {
        return this.delegate.getHeartbeat();
    }
}
