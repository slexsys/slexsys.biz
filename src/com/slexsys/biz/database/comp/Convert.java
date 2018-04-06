package com.slexsys.biz.database.comp;

import android.widget.ListView;
import android.widget.Spinner;

import java.io.Serializable;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

/**
 * Created by slexsys on 3/18/16.
 */
public class Convert {

    public static List<Double> ToPriceList(Object value) {
        List<String> list = ToSplitedText(value);
        List<Double> dlist = new LinkedList<Double>();
        for (String price : list) {
            dlist.add(Double.parseDouble(price));
        }
        return dlist;
    }

    public static List<String> ToSplitedText(Object value){
        List<String> result = new LinkedList<String>();
        String svalue = value.toString();
        if (svalue.contains(":")) {
            result = new LinkedList<String>(Arrays.asList(svalue.split(":", -1)));
        } else {
            result.add(svalue);
        }
        return result;
    }

    public static double ToDouble(Object value){
        double result = 0;
        try {
            result = Double.parseDouble(value.toString());
        } catch (Exception ex) { }
        return result;
    }

    public static int ToInteger(Object value){
        int result = 0;
        try {
            result = Integer.parseInt(value.toString());
        } catch (Exception ex) { }
        return result;
    }

    public static String ToString(Object value){
        String result = "";
        if (value != null)
            result = value.toString();
        return result;
    }

    public static String ToString(Boolean value){
        String result = "0";
        if (value)
            result = "1";
        return result;
    }

    public static String ToSQLString(String value){
        String result = "''";
        if (value != null)
            result = "'" + value + "'";
        return result;
    }

    public static boolean ToBoolean(Object value){
        boolean result = false;
        try {
            result = Boolean.parseBoolean(value.toString());
        } catch (Exception ex) { }
        return result;
    }

    public static boolean ToBooleanNot(Object value){
        boolean result = false;
        try {
            result = !Boolean.parseBoolean(value.toString());
        } catch (Exception ex) { }
        return result;
    }

    public static String StringListToString(List<String> list){
        String result = "";
        if(list != null){
            for(String l : list){
                result += l + ":";
            }
            result = trimmer(result, list.size());
        }
        return result;
    }

    public static String DoubleListToString(List<Double> list){
        String result = "";
        if(list != null){
            for(Double l : list){
                result += l + ":";
            }
            result = trimmer(result, list.size());
        }
        return result;
    }

    public static List<Object> StringToListObject(String line){
        List<Object> result = new LinkedList<Object>();
        if (line != null && line.length() > 0) {
            String[] items = line.split(":", -1);
            for (String item : items) {
                if (item.trim() != "") {
                    result.add(item);
                }
            }
        }
        return result;
    }

    public static String ListObjectToString(List<Object> list){
        String result = "";
        if (list != null) {
            for (Object item : list) {
                result += item + ":";
            }
            result = trimmer(result, list.size());
        }
        return result;
    }

    private static String trimmer(String result, int size) {
        if(size > 0) {
            result = result.substring(0, result.length() - 1);
            while (result.contains("::")) {
                result = result.replace("::", ":");
            }
        }
        return result;
    }
}
