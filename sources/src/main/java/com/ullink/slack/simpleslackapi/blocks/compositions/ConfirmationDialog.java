package com.ullink.slack.simpleslackapi.blocks.compositions;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Builder
@NoArgsConstructor
@AllArgsConstructor
@Getter
public class ConfirmationDialog {

  private PlainText title;

  private Text text;

  private PlainText confirm;

  private PlainText deny;

}
