package com.ullink.slack.simpleslackapi.impl;

import com.ullink.slack.simpleslackapi.SlackBot;
import com.ullink.slack.simpleslackapi.SlackChannel;
import com.ullink.slack.simpleslackapi.SlackMessageListener;
import com.ullink.slack.simpleslackapi.SlackSession;
import com.ullink.slack.simpleslackapi.SlackUser;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

abstract class AbstractSlackSessionImpl implements SlackSession
{

    protected Collection<SlackChannel> channels = new ArrayList<>();
    protected Collection<SlackUser> users = new ArrayList<>();
    protected Collection<SlackBot> bots = new ArrayList<>();

    protected Set<SlackMessageListener> messageListeners = new HashSet<>();

    @Override
    public Collection<SlackChannel> getChannels()
    {
        return new ArrayList<>(channels);
    }

    @Override
    public Collection<SlackUser> getUsers()
    {
        return new ArrayList<>(users);
    }

    @Override
    public Collection<SlackBot> getBots()
    {
        return new ArrayList<>(bots);
    }

    @Override
    public SlackChannel findChannelByName(String channelName)
    {
        for (SlackChannel channel : channels)
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
        for (SlackChannel channel : channels)
        {
            if (channelId.equals(channel.getId()))
            {
                return channel;
            }
        }
        return null;
    }

    @Override
    public SlackUser findUserById(String userId)
    {
        for (SlackUser user : users)
        {
            if (userId.equals(user.getId()))
            {
                return user;
            }
        }
        return null;
    }

    @Override
    public SlackUser findUserByUserName(String userName)
    {
        for (SlackUser user : users)
        {
            if (userName.equals(user.getUserName()))
            {
                return user;
            }
        }
        return null;
    }

    @Override
    public SlackBot findBotById(String botId)
    {
        for (SlackBot bot : bots)
        {
            if (botId.equals(bot.getId()))
            {
                return bot;
            }
        }
        return null;
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
