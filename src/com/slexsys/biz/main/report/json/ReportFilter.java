package com.slexsys.biz.main.report.json;

import com.slexsys.biz.comp.std;
import com.slexsys.biz.database.comp.iIBase;
import com.slexsys.biz.database.comp.iItems;
import com.slexsys.biz.main.report.comp.UIFilterType;
import com.slexsys.biz.main.report.comp.UIFilter;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

/**
 * Created by slexsys on 2/4/17.
 */

public class ReportFilter implements Serializable {
    //region constants
    private static final String END_SPLIT = ",";
    private static final String END_OR = "OR";
    private static final String END_AND = "AND";
    private static final String END_LIKE = "LIKE";

    private static final String JSON_NAME_FIELDS = "fields";
    private static final String JSON_NAME_OPERATOR = "operator";
    private static final String JSON_NAME_FILTER = "filter";
    //endregion

    //region fields
    private List<String> fields;
    private String operator;
    private UIFilterType filter;

    private String endback = END_AND;
    //endregion

    //region constructors
    public ReportFilter() { }

    public ReportFilter(String field, String operator, UIFilterType filter) {
        this.fields = Arrays.asList(field);
        this.operator = operator;
        this.filter = filter;
    }

    public ReportFilter(List<String> fields, String operator, UIFilterType filter) {
        this.fields = fields;
        this.operator = operator;
        this.filter = filter;
    }
    //endregion

    // region getters setters
    public List<String> getFields() {
        return fields;
    }

    public void setFields(List<String> fields) {
        this.fields = fields;
    }

    public String getOperator() {
        return operator;
    }

    public void setOperator(String operator) {
        this.operator = operator;
    }

    public UIFilterType getFilter() {
        return filter;
    }

    public void setFilter(UIFilterType filter) {
        this.filter = filter;
    }
    //endregion

