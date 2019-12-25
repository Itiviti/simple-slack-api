package com.ullink.slack.simpleslackapi;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonParseException;
import com.google.gson.JsonPrimitive;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;

import java.lang.reflect.Type;

public class SlackPresenceSerDes implements JsonSerializer<SlackPresence>, JsonDeserializer<SlackPresence> {

  @Override
  public SlackPresence deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
    return SlackPresence.from(json.getAsString());
  }

  @Override
  public JsonElement serialize(SlackPresence presence, Type typeOfSrc, JsonSerializationContext context) {
    return new JsonPrimitive(presence.getPresence());
  }
}
