package com.sf.utility;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.google.gson.JsonPrimitive;

public class JsonUtil {

    private static final JsonParser JSON_PARSER = new JsonParser();

    public static boolean hasValue(JsonObject obj, String name) {
        return obj.has(name) && !obj.get(name).isJsonNull();
    }

    public static JsonElement optElement(JsonObject obj, String name) {
        if (!obj.has(name)) {
            return null;
        }
        JsonElement element = obj.get(name);
        if (element.isJsonNull()) {
            return null;
        }
        if (element instanceof JsonPrimitive) {
            // 部分从JSONObject转过来的这个字段是用的String
            element = JSON_PARSER.parse(element.getAsString());
        }
        return element;
    }

    public static String optString(JsonObject obj, String name, String defaultValue) {
        JsonElement element = obj.get(name);
        return element != null
                ? element.isJsonPrimitive() ? element.getAsString() : defaultValue
                : defaultValue;
    }

    public static int optInt(JsonObject obj, String name, int defaultValue) {
        JsonElement element = obj.get(name);
        return element != null
                ? element.isJsonPrimitive()
                ? ((JsonPrimitive) element).isNumber() ? element.getAsInt() : defaultValue
                : defaultValue
                : defaultValue;
    }

    public static long optLong(JsonObject obj, String name, long defaultValue) {
        JsonElement element = obj.get(name);
        return element != null
                ? element.isJsonPrimitive()
                ? ((JsonPrimitive) element).isNumber() ? element.getAsLong() : defaultValue
                : defaultValue
                : defaultValue;
    }

    public static boolean optBoolean(JsonObject obj, String name, boolean defaultValue) {
        JsonElement element = obj.get(name);
        return element != null
                ? element.isJsonPrimitive()
                ? ((JsonPrimitive) element).isBoolean() ? element.getAsBoolean() : defaultValue
                : defaultValue
                : defaultValue;
    }

    public static double optDouble(JsonObject obj, String name, double defaultValue) {
        JsonElement element = obj.get(name);
        return element != null
                ? element.isJsonPrimitive()
                ? ((JsonPrimitive) element).isNumber() ? element.getAsDouble() : defaultValue
                : defaultValue
                : defaultValue;
    }
}
