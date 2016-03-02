package com.ullink.slack.simpleslackapi.impl;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import com.ullink.slack.simpleslackapi.SlackAttachment;
import com.ullink.slack.simpleslackapi.SlackBot;
import com.ullink.slack.simpleslackapi.SlackChannel;
import com.ullink.slack.simpleslackapi.SlackMessageHandle;
import com.ullink.slack.simpleslackapi.SlackPersona;
import com.ullink.slack.simpleslackapi.SlackSession;
import com.ullink.slack.simpleslackapi.SlackTeam;
import com.ullink.slack.simpleslackapi.SlackUser;
import com.ullink.slack.simpleslackapi.listeners.PinAddedListener;
import com.ullink.slack.simpleslackapi.listeners.PinRemovedListener;
import com.ullink.slack.simpleslackapi.listeners.ReactionAddedListener;
import com.ullink.slack.simpleslackapi.listeners.ReactionRemovedListener;
import com.ullink.slack.simpleslackapi.listeners.SlackChannelArchivedListener;
import com.ullink.slack.simpleslackapi.listeners.SlackChannelCreatedListener;
import com.ullink.slack.simpleslackapi.listeners.SlackChannelDeletedListener;
import com.ullink.slack.simpleslackapi.listeners.SlackChannelRenamedListener;
import com.ullink.slack.simpleslackapi.listeners.SlackChannelUnarchivedListener;
import com.ullink.slack.simpleslackapi.listeners.SlackConnectedListener;
import com.ullink.slack.simpleslackapi.listeners.SlackGroupJoinedListener;
import com.ullink.slack.simpleslackapi.listeners.SlackMessageDeletedListener;
import com.ullink.slack.simpleslackapi.listeners.SlackMessagePostedListener;
import com.ullink.slack.simpleslackapi.listeners.SlackMessageUpdatedListener;
import com.ullink.slack.simpleslackapi.listeners.SlackUserChangeListener;
import com.ullink.slack.simpleslackapi.replies.SlackMessageReply;

abstract class AbstractSlackSessionImpl implements SlackSession
{

    protected Map<String, SlackChannel>            channels                 = new HashMap<>();
    protected Map<String, SlackUser>               users                    = new HashMap<>();
    protected SlackPersona                         sessionPersona;
    protected SlackTeam                            team;

    protected List<SlackChannelArchivedListener>   channelArchiveListener   = new ArrayList<>();
    protected List<SlackChannelCreatedListener>    channelCreateListener    = new ArrayList<>();
    protected List<SlackChannelDeletedListener>    channelDeleteListener    = new ArrayList<>();
    protected List<SlackChannelRenamedListener>    channelRenamedListener   = new ArrayList<>();
    protected List<SlackChannelUnarchivedListener> channelUnarchiveListener = new ArrayList<>();
    protected List<SlackGroupJoinedListener>       groupJoinedListener      = new ArrayList<>();
    protected List<SlackMessageDeletedListener>    messageDeletedListener   = new ArrayList<>();
    protected List<SlackMessagePostedListener>     messagePostedListener    = new ArrayList<>();
    protected List<SlackMessageUpdatedListener>    messageUpdatedListener   = new ArrayList<>();
    protected List<SlackConnectedListener>         slackConnectedListener   = new ArrayList<>();
    protected List<ReactionAddedListener>          reactionAddedListener    = new ArrayList<>();
    protected List<ReactionRemovedListener>        reactionRemovedListener  = new ArrayList<>();
    protected List<SlackUserChangeListener>        slackUserChangeListener  = new ArrayList<>();
    protected List<PinAddedListener>               pinAddedListener         = new ArrayList<>();
    protected List<PinRemovedListener>             pinRemovedListener       = new ArrayList<>();

    static final SlackChatConfiguration            DEFAULT_CONFIGURATION    = SlackChatConfiguration.getConfiguration().asUser();
    static final boolean                           DEFAULT_UNFURL           = true;

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
                toReturn = new SlackChannelImpl(channelId, "", "", "", true);
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
            if (userMail.equals(user.getUserMail()))
            {
                return user;
            }
        }
        return null;
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
        return sendMessage(channel, message, null, DEFAULT_CONFIGURATION, unfurl);
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
    public void addchannelArchivedListener(SlackChannelArchivedListener listener)
    {
        channelArchiveListener.add(listener);
    }

    @Override
    public void removeChannelArchivedListener(SlackChannelArchivedListener listener)
    {
        channelArchiveListener.remove(listener);
    }

    @Override
    public void addchannelCreatedListener(SlackChannelCreatedListener listener)
    {
        channelCreateListener.add(listener);
    }

    @Override
    public void removeChannelCreatedListener(SlackChannelCreatedListener listener)
    {
        channelCreateListener.remove(listener);
    }

    @Override
    public void addchannelDeletedListener(SlackChannelDeletedListener listener)
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

}
