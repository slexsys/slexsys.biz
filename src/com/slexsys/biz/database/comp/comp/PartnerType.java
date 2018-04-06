package com.slexsys.biz.database.comp.comp;

import android.content.Context;

import com.slexsys.biz.R;

/**
 * Created by slexsys on 12/28/17.
 */

public enum PartnerType {
    Universal(0),
    Client(1),
    Supplier(2);

    private int value;

    PartnerType(int value) {
        this.value = value;
    }

    public int value() { return value; }

    public static String[] getNamesTranslated(Context context) {
        return new String[] {
                context.getString(R.string.universal),
                context.getString(R.string.client),
                context.getString(R.string.supplier)
        };
    }

    public static PartnerType getValueByPos(int pos) {
        return PartnerType.values()[pos];
    }

    public static PartnerType fromValue(int value) {
        PartnerType result = null;
        for (PartnerType type : PartnerType.values()) {
            if (type.value() == value) {
                result = type;
                break;
            }
        }
        return result;
    }
}
