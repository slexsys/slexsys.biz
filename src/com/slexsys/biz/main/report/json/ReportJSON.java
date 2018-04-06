package com.slexsys.biz.main.report.json;

import android.database.Cursor;

import com.slexsys.biz.main.report.comp.UIFilterType;
import com.slexsys.biz.main.report.comp.UIFilters;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;
import java.util.LinkedList;
import java.util.List;

/**
 * Created by slexsys on 2/1/17.
 */

public class ReportJSON implements Serializable{
    private String name;
    private ReportQuerys queryTotals;
    private ReportQuerys queryLines;

    private static final String JSON_NAME_NAME = "Name";
    private static final String JSON_NAME_TOTALS = "Totals";
    private static final String JSON_NAME_LINES = "Lines";

    public ReportJSON() { }

    public ReportJSON(String name, ReportQuerys queryTotals, ReportQuerys queryLines) {
        this.name = name;
        this.queryTotals = queryTotals;
        this.queryLines = queryLines;
    }

    public static ReportJSON fromJSON(Cursor cursor) {
        ReportJSON result = null;
        if (cursor.getColumnCount() > 0) {
            String json = cursor.getString(0);
            result = fromJSON(json);
        }
        return result;
    }

    public List<UIFilterType> getActiveFilters() {
        List<UIFilterType> list = new LinkedList<UIFilterType>();
        if (queryTotals != null) {
            list.addAll(queryTotals.getActiveFilters());
        }
        if (queryLines != null) {
            list.addAll(queryLines.getActiveFilters());
        }
        return list;
    }

    //region getters setters

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public ReportQuerys getQueryTotals() {
        return queryTotals;
    }

    public void setQueryTotals(ReportQuerys queryTotals) {
        this.queryTotals = queryTotals;
    }

    public ReportQuerys getQueryLines() {
        return queryLines;
    }

    public void setQueryLines(ReportQuerys queryLines) {
        this.queryLines = queryLines;
    }

    //endregion

    //region json
    public static ReportJSON fromJSON(String json) {
        ReportJSON result = null;
        try {
            JSONObject report = new JSONObject(json);
            String name = report.getString(JSON_NAME_NAME);
            ReportQuerys totals = null;
            if (!report.isNull(JSON_NAME_TOTALS)) {
                JSONObject jsonObject = report.getJSONObject(JSON_NAME_TOTALS);
                totals = ReportQuerys.fromJSON(jsonObject);
            }

            ReportQuerys lines = null;
            if (!report.isNull(JSON_NAME_LINES)) {
                JSONObject jsonObject = report.getJSONObject(JSON_NAME_LINES);
                lines = ReportQuerys.fromJSON(jsonObject);
            }
            result = new ReportJSON(name, totals, lines);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return result;
    }

    public JSONObject toJSON() {
        JSONObject result = new JSONObject();
        try {
            if (name != "" && name != null) {
                result.put(JSON_NAME_NAME, name);
            }
            if (queryTotals != null) {
                result.put(JSON_NAME_TOTALS, queryTotals.toJSON());
            }
            if (queryLines != null) {
                result.put(JSON_NAME_LINES, queryLines.toJSON());
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return result;
    }
    //endregion

    //region SQL Builder
    public List<String> buildSQLQueryTotals(UIFilters filters) {
        return queryTotals.buildSQLQueryList(filters);
    }

    public String buildSQLQueryLines(UIFilters filters) {
        return buildSQLQuery(filters, queryLines);
    }

    private String buildSQLQuery(UIFilters filters, ReportQuerys querys) {
        return querys.buildSQLQuery(filters);
    }
    //endregion
}
