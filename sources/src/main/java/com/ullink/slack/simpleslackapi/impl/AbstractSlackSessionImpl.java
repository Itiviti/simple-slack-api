package com.ullink.slack.simpleslackapi.impl;

import com.ullink.slack.simpleslackapi.*;
import com.ullink.slack.simpleslackapi.listeners.*;
import com.ullink.slack.simpleslackapi.replies.SlackMessageReply;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArrayList;

abstract class AbstractSlackSessionImpl implements SlackSession
{

    protected Map<String, SlackChannel>            channels                 = new ConcurrentHashMap<>();
    protected Map<String, SlackUser>               users                    = new ConcurrentHashMap<>();
    protected Map<String, SlackIntegration>        integrations             = new ConcurrentHashMap<>();
    protected SlackPersona                         sessionPersona;
    protected SlackTeam team;

    protected List<SlackChannelArchivedListener>   channelArchiveListener   = new CopyOnWriteArrayList<>();
    protected List<SlackChannelCreatedListener>    channelCreateListener    = new CopyOnWriteArrayList<>();
    protected List<SlackChannelDeletedListener>    channelDeleteListener    = new CopyOnWriteArrayList<>();
    protected List<SlackChannelRenamedListener>    channelRenamedListener   = new CopyOnWriteArrayList<>();
    protected List<SlackChannelUnarchivedListener> channelUnarchiveListener = new CopyOnWriteArrayList<>();
    protected List<SlackChannelJoinedListener>     channelJoinedListener    = new CopyOnWriteArrayList<>();
    protected List<SlackChannelLeftListener>       channelLeftListener      = new CopyOnWriteArrayList<>();
    protected List<SlackGroupJoinedListener>       groupJoinedListener      = new CopyOnWriteArrayList<>();
    protected List<SlackMessageDeletedListener>    messageDeletedListener   = new CopyOnWriteArrayList<>();
    protected List<SlackMessagePostedListener>     messagePostedListener    = new CopyOnWriteArrayList<>();
    protected List<SlackMessageUpdatedListener>    messageUpdatedListener   = new CopyOnWriteArrayList<>();
    protected List<SlackConnectedListener>         slackConnectedListener   = new CopyOnWriteArrayList<>();
    protected List<ReactionAddedListener>          reactionAddedListener    = new CopyOnWriteArrayList<>();
    protected List<ReactionRemovedListener>        reactionRemovedListener  = new CopyOnWriteArrayList<>();
    protected List<SlackUserChangeListener>        slackUserChangeListener  = new CopyOnWriteArrayList<>();
    protected List<SlackTeamJoinListener>          slackTeamJoinListener    = new CopyOnWriteArrayList<>();
    protected List<PinAddedListener>               pinAddedListener         = new CopyOnWriteArrayList<>();
    protected List<PinRemovedListener>             pinRemovedListener       = new CopyOnWriteArrayList<>();
    protected List<PresenceChangeListener>         presenceChangeListener   = new CopyOnWriteArrayList<>();
    protected List<SlackDisconnectedListener>     slackDisconnectedListener = new CopyOnWriteArrayList<>();
    protected List<UserTypingListener>             userTypingListener       = new CopyOnWriteArrayList<>();

    static final SlackChatConfiguration            DEFAULT_CONFIGURATION    = SlackChatConfiguration.getConfiguration().asUser();
    static final boolean                           DEFAULT_UNFURL           = true;

    @Override
    public SlackTeam getTeam()
    {
        return team;
    }

    @Override
    public Collection<SlackChannel> getChannels()
    {
        return new ArrayList<>(channels.values());
    }

    @Override
    public Collection<SlackUser> getUsers()
    {
        return new ArrayList<>(users.values());
    }

    @Override
    public Collection<SlackIntegration> getIntegrations() {
        return new ArrayList<>(integrations.values());
    }

    @Override
    @Deprecated
    public Collection<SlackBot> getBots()
    {
        ArrayList<SlackBot> toReturn = new ArrayList<>();
        for (SlackUser user : users.values())
        {
            if (user.isBot())
            {
                toReturn.add(user);
            }
        }
        return toReturn;
    }

