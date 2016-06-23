package com.ullink.slack.simpleslackapi.events;

import com.ullink.slack.simpleslackapi.SlackChannel;
import com.ullink.slack.simpleslackapi.SlackFile;
import com.ullink.slack.simpleslackapi.SlackUser;


public interface PinAdded extends SlackEvent {

    SlackUser getSender();

    SlackChannel getChannel();

    String getTimestamp();

    SlackFile getFile();

    String getMessage();

}
