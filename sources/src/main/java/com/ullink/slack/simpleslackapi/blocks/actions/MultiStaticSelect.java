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
public class MultiStaticSelect extends SelectMenu implements SectionElement {

  @Builder.Default
  private String type = ActionType.MULTI_STATIC_SELECT.getType();

  private PlainText placeholder;

  @Singular
  private List<Option> options;

  @SerializedName("option_groups")
  private List<OptionGroup> optionGroups;

  @SerializedName("initial_options")
  private Option initialOptions;

  @SerializedName("max_selected_items")
  private int maxSelectedItems;

}
