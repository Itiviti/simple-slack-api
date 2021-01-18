package com.ullink.slack.simpleslackapi.blocks.actions;

import com.ullink.slack.simpleslackapi.blocks.compositions.ConfirmationDialog;
import com.ullink.slack.simpleslackapi.blocks.compositions.PlainText;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@Getter
public abstract class SelectMenu extends AbstractAction implements InputElement {

  private PlainText placeholder;

  private ConfirmationDialog confirm;

}
