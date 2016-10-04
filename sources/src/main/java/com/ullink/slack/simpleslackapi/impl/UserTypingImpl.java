package com.ullink.slack.simpleslackapi.impl;

import com.ullink.slack.simpleslackapi.SlackChannel;
import com.ullink.slack.simpleslackapi.SlackUser;
import com.ullink.slack.simpleslackapi.events.SlackEventType;
import com.ullink.slack.simpleslackapi.events.UserTyping;

public class UserTypingImpl implements UserTyping {
  private final SlackChannel slackChannel;
  private final SlackUser slackUser;
  private final SlackEventType slackEventType;

  public UserTypingImpl(SlackChannel slackChannel, SlackUser slackUser, SlackEventType slackEventType) {
    this.slackChannel = slackChannel;
    this.slackUser = slackUser;
    this.slackEventType = slackEventType;
  }

  @Override
  public SlackChannel getChannel() {
    return slackChannel;
  }

  @Override
  public SlackUser getUser() {
    return slackUser;
  }

  @Override
  public SlackEventType getEventType() {
    return slackEventType;
  }
}
