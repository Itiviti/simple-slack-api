package com.ullink.slack.simpleslackapi;

import com.ullink.slack.simpleslackapi.events.SlackMessagePosted;
import java.time.LocalDate;
import java.util.List;

public interface ChannelHistoryModule {
    
    public List<SlackMessagePosted> fetchHistoryOfChannel(String channelName);

    public List<SlackMessagePosted> fetchHistoryOfChannel(String channelName, LocalDate day);

    public List<SlackMessagePosted> fetchHistoryOfChannel(String channelName, int numberOfMessages);

    public List<SlackMessagePosted> fetchHistoryOfChannel(String channelName, LocalDate day, int numberOfMessages);

    public List<SlackMessagePosted> fetchUpdatingHistoryOfChannel(String channelId);
    
    public List<SlackMessagePosted> fetchUpdatingHistoryOfChannel(String channelId, LocalDate day);

    public List<SlackMessagePosted> fetchUpdatingHistoryOfChannel(String channelId, int numberOfMessages);

    public List<SlackMessagePosted> fetchUpdatingHistoryOfChannel(String channelId, LocalDate day, int numberOfMessages);

    
}
