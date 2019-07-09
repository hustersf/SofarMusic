package com.sf.libnet.gson;

import java.lang.reflect.Type;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import com.google.gson.JsonPrimitive;

/**
 * 解决gson转map int变为double的问题
 * 直接用entry.getValue()，会导致字符串类型多了双引号嵌套
 */
public class MapDeserializer implements JsonDeserializer<Map> {
  @Override
  public Map deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context)
      throws JsonParseException {
    Map<String, Object> resultMap = new HashMap<>();
    JsonObject jsonObject = json.getAsJsonObject();
    Set<Map.Entry<String, JsonElement>> entrySet = jsonObject.entrySet();
    for (Map.Entry<String, JsonElement> entry : entrySet) {
      Object ot = entry.getValue();
      if (ot instanceof JsonPrimitive) {
        resultMap.put(entry.getKey(), ((JsonPrimitive) ot).getAsString());
      } else {
        resultMap.put(entry.getKey(), ot);
      }
    }
    return resultMap;
  }
}
