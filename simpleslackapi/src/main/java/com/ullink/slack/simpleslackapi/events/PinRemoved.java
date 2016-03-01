package com.ullink.slack.simpleslackapi.events;

import com.ullink.slack.simpleslackapi.SlackChannel;
import com.ullink.slack.simpleslackapi.SlackFile;
import com.ullink.slack.simpleslackapi.SlackUser;

public interface PinRemoved extends SlackEvent {

    public SlackUser getSender();

    public SlackChannel getChannel();

    public String getTimestamp();

    public SlackFile getFile();

    public String getMessage();

}
