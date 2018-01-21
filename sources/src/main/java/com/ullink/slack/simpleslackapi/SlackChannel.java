package com.ullink.slack.simpleslackapi;

import java.util.Collection;
import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.TimeUnit;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.threeten.bp.LocalDateTime;

import com.ullink.slack.simpleslackapi.SlackSession.GetMembersForChannelCallable;


//TODO: a domain object
public class SlackChannel {
    private static final Logger LOGGER = LoggerFactory.getLogger(SlackChannel.class);
    private static final long REFRESH_MEMBERS_EVERY_SECONDS = TimeUnit.HOURS.toSeconds(1);

    private final boolean direct;
    private String         id;
    private String         name;
    private Set<SlackUser> members = new HashSet<>();
    private GetMembersForChannelCallable getMembersForChannelCallable;
    private String         topic;
    private String         purpose;
    private boolean        isMember;
    private boolean        isArchived;
    private LocalDateTime  membersLastUpdated;

    public SlackChannel(String id,
                        String name,
                        GetMembersForChannelCallable getMembersForChannelCallable,
                        String topic,
                        String purpose,
                        boolean direct,
                        boolean isMember,
                        boolean isArchived)
    {
        this.id = id;
        this.name = name;
        this.getMembersForChannelCallable = getMembersForChannelCallable;
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

    public Collection<SlackUser> getMembers() {
        if (shouldRefreshMembers()) {
            try {
                members = getMembersForChannelCallable.setChannelId(id).call();
                membersLastUpdated = LocalDateTime.now();
            } catch (Exception e) {
                LOGGER.error("Failed to refresh members for {}", name, e);
            }
        }

        return members;
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

    private boolean shouldRefreshMembers() {
        return membersLastUpdated == null ||
            LocalDateTime.now().isAfter(membersLastUpdated.plusSeconds(REFRESH_MEMBERS_EVERY_SECONDS));
    }
}
