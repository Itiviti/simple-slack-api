package com.ullink.slack.simpleslackapi.blocks.actions;

import com.google.gson.annotations.SerializedName;
import com.ullink.slack.simpleslackapi.blocks.compositions.Option;
import com.ullink.slack.simpleslackapi.blocks.compositions.OptionGroup;
import com.ullink.slack.simpleslackapi.blocks.compositions.PlainText;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Singular;
import lombok.experimental.SuperBuilder;

import java.util.List;

@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@Getter
public class StaticSelect extends SelectMenu implements SectionElement, ActionsElement {

  @Builder.Default
  private String type = ActionType.STATIC_SELECT.getType();

  private PlainText placeholder;

  @Singular
  private List<Option> options;

  @Singular
  @SerializedName("option_groups")
  private List<OptionGroup> optionGroups;

  @SerializedName("initial_option")
  private Option initialOption;

}
