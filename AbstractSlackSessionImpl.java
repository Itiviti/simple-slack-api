//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by FernFlower decompiler)
//

package com.ullink.slack.simpleslackapi.impl;

import com.ullink.slack.simpleslackapi.SlackAttachment;
import com.ullink.slack.simpleslackapi.SlackBot;
import com.ullink.slack.simpleslackapi.SlackChannel;
import com.ullink.slack.simpleslackapi.SlackChatConfiguration;
import com.ullink.slack.simpleslackapi.SlackIntegration;
import com.ullink.slack.simpleslackapi.SlackMessageHandle;
import com.ullink.slack.simpleslackapi.SlackPersona;
import com.ullink.slack.simpleslackapi.SlackPreparedMessage;
import com.ullink.slack.simpleslackapi.SlackSession;
import com.ullink.slack.simpleslackapi.SlackTeam;
import com.ullink.slack.simpleslackapi.SlackUser;
import com.ullink.slack.simpleslackapi.listeners.*;
import com.ullink.slack.simpleslackapi.replies.SlackMessageReply;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArrayList;

abstract class AbstractSlackSessionImpl implements SlackSession {
    protected Map<String, SlackChannel> channels = new ConcurrentHashMap();
    protected Map<String, SlackUser> users = new ConcurrentHashMap();
    protected Map<String, SlackIntegration> integrations = new ConcurrentHashMap();
    protected SlackPersona sessionPersona;
    protected SlackTeam team;
    protected final List<SlackChannelArchivedListener> channelArchiveListener = new CopyOnWriteArrayList();
    protected final List<SlackChannelCreatedListener> channelCreateListener = new CopyOnWriteArrayList();
    protected final List<SlackChannelDeletedListener> channelDeleteListener = new CopyOnWriteArrayList();
    protected final List<SlackChannelRenamedListener> channelRenamedListener = new CopyOnWriteArrayList();
    protected final List<SlackChannelUnarchivedListener> channelUnarchiveListener = new CopyOnWriteArrayList();
    protected final List<SlackChannelJoinedListener> channelJoinedListener = new CopyOnWriteArrayList();
    protected final List<SlackChannelLeftListener> channelLeftListener = new CopyOnWriteArrayList();
    protected final List<SlackGroupJoinedListener> groupJoinedListener = new CopyOnWriteArrayList();
    protected final List<SlackMessageDeletedListener> messageDeletedListener = new CopyOnWriteArrayList();
    protected final List<SlackMessagePostedListener> messagePostedListener = new CopyOnWriteArrayList();
    protected final List<SlackMessageUpdatedListener> messageUpdatedListener = new CopyOnWriteArrayList();
    protected final List<SlackConnectedListener> slackConnectedListener = new CopyOnWriteArrayList();
    protected final List<ReactionAddedListener> reactionAddedListener = new CopyOnWriteArrayList();
    protected final List<ReactionRemovedListener> reactionRemovedListener = new CopyOnWriteArrayList();
    protected final List<SlackUserChangeListener> slackUserChangeListener = new CopyOnWriteArrayList();
    protected final List<SlackTeamJoinListener> slackTeamJoinListener = new CopyOnWriteArrayList();
    protected final List<PinAddedListener> pinAddedListener = new CopyOnWriteArrayList();
    protected final List<PinRemovedListener> pinRemovedListener = new CopyOnWriteArrayList();
    protected final List<PresenceChangeListener> presenceChangeListener = new CopyOnWriteArrayList();
    protected final List<SlackDisconnectedListener> slackDisconnectedListener = new CopyOnWriteArrayList();
    protected final List<UserTypingListener> userTypingListener = new CopyOnWriteArrayList();
    protected final List<SlackMemberJoinedListener> slackMemberJoinedListener = new CopyOnWriteArrayList();
    static final SlackChatConfiguration DEFAULT_CONFIGURATION = SlackChatConfiguration.getConfiguration().asUser();
    static final boolean DEFAULT_UNFURL = true;

    AbstractSlackSessionImpl() {
    }

    public SlackTeam getTeam() {
        return this.team;
    }

