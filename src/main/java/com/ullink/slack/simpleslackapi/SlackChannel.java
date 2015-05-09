package com.ullink.slack.simpleslackapi;

import java.util.Collection;

public interface SlackChannel
{
    String getId();

    String getName();

    Collection<SlackUser> getMembers();

    String getTopic();

    String getPurpose();

    boolean isDirect();

}
