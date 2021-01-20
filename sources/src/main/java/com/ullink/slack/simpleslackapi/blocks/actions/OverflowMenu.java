package com.ullink.slack.simpleslackapi.blocks.actions;

import com.ullink.slack.simpleslackapi.blocks.compositions.ConfirmationDialog;
import com.ullink.slack.simpleslackapi.blocks.compositions.Option;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import java.util.ArrayList;
import java.util.List;

@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@Getter
public class OverflowMenu extends AbstractAction implements SectionElement, ActionsElement {

  @Builder.Default
  private String type = ActionType.OVERFLOW.getType();

  private static final int MAX = 6;

  private List<Option> options;

  public void option(Option option) {
    if (options == null) options = new ArrayList<>();
    if (options.size() > MAX) throw new IllegalArgumentException("n more than 5 items allowed");
    options.add(option);
  }

  private ConfirmationDialog confirm;

}
