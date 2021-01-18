package com.ullink.slack.simpleslackapi.blocks.actions;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor(access = AccessLevel.PACKAGE)
public enum ActionType {

  BUTTON("button"),
  DATEPICKER("datepicker"),
  IMAGE("image"),
  MULTI_STATIC_SELECT("multi_static_select"),
  MULTI_EXTERNAL_SELECT("multi_external_select"),
  MULTI_USERS_SELECT("multi_users_select"),
  MULTI_CONVERSATIONS_SELECT("multi_conversations_select"),
  MULTI_CHANNELS_SELECT("multi_channels_select"),
  OVERFLOW("overflow"),
  PLAIN_TEXT_INPUT("plain_text_input"),
  RADIO_BUTTONS("radio_buttons"),
  STATIC_SELECT("static_select"),
  EXTERNAL_SELECT("external_select"),
  USERS_SELECT("users_select"),
  CONVERSATIONS_SELECT("conversations_select"),
  CHANNELS_SELECT("channels_select");

  @Getter
  private String type;

}
