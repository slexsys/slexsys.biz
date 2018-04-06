package com.slexsys.biz.main.operations.comp;

import com.slexsys.biz.comp.LoginInfo;
import com.slexsys.biz.GO;
import com.slexsys.biz.database.comp.Convert;
import com.slexsys.biz.database.comp.iIBase;
import com.slexsys.biz.database.comp.iItems;
import com.slexsys.biz.database.items.iObject;
import com.slexsys.biz.database.items.iPartner;
import com.slexsys.biz.database.items.iUser;
import com.slexsys.biz.database.sqls.iSQL;
import com.slexsys.biz.main.comp.COPDRF.COPDRFFilters;
import com.slexsys.biz.main.comp.NewTypes.DataRow;
import com.slexsys.biz.main.comp.NewTypes.DataTable;
import com.slexsys.biz.main.comp.NewTypes.DateTime;
import com.slexsys.biz.main.comp.enums.ConsType;
import com.slexsys.biz.main.finance.comp.PayInfo;
import com.slexsys.biz.main.finance.comp.comp.EditPaymentsData;
import com.slexsys.biz.main.operations.json.InsertionMode;
import com.slexsys.biz.main.operations.json.OperationJSON;
import com.slexsys.biz.main.operations.json.PayMode;
import com.slexsys.biz.main.operations.json.QttyMode;
import com.slexsys.biz.main.operations.json.TotalMode;
import com.slexsys.biz.main.report.comp.UIFilter;
import com.slexsys.biz.main.report.comp.UIFilterType;
import com.slexsys.biz.main.report.comp.UIFilters;

import java.io.Serializable;
import java.util.LinkedList;
import java.util.List;

/**
 * Created by slexsys on 3/30/16.
 */
public class OperationData implements Serializable{
    //region fields
    private DateTime date;
    private DateTime dateEnd;
    private DateTime dateRealTime;
    private int operJSONPos;
    private iPartner partner;
    private iObject object1;
    private iObject object2;
    private iUser user;
    private List<OperItem> list;
    private PayInfo payinfo;
    private String acct;
    private EditPaymentsData editedpayment;
    private boolean isDocument;
    //endregion

    //region constructors
    public OperationData() {
        date = DateTime.now();
        list = new LinkedList<OperItem>();
        object1 = LoginInfo.object;
        user = LoginInfo.user;
    }

    public OperationData(int operJSONPos) {
        this();
        this.operJSONPos = operJSONPos;
    }

    public OperationData(int operJSONPos, iPartner partner, List<OperItem> list, PayInfo payinfo, List<iObject> objects, iUser user) {
        this.operJSONPos = operJSONPos;
        this.partner = partner;
        this.list = list;
        this.payinfo = payinfo;
        int i = 0;
        object1 = objects.get(i++);
        if (objects.size() > 1) {
            object2 = objects.get(i);
        }
        this.user = user;
    }

    public OperationData(DataTable dataTable, int operJSONPos, String acct) {
        DataRow row = dataTable.getFirstWithNames();
        this.date = getDate(row);
        this.partner = getPartner(row);
        this.object1 = getObject1(row);
        this.object2 = getObject2(row);
        this.user = getUser(row);
        this.list = OperItem.getOperItemList(dataTable);
        this.acct = acct;
        this.operJSONPos = operJSONPos;
    }
    //endregion

    //region getters setters
    public DateTime getDate() {
        return date;
    }

    public void setDate(DateTime date) {
        this.date = date;
    }

    public int getOperJSONPos() {
        return operJSONPos;
    }

