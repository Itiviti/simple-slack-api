package com.ullink.slack.simpleslackapi.blocks.actions;

import com.google.gson.annotations.SerializedName;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import java.util.List;

@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@Getter
public class MultiChannelsSelect extends SelectMenu implements SectionElement {

  @Builder.Default
  private String type = ActionType.MULTI_CHANNELS_SELECT.getType();

  @SerializedName("initial_channels")
  private List<String> initialChannels;

  @SerializedName("max_selected_items")
  private int maxSelectedItems;

}
