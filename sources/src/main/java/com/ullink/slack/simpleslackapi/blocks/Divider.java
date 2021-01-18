package com.ullink.slack.simpleslackapi.blocks;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.experimental.SuperBuilder;

@SuperBuilder
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@Getter
public class Divider extends AbstractBlock implements MessageElement, ModalElement, HomeElement {

  @Builder.Default
  private String type = BlockType.DIVIDER.getType();



}
