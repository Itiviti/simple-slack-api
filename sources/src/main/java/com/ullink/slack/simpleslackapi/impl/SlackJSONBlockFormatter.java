package com.ullink.slack.simpleslackapi.impl;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.ullink.slack.simpleslackapi.blocks.Block;

import java.util.List;

class SlackJSONBlockFormatter {

  public static String encodeBlocks(List<Block> blocks) {
      GsonBuilder builder = new GsonBuilder();
      Gson gson = builder.create();
      return gson.toJson(blocks);
  }
}
