package com.ullink.slack.simpleslackapi.impl;

import com.ullink.slack.simpleslackapi.*;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

abstract class AbstractSlackSessionImpl implements SlackSession
{

    protected Map<String, SlackChannel> channels         = new HashMap<>();
    protected Map<String, SlackUser>    users            = new HashMap<>();
    protected Map<String, SlackBot>     bots             = new HashMap<>();
    protected SlackPersona sessionPersona;

    protected Set<SlackMessageListener> messageListeners = new HashSet<>();

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
        return channels.get(channelId);
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
        messageListeners.add(listenerToAdd);
    }

    @Override
    public void removeMessageListener(SlackMessageListener listenerToRemove)
    {
        messageListeners.add(listenerToRemove);
    }

}
