package com.slexsys.biz.database.comp.comp;

import android.content.Context;

import com.slexsys.biz.R;

import java.util.LinkedList;
import java.util.List;

/**
 * Created by slexsys on 12/28/17.
 */

public enum  PriceGroups {
    PriceGroup1(1),
    PriceGroup2(2),
    PriceGroup3(3),
    PriceGroup4(4),
    PriceGroup5(5),
    PriceGroup6(6),
    PriceGroup7(7),
    PriceGroup8(8),
    PriceGroup9(9),
    PriceGroup10(10);

    private int value;

    PriceGroups(int value) {
        this.value = value;
    }

    public int value() { return value; }

    public static String[] getNamesTranslated(Context context) {
        List<String> list = new LinkedList<String>();
        for (PriceGroups priceGroups : PriceGroups.values()) {
            list.add(priceGroups.getNameTranslated(context));
        }
        return list.toArray(new String[list.size()]);
    }

    public static PriceGroups getValueByPos(int pos) {
        return PriceGroups.values()[pos];
    }

    private String getNameTranslated(Context context) {
        String pg = context.getResources().getString(R.string.PriceGroup);
        return pg + " " + value;
    }

    public static PriceGroups fromValue(int value) {
        PriceGroups result = null;
        for (PriceGroups priceGroup : PriceGroups.values()) {
            if (priceGroup.value() == value) {
                result = priceGroup;
                break;
            }
        }
        return result;
    }
}
