package com.ullink.slack.simpleslackapi.blocks;

import com.google.gson.Gson;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonParseException;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;
import com.ullink.slack.simpleslackapi.blocks.compositions.Image;
import com.ullink.slack.simpleslackapi.blocks.compositions.Markdown;
import com.ullink.slack.simpleslackapi.blocks.compositions.PlainText;

import java.lang.reflect.Type;

public class ContextElementSerDes implements JsonSerializer<ContextElement>, JsonDeserializer<ContextElement> {

  private Gson gson;
  public ContextElementSerDes() {
    gson = new Gson();
  }


  @Override
  public ContextElement deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
    String type = json.getAsJsonObject().get("type").getAsString();
    switch (type) {
      case "image": return gson.fromJson(json, Image.class);
      case "mrkdwn": return gson.fromJson(json, Markdown.class);
      case "plain_text": return gson.fromJson(json, PlainText.class);
      default: throw new IllegalArgumentException(type);
    }
  }

  @Override
  public JsonElement serialize(ContextElement src, Type typeOfSrc, JsonSerializationContext context) {
    return gson.toJsonTree(src);
  }
}
