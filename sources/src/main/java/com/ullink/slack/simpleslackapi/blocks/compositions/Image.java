package com.ullink.slack.simpleslackapi.blocks.compositions;


import com.google.gson.annotations.SerializedName;
import com.ullink.slack.simpleslackapi.blocks.ContextElement;
import com.ullink.slack.simpleslackapi.blocks.actions.AbstractAction;
import com.ullink.slack.simpleslackapi.blocks.actions.ActionType;
import com.ullink.slack.simpleslackapi.blocks.actions.SectionElement;
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
public class Image implements ContextElement, SectionElement {

  @Builder.Default
  private String type = ActionType.IMAGE.getType();

  @SerializedName("image_url")
  private String imageUrl;

  @SerializedName("alt_text")
  private String alt_text;

}
