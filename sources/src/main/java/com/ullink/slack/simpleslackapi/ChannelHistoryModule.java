package com.ullink.slack.simpleslackapi;

import java.util.List;
import org.threeten.bp.LocalDate;
import com.ullink.slack.simpleslackapi.events.SlackMessagePosted;

public interface ChannelHistoryModule {
    
    List<SlackMessagePosted> fetchHistoryOfChannel(String channelName);

    List<SlackMessagePosted> fetchHistoryOfChannel(String channelName, LocalDate day);

    List<SlackMessagePosted> fetchHistoryOfChannel(String channelName, int numberOfMessages);

    List<SlackMessagePosted> fetchHistoryOfChannel(String channelName, LocalDate day, int numberOfMessages);

    List<SlackMessagePosted> fetchUpdatingHistoryOfChannel(String channelId);
    
    List<SlackMessagePosted> fetchUpdatingHistoryOfChannel(String channelId, LocalDate day);

    List<SlackMessagePosted> fetchUpdatingHistoryOfChannel(String channelId, int numberOfMessages);

    List<SlackMessagePosted> fetchUpdatingHistoryOfChannel(String channelId, LocalDate day, int numberOfMessages);

    
}
