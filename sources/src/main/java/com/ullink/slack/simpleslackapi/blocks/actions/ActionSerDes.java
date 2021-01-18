package com.ullink.slack.simpleslackapi.blocks.actions;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonParseException;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;
import com.ullink.slack.simpleslackapi.blocks.ContextElement;
import com.ullink.slack.simpleslackapi.blocks.ContextElementSerDes;
import com.ullink.slack.simpleslackapi.blocks.compositions.Image;

import java.lang.reflect.Type;

public class ActionSerDes implements JsonSerializer<Action>, JsonDeserializer<Action> {

  private Gson gson;
  public ActionSerDes() {
    GsonBuilder builder = new GsonBuilder();
    builder.registerTypeAdapter(ContextElement.class, new ContextElementSerDes());
    gson = builder.create();
  }

  @Override
  public Action deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
    String type = json.getAsJsonObject().get("type").getAsString();
    ActionType actionType = ActionType.valueOf(type.toUpperCase());
    switch (actionType) {
      case BUTTON: return gson.fromJson(json, Button.class);
      case DATEPICKER: return gson.fromJson(json, DatePicker.class);
      case IMAGE: return gson.fromJson(json, Image.class);
      case OVERFLOW: return gson.fromJson(json, OverflowMenu.class);
      case RADIO_BUTTONS: return gson.fromJson(json, RadioButtonGroup.class);
      case PLAIN_TEXT_INPUT: return gson.fromJson(json, PlaintextInput.class);
      case MULTI_STATIC_SELECT: return gson.fromJson(json, MultiStaticSelect.class);
      case MULTI_USERS_SELECT: return gson.fromJson(json, MultiUsersSelect.class);
      case MULTI_EXTERNAL_SELECT: return gson.fromJson(json, MultiExternalSelect.class);
      case MULTI_CONVERSATIONS_SELECT: return gson.fromJson(json, MultiConversationsSelect.class);
      case MULTI_CHANNELS_SELECT: return gson.fromJson(json, MultiChannelsSelect.class);
      case STATIC_SELECT: return gson.fromJson(json, StaticSelect.class);
      case USERS_SELECT: return gson.fromJson(json, UsersSelect.class);
      case EXTERNAL_SELECT: return gson.fromJson(json, ExternalSelect.class);
      case CONVERSATIONS_SELECT: return gson.fromJson(json, ConversationsSelect.class);
      case CHANNELS_SELECT: return gson.fromJson(json, ChannelsSelect.class);
      default: throw new IllegalArgumentException(type);
    }
  }

  @Override
  public JsonElement serialize(Action src, Type typeOfSrc, JsonSerializationContext context) {
    return gson.toJsonTree(src);
//    ActionType actionType = ActionType.valueOf(src.getType().toUpperCase());
//    switch (actionType) {
//      case BUTTON: return gson.toJsonTree(src);
//      case DATEPICKER: return gson.toJsonTree(src);
//      case IMAGE: return gson.toJsonTree(src);
//      case OVERFLOW: return gson.toJsonTree(src);
//      case RADIO_BUTTONS: return gson.toJsonTree(src);
//      case PLAIN_TEXT_INPUT: return gson.toJsonTree(src);
//      case MULTI_STATIC_SELECT: return gson.toJsonTree(src);
//      case MULTI_USERS_SELECT: return gson.toJsonTree(src);
//      case MULTI_EXTERNAL_SELECT: return gson.toJsonTree(src);
//      case MULTI_CONVERSATIONS_SELECT: return gson.toJsonTree(src);
//      case MULTI_CHANNELS_SELECT: return gson.toJsonTree(src);
//      case STATIC_SELECT: return gson.toJsonTree(src);
//      case USERS_SELECT: return gson.toJsonTree(src);
//      case EXTERNAL_SELECT: return gson.toJsonTree(src);
//      case CONVERSATIONS_SELECT: return gson.toJsonTree(src);
//      case CHANNELS_SELECT: return gson.toJsonTree(src);
//      default: throw new IllegalArgumentException(src.getType());
//    }
  }
}
