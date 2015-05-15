package com.ullink.slack.simpleslackapi.impl;

import com.ullink.slack.simpleslackapi.*;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import com.ullink.slack.simpleslackapi.listeners.SlackChannelArchiveListener;
import com.ullink.slack.simpleslackapi.listeners.SlackChannelCreateListener;
import com.ullink.slack.simpleslackapi.listeners.SlackChannelDeleteListener;
import com.ullink.slack.simpleslackapi.listeners.SlackChannelRenamedListener;
import com.ullink.slack.simpleslackapi.listeners.SlackChannelUnarchiveListener;
import com.ullink.slack.simpleslackapi.listeners.SlackGroupJoinedListener;
import com.ullink.slack.simpleslackapi.listeners.SlackMessageDeletedListener;
import com.ullink.slack.simpleslackapi.listeners.SlackMessagePostedListener;
import com.ullink.slack.simpleslackapi.listeners.SlackMessageUpdatedListener;

abstract class AbstractSlackSessionImpl implements SlackSession
{

    protected Map<String, SlackChannel>           channels                 = new HashMap<>();
    protected Map<String, SlackUser>              users                    = new HashMap<>();
    protected Map<String, SlackBot>               bots                     = new HashMap<>();
    protected SlackPersona                        sessionPersona;

    protected List<SlackChannelArchiveListener>   channelArchiveListener   = new ArrayList<SlackChannelArchiveListener>();
    protected List<SlackChannelCreateListener>    channelCreateListener    = new ArrayList<SlackChannelCreateListener>();
    protected List<SlackChannelDeleteListener>    channelDeleteListener    = new ArrayList<SlackChannelDeleteListener>();
    protected List<SlackChannelRenamedListener>   channelRenamedListener   = new ArrayList<SlackChannelRenamedListener>();
    protected List<SlackChannelUnarchiveListener> channelUnarchiveListener = new ArrayList<SlackChannelUnarchiveListener>();
    protected List<SlackGroupJoinedListener>      groupJoinedListener      = new ArrayList<SlackGroupJoinedListener>();
    protected List<SlackMessageDeletedListener>   messageDeletedListener   = new ArrayList<SlackMessageDeletedListener>();
    protected List<SlackMessagePostedListener>    messagePostedListener    = new ArrayList<SlackMessagePostedListener>();
    protected List<SlackMessageUpdatedListener>   messageUpdatedListener   = new ArrayList<SlackMessageUpdatedListener>();
    protected List<SlackReplyListener>            slackReplyListener       = new ArrayList<SlackReplyListener>();

    static final SlackChatConfiguration           DEFAULT_CONFIGURATION    = SlackChatConfiguration.getConfiguration().asUser();

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
    public SlackPersona sessionPersona()
    {
        return sessionPersona;
    }

    @Override
    public SlackBot findBotById(String botId)
    {
        return bots.get(botId);
    }

    @Override
    public SlackMessageHandle sendMessage(SlackChannel channel, String message, SlackAttachment attachment)
    {
        return sendMessage(channel, message, attachment, DEFAULT_CONFIGURATION);
    }

    @Override
    public void addchannelArchiveListener(SlackChannelArchiveListener listener)
    {
        channelArchiveListener.add(listener);
    }

    @Override
    public void removeChannelArchiveListener(SlackChannelArchiveListener listener)
    {
        channelArchiveListener.remove(listener);
    }

    @Override
    public void addchannelCreateListener(SlackChannelCreateListener listener)
    {
        channelCreateListener.add(listener);
    }

    @Override
    public void removeChannelCreateListener(SlackChannelCreateListener listener)
    {
        channelCreateListener.remove(listener);
    }

    @Override
    public void addchannelDeleteListener(SlackChannelDeleteListener listener)
    {
        channelDeleteListener.add(listener);
    }

    @Override
    public void removeChannelDeleteListener(SlackChannelDeleteListener listener)
    {
        channelDeleteListener.remove(listener);
    }

    @Override
    public void addchannelRenamedListener(SlackChannelRenamedListener listener)
    {
        channelRenamedListener.add(listener);
    }

    @Override
    public void removeChannelRenamedListener(SlackChannelRenamedListener listener)
    {
        channelRenamedListener.remove(listener);
    }

    @Override
    public void addChannelUnarchiveListener(SlackChannelUnarchiveListener listener)
    {
        channelUnarchiveListener.add(listener);
    }

    @Override
    public void removeChannelUnarchiveListener(SlackChannelUnarchiveListener listener)
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

    void addSlackReplyListener(SlackReplyListener listener)
    {
        slackReplyListener.add(listener);
    }

    void removeSlackReplyListener(SlackReplyListener listener)
    {
        slackReplyListener.remove(listener);
    }

}
