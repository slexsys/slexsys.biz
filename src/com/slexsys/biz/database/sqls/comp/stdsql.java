package com.slexsys.biz.database.sqls.comp;

/**
 * Created by slexsys on 12/19/17.
 */

public class stdsql {
    //region finals
    public static final String START_WHERE = "WHERE";
    public static final String END_SPLIT = ",";
    public static final String DOT = ".";
    public static final String OR = "OR";
    public static final String AND = "AND";
    public static final String LIKE = "LIKE";
    public static final String EQUALS = "=";
    public static final String GREATE_EQUALS = ">=";
    public static final String LITTLE_EQUALS = "<=";
    public static final String SPACE = " ";
    public static final String APOSTROPHE = "'";
    //endregion

    //region methods
    public static String trimer(String text) {
        return trimer(text, AND);
    }

    public static String trimer(String text, String trim) {
        return text.trim().replaceAll(trim + "$", "").trim();
    }

    public static String getFinalResult(String result) {
        if (result != null && result != "") {
            result = START_WHERE + SPACE + trimer(result);
        } else {
            result = "";
        }
        return result;
    }

    public static String getSQLUnion(String field, String operator, Object value) {
        return SPACE + field + SPACE + operator + SPACE + value + SPACE + AND + SPACE;
    }

    public static String getSQLString(String value) {
        return APOSTROPHE + value + APOSTROPHE;
    }

    public static String getTableFieldUnion(String table, String field) {
        return table + DOT + field;
    }
    //endregion
}
