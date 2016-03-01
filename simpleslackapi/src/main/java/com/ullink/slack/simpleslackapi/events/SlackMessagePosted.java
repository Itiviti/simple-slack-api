package com.ullink.slack.simpleslackapi.events;

import java.util.Map;
import org.json.simple.JSONObject;
import com.ullink.slack.simpleslackapi.SlackBot;
import com.ullink.slack.simpleslackapi.SlackChannel;
import com.ullink.slack.simpleslackapi.SlackFile;
import com.ullink.slack.simpleslackapi.SlackUser;

public interface SlackMessagePosted extends SlackMessageEvent
{
    String getMessageContent();

    SlackUser getSender();

    @Deprecated
    SlackBot getBot();

    SlackChannel getChannel();
    
    SlackFile getSlackFile();
    
    JSONObject getJsonSource();
    
    String getTimestamp();
    
    Map<String, Integer> getReactions();
    
    int getTotalCountOfReactions();

}
