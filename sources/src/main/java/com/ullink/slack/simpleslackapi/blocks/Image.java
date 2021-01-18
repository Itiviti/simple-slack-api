package com.ullink.slack.simpleslackapi.blocks;

import com.google.gson.annotations.SerializedName;
import com.ullink.slack.simpleslackapi.blocks.compositions.PlainText;
import com.ullink.slack.simpleslackapi.blocks.compositions.Text;
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
public class Image extends AbstractBlock implements MessageElement, ModalElement, HomeElement {

  @Builder.Default
  private String type = BlockType.IMAGE.getType();

  @SerializedName("image_url")
  private String imageUrl;

  @SerializedName("alt_text")
  @NonNull
  private String altText;

  private PlainText title;

}
