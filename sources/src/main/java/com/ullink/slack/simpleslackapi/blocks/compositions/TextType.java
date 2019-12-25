package com.ullink.slack.simpleslackapi.blocks.compositions;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor(access = AccessLevel.PACKAGE)
public enum TextType {

  MARKDOWN("mrkdwn"),
  PLAIN_TEXT("plain_text");

  @Getter
  private String type;

}
