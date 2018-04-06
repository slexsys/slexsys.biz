package com.slexsys.biz.main.comp.json;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.LinkedList;
import java.util.List;

/**
 * Created by slexsys on 2/27/17.
 */

public class JSON_STD {
    public static void putString(JSONObject jsonObject, String string, String jsonName) {
        if (string != null && !string.equals("")) {
            putJSONObject(jsonObject, string, jsonName);
        }
    }

    public static void putEnum(JSONObject jsonObject, Enum en, String jsonName) {
        if (en != null) {
            putJSONObject(jsonObject, en.ordinal(), jsonName);
        }
    }

    public static JSONArray getJSONArrayFromListJSONInterface(List<? extends JSONObjectInterface> list) {
        JSONArray result = null;
        if (list != null) {
            result = new JSONArray();
            for (int i = 0; i < list.size(); i++) {
                JSONObjectInterface jsonInterface = list.get(i);
                if (jsonInterface != null) {
                    result.put(jsonInterface.toJSONObject());
                }
            }
        }
        return result;
    }

    public static void putJSONObject(JSONObject jsonObject, Object object, String jsonName) {
        if (jsonObject != null && object != null && jsonName != null && !jsonName.equals("")) {
            try {
                jsonObject.put(jsonName, object);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }

    public static Object getObjectFromJSONObject(JSONObject jsonObject, String jsonName) {
        Object result = null;
        if (jsonObject != null && jsonName != null && !jsonName.equals("")) {
            try {
                if (!jsonObject.isNull(jsonName)) {
                    result = jsonObject.get(jsonName);
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        return result;
    }

    public static JSONObject getInnerJSONObject(JSONObject jsonObject, String jsonName) {
        JSONObject result = null;
        if (jsonObject != null && jsonName != null && !jsonName.equals("")) {
            try {
                if (!jsonObject.isNull(jsonName)) {
                    result = jsonObject.getJSONObject(jsonName);
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        return result;
    }

    public static JSONArray getJSONArrayFromJSONObject(JSONObject jsonObject, String jsonName) {
        JSONArray jsonArray = null;
        if (!jsonObject.isNull(jsonName)) {
            try {
                jsonArray = jsonObject.getJSONArray(jsonName);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        return jsonArray;
    }

    public static List<JSONObject> getListJSONObjectFromJSONArray(JSONArray jsonArray) {
        List<JSONObject> result = null;
        if (jsonArray != null) {
            result = new LinkedList<JSONObject>();
            for (int i = 0; i < jsonArray.length(); ++i) {
                try {
                    JSONObject jsonObject = (JSONObject) jsonArray.get(i);
                    result.add(jsonObject);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }
        return result;
    }

    public static JSONObject fromString(String json) {
        JSONObject result = null;
        try {
            result = new JSONObject(json);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return result;
    }

    public static int getInt(JSONObject jsonObject, String jsonName) {
        return (Integer) getObjectFromJSONObject(jsonObject, jsonName);
    }

    public static <T extends Enum<T>> T getEnumFromJSONObject(JSONObject jsonObject, String jsonName, Class<T> enumType) {
        String name = (String) getObjectFromJSONObject(jsonObject, jsonName);
        return Enum.valueOf(enumType, name);
    }
}
