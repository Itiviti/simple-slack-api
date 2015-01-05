package com.ullink.slack.simpleslackapi.impl;

import com.ullink.slack.simpleslackapi.SlackChannel;
import com.ullink.slack.simpleslackapi.SlackUser;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

class SlackChannelImpl implements SlackChannel
{
    private String         id;
    private String         name;
    private Set<SlackUser> members = new HashSet<>();
    private String         topic;
    private String         purpose;

    SlackChannelImpl(String id, String name, String topic, String purpose)
    {
        this.id = id;
        this.name = name;
        this.topic = topic;
        this.purpose = purpose;
    }

    void addUser(SlackUser user)
    {
        members.add(user);
    }

    void removeUser(SlackUser user)
    {
        members.remove(user);
    }

    @Override
    public String getId()
    {
        return id;
    }

    @Override
    public String getName()
    {
        return name;
    }

    @Override
    public Collection<SlackUser> getMembers()
    {
        return new ArrayList<>(members);
    }

    @Override
    public String getTopic()
    {
        return topic;
    }

    @Override
    public String getPurpose()
    {
        return purpose;
    }
}
