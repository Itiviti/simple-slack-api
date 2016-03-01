package com.ullink.slack.simpleslackapi.impl;

import com.ullink.slack.simpleslackapi.SlackChannel;
import com.ullink.slack.simpleslackapi.SlackFile;
import com.ullink.slack.simpleslackapi.SlackUser;
import com.ullink.slack.simpleslackapi.events.PinRemoved;
import com.ullink.slack.simpleslackapi.events.SlackEventType;

public class PinRemovedImpl implements PinRemoved {

  private final SlackUser sender;
  private final SlackChannel channel;
  private final String timestamp;
  private final SlackFile file;
  private final String message;

  public PinRemovedImpl(SlackUser sender, SlackChannel channel, String timestamp, SlackFile file, String message) {
        this.sender = sender;
        this.channel = channel;
        this.timestamp = timestamp;
        this.file = file;
        this.message = message;
    }

    @Override
    public SlackUser getSender() {
        return this.sender;
    }

    @Override
    public SlackChannel getChannel() {
        return this.channel;
    }

    @Override
    public String getTimestamp() {
        return this.timestamp;
    }

    @Override
    public SlackFile getFile() {
        return this.file;
    }

    @Override
    public String getMessage() {
        return this.message;
    }
  
    @Override
    public SlackEventType getEventType() {
        return SlackEventType.PIN_REMOVED;
    }

}
