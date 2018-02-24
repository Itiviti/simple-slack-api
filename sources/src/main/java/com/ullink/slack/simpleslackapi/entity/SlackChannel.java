package com.ullink.slack.simpleslackapi.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.HashSet;
import java.util.Set;

//TODO: a domain object
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class SlackChannel {
    private boolean direct;
    private String id;
    private String name;
    private Set<SlackUser> members = new HashSet<>();
    private String topic;
    private String purpose;
    private boolean isMember;
    private boolean isArchived;

    public SlackChannel(String id, String name, String topic, String purpose, boolean direct, boolean isMember, boolean isArchived) {
        this.id = id;
        this.name = name;
        this.topic = topic;
        this.purpose = purpose;
        this.direct = direct;
        this.isMember = isMember;
        this.isArchived = isArchived;
    }

    public void addUser(SlackUser user) {
        members.add(user);
    }

    void removeUser(SlackUser user) {
        members.remove(user);
    }

    public SlackChannelType getType() {
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
