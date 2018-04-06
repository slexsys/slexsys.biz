package com.slexsys.biz.main.report.json;

import com.slexsys.biz.main.report.comp.UIFilterType;
import com.slexsys.biz.main.report.comp.UIFilter;
import com.slexsys.biz.main.report.comp.UIFilters;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;
import java.util.LinkedList;
import java.util.List;

/**
 * Created by slexsys on 2/4/17.
 */

public class ReportFilters extends LinkedList<ReportFilter> implements Serializable {
    public static final String SQL_WHERE_POINT = "#where";
    public static final String SQL_WHERE_VALUE = "WHERE";
    private static final String JSON_NAME = "ReportFilters";

    public List<UIFilterType> getActiveFilters() {
        List<UIFilterType> list = new LinkedList<UIFilterType>();
        for (ReportFilter item : this) {
            list.add(item.getFilter());
        }
        return list;
    }

    //region json
    public static ReportFilters fromJSON(JSONObject jsonObject) {
        ReportFilters result = null;
        if (jsonObject != null && !jsonObject.isNull(JSON_NAME)) {
            result = new ReportFilters();
            try {
                JSONArray jsonArray = jsonObject.getJSONArray(JSON_NAME);
                for (int i = 0; i < jsonArray.length(); ++i) {
                    JSONObject object = jsonArray.getJSONObject(i);
                    ReportFilter filter = ReportFilter.fromJSON(object);
                    result.add(filter);
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
        for (ReportFilter item : this) {
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

    //region SQL Format
    public String buildSQLQueryWhere(UIFilters filters) {
        String result = "";
        for (ReportFilter filter : this) {
            UIFilter uifilter = filters.get(filter.getFilter());
            result += filter.buildSQLQueryWhereFilter(uifilter);
        }
        result = ReportFilter.trimer(result);
        if (!result.equals("")) {
            result = " " + SQL_WHERE_VALUE + " " + result;
        }
        return result;
    }
    //endregion
}
