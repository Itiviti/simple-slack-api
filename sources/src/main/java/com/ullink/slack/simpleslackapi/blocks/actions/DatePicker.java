package com.ullink.slack.simpleslackapi.blocks.actions;

import com.google.gson.annotations.SerializedName;
import com.ullink.slack.simpleslackapi.blocks.compositions.ConfirmationDialog;
import com.ullink.slack.simpleslackapi.blocks.compositions.PlainText;
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
public class DatePicker extends AbstractAction  implements SectionElement, ActionsElement, InputElement {

  @Builder.Default
  private String type = ActionType.DATEPICKER.getType();

  private PlainText placeholder;

  @SerializedName("initial_date")
  private String initialDate;

  private ConfirmationDialog confirm;

}
