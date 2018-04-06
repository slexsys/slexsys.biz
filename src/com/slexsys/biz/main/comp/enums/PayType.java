package com.slexsys.biz.main.comp.enums;

import java.io.Serializable;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

/**
 * Created by slexsys on 3/30/16.
 */
public enum PayType implements Serializable{
    Cash(1),
    NoCash(2);

    private int value;

    PayType(int i) {
        value = i;
    }

    public static List<String> names() {

        List<String> list = new LinkedList<String>();
        for (PayType pt : PayType.values()) {
            list.add(pt.name());
        }

        return list;
    }

    public int getValue() {
        return value;
    }

    public String getValueString() {
        return Integer.toString(value);
    }

    public static PayType fromValue(int value) {
        PayType result = null;
        for (PayType type : PayType.values()) {
            if (type.getValue() == value) {
                result = type;
            }
        }
        return result;
    }

    public static PayType fromIndex(int index) {
        PayType result = null;
        int i = 0;
        for (PayType type : PayType.values()) {
            if (i++ == index) {
                result = type;
                break;
            }
        }
        return result;
    }

    public int toIndex() {
        int result = -1;
        int i = 0;
        for (PayType type : PayType.values()) {
            if (type == this) {
                result = i;
                break;
            } else {
                i++;
            }
        }
        return result;
    }

    public static Map<String, String> getMap() {
        Map<String, String> map = new LinkedHashMap<String, String>();
        for (PayType type : PayType.values()) {
            map.put(type.getValueString(), type.name());
        }
        return map;
    }
}