    public Collection<SlackChannel> getChannels() {
        return new ArrayList(this.channels.values());
    }

    public Collection<SlackUser> getUsers() {
        return new ArrayList(this.users.values());
    }

    public Collection<SlackIntegration> getIntegrations() {
        return new ArrayList(this.integrations.values());
    }

    /** @deprecated */
    @Deprecated
    public Collection<SlackBot> getBots() {
        ArrayList<SlackBot> toReturn = new ArrayList();
        Iterator var2 = this.users.values().iterator();

        while(var2.hasNext()) {
            SlackUser user = (SlackUser)var2.next();
            if (user.isBot()) {
                toReturn.add(user);
            }
        }

        return toReturn;
    }

    public SlackChannel findChannelByName(String channelName) {
        Iterator var2 = this.channels.values().iterator();

        SlackChannel channel;
        do {
            if (!var2.hasNext()) {
                return null;
            }

            channel = (SlackChannel)var2.next();
        } while(!channelName.equals(channel.getName()));

        return channel;
    }

    public SlackChannel findChannelById(String channelId) {
        SlackChannel toReturn = (SlackChannel)this.channels.get(channelId);
        if (toReturn == null && channelId != null && channelId.startsWith("D")) {
            toReturn = SlackChannel.builder().id(channelId).name("").topic("").name("").build();
        }

        return toReturn;
    }

    public SlackUser findUserById(String userId) {
        return (SlackUser)this.users.get(userId);
    }

    public SlackUser findUserByUserName(String userName) {
        Iterator var2 = this.users.values().iterator();

        SlackUser user;
        do {
            if (!var2.hasNext()) {
                return null;
            }

            user = (SlackUser)var2.next();
        } while(!userName.equals(user.getUserName()));

        return user;
    }

    public SlackUser findUserByEmail(String userMail) {
        Iterator var2 = this.users.values().iterator();

        SlackUser user;
        do {
            if (!var2.hasNext()) {
                return null;
            }

            user = (SlackUser)var2.next();
        } while(!userMail.equalsIgnoreCase(user.getUserMail()));

        return user;
    }

    public SlackIntegration findIntegrationById(String integrationId) {
        return (SlackIntegration)this.integrations.get(integrationId);
    }

    public SlackPersona sessionPersona() {
        return this.sessionPersona;
    }

    /** @deprecated */
    @Deprecated
    public SlackBot findBotById(String botId) {
        return (SlackBot)this.users.get(botId);
    }

    public SlackMessageHandle<SlackMessageReply> sendMessage(SlackChannel channel, String message, SlackAttachment attachment) {
        return this.sendMessage(channel, message, attachment, DEFAULT_CONFIGURATION);
    }

    public SlackMessageHandle<SlackMessageReply> sendMessage(SlackChannel channel, String message) {
        return this.sendMessage(channel, message, true);
    }

    public SlackMessageHandle<SlackMessageReply> sendMessage(SlackChannel channel, String message, boolean unfurl) {
        SlackPreparedMessage preparedMessage = SlackPreparedMessage.builder().message(message).unfurl(unfurl).build();
        return this.sendMessage(channel, preparedMessage, DEFAULT_CONFIGURATION);
    }

    public SlackMessageHandle<SlackMessageReply> sendMessage(SlackChannel channel, String message, SlackAttachment attachment, boolean unfurl) {
        return this.sendMessage(channel, message, attachment, DEFAULT_CONFIGURATION, unfurl);
    }

    public SlackMessageHandle<SlackMessageReply> sendMessage(SlackChannel channel, String message, SlackAttachment attachment, SlackChatConfiguration chatConfiguration) {
        return this.sendMessage(channel, message, attachment, chatConfiguration, true);
    }

    public SlackMessageHandle<SlackMessageReply> sendMessage(SlackChannel channel, SlackPreparedMessage preparedMessage) {
        return this.sendMessage(channel, preparedMessage, DEFAULT_CONFIGURATION);
    }

