package com.slexsys.biz.main.report.comp;

import com.slexsys.biz.GO;
import com.slexsys.biz.R;
import com.slexsys.biz.database.comp.iIBase;
import com.slexsys.biz.database.comp.iItems;
import com.slexsys.biz.database.items.iItem;
import com.slexsys.biz.database.items.iObject;
import com.slexsys.biz.database.items.iPartner;
import com.slexsys.biz.database.items.iUser;
import com.slexsys.biz.main.comp.NewTypes.DateTime;

import java.io.Serializable;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

/**
 * Created by slexsys on 2/8/17.
 */

public enum UIFilterType implements Serializable {
    Date,
    StartDate,
    EndDate,
    DateInterval,
    Partner,
    Item,
    Object,
    Object1,
    Object2,
    User,
    StartAcct,
    EndAcct,
    Qtty,
    Price,
    OperTypeAll,
    OperTypePay,
    OperTypeNoPay,
    PayType,
    ConsType,
    Measure1,
    Measure2,
    Description;

    public boolean isMain() {
        boolean result = false;
        switch (this) {
            case Date:
            case StartDate:
            case EndDate:
            case DateInterval:
            case Partner:
            case Item:
            case Object:
            case Object1:
            case Object2:
            case User:
                result = true;
                break;
        }
        return result;
    }

    public boolean isDateType() {
        boolean result;
        switch (this) {
            case Date:
            case StartDate:
            case EndDate:
            case DateInterval:
                result = true;
                break;
            default:
                result = false;
                break;
        }
        return result;
    }

    public boolean isItemType() {
        boolean result;
        switch (this) {
            case Partner:
            case Item:
            case Object:
            case Object1:
            case Object2:
            case User:
                result = true;
                break;
            default:
                result = false;
                break;
        }
        return result;
    }

    public boolean isAcct() {
        boolean result;
        switch (this) {
            case StartAcct:
            case EndAcct:
                result = true;
                break;
            default:
                result = false;
                break;
        }
        return result;
    }

    public boolean isQttyPriceType() {
        boolean result;
        switch (this) {
            case Qtty:
            case Price:
                result = true;
                break;
            default:
                result = false;
                break;
        }
        return result;
    }

    public boolean isMultiValuesType() {
        boolean result;
        switch (this) {
            case OperTypeAll:
            case OperTypePay:
            case OperTypeNoPay:
            case PayType:
            case ConsType:
            case Measure1:
            case Measure2:
                result = true;
                break;
            default:
                result = false;
                break;
        }
        return result;
    }

    public boolean isNoButton() {
        boolean result;
        switch (this) {
            case StartAcct:
            case EndAcct:
            case Description:
                result = true;
                break;
            default:
                result = false;
                break;
        }
        return result;
    }

    public iIBase getiIbase() {
        iIBase result = null;
        switch (this) {
            case Partner:
                result = new iPartner();
                break;
            case Item:
                result = new iItem();
                break;
            case Object:
            case Object1:
            case Object2:
                result = new iObject();
                break;
            case User:
                result = new iUser();
                break;
        }
        return result;
    }

    public static List<UIFilterType> normalize(List<UIFilterType> items) {
        List<UIFilterType> result = new LinkedList<UIFilterType>();
        for (UIFilterType item : items) {
            if (!result.contains(item)) {
                result.add(item);
            }
        }
        UIFilterType[] array = result.toArray(new UIFilterType[result.size()]);
        Arrays.sort(array);
        result = Arrays.asList(array);
        return result;
    }

    public int getResId() {
        int result;
        switch (this) {
            case Date:
                result = R.string.date;
                break;
            case StartDate:
                result = R.string.start_date;
                break;
            case EndDate:
                result = R.string.end_date;
                break;
            case DateInterval:
                result = R.string.date_interval;
                break;
            case Partner:
                result = R.string.partner;
                break;
            case Item:
                result = R.string.item;
                break;
            case Object:
                result = R.string.object;
                break;
            case Object1:
                result = R.string.from_object;
                break;
            case Object2:
                result = R.string.to_object;
                break;
            case User:
                result = R.string.user;
                break;
            case StartAcct:
                result = R.string.start_acct;
                break;
            case EndAcct:
                result = R.string.end_acct;
                break;
            case Qtty:
                result = R.string.qtty;
                break;
            case Price:
                result = R.string.price;
                break;
            case OperTypeAll:
            case OperTypePay:
            case OperTypeNoPay:
                result = R.string.operation;
                break;
            case PayType:
                result = R.string.pay_type;
                break;
            case ConsType:
                result = R.string.consumption;
                break;
            case Measure1:
                result = R.string.measure1;
                break;
            case Measure2:
                result = R.string.measuer2;
                break;
            case Description:
                result = R.string.description;
                break;
            default:
                result = R.string.none;
                break;
        }
        return result;
    }

    public String getShowValue(java.lang.Object object) {
        String result;
        switch (this) {
            case Date:
            case StartDate:
            case EndDate:
                result = getDateTimeValue(object);
                break;
            case DateInterval:
                result = "Interval ...."; ///////uxxel ays
                break;
            case Partner:
            case Item:
            case Object:
            case Object1:
            case Object2:
            case User:
                result = getiBaseName(object);
                break;
            case StartAcct:
            case EndAcct:
            case Qtty:
            case Price:
            case OperTypeAll:
            case OperTypePay:
            case OperTypeNoPay:
            case PayType:
            case ConsType:
            case Measure1:
            case Measure2:
            case Description:
            default:
                result = null;
                break;
        }
        return result;
    }

    private String getDateTimeValue(java.lang.Object object) {
        String result = "";
        DateTime dateTime = (DateTime) object;
        if (dateTime != null) {
            result = dateTime.toNormalDateFormat();
        }
        return result;
    }

    private String getiBaseName(java.lang.Object object) {
        String result = "";
        iItems root = (iItems) object;
        if (root != null) {
            if (root.items.size() > 0) {
                result = ((iIBase) root.items.get(0)).getName();
            }
        }
        return result;
    }

    public boolean isString() {
        return false;
    }
}
