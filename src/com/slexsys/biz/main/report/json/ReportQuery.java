package com.slexsys.biz.main.report.json;

import com.slexsys.biz.main.report.comp.UIFilters;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by slexsys on 2/4/17.
 */

public class ReportQuery {
    private String query;
    private ReportFilters filters;
    private ReportColumns columns;

    private static final String JSON_NAME_QUERY = "Query";
    private static final String JSON_NAME_FILTERS = "Filters";
    private static final String JSON_NAME_COLUMNS = "Columns";

    public ReportQuery() { }

    public ReportQuery(String query) {
        this.query = query;
    }

    public ReportQuery(String query, ReportFilters filters, ReportColumns columns) {
        this.query = query;
        this.filters = filters;
        this.columns = columns;
    }

    public ReportColumn indexOf(int position) {
        ReportColumn result = null;
        if (columns.size() > position) {
            result = columns.get(position);
        }
        return result;
    }

    // region getters setters
    public String getQuery() {
        return query;
    }

    public void setQuery(String query) {
        this.query = query;
    }

    public ReportFilters getFilters() {
        return filters;
    }

    public void setFilters(ReportFilters filters) {
        this.filters = filters;
    }

    public ReportColumns getColumns() {
        return columns;
    }

    public void setColumns(ReportColumns columns) {
        this.columns = columns;
    }
    //endregion

    //region json
    public static ReportQuery fromJSON(JSONObject jsonObject) {
        ReportQuery result = null;
        if (jsonObject != null) {
            try {
                String query = "";
                if (!jsonObject.isNull(JSON_NAME_QUERY)) {
                    query = jsonObject.getString(JSON_NAME_QUERY);
                }
                ReportFilters filters = null;
                if (!jsonObject.isNull(JSON_NAME_FILTERS)) {
                    JSONObject object = jsonObject.getJSONObject(JSON_NAME_FILTERS);
                    filters = ReportFilters.fromJSON(object);
                }

                ReportColumns columns = null;
                if (!jsonObject.isNull(JSON_NAME_COLUMNS)) {
                    JSONObject object = jsonObject.getJSONObject(JSON_NAME_COLUMNS);
                    columns = ReportColumns.fromJSON(object);
                }
                result = new ReportQuery(query, filters, columns);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        return result;
    }

    public JSONObject toJSON() {
        JSONObject result = null;
        try {
            result = new JSONObject();
            if (query != null) {
                result.put(JSON_NAME_QUERY, query);
            }
            if (filters != null) {
                result.put(JSON_NAME_FILTERS, filters.toJSON());
            }
            if (columns != null) {
                result.put(JSON_NAME_COLUMNS, columns.toJSON());
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return result;
    }
    //endregion

    //region SQL Builder
    public String buildSQLQuery(UIFilters uifilters, ReportFilters filters) {
        ReportFilters allfilters = getMergedFilters(filters);
        String whereText = allfilters.buildSQLQueryWhere(uifilters);
        return query.replace(ReportFilters.SQL_WHERE_POINT, whereText);
    }

    private ReportFilters getMergedFilters(ReportFilters filters) {
        ReportFilters result = new ReportFilters();
        if (filters != null) {
            result.addAll(filters);
        }
        if (this.filters != null) {
            result.addAll(this.filters);
        }
        return result;
    }
    //endregion
}
