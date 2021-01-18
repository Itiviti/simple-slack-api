package com.ullink.slack.simpleslackapi.blocks.actions;

import com.google.gson.annotations.SerializedName;
import com.ullink.slack.simpleslackapi.blocks.compositions.ConfirmationDialog;
import com.ullink.slack.simpleslackapi.blocks.compositions.Option;
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
public class RadioButtonGroup extends AbstractAction implements SectionElement, ActionsElement {

  @Builder.Default
  private String type = ActionType.RADIO_BUTTONS.getType();

  private List<Option> options;

  @SerializedName("initial_option")
  private Option initialOption;

  private ConfirmationDialog confirm;
}
