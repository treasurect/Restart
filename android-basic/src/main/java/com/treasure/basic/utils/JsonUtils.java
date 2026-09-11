package com.treasure.basic.utils;

import com.google.gson.Gson;
import com.google.gson.JsonParseException;

import java.lang.reflect.Type;

import com.google.gson.reflect.TypeToken;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Iterator;

public class JsonUtils {

    public static String toJson(Object obj) {
        try {
            return new Gson().toJson(obj);
        } catch (JsonParseException ignored) {
            return "";
        }
    }

    public static <T> T fromJson(String json, Class<T> classOfT) {
        try {
            return new Gson().fromJson(json, classOfT);
        } catch (JsonParseException ignored) {
            return null;
        }
    }

    public static <T> T fromJson(String json, Type typeOfT) {
        try {
            return new Gson().fromJson(json, typeOfT);
        } catch (JsonParseException ignored) {
            return null;
        }
    }


    public static HashMap<String, Object> jsonToMap(String jsonStr) {
        HashMap<String, Object> map = new HashMap<>();
        try {
            JSONObject jsonObject = new JSONObject(jsonStr);
            Iterator<String> keys = jsonObject.keys();
            while (keys.hasNext()) {
                String key = keys.next();
                Object value = jsonObject.get(key);
                map.put(key, value);
            }
        } catch (JSONException e) {
        }
        return map;
    }

    public static HashMap<String, Object> jsonToMapGson(String jsonStr) {
        Gson gson = new Gson();
        Type type = new TypeToken<HashMap<String, String>>() {
        }.getType();
        return gson.fromJson(jsonStr, type);
    }

    public static JSONObject toJsonObject(String obj) {
        if (obj == null) return null;
        try {
            return new JSONObject(obj);
        } catch (JSONException e) {
            return null;
        }
    }
}
