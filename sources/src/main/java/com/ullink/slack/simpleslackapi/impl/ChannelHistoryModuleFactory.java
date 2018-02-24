package com.ullink.slack.simpleslackapi.impl;

import com.ullink.slack.simpleslackapi.ChannelHistoryModule;
import com.ullink.slack.simpleslackapi.entity.SlackSession;

public class ChannelHistoryModuleFactory {
    
    public static ChannelHistoryModule createChannelHistoryModule(SlackSession session){
        return new ChannelHistoryModuleImpl(session);
    };
    
}
