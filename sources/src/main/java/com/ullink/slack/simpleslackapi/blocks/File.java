package com.ullink.slack.simpleslackapi.blocks;

import com.google.gson.annotations.SerializedName;
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
public class File extends AbstractBlock implements MessageElement {

  @Builder.Default
  private String type = BlockType.FILE.getType();

  @SerializedName("external_id")
  private String externalId;

  private String source;

}