    @Override
    public SlackChannel findChannelByName(String channelName)
    {
        for (SlackChannel channel : channels.values())
        {
            if (channelName.equals(channel.getName()))
            {
                return channel;
            }
        }
        return null;
    }

    @Override
    public SlackChannel findChannelById(String channelId)
    {
        SlackChannel toReturn = channels.get(channelId);
        if (toReturn == null)
        {
            // direct channel case
            if (channelId != null && channelId.startsWith("D"))
            {
                toReturn = new SlackChannel(channelId, "", "", "", true, false, false);
            }
        }
        return toReturn;
    }

    @Override
    public SlackUser findUserById(String userId)
    {
        return users.get(userId);
    }

    @Override
    public SlackUser findUserByUserName(String userName)
    {
        for (SlackUser user : users.values())
        {
            if (userName.equals(user.getUserName()))
            {
                return user;
            }
        }
        return null;
    }

    @Override
    public SlackUser findUserByEmail(String userMail)
    {
        for (SlackUser user : users.values())
        {
            if (userMail.equalsIgnoreCase(user.getUserMail()))
            {
                return user;
            }
        }
        return null;
    }

    @Override
    public SlackIntegration findIntegrationById(String integrationId)
    {
        return integrations.get(integrationId);
    }

    @Override
    public SlackPersona sessionPersona()
    {
        return sessionPersona;
    }

    @Override
    @Deprecated
    public SlackBot findBotById(String botId)
    {
        return users.get(botId);
    }

    @Override
    public SlackMessageHandle<SlackMessageReply> sendMessage(SlackChannel channel, String message, SlackAttachment attachment)
    {
        return sendMessage(channel, message, attachment, DEFAULT_CONFIGURATION);
    }

    @Override
    public SlackMessageHandle<SlackMessageReply> sendMessage(SlackChannel channel, String message)
    {
        return sendMessage(channel, message, DEFAULT_UNFURL);
    }

    @Override
    public SlackMessageHandle<SlackMessageReply> sendMessage(SlackChannel channel, String message, boolean unfurl)
    {
        SlackPreparedMessage preparedMessage = new SlackPreparedMessage.Builder()
                .withMessage(message)
                .withUnfurl(unfurl)
                .build();
        return sendMessage(channel, preparedMessage, DEFAULT_CONFIGURATION);
    }

    @Override
    public SlackMessageHandle<SlackMessageReply> sendMessage(SlackChannel channel, String message, SlackAttachment attachment, boolean unfurl)
    {
        return sendMessage(channel, message, attachment, DEFAULT_CONFIGURATION, unfurl);
    }

    @Override
    public SlackMessageHandle<SlackMessageReply> sendMessage(SlackChannel channel, String message, SlackAttachment attachment, SlackChatConfiguration chatConfiguration)
    {
        return sendMessage(channel, message, attachment, chatConfiguration, DEFAULT_UNFURL);
    }

    @Override
    public SlackMessageHandle<SlackMessageReply> sendMessage(SlackChannel channel, SlackPreparedMessage preparedMessage) {
        return sendMessage(channel, preparedMessage, DEFAULT_CONFIGURATION);
    }

    @Override
    public SlackMessageHandle<SlackMessageReply> sendMessage(SlackChannel channel, String message, SlackAttachment attachment, SlackChatConfiguration chatConfiguration, boolean unfurl)
    {
        SlackPreparedMessage preparedMessage = new SlackPreparedMessage.Builder()
                .withMessage(message)
                .withUnfurl(unfurl)
                .addAttachment(attachment)
                .build();

        return sendMessage(channel, preparedMessage, chatConfiguration);
    }

    @Override
    public SlackMessageHandle<SlackMessageReply> sendEphemeralMessage(SlackChannel channel, SlackUser user, SlackPreparedMessage preparedMessage)
    {
        return sendEphemeralMessage(channel, user, preparedMessage, DEFAULT_CONFIGURATION);
    }

