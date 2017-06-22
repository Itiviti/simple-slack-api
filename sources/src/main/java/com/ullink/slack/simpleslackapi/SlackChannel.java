package com.ullink.slack.simpleslackapi;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

import com.ullink.slack.simpleslackapi.SlackUser;

//TODO: a domain object
public class SlackChannel {
    private final boolean direct;
    private String         id;
    private String         name;
    private Set<SlackUser> members = new HashSet<>();
    private String         topic;
    private String         purpose;
    private boolean        isMember;
    private boolean        isArchived;

    public SlackChannel(String id, String name, String topic, String purpose, boolean direct, boolean isMember, boolean isArchived)
    {
        this.id = id;
        this.name = name;
        this.topic = topic;
        this.purpose = purpose;
        this.direct = direct;
        this.isMember = isMember;
        this.isArchived = isArchived;
    }

    public void addUser(SlackUser user)
    {
        members.add(user);
    }

    void removeUser(SlackUser user)
    {
        members.remove(user);
    }

    public String getId()
    {
        return id;
    }

    public String getName()
    {
        return name;
    }

    public Collection<SlackUser> getMembers()
    {
        return new ArrayList<>(members);
    }

    public String getTopic()
    {
        return topic;
    }

    @Override
    public String toString() {
        return "SlackChannel{" +
                "topic='" + topic + '\'' +
                ", purpose='" + purpose + '\'' +
                ", id='" + id + '\'' +
                ", name='" + name + '\'' +
                '}';
    }

    public String getPurpose()
    {
        return purpose;
    }

    public boolean isDirect() {
        return direct;
    }

    public boolean isMember() {
        return isMember;
    }

    public boolean isArchived() {
        return isArchived;
    }

    public SlackChannelType getType()
    {
        //that's a bit hacky
        if (isDirect()) {
            return SlackChannelType.INSTANT_MESSAGING;
        }
        if (id.startsWith("G")) {
            return SlackChannelType.PRIVATE_GROUP;
        }
        return SlackChannelType.PUBLIC_CHANNEL;
    }

    public enum SlackChannelType {
        PUBLIC_CHANNEL, PRIVATE_GROUP, INSTANT_MESSAGING
    }
}
