package com.ullink.slack.simpleslackapi;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.ullink.slack.simpleslackapi.blocks.Block;
import com.ullink.slack.simpleslackapi.blocks.BlockSerDes;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

public class TestUtils {

  public static String getFile(String filename) throws IOException {
    InputStream stream = TestUtils.class.getResourceAsStream(filename);
    InputStreamReader isReader = new InputStreamReader(stream);
    BufferedReader reader = new BufferedReader(isReader);
    StringBuilder strBuilder = new StringBuilder();
    String line;
    while ((line = reader.readLine()) != null) {
      strBuilder.append(line);
    }
    return strBuilder.toString();
  }

  private static Gson gson;

  public static Gson gson() throws IOException {
    if (gson == null) {
      GsonBuilder builder = new GsonBuilder();
      builder.registerTypeAdapter(SlackPresence.class, new SlackPresenceSerDes());
      builder.registerTypeAdapter(Block.class, new BlockSerDes());
      gson = builder.create();
    }
    return gson;
  }


}
