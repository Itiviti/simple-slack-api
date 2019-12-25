package com.ullink.slack.simpleslackapi.blocks;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor(access = AccessLevel.PACKAGE)
public enum BlockType {

  ACTIONS("actions"),
  CONTEXT("context"),
  DIVIDER("divider"),
  FILE("file"),
  IMAGE("image"),
  INPUT("input"),
  SECTION("section");

  @Getter
  private String type;


}
