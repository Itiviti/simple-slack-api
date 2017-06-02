package com.ullink.slack.simpleslackapi.events;

import com.ullink.slack.simpleslackapi.SlackFile;
import com.ullink.slack.simpleslackapi.SlackUser;
import com.ullink.slack.simpleslackapi.SlackChannel;

public class PinAdded implements SlackEvent {

  private final SlackUser sender;
  private final SlackChannel channel;
  private final String timestamp;
  private final SlackFile file;
  private final String message;

  public PinAdded(SlackUser sender, SlackChannel channel, String timestamp, SlackFile file, String message) {
        this.sender = sender;
        this.channel = channel;
        this.timestamp = timestamp;
        this.file = file;
        this.message = message;
    }

    public SlackUser getSender() {
        return this.sender;
    }

    public SlackChannel getChannel() {
        return this.channel;
    }

    public String getTimestamp() {
        return this.timestamp;
    }

    public SlackFile getFile() {
        return this.file;
    }

    public String getMessage() {
        return this.message;
    }
  
    @Override
    public SlackEventType getEventType() {
        return SlackEventType.PIN_ADDED;
    }

}
