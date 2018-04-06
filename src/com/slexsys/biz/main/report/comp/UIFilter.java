package com.slexsys.biz.main.report.comp;

import com.slexsys.biz.database.sqls.comp.stdsql;
import com.slexsys.biz.database.comp.iIBase;
import com.slexsys.biz.database.comp.iItems;
import com.slexsys.biz.main.comp.NewTypes.DateTime;

import java.io.Serializable;
import java.util.Arrays;
import java.util.List;

/**
 * Created by slexsys on 2/8/17.
 */

public class UIFilter implements Serializable{
    //region fields
    private String table;
    private UIFilterType type;
    private String field;
    private String operator;
    private List<String> values;
    private boolean ignoreEditTextchanges;
    private Object object;
    private int resid;
    private boolean invisible;
    //endregion

    //region constructors
    public UIFilter(UIFilterType type) {
        this.type = type;
        this.resid = type.getResId();
    }

    public UIFilter(UIFilterType type, Object object) {
        this(type);
        this.object = object;
    }

    public UIFilter(UIFilterType type, String value, boolean invisible) {
        this(type);
        this.values = Arrays.asList(value);
        this.invisible = invisible;
    }
    //endregion

    //region getter setter
    public UIFilterType getType() {
        return type;
    }

    public void setType(UIFilterType type) {
        this.type = type;
    }

    public String getField() {
        return field;
    }

    public void setField(String field) {
        this.field = field;
    }

    public String getOperator() {
        return operator;
    }

    public void setOperator(String operator) {
        this.operator = operator;
    }

    public List<String> getValues() {
        return values;
    }

    public void setValue(String value) {
        if (value != null && !value.equals("")) {
            this.values = Arrays.asList(value);
        } else {
            this.values = null;
        }
    }

    public void setValues(List<String> values) {
        this.values = values;
    }

    public void setValueSQLString(String value) {
        if (!ignoreEditTextchanges) {
            setValue(toSQLString(value));
        }
    }

    public void setValuesSQLString(List<String> values) {
        if (!ignoreEditTextchanges) {
            this.values = toSQLString(values);
        }
    }

    public String getValue() {
        String result = "";
        if (values != null && values.size() > 0) {
            result = values.get(0);
        }
        return result;
    }

    public boolean isIgnoreEditTextchanges() {
        return ignoreEditTextchanges;
    }

    public void setIgnoreEditTextchanges(boolean ignoreEditTextchanges) {
        this.ignoreEditTextchanges = ignoreEditTextchanges;
    }

    public Object getObject() {
        return object;
    }

    public void setObject(Object object) {
        this.object = object;
    }

    public int getResid() {
        return resid;
    }

    public void setResid(int resid) {
        this.resid = resid;
    }

    public boolean isInvisible() {
        return invisible;
    }

    public void setInvisible(boolean invisible) {
        this.invisible = invisible;
    }
    //endregion

    //region public methods
    public String getShowValue() {
        return type.getShowValue(object);
    }

    public static UIFilter fromiIBase(UIFilterType type, iIBase item) {
        UIFilter result = new UIFilter(type);
        if (item != null) {
            iItems items = new iItems();
            items.items.add(item);
            result = new UIFilter(type, items);
        }
        return result;
    }

    public iIBase toiIBase() {
        iIBase item = null;
        if (object != null && object instanceof iItems) {
            iItems items = (iItems) object;
            if (items.items.size() > 0) {
                item = (iIBase) items.items.get(0);
            }
        }
        return item;
    }
    //endregion

    //region private methods
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

    public String getFilterField(String table) {
        String result = "";
        if (object != null || (values != null && !values.isEmpty())) {///stugel paymany like-i depqum
            this.table = table;
            if (type.isDateType()) {
                result = getDateFilter(table);
            } else if (type.isItemType()) {
                result = getItemFilter(table);
            } else if (type.isMultiValuesType()) {

            }
        }
        return result;
    }

    private String getDateFilter(String table) {
        String field = stdsql.getTableFieldUnion(table, "Date");
        String operator = getDateOperator();
        Object value = getDateValue();
        return stdsql.getSQLUnion(field, operator, value);
    }

    private String getItemFilter(String table) {
        iIBase item = this.toiIBase();
        String field = stdsql.getTableFieldUnion(table, item.getItemQuerys().Item.IDName);
        String operator = stdsql.EQUALS;
        Object value = item.getId();
        return stdsql.getSQLUnion(field, operator, value);
    }

    private String getDateOperator() {
        String operator = "";
        if (type.equals(UIFilterType.Date)) {
            operator = stdsql.EQUALS;
        } else if (type.equals(UIFilterType.StartDate)) {
            operator = stdsql.GREATE_EQUALS;
        } else if (type.equals(UIFilterType.EndDate)) {
            operator = stdsql.LITTLE_EQUALS;
        }
        return operator;
    }

    private Object getDateValue() {
        Object result = null;
        if (object != null) {
            DateTime dateTime = (DateTime) object;
            String date = dateTime.toSQLFormatDate();
            result = stdsql.getSQLString(date);
        }
        return result;
    }
    //endregion
}
