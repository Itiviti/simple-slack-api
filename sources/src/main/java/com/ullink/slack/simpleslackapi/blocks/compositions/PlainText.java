package com.ullink.slack.simpleslackapi.blocks.compositions;


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
public class PlainText implements Text {

  @Builder.Default
  private String type = TextType.PLAIN_TEXT.getType();

  @NonNull
  private String text;

  boolean emoji;

}
