package com.ullink.slack.simpleslackapi.events;

import com.ullink.slack.simpleslackapi.SlackChannel;
import com.ullink.slack.simpleslackapi.SlackUser;
import com.ullink.slack.simpleslackapi.events.SlackEvent;
import com.ullink.slack.simpleslackapi.events.SlackEventType;

public class UserTyping implements SlackEvent {
  private final SlackChannel slackChannel;
  private final SlackUser slackUser;
  private final SlackEventType slackEventType;

  public UserTyping(SlackChannel slackChannel, SlackUser slackUser, SlackEventType slackEventType) {
    this.slackChannel = slackChannel;
    this.slackUser = slackUser;
    this.slackEventType = slackEventType;
  }

  public SlackChannel getChannel() {
    return slackChannel;
  }

  public SlackUser getUser() {
    return slackUser;
  }

  @Override
  public SlackEventType getEventType() {
    return slackEventType;
  }
}
