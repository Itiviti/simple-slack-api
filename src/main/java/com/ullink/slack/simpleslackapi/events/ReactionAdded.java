package com.ullink.slack.simpleslackapi.events;

import com.ullink.slack.simpleslackapi.SlackChannel;


public interface ReactionAdded extends SlackEvent{
    
    public String getEmojiName();
    public SlackChannel getChannel();
    public String getMessageID();
    
}