    public SlackMessageHandle<SlackMessageReply> sendMessage(SlackChannel channel, String message, SlackAttachment attachment, SlackChatConfiguration chatConfiguration, boolean unfurl) {
        SlackPreparedMessage preparedMessage = SlackPreparedMessage.builder().message(message).unfurl(unfurl).attachment(attachment).build();
        return this.sendMessage(channel, preparedMessage, chatConfiguration);
    }

    public SlackMessageHandle<SlackMessageReply> sendEphemeralMessage(SlackChannel channel, SlackUser user, SlackPreparedMessage preparedMessage) {
        return this.sendEphemeralMessage(channel, user, preparedMessage, DEFAULT_CONFIGURATION);
    }

    public SlackMessageHandle<SlackMessageReply> sendEphemeralMessage(SlackChannel channel, SlackUser user, String message, SlackAttachment attachment, SlackChatConfiguration chatConfiguration, boolean unfurl) {
        SlackPreparedMessage preparedMessage = SlackPreparedMessage.builder().message(message).unfurl(unfurl).attachment(attachment).build();
        return this.sendEphemeralMessage(channel, user, preparedMessage, chatConfiguration);
    }

    public SlackMessageHandle<SlackMessageReply> sendEphemeralMessage(SlackChannel channel, SlackUser user, String message, SlackAttachment attachment, SlackChatConfiguration chatConfiguration) {
        return this.sendEphemeralMessage(channel, user, message, attachment, chatConfiguration, true);
    }

    public SlackMessageHandle<SlackMessageReply> sendEphemeralMessage(SlackChannel channel, SlackUser user, String message, SlackAttachment attachment, boolean unfurl) {
        return this.sendEphemeralMessage(channel, user, message, attachment, DEFAULT_CONFIGURATION, unfurl);
    }

    public SlackMessageHandle<SlackMessageReply> sendEphemeralMessage(SlackChannel channel, SlackUser user, String message, SlackAttachment attachment) {
        return this.sendEphemeralMessage(channel, user, message, attachment, DEFAULT_CONFIGURATION);
    }

    public SlackMessageHandle<SlackMessageReply> sendEphemeralMessage(SlackChannel channel, SlackUser user, String message, boolean unfurl) {
        SlackPreparedMessage preparedMessage = SlackPreparedMessage.builder().message(message).unfurl(unfurl).build();
        return this.sendEphemeralMessage(channel, user, preparedMessage, DEFAULT_CONFIGURATION);
    }

    public SlackMessageHandle<SlackMessageReply> sendEphemeralMessage(SlackChannel channel, SlackUser user, String message) {
        return this.sendEphemeralMessage(channel, user, message, true);
    }

    public void addChannelArchivedListener(SlackChannelArchivedListener listener) {
        this.channelArchiveListener.add(listener);
    }

    public void removeChannelArchivedListener(SlackChannelArchivedListener listener) {
        this.channelArchiveListener.remove(listener);
    }

    public void addChannelCreatedListener(SlackChannelCreatedListener listener) {
        this.channelCreateListener.add(listener);
    }

    public void removeChannelCreatedListener(SlackChannelCreatedListener listener) {
        this.channelCreateListener.remove(listener);
    }

    public void addChannelDeletedListener(SlackChannelDeletedListener listener) {
        this.channelDeleteListener.add(listener);
    }

    public void removeChannelDeletedListener(SlackChannelDeletedListener listener) {
        this.channelDeleteListener.remove(listener);
    }

    public void addChannelRenamedListener(SlackChannelRenamedListener listener) {
        this.channelRenamedListener.add(listener);
    }

    public void removeChannelRenamedListener(SlackChannelRenamedListener listener) {
        this.channelRenamedListener.remove(listener);
    }

    public void addChannelUnarchivedListener(SlackChannelUnarchivedListener listener) {
        this.channelUnarchiveListener.add(listener);
    }

    public void removeChannelUnarchivedListener(SlackChannelUnarchivedListener listener) {
        this.channelUnarchiveListener.remove(listener);
    }

    public void addMessageDeletedListener(SlackMessageDeletedListener listener) {
        this.messageDeletedListener.add(listener);
    }

    public void removeMessageDeletedListener(SlackMessageDeletedListener listener) {
        this.messageDeletedListener.remove(listener);
    }

