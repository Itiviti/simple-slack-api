package com.ullink.slack.simpleslackapi.blocks.actions;

import com.google.gson.annotations.SerializedName;
import com.ullink.slack.simpleslackapi.blocks.compositions.Option;
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
public class MultiExternalSelect extends SelectMenu implements SectionElement {

  @Builder.Default
  private String type = ActionType.MULTI_EXTERNAL_SELECT.getType();

  @SerializedName("initial_options")
  private Option initialOptions;

  @SerializedName("max_selected_items")
  private int maxSelectedItems;

  @SerializedName("min_query_length")
  private int minQueryLength;

}
