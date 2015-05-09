package com.ullink.slack.simpleslackapi.impl;

import com.ullink.slack.simpleslackapi.*;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import com.ullink.slack.simpleslackapi.listeners.SlackChannelArchiveListener;
import com.ullink.slack.simpleslackapi.listeners.SlackChannelCreateListener;
import com.ullink.slack.simpleslackapi.listeners.SlackChannelDeleteListener;
import com.ullink.slack.simpleslackapi.listeners.SlackChannelUnarchiveListener;
import com.ullink.slack.simpleslackapi.listeners.SlackMessageDeletedListener;
import com.ullink.slack.simpleslackapi.listeners.SlackMessageSentListener;
import com.ullink.slack.simpleslackapi.listeners.SlackMessageUpdatedListener;

abstract class AbstractSlackSessionImpl implements SlackSession
{

    protected Map<String, SlackChannel>                     channels                  = new HashMap<>();
    protected Map<String, SlackUser>                        users                     = new HashMap<>();
    protected Map<String, SlackBot>                         bots                      = new HashMap<>();
    protected SlackPersona                                  sessionPersona;

    protected Set<SlackMessageListener>                     oldMessageListeners       = new HashSet<>();

    protected List<SlackMessageSentListener>                messageSentListeners      = new ArrayList<>();
    protected List<SlackMessageDeletedListener>             messageDeletedListeners   = new ArrayList<>();
    protected List<SlackMessageUpdatedListener>             messageUpdatedListeners   = new ArrayList<>();

    protected List<SlackChannelArchiveListener>             channelArchivedListener   = new ArrayList<>();
    protected List<SlackChannelUnarchiveListener>           channelUnarchivedListener = new ArrayList<>();
    protected List<SlackChannelCreateListener>              channelCreatedListener    = new ArrayList<>();
    protected List<SlackChannelDeleteListener>              channelDeletedListener    = new ArrayList<>();

    static final SlackChatConfiguration                     DEFAULT_CONFIGURATION     = SlackChatConfiguration.getConfiguration().asUser();

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
    public Collection<SlackBot> getBots()
    {
        return new ArrayList<>(bots.values());
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
    public SlackPersona sessionPersona() {
        return sessionPersona;
    }

    @Override
    public SlackBot findBotById(String botId)
    {
        return bots.get(botId);
    }

    @Override
    public void addMessageListener(SlackMessageListener listenerToAdd)
    {
        oldMessageListeners.add(listenerToAdd);
    }

    @Override
    public void removeMessageListener(SlackMessageListener listenerToRemove)
    {
        oldMessageListeners.add(listenerToRemove);
    }

    @Override
    public SlackMessageHandle sendMessage(SlackChannel channel, String message, SlackAttachment attachment)
    {
        return sendMessage(channel, message, attachment, DEFAULT_CONFIGURATION);
    }

}
