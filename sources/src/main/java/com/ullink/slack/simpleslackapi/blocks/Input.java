package com.ullink.slack.simpleslackapi.blocks;

import com.ullink.slack.simpleslackapi.blocks.actions.InputElement;
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
public class Input extends AbstractBlock implements ModalElement {

  @Builder.Default
  private String type = BlockType.INPUT.getType();

  private PlainText label;

  private InputElement element;

  private PlainText hint;

  private Boolean optional;

}
