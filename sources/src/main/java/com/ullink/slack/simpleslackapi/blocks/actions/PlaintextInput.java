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
public class PlaintextInput extends AbstractAction implements SectionElement, InputElement, ActionsElement {

  @Builder.Default
  private String type = ActionType.PLAIN_TEXT_INPUT.getType();

  private String placeholder;

  @SerializedName("initial_value")
  private String initialValue;

  private boolean multiline;

  @SerializedName("min_length")
  private int minLength;

  @SerializedName("max_length")
  private int maxLength;

}