    @Override
    public SlackMessageHandle<SlackMessageReply> sendEphemeralMessage(SlackChannel channel, SlackUser user, String message, SlackAttachment attachment, SlackChatConfiguration chatConfiguration, boolean unfurl)
    {
        SlackPreparedMessage preparedMessage = new SlackPreparedMessage.Builder()
                .withMessage(message)
                .withUnfurl(unfurl)
                .addAttachment(attachment)
                .build();

        return sendEphemeralMessage(channel, user, preparedMessage, chatConfiguration);
    }

    @Override
    public SlackMessageHandle<SlackMessageReply> sendEphemeralMessage(SlackChannel channel, SlackUser user, String message, SlackAttachment attachment, SlackChatConfiguration chatConfiguration)
    {
        return sendEphemeralMessage(channel, user, message, attachment, chatConfiguration, DEFAULT_UNFURL);
    }

    @Override
    public SlackMessageHandle<SlackMessageReply> sendEphemeralMessage(SlackChannel channel, SlackUser user, String message, SlackAttachment attachment, boolean unfurl)
    {
        return sendEphemeralMessage(channel, user, message, attachment, DEFAULT_CONFIGURATION, unfurl);
    }

    @Override
    public SlackMessageHandle<SlackMessageReply> sendEphemeralMessage(SlackChannel channel, SlackUser user, String message, SlackAttachment attachment)
    {
        return sendEphemeralMessage(channel, user, message, attachment, DEFAULT_CONFIGURATION);
    }

    @Override
    public SlackMessageHandle<SlackMessageReply> sendEphemeralMessage(SlackChannel channel, SlackUser user, String message, boolean unfurl)
    {
        SlackPreparedMessage preparedMessage = new SlackPreparedMessage.Builder()
                .withMessage(message)
                .withUnfurl(unfurl)
                .build();
        return sendEphemeralMessage(channel, user, preparedMessage, DEFAULT_CONFIGURATION);
    }

    @Override
    public SlackMessageHandle<SlackMessageReply> sendEphemeralMessage(SlackChannel channel, SlackUser user, String message)
    {
        return sendEphemeralMessage(channel, user, message, DEFAULT_UNFURL);
    }

    @Override
    public void addChannelArchivedListener(SlackChannelArchivedListener listener)
    {
        channelArchiveListener.add(listener);
    }

    @Override
    public void removeChannelArchivedListener(SlackChannelArchivedListener listener)
    {
        channelArchiveListener.remove(listener);
    }

    @Override
    public void addChannelCreatedListener(SlackChannelCreatedListener listener)
    {
        channelCreateListener.add(listener);
    }

    @Override
    public void removeChannelCreatedListener(SlackChannelCreatedListener listener)
    {
        channelCreateListener.remove(listener);
    }

    @Override
    public void addChannelDeletedListener(SlackChannelDeletedListener listener)
    {
        channelDeleteListener.add(listener);
    }

    @Override
    public void removeChannelDeletedListener(SlackChannelDeletedListener listener)
    {
        channelDeleteListener.remove(listener);
    }

    @Override
    public void addChannelRenamedListener(SlackChannelRenamedListener listener)
    {
        channelRenamedListener.add(listener);
    }

    @Override
    public void removeChannelRenamedListener(SlackChannelRenamedListener listener)
    {
        channelRenamedListener.remove(listener);
    }

    @Override
    public void addChannelUnarchivedListener(SlackChannelUnarchivedListener listener)
    {
        channelUnarchiveListener.add(listener);
    }

    @Override
    public void removeChannelUnarchivedListener(SlackChannelUnarchivedListener listener)
    {
        channelUnarchiveListener.remove(listener);
    }

    @Override
    public void addMessageDeletedListener(SlackMessageDeletedListener listener)
    {
        messageDeletedListener.add(listener);
    }

    @Override
    public void removeMessageDeletedListener(SlackMessageDeletedListener listener)
    {
        messageDeletedListener.remove(listener);
    }

    @Override
    public void addMessagePostedListener(SlackMessagePostedListener listener)
    {
        messagePostedListener.add(listener);
    }