    public void setOperJSONPos(int operJSONPos) {
        this.operJSONPos = operJSONPos;
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

    public List<OperItem> getList() {
        return list;
    }

    public void setList(List<OperItem> list) {
        this.list = list;
    }

    public PayInfo getPayinfo() {
        return payinfo;
    }

    public void setPayinfo(PayInfo payinfo) {
        this.payinfo = payinfo;
    }

    public EditPaymentsData getEditedPayment() {
        return editedpayment;
    }

    public void setEditedPayment(EditPaymentsData data) {
        this.editedpayment = data;
    }
    //endregion

    //region public methods
    public void addOperItem(OperItem item) {
        this.list.add(item);
    }

    public void removeOperItem(int index) {
        this.list.remove(index);
    }

    public void addOperList(List<OperItem> list) {
        this.list.addAll(list);
    }

    public void updateOperByIndex(OperItem item, int index) {
        this.list.remove(index);
        this.list.add(index, item);
    }

    public OperItem getOperItemByIndex(int index) {
        return list.get(index);
    }

    public PayInfo getPayinfoWithTotalAndCredit(TotalMode totalMode) {
        return new PayInfo(getTotal(totalMode), getCredit());
    }

    public double getTotal() {
        TotalMode totalMode = getOperJSON().getTotalMode();
        return getTotal(totalMode);
    }

    public double getTotal(TotalMode totalMode) {
        double result = 0;
        switch (totalMode) {
            case TotalIn:
                result = getTotalIn();
                break;
            case TotalOut:
                result = getTotalOut();
                break;
        }
        return result;
    }

    public double getTotalIn() {
        double total = 0;
        for (OperItem item : this.list) {
            total += item.Qtty * item.PriceIn * (100 - item.Discount) / 100;
        }
        return total;
    }

    public double getTotalOut() {
        double total = 0;
        for (OperItem item : this.list) {
            total += item.Qtty * item.PriceOut * (100 - item.Discount) / 100;
        }
        return total;
    }

    public double getProfit() {
        return getTotalOut() - getTotalIn();
    }

    public String getOperType() {
        String result = getOperJSON().getOperType();
        return result;
    }

    public String getAcct() {
        if(acct == null) {
            acct = getNextAcct();
        }
        return acct;
    }

    private String getNextAcct() {
        int id = getOperJSON().getId();
        return iSQL.getNextAcct(id);
    }

    public String getPartnerID() {
        return Convert.ToString(partner.getId());
    }

    public String getObjectID() {
        return Convert.ToString(object1.getId());
    }

    public String getObjectID2() {
        return Convert.ToString(object2.getId());
    }

    public String getUserID() {
        return Convert.ToString(user.getId());
    }

    public String getPayed() {
        return Convert.ToString(payinfo.payed);
    }

    public String getPayType() {
        return payinfo.type.getValueString();
    }

    public String getTotalString() {
        return Convert.ToString(payinfo.total);
    }

    public String getOperTypeCashBook() {
        TotalMode totalMode = getOperJSON().getTotalMode();
        return ConsType.fromTotalMode(totalMode);
    }

    public String getQttySigned(double qtty, double realQtty) {
        String result;
        QttyMode qttyMode = getOperJSON().getQttyMode();
        switch (qttyMode) {
            case Plus:
                result = Convert.ToString(qtty);
                break;
            case Minus:
                result = Convert.ToString(-qtty);
                break;
            case Equals:
                result = Convert.ToString(qtty - realQtty);
                break;
            case None:
            default:
                result = "0";
                break;
        }
        return result;
    }

    public String getOperType2() {
        String result = Integer.toString(getOperJSON().getSid());
        return result;
    }

    private String getSignTemplate(QttyMode qttyMode, String a, String b, String c) {
        String result;
        switch (qttyMode) {
            case Plus:
            case Equals:
                result = a;
                break;
            case Minus:
                result = b;
                break;
            default:
                result = c;
                break;
        }
        return result;
    }

    public String getSignOper() {
        QttyMode qttyMode = getOperJSON().getQttyMode();
        String result = getSignTemplate(qttyMode, "1", "-1", "0");
        return result;
    }

    public String getSignOper2() {
        QttyMode qttyMode = getOperJSON().getQttyMode();
        String result = getSignTemplate(qttyMode, "-1", "1", "0");
        return result;
    }

    public String getPaySign() {
        QttyMode qttyMode = getOperJSON().getQttyMode();
        String result = getSignTemplate(qttyMode, "-1", "1", "0");
        return result;
    }

    public String getCurrencyRate() {
        return Convert.ToString(GO.currencies.getItemByID(list.get(0).CurrencyID).value);
    }

    private double getCredit() {
        return 0;
    }

    public String getDateString() {
        return getDateValue(date);
    }

    public String getDateEndString() {
        return getDateValue(dateEnd);
    }

    public String getRealTimeString() {
        return getDateTimeValue(dateRealTime);
    }

    private String getDateValue(DateTime dateTime) {
        String result = "now";
        if (dateTime != null) {
            result = dateTime.toSQLFormatDate();
        }
        return result;
    }

    private String getDateTimeValue(DateTime dateTime) {
        String result = "now";
        if (dateTime != null) {
            result = dateTime.toSQLFormatDateTime();
        }
        return result;
    }

    public boolean isDocument() {
        return isDocument;
    }

    public void setIsDocument(boolean value) {
        isDocument = value;
    }

    public boolean isFinancialOperation() {
        boolean result;
        PayMode payMode = getOperJSON().getPayMode();
        if (payMode.equals(PayMode.Pay)) {
            result = true;
        } else {
            result = false;
        }
        return result;
    }

    public String getOperationName() {
        return getOperJSON().getName();
    }

    public String getPartnerName() {
        return partner.getName();
    }

    public void fill(COPDRFFilters filters) {
        date = filters.getDateTime();
        partner = filters.getPartner();
        object1 = filters.getObject1();
        object2 = filters.getObject2();
        user = filters.getUser();
    }

    public void fill(UIFilters filters) {
        UIFilter filter = filters.get(UIFilterType.StartDate);
        if (filter != null) {
            date = (DateTime) filter.getObject();
        }
        filter = filters.get(UIFilterType.Partner);
        if (filter != null) {
            partner = (iPartner) filter.toiIBase();
        }
        filter = filters.get(UIFilterType.Object);
        if (filter != null) {
            object1 = (iObject) filter.toiIBase();
        }
        filter = filters.get(UIFilterType.Object1);
        if (filter != null) {
            object1 = (iObject) filter.toiIBase();
        }
        filter = filters.get(UIFilterType.Object2);
        if (filter != null) {
            object2 = (iObject) filter.toiIBase();
        }
        filter = filters.get(UIFilterType.User);
        if (filter != null) {
            user = (iUser) filter.toiIBase();
        }
    }

    public boolean isDataFilled() {
        boolean result = false;
        InsertionMode mode = getOperJSON().getInsertionMode();
        switch (mode) {
            case Transfer:
                if (date != null && object1 != null && object2 != null && user != null) {
                    result = true;
                }
                break;
            case StockTacking:
                if (object1 != null && user != null) {
                    result = true;
                    partner = new iPartner() {{     /////////////// init anel erb mtnum e oper
                        setId(1);                   ///////////////   ^^^^^^^^^^^^^^
                    }};                             ///////////////    ^^^^^^^^^^^
                }
                break;
            default:
                if (date != null && partner != null && object1 != null && user != null) {
                    result = true;
                }
                break;
        }
        return result;
    }

    public boolean isRowsFilled() {
        boolean result;
        if (list.size() > 0){
            result = true;
        } else {
            result = false;
        }
        return result;
    }

    public OperationJSON getOperJSON() {
        return GO.operations.get(operJSONPos);
    }

    public List<String> getTotals() {
        List<String> result = new LinkedList<String>();
        result.add(Convert.ToString(getTotalIn()));
        result.add(Convert.ToString(getTotalOut()));
        result.add(Convert.ToString(getProfit()));
        return result;
    }

    public double getQttySum() {
        double result = 0;
        for (OperItem item : list) {
            result += item.Qtty;
        }
        return result;
    }

    public void clear() {
        list.clear();
    }
    //endregion

    //region inner methods

    private DateTime getDate(DataRow row) {
        String value = row.getString("date");
        return DateTime.fromSQLFormat(value);
    }

    private iPartner getPartner(DataRow row) {
        String id = row.getString("partnerid");
        return iPartner.getByID(id);
    }

    private iObject getObject1(DataRow row) {
        String id = row.getString("objectid");
        return iObject.getByID(id);
    }

    private iObject getObject2(DataRow row) {
        return null;
    }

    private iUser getUser(DataRow row) {
        String id = row.getString("userid");
        return iUser.getByID(id);
    }

    private iIBase getIBase(iItems iBases, int index, DataRow row) {
        int id = row.getInt(index);
        return iBases.getItemByID(id);
    }

    public void setDefaultObject() {
        LoginInfo.object = object1;
    }
    //endregion
}
