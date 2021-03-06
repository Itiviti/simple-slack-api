package com.ullink.slack.simpleslackapi.blocks.actions;

import com.google.gson.annotations.SerializedName;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@Getter
public class ConversationsSelect extends SelectMenu implements SectionElement, ActionsElement {

  @Builder.Default
  private String type = ActionType.CONVERSATIONS_SELECT.getType();

  @SerializedName("initial_conversation")
  private String initialConversation;

}
