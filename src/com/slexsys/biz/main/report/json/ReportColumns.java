package com.slexsys.biz.main.report.json;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.LinkedList;

/**
 * Created by slexsys on 2/4/17.
 */

public class ReportColumns extends LinkedList<ReportColumn> {
    private static final String JSON_NAME = "ReportColumns";

    //region json
    public static ReportColumns fromJSON(JSONObject jsonObject) {
        ReportColumns result = null;
        if (jsonObject != null && !jsonObject.isNull(JSON_NAME)) {
            result = new ReportColumns();
            try {
                JSONArray jsonArray = jsonObject.getJSONArray(JSON_NAME);
                for (int i = 0; i < jsonArray.length(); ++i) {
                    ReportColumn column = ReportColumn.fromJSON(jsonArray.getJSONObject(i));
                    result.add(column);
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        return result;
    }

    public JSONObject toJSON() {
        JSONObject result = new JSONObject();
        JSONArray jsonArray = new JSONArray();
        for (ReportColumn item : this) {
            jsonArray.put(item.toJSON());
        }
        try {
            result.put(JSON_NAME, jsonArray);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return result;
    }
    //endregion
}
