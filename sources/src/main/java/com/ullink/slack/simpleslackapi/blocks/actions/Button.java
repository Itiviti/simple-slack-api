package com.ullink.slack.simpleslackapi.blocks.actions;

import com.ullink.slack.simpleslackapi.blocks.compositions.ConfirmationDialog;
import com.ullink.slack.simpleslackapi.blocks.compositions.PlainText;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import lombok.experimental.SuperBuilder;

@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@Getter
public class Button extends AbstractAction implements SectionElement, ActionsElement {

  @AllArgsConstructor(access = AccessLevel.PRIVATE)
  public static enum Style {
    PRIMARY("primary"),
    DANGER("danger");

    @Getter
    private String label;

  }

  @Builder.Default
  private String type = ActionType.BUTTON.getType();

  private String url;

  @NonNull
  private PlainText text;

  private String value;

  private String style;

  private ConfirmationDialog confirm;
}
