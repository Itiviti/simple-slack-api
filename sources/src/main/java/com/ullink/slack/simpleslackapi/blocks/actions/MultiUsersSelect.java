package com.ullink.slack.simpleslackapi.blocks.actions;

import com.google.gson.annotations.SerializedName;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import java.util.List;

@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@Getter
public class MultiUsersSelect extends SelectMenu implements SectionElement {

  @Builder.Default
  private String type = ActionType.MULTI_USERS_SELECT.getType();

  @SerializedName("initial_users")
  private List<String> initialUsers;

  @SerializedName("max_selected_items")
  private int maxSelectedItems;

}
