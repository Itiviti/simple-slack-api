package com.ullink.slack.simpleslackapi.blocks;

import com.ullink.slack.simpleslackapi.blocks.actions.SectionElement;
import com.ullink.slack.simpleslackapi.blocks.compositions.Text;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Singular;
import lombok.experimental.SuperBuilder;

import java.util.List;

@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@Getter
public class Section extends AbstractBlock implements MessageElement, ModalElement, HomeElement {

  @Builder.Default
  private String type = BlockType.SECTION.getType();

  @Singular
  private List<Text> fields;

  private SectionElement accessory;

}
