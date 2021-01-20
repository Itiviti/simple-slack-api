package com.ullink.slack.simpleslackapi.blocks;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonParseException;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;
import com.ullink.slack.simpleslackapi.blocks.actions.Action;
import com.ullink.slack.simpleslackapi.blocks.actions.ActionSerDes;
import com.ullink.slack.simpleslackapi.blocks.actions.ActionsElement;
import com.ullink.slack.simpleslackapi.blocks.actions.InputElement;
import com.ullink.slack.simpleslackapi.blocks.actions.SectionElement;
import com.ullink.slack.simpleslackapi.blocks.compositions.Text;

import java.lang.reflect.Type;

public class BlockSerDes implements JsonDeserializer<Block>, JsonSerializer<Block> {

  private Gson gson;
  public BlockSerDes() {
    GsonBuilder builder = new GsonBuilder();
    ActionSerDes actionSerializer = new ActionSerDes();
    builder.registerTypeAdapter(Action.class, actionSerializer);
    builder.registerTypeAdapter(ActionsElement.class, actionSerializer);
    builder.registerTypeAdapter(SectionElement.class, actionSerializer);
    builder.registerTypeAdapter(InputElement.class, actionSerializer);
    ContextElementSerDes ContextElementSerializer = new ContextElementSerDes();
    builder.registerTypeAdapter(ContextElement.class, ContextElementSerializer);
    builder.registerTypeAdapter(Text.class, ContextElementSerializer);
    gson = builder.create();
  }

  @Override
  public Block deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
    String type = json.getAsJsonObject().get("type").getAsString();
    BlockType blockType = BlockType.valueOf(type.toUpperCase());
    switch (blockType) {
      case CONTEXT: return gson.fromJson(json, Context.class);
      case IMAGE: return gson.fromJson(json, Image.class);
      case FILE: return gson.fromJson(json, File.class);
      case DIVIDER: return gson.fromJson(json, Divider.class);
      case INPUT: return gson.fromJson(json, Input.class);
      case SECTION: return gson.fromJson(json, Section.class);
      case ACTIONS: return gson.fromJson(json, Actions.class);
      default: throw new IllegalArgumentException(type);
    }
  }

  @Override
  public JsonElement serialize(Block src, Type typeOfSrc, JsonSerializationContext context) {
    return gson.toJsonTree(src);
  }
}
