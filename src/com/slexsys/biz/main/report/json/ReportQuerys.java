package com.slexsys.biz.main.report.json;

import com.slexsys.biz.main.report.comp.UIFilterType;
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

public class ReportQuerys extends LinkedList<ReportQuery> implements Serializable {
    private ReportFilters filters;

    private static final String JSON_NAME_QUERYS = "Querys";
    private static final String JSON_NAME_FILTERS = "Filters";
    private static final String UNION_ALL = "UNION ALL";

    public List<UIFilterType> getActiveFilters() {
        List<UIFilterType> list = new LinkedList<UIFilterType>();
        for (ReportQuery item : this) {
            ReportFilters filters = item.getFilters();
            if (filters != null) {
                list.addAll(filters.getActiveFilters());
            }
        }
        if (filters != null) {
            list.addAll(filters.getActiveFilters());
        }
        return list;
    }

    //region getters setters
    public ReportFilters getFilters() {
        return filters;
    }

    public void setFilters(ReportFilters filters) {
        this.filters = filters;
    }
    //endregion

    //region json
    public static ReportQuerys fromJSON(JSONObject jsonObject) {
        ReportQuerys result = null;
        if (jsonObject != null) {
            result = new ReportQuerys();
            try {
                if (!jsonObject.isNull(JSON_NAME_FILTERS)) {
                    JSONObject object = jsonObject.getJSONObject(JSON_NAME_FILTERS);
                    ReportFilters filters = ReportFilters.fromJSON(object);
                    result.setFilters(filters);
                }
                if (!jsonObject.isNull(JSON_NAME_QUERYS)) {
                    JSONArray jsonArray = jsonObject.getJSONArray(JSON_NAME_QUERYS);
                    for (int i = 0; i < jsonArray.length(); ++i) {
                        JSONObject object = jsonArray.getJSONObject(i);
                        ReportQuery filter = ReportQuery.fromJSON(object);
                        result.add(filter);
                    }
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
        for (ReportQuery item : this) {
            jsonArray.put(item.toJSON());
        }
        try {
            if (filters != null) {
                result.put(JSON_NAME_FILTERS, filters.toJSON());
            }
            result.put(JSON_NAME_QUERYS, jsonArray);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return result;
    }
    //endregion

    //region SQL Builder
    public String buildSQLQuery(UIFilters filters) {
        String result = "";
        for (ReportQuery query : this) {
            result += query.buildSQLQuery(filters, this.filters) + " " + UNION_ALL + " ";
        }
        result = ReportFilter.trimer(result, UNION_ALL);
        return result;
    }

    public List<String> buildSQLQueryList(UIFilters filters) {
        List<String> querys = new LinkedList<String>();
        for (ReportQuery query : this) {
            querys.add(query.buildSQLQuery(filters, this.filters));
        }
        return querys;
    }
    //endregion
}
