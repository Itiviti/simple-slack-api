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
public class Markdown implements Text {

  @Builder.Default
  private String type = TextType.MARKDOWN.getType();

  @NonNull
  private String text;

}
