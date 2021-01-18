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
public class ChannelsSelect extends SelectMenu implements SectionElement, ActionsElement {

  @Builder.Default
  private String type = ActionType.CHANNELS_SELECT.getType();

  @SerializedName("initial_channel")
  private String initialChannel;

}
