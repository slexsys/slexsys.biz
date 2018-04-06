package com.slexsys.biz.main.comp.COPDRF;

import com.slexsys.biz.database.comp.iIBase;
import com.slexsys.biz.database.items.iObject;
import com.slexsys.biz.database.items.iPartner;
import com.slexsys.biz.database.items.iUser;
import com.slexsys.biz.main.comp.NewTypes.DateTime;
import com.slexsys.biz.main.operations.comp.OperationData;
import com.slexsys.biz.main.operations.json.InsertionMode;
import com.slexsys.biz.main.report.comp.UIFilterType;
import com.slexsys.biz.main.report.comp.UIFilter;
import com.slexsys.biz.main.report.comp.UIFilters;

import java.io.Serializable;

/**
 * Created by slexsys on 3/14/17.
 */

public class COPDRFFilters implements Serializable {
    //region fields
    private DateTime dateTime;
    private iPartner partner;
    private iObject object1;
    private iObject object2;
    private iUser user;
    //endregion

    //region constructors
    public COPDRFFilters() { }

    public COPDRFFilters(DateTime dateTime, iPartner partner, iObject object1, iObject object2, iUser user) {
        this.dateTime = dateTime;
        this.partner = partner;
        this.object1 = object1;
        this.object2 = object2;
        this.user = user;
    }
    //endregion

    //region getters setters
    public DateTime getDateTime() {
        return dateTime;
    }

    public void setDateTime(DateTime dateTime) {
        this.dateTime = dateTime;
    }

    public iPartner getPartner() {
        return partner;
    }

    public void setPartner(iPartner partner) {
        this.partner = partner;
    }

    public iObject getObject1() {
        return object1;
    }

    public void setObject1(iObject object1) {
        this.object1 = object1;
    }

    public iObject getObject2() {
        return object2;
    }

    public void setObject2(iObject object2) {
        this.object2 = object2;
    }

    public iUser getUser() {
        return user;
    }

    public void setUser(iUser user) {
        this.user = user;
    }
    //endregion

    //region public methods
    public static COPDRFFilters fromOperationData(OperationData operationData) {
        DateTime dateTime = DateTime.now();//operationData.getDateNow();
        iPartner partner = operationData.getPartner();
        iObject object1 = operationData.getObject1();
        iObject object2 = operationData.getObject2();
        iUser user = operationData.getUser();
        return new COPDRFFilters(dateTime, partner, object1, object2, user);
    }

    public static COPDRFFilters fromUIFilters(UIFilters uiFilters) {
        COPDRFFilters filters = new COPDRFFilters();
        for (UIFilter filter : uiFilters) {
            switch (filter.getType()) {
                case StartDate:
                    filters.dateTime = (DateTime) filter.getObject();
                    break;
                case Partner:
                    filters.partner = (iPartner) filter.toiIBase();
                    break;
                case Object:
                case Object1:
                    filters.object1 = (iObject) filter.toiIBase();
                    break;
                case Object2:
                    filters.object2 = (iObject) filter.toiIBase();
                    break;
                case User:
                    filters.user = (iUser) filter.toiIBase();
                    break;
            }
        }
        return filters;
    }

    public UIFilters getUIFilters(InsertionMode mode) {
        UIFilters filters = new UIFilters();
        switch (mode) {
            case Transfer:
                filters.add(new UIFilter(UIFilterType.StartDate, dateTime));
                addNewFilter(filters, UIFilterType.Object1, object1);
                addNewFilter(filters, UIFilterType.Object2, object2);
                addNewFilter(filters, UIFilterType.User, user);
                break;
            case StockTacking:
                addNewFilter(filters, UIFilterType.Object, object1);
                addNewFilter(filters, UIFilterType.User, user);
                break;
            default:
                filters.add(new UIFilter(UIFilterType.StartDate, dateTime));
                addNewFilter(filters, UIFilterType.Partner, partner);
                addNewFilter(filters, UIFilterType.Object, object1);
                addNewFilter(filters, UIFilterType.User, user);
                break;
        }
        return filters;
    }
    //endregion

    //region private methods
    private void addNewFilter(UIFilters filters, UIFilterType type, iIBase item) {
        if (item != null) {
            filters.add(UIFilter.fromiIBase(type, item));
        } else {
            filters.add(new UIFilter(type));
        }
    }
    //endregion
}
