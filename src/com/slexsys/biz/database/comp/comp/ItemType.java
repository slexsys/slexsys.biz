package com.slexsys.biz.database.comp.comp;

import android.content.Context;

import com.slexsys.biz.R;

/**
 * Created by slexsys on 12/28/17.
 */

public enum ItemType {
    Standard(0),
    Fixed(1);

    private int value;

    ItemType(int value) {
        this.value = value;
    }

    public int value() {
        return value;
    }

    public static String[] getNamesTranslated(Context context) {
        return new String[] {
                context.getString(R.string.standard),
                context.getString(R.string.fixed)
        };
    }

    public static ItemType getValueByPos(int pos) {
        return ItemType.values()[pos];
    }

    public static ItemType fromValue(int value) {
        ItemType result = null;
        for (ItemType type : ItemType.values()) {
            if (type.value() == value) {
                result = type;
                break;
            }
        }
        return result;
    }
}
