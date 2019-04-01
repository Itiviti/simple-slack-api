package com.ullink.slack.simpleslackapi.impl;

import java.text.NumberFormat;
import java.text.ParseException;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonPrimitive;

class GsonHelper
{
    private GsonHelper() {}

    static Long getLongOrNull(JsonElement jsonElement)
    {
        return getLongOrDefaultValue(jsonElement,null);
    }

    static JsonArray getJsonArrayOrNull(JsonElement jsonElement)
    {
        return jsonElement != null && !jsonElement.isJsonNull() ? jsonElement.getAsJsonArray() : null;
    }

    static JsonObject getJsonObjectOrNull(JsonElement jsonElement)
    {
        return jsonElement != null && !jsonElement.isJsonNull() ? jsonElement.getAsJsonObject() : null;
    }

    static String getStringOrNull(JsonElement jsonElement)
    {
        return getStringOrDefaultValue(jsonElement,null);
    }

    static Boolean getBooleanOrDefaultValue(JsonElement jsonElement, Boolean defaultValue)
    {
        return jsonElement != null ? (Boolean) jsonElement.getAsBoolean() : defaultValue;
    }

    static Long getLongOrDefaultValue(JsonElement jsonElement, Long defaultValue)
    {
        if (jsonElement == null || jsonElement.isJsonNull()) {
            return defaultValue;
        }
        if (jsonElement instanceof JsonPrimitive) {
            JsonPrimitive jsonPrimitive = (JsonPrimitive) jsonElement;
            if (jsonPrimitive.isNumber()) {
                return jsonPrimitive.getAsNumber().longValue();
            }
            String elementAsString = jsonElement.getAsString();
            if (elementAsString.trim().isEmpty()) {
                return defaultValue;
            }
            try {
                return NumberFormat.getInstance().parse(elementAsString).longValue();
            } catch (ParseException e) {
                return jsonElement.getAsLong();
            }
        }
        return jsonElement.getAsLong();
    }

    static String getStringOrDefaultValue(JsonElement jsonElement, String defaultValue)
    {
        return jsonElement != null && !jsonElement.isJsonNull() ? jsonElement.getAsString() : defaultValue;
    }

    static Boolean ifNullFalse(JsonElement jsonElement)
    {
        return jsonElement != null && !jsonElement.isJsonNull() ? jsonElement.getAsBoolean() : false;
    }
}
