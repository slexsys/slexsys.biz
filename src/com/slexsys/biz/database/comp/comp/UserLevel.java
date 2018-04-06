package com.slexsys.biz.database.comp.comp;

import android.content.Context;

import com.slexsys.biz.R;

/**
 * Created by slexsys on 12/28/17.
 */

public enum UserLevel {
    Owner(3),
    Administrator(2),
    Manager(1),
    Operator(0);

    private int value;

    UserLevel(int value) {
        this.value = value;
    }

    public int value() { return value; }

    public static String[] getNamesTranslated(Context context) {
        return new String[] {
                context.getString(R.string.owner),
                context.getString(R.string.administrator),
                context.getString(R.string.manager),
                context.getString(R.string.operator)
        };
    }

    public static UserLevel getValueByPos(int pos) {
        return UserLevel.values()[pos];
    }

    public static UserLevel fromValue(int value) {
        UserLevel result = null;
        for (UserLevel userLevel : UserLevel.values()) {
            if (userLevel.value() == value) {
                result = userLevel;
                break;
            }
        }
        return result;
    }
}
