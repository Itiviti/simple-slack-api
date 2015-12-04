package com.ullink.slack.simpleslackapi.events;

import com.ullink.slack.simpleslackapi.SlackChannel;

import java.util.Collection;

/**
 * User: viktor
 * Date: 12/2/15
 */
public interface SlackChannelHistory {

    public Collection<? extends SlackMessageEvent> getChannelEvents();

    public SlackChannel getChannel();

    public String getLatest();

    public boolean hasMore();

}
