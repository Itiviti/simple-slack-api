package com.ullink.slack.simpleslackapi.blocks.actions;

import com.google.gson.annotations.SerializedName;
import com.ullink.slack.simpleslackapi.blocks.compositions.Option;
import com.ullink.slack.simpleslackapi.blocks.compositions.PlainText;
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
public class ExternalSelect extends SelectMenu implements SectionElement, ActionsElement {

  @Builder.Default
  private String type = ActionType.EXTERNAL_SELECT.getType();

  private PlainText placeholder;

  @SerializedName("initial_option")
  private Option initialOption;

  @SerializedName("min_query_length")
  private int minQueryLength;

}
