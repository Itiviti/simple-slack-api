package com.ullink.slack.simpleslackapi.impl;

import com.google.gson.annotations.SerializedName;
import com.ullink.slack.simpleslackapi.SlackPresence;
import com.ullink.slack.simpleslackapi.SlackStatus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Builder(toBuilder = true)
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class SlackProfileImpl {

  @SerializedName("avatar_hash")
  private String avatarHash;
  @SerializedName("status_text")
  private String statusText;
  @SerializedName("status_emoji")
  private String statusEmoji;
  @SerializedName("status_expiration")
  private float statusExpiration;
  @SerializedName("real_name")
  private String realName;
  private String skype;
  private String title;
  private String phone;
  @SerializedName("display_name")
  private String displayName;
  @SerializedName("real_name_normalized")
  private String realNameNormalized;
  @SerializedName("display_name_normalized")
  private String displayNameNormalized;
  private String email;
  @SerializedName("image_original")
  private String imageOriginal;
  @SerializedName("image_24")
  private String image24;
  @SerializedName("image_32")
  private String image32;
  @SerializedName("image_48")
  private String image48;
  @SerializedName("image_72")
  private String image72;
  @SerializedName("image_192")
  private String image192;
  @SerializedName("image_512")
  private String image512;
  private String team;
  @SerializedName("status")
  private SlackStatus status;

  @Builder.Default
  private SlackPresence presence = SlackPresence.UNKNOWN;

}
