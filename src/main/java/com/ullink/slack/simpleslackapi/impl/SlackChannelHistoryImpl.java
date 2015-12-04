package com.ullink.slack.simpleslackapi.impl;

import com.ullink.slack.simpleslackapi.SlackChannel;
import com.ullink.slack.simpleslackapi.events.SlackChannelHistory;
import com.ullink.slack.simpleslackapi.events.SlackEvent;
import com.ullink.slack.simpleslackapi.events.SlackMessageEvent;

import java.util.Collection;
import java.util.List;

/**
 * User: viktor
 * Date: 12/3/15
 */
public class SlackChannelHistoryImpl implements SlackChannelHistory {

    private SlackChannel slackChannel;
    private final List<SlackMessageEvent> historyMessages;
    private final String latest;
    private final boolean hasMore;

    public SlackChannelHistoryImpl(SlackChannel slackChannel, List<SlackMessageEvent> historyMessages, String latest, boolean hasMore) {
        this.slackChannel = slackChannel;
        this.historyMessages = historyMessages;
        this.latest = latest;
        this.hasMore = hasMore;
    }

    @Override
    public Collection<? extends SlackMessageEvent> getChannelEvents() {
        return historyMessages;
    }

    @Override
    public SlackChannel getChannel() {
        return slackChannel;
    }

    @Override
    public String getLatest() {
        return latest;
    }

    @Override
    public boolean hasMore() {
        return hasMore;
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder("SlackChannelHistoryImpl{");
        sb.append("slackChannel=").append(slackChannel);
        sb.append(", historyMessages=").append(historyMessages);
        sb.append(", latest='").append(latest).append('\'');
        sb.append(", hasMore=").append(hasMore);
        sb.append('}');
        return sb.toString();
    }
}
