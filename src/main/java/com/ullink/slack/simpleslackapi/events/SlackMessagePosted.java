package com.ullink.slack.simpleslackapi.events;

import org.json.simple.JSONObject;

import com.ullink.slack.simpleslackapi.SlackBot;
import com.ullink.slack.simpleslackapi.SlackChannel;
import com.ullink.slack.simpleslackapi.SlackFile;
import com.ullink.slack.simpleslackapi.SlackUser;

public interface SlackMessagePosted extends SlackMessageEvent
{
    String getMessageContent();

    SlackUser getSender();

    SlackBot getBot();

    SlackChannel getChannel();
    
    SlackFile getSlackFile();
    
    JSONObject getJsonSource();
    
}