    //region json
    public static ReportFilter fromJSON(JSONObject jsonObject) {
        ReportFilter result = null;
        if (jsonObject != null) {

            try {
                List<String> fields = null;
                if (!jsonObject.isNull(JSON_NAME_FIELDS)) {
                    JSONArray jsonArray = jsonObject.getJSONArray(JSON_NAME_FIELDS);
                    fields = getJSONArray2List(jsonArray);

                }
                String operator = "";
                if (!jsonObject.isNull(JSON_NAME_OPERATOR)) {
                    operator = jsonObject.getString(JSON_NAME_OPERATOR);
                }
                UIFilterType filter = null;
                if (!jsonObject.isNull(JSON_NAME_FILTER)) {
                    String filtername = jsonObject.getString(JSON_NAME_FILTER);
                    filter = UIFilterType.valueOf(filtername);
                }
                result = new ReportFilter(fields, operator, filter);
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
            if (fields != null) {
                JSONArray array = getList2JSONArray(fields);
                result.put(JSON_NAME_FIELDS, array);
            }
            if (operator != null && operator != "") {
                result.put(JSON_NAME_OPERATOR, operator);
            }
            if (filter != null) {
                result.put(JSON_NAME_FILTER, filter.name());
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return result;
    }

    private static List<String> getJSONArray2List(JSONArray jsonArray) {
        List<String> list = new LinkedList<String>();
        for (int i = 0; i < jsonArray.length(); ++i) {
            try {
                list.add(jsonArray.getString(i));
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        return list;
    }

    private static JSONArray getList2JSONArray(List<String> list) {
        JSONArray array = new JSONArray();
        for (int i = 0; i < list.size(); i++) {
            array.put(list.get(i));
        }
        return array;
    }
    //endregion

    //region SQL Builder
    //region public methods
    public String buildSQLQueryWhereFilter(UIFilter uifilter) {
        String result;
        initOperator(uifilter);
        List<String> values = uifilter.getValues();
        Object object = uifilter.getObject();
        if (filter.isItemType()) {
            result = getItemsFilterSQLFormat(values, object);
        } else {
            result = getSimpleFilterSQLFormat(values);
        }
        return result;
    }

    //region static methods
    public static String trimer(String text) {
        return trimer(text, END_AND);
    }

    public static String trimer(String text, String trim) {
        return text.trim().replaceAll(trim + "$", "").trim();
    }
    //endregion
    //endregion

    //region private methods
    private String getItemsFilterSQLFormat(List<String> values, Object object) {
        String result = "";
        if (object != null) {
            iItems base = (iItems) object;
            result = getFilterSQLFormatBYiIBases(base);
        } else if(values != null && values.size() > 0) {
            String text = values.get(0);
            result = getItemLikeByText(text);
        }
        return result;
    }

    private String getFilterSQLFormatBYiIBases(iItems base) {
        String result = "";
        if (base.groups != null && base.groups.size() > 0) {
            String field = filter.getiIbase().getItemTableName() + ".code";
            List<String> values = std.getCodes(base.groups);
            result += getFilterSQLFormatBYLike(field, values);
        }
        if (base.items != null && base.items.size() > 0) {
            List<String> values = std.getIDs(base.items);
            result += getSimpleFilterSQLFormat(values);
        }
        return result;
    }

    private String getSimpleFilterSQLFormat(List<String> values) {
        String result = "";
        if (values != null) {
            if (operator.equals("=") && values.size() > 1) {
                result = getFilterSQLFormatBYIN(values);
            } else {
                result = getFilterSQLFormatBYOR(values);
            }
        }
        return result;
    }

    private void initOperator(UIFilter uifilter) {
        String operator = uifilter.getOperator();
        if (operator != null && !operator.equals("")) {
            this.operator = operator;
        }
    }

    private String getFilterSQLFormatBYOR(List<String> values) {
        String result = "";
        if (fields != null && values != null && values.size() > 0) {
            for (String field : fields) {
                for (String value : values) {
                    if (value != null && !value.equals("")) {
                        result += field + " " + operator + " " + value + " " + END_OR + " ";
                    }
                }
            }
            result = trimer(result, END_OR);
        }
        result = backfiller(result, endback);
        return result;
    }

    private String getFilterSQLFormatBYIN(List<String> values) {
        String result = "";
        if (fields != null && values != null) {
            for (String field : fields) {
                String text = "";
                for (String value : values) {
                    if (value != null && !value.equals("")) {
                        text += value + END_SPLIT + " ";
                    }
                }
                text = trimer(text, END_SPLIT);
                result += field + " IN (" + text + ") " + END_OR + " ";
            }
            result = trimer(result, END_OR);
        }
        result = backfiller(result, endback);
        return result;
    }

    private String getFilterSQLFormatBYLike(String field, List<String> values) {
        String result = "";
        if (values != null && values.size() > 0) {
            for (String value : values) {
                if (value != null && !value.equals("")) {
                    result += field + " " + END_LIKE + " '" + value + "%' " + END_OR + " ";
                }
            }
            result = trimer(result, END_OR);
        }
        result = backfiller(result, endback);
        return result;
    }

    private String getItemLikeByText(String text) {
        String result = "";
        iIBase item = filter.getiIbase();
        if (item != null) {
            result = backfiller(item.getItemLike(text), endback);
        }
        return result;
    }

    private static String toSQLString(String value) {
        String result = "";
        if (value != null && value != "") {
            result = "'" + value + "'";
        }
        return result;
    }

    private static List<String> toSQLString(List<String> values) {
        List<String> result = null;
        if (values != null) {
            for (String value : values) {
                if (value != null && value != "") {
                    result.add("'" + value + "'");
                }
            }
        }
        return result;
    }

    private static String backfiller(String text, String endback) {
        if (!text.equals("")) {
            if (text.contains(" " + END_OR + " ")) {
                text = "(" + text + ")";
            }
            text += " " + endback + " ";
        }
        return text;
    }
    //endregion
    //endregion
}