    @Override
    public void removeMessagePostedListener(SlackMessagePostedListener listener)
    {
        messagePostedListener.remove(listener);
    }

    @Override
    public void addMessageUpdatedListener(SlackMessageUpdatedListener listener)
    {
        messageUpdatedListener.add(listener);
    }

    @Override
    public void removeMessageUpdatedListener(SlackMessageUpdatedListener listener)
    {
        messageUpdatedListener.remove(listener);
    }

    @Override
    public void addChannelJoinedListener(SlackChannelJoinedListener listener)
    {
        channelJoinedListener.add(listener);
    }

    @Override
    public void removeChannelJoinedListener(SlackChannelJoinedListener listener)
    {
        channelJoinedListener.remove(listener);
    }

    @Override
    public void addChannelLeftListener(SlackChannelLeftListener listener)
    {
        channelLeftListener.add(listener);
    }

    @Override
    public void removeChannelLeftListener(SlackChannelLeftListener listener)
    {
        channelLeftListener.remove(listener);
    }

    @Override
    public void addGroupJoinedListener(SlackGroupJoinedListener listener)
    {
        groupJoinedListener.add(listener);
    }

    @Override
    public void removeGroupJoinedListener(SlackGroupJoinedListener listener)
    {
        groupJoinedListener.remove(listener);
    }

    @Override
    public void addSlackConnectedListener(SlackConnectedListener listener)
    {
        slackConnectedListener.add(listener);
    }

    @Override
    public void removeSlackConnectedListener(SlackConnectedListener listener)
    {
        slackConnectedListener.remove(listener);
    }

    @Override
    public void addSlackDisconnectedListener(SlackDisconnectedListener listener) {
        slackDisconnectedListener.add(listener);
    }

    @Override
    public void removeSlackDisconnectedListener(SlackDisconnectedListener listener) {
        slackDisconnectedListener.remove(listener);
    }

    @Override
    public void addReactionAddedListener(ReactionAddedListener listener)
    {
        reactionAddedListener.add(listener);
    }

    @Override
    public void removeReactionAddedListener(ReactionAddedListener listener)
    {
        reactionAddedListener.remove(listener);
    }

    @Override
    public void addReactionRemovedListener(ReactionRemovedListener listener)
    {
        reactionRemovedListener.add(listener);
    }

    @Override
    public void removeReactionRemovedListener(ReactionRemovedListener listener)
    {
        reactionRemovedListener.remove(listener);
    }

    @Override
    public void addSlackUserChangeListener(SlackUserChangeListener listener)
    {
        slackUserChangeListener.add(listener);
    }

    @Override
    public void removeSlackUserChangeListener(SlackUserChangeListener listener)
    {
        slackUserChangeListener.remove(listener);
    }

    @Override
    public void addSlackTeamJoinListener(SlackTeamJoinListener listener)
    {
        slackTeamJoinListener.add(listener);
    }

    @Override
    public void removeSlackTeamJoinListener(SlackTeamJoinListener listener)
    {
        slackTeamJoinListener.remove(listener);
    }

    @Override
    public void addPinAddedListener(PinAddedListener listener)
    {
        pinAddedListener.add(listener);
    }

    @Override
    public void removePinAddedListener(PinAddedListener listener)
    {
        pinAddedListener.remove(listener);
    }

    @Override
    public void addPinRemovedListener(PinRemovedListener listener)
    {
        pinRemovedListener.add(listener);
    }

    @Override
    public void removePinRemovedListener(PinRemovedListener listener)
    {
        pinRemovedListener.remove(listener);
    }

    @Override
    public void addPresenceChangeListener(PresenceChangeListener listener) {
        presenceChangeListener.add(listener);
    }

    @Override
    public void removePresenceChangeListener(PresenceChangeListener listener) {
        presenceChangeListener.remove(listener);
    }

    @Override
    public void addUserTypingListener(UserTypingListener listener) {
        userTypingListener.add(listener);
    }

    @Override
    public void removeUserTypingListener(UserTypingListener listener) {
        userTypingListener.remove(listener);
    }
}
