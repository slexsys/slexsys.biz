package com.slexsys.biz.main.comp.enums;

import com.slexsys.biz.main.operations.json.TotalMode;

import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

/**
 * Created by slexsys on 1/14/17.
 */

public enum ConsType {

    Purchase(1),
    Sale(8),
    Refund(1),
    RefundToSupplier(8),
    StockTacking(8);

    private int value;

    ConsType(int i) {
        value = i;
    }

    public int getValue() {
        return value;
    }

    public String getValueString() {
        return Integer.toString(value);
    }

    public static String fromTotalMode(TotalMode totalMode) {
        String result;
        switch (totalMode) {
            case TotalIn:
                result = "1";
                break;
            case TotalOut:
                result = "8";
                break;
            default:
                result = null;
                break;
        }
        return result;
    }

    public static List<String> names() {
        List<String> result = new LinkedList<String>();
        ConsType[] values = ConsType.values();
        for (int i = 0; i < values.length; ++i) {
            result.add(values[i].name());
        }
        return result;
    }

    public static int getIndexById(int id) {
        int result = 0;
        ConsType[] values = ConsType.values();
        for (int i = 0; i < values.length; ++i) {
            if (values[i].value == id) {
                result = i;
                break;
            }
        }
        return result;
    }

    public static String getIdByIndex(int pos) {
        String result = null;
        ConsType[] values = ConsType.values();
        for (int i = 0; i < values.length; ++i) {
            if (i == pos) {
                result = Integer.toString(values[i].value);
                break;
            }
        }
        return result;
    }

    public static String getSignById(String id) {
        int index = getIndexById(Integer.parseInt(id));
        ConsType[] values = ConsType.values();
        String result;
        switch (values[index]) {
            case Purchase:
            case Refund:
                result = "-1";
                break;
            case Sale:
            case RefundToSupplier:
            case StockTacking:
            default:
                result = "1";
                break;
        }
        return result;
    }

    public static Map<String, String> getMap() {
        Map<String, String> map = new LinkedHashMap<String, String>();
        for (ConsType type : ConsType.values()) {
            map.put(type.getValueString(), type.name());
        }
        return map;
    }
}
