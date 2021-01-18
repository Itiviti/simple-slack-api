package com.ullink.slack.simpleslackapi;

import lombok.Getter;

public enum SlackPresence {

  UNKNOWN("unknown"), ACTIVE("active"), AWAY("away"), AUTO("auto");

  @Getter
  private String presence;

  SlackPresence(String presence) {
    this.presence = presence;
  }

  public static SlackPresence from(String presence) {
    switch (presence) {
      case "active": return ACTIVE;
      case "away": return AWAY;
      case "auto": return AUTO;
      default: return UNKNOWN;
    }
  }

}