    public void addMessagePostedListener(SlackMessagePostedListener listener) {
        this.messagePostedListener.add(listener);
    }

    public void removeMessagePostedListener(SlackMessagePostedListener listener) {
        this.messagePostedListener.remove(listener);
    }

    public void addMessageUpdatedListener(SlackMessageUpdatedListener listener) {
        this.messageUpdatedListener.add(listener);
    }

    public void removeMessageUpdatedListener(SlackMessageUpdatedListener listener) {
        this.messageUpdatedListener.remove(listener);
    }

    public void addChannelJoinedListener(SlackChannelJoinedListener listener) {
        this.channelJoinedListener.add(listener);
    }

    public void removeChannelJoinedListener(SlackChannelJoinedListener listener) {
        this.channelJoinedListener.remove(listener);
    }

    public void addChannelLeftListener(SlackChannelLeftListener listener) {
        this.channelLeftListener.add(listener);
    }

    public void removeChannelLeftListener(SlackChannelLeftListener listener) {
        this.channelLeftListener.remove(listener);
    }

    public void addGroupJoinedListener(SlackGroupJoinedListener listener) {
        this.groupJoinedListener.add(listener);
    }

    public void removeGroupJoinedListener(SlackGroupJoinedListener listener) {
        this.groupJoinedListener.remove(listener);
    }

    public void addSlackConnectedListener(SlackConnectedListener listener) {
        this.slackConnectedListener.add(listener);
    }

    public void removeSlackConnectedListener(SlackConnectedListener listener) {
        this.slackConnectedListener.remove(listener);
    }

    public void addSlackDisconnectedListener(SlackDisconnectedListener listener) {
        this.slackDisconnectedListener.add(listener);
    }

    public void removeSlackDisconnectedListener(SlackDisconnectedListener listener) {
        this.slackDisconnectedListener.remove(listener);
    }

    public void addReactionAddedListener(ReactionAddedListener listener) {
        this.reactionAddedListener.add(listener);
    }

    public void removeReactionAddedListener(ReactionAddedListener listener) {
        this.reactionAddedListener.remove(listener);
    }

    public void addReactionRemovedListener(ReactionRemovedListener listener) {
        this.reactionRemovedListener.add(listener);
    }

    public void removeReactionRemovedListener(ReactionRemovedListener listener) {
        this.reactionRemovedListener.remove(listener);
    }

    public void addSlackUserChangeListener(SlackUserChangeListener listener) {
        this.slackUserChangeListener.add(listener);
    }

    public void removeSlackUserChangeListener(SlackUserChangeListener listener) {
        this.slackUserChangeListener.remove(listener);
    }

    public void addSlackTeamJoinListener(SlackTeamJoinListener listener) {
        this.slackTeamJoinListener.add(listener);
    }

    public void removeSlackTeamJoinListener(SlackTeamJoinListener listener) {
        this.slackTeamJoinListener.remove(listener);
    }

    public void addPinAddedListener(PinAddedListener listener) {
        this.pinAddedListener.add(listener);
    }

    public void removePinAddedListener(PinAddedListener listener) {
        this.pinAddedListener.remove(listener);
    }

    public void addPinRemovedListener(PinRemovedListener listener) {
        this.pinRemovedListener.add(listener);
    }

    public void removePinRemovedListener(PinRemovedListener listener) {
        this.pinRemovedListener.remove(listener);
    }

    public void addPresenceChangeListener(PresenceChangeListener listener) {
        this.presenceChangeListener.add(listener);
    }

    public void removePresenceChangeListener(PresenceChangeListener listener) {
        this.presenceChangeListener.remove(listener);
    }

    public void addUserTypingListener(UserTypingListener listener) {
        this.userTypingListener.add(listener);
    }

    public void removeUserTypingListener(UserTypingListener listener) {
        this.userTypingListener.remove(listener);
    }

    public void addSlackMemberJoinedListener(SlackMemberJoinedListener listener) {
        this.slackMemberJoinedListener.add(listener);
    }

    public void removeSlackMemberJoinedListener(SlackMemberJoinedListener listener) {
        this.slackMemberJoinedListener.remove(listener);
    }
}
