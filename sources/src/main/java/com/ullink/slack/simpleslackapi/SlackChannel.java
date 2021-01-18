package com.ullink.slack.simpleslackapi;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Singular;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

@Builder(toBuilder = true)
@AllArgsConstructor
@Getter
public class SlackChannel {
    private String         id;
    private String         name;
    private String         topic;
    private String         purpose;
    private boolean        direct;
    private boolean        isMember;
    private boolean        isArchived;
    @Singular
    private transient Set<SlackUser> members;

    public SlackChannel(String id, String name, String topic, String purpose, boolean direct, boolean isMember, boolean isArchived)
    {
        this.id = id;
        this.name = name;
        this.topic = topic;
        this.purpose = purpose;
        this.direct = direct;
        this.isMember = isMember;
        this.isArchived = isArchived;
        this.members = new HashSet<>();
    }

    public SlackChannel() {
        this.members = new HashSet<>();
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
