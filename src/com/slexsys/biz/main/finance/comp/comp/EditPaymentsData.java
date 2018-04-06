package com.slexsys.biz.main.finance.comp.comp;

import android.database.Cursor;

import com.slexsys.biz.GO;
import com.slexsys.biz.database.items.iPartner;
import com.slexsys.biz.main.comp.NewTypes.DataRow;
import com.slexsys.biz.main.comp.NewTypes.DataTable;
import com.slexsys.biz.main.comp.NewTypes.DateTime;

import java.io.Serializable;
import java.util.LinkedList;
import java.util.List;

/**
 * Created by slexsys on 1/25/17.
 */

public class EditPaymentsData implements Serializable{

    private iPartner partner;
    private String acct;
    private String operType;
    private String sign;
    private String partnerid;
    private String objectid;
    private String userid;
    private String enddate;
    private List<EditPaymentsRowData> list;
    private DateTime dateTime;

    public EditPaymentsData() { }

    public EditPaymentsData(Cursor cursor, String acct, String operType) {
        if (cursor.getCount() > 0) {
            cursor.moveToFirst();
            initVariables(cursor, acct, operType);
            do {
                list.add(new EditPaymentsRowData(cursor));
            } while (cursor.moveToNext());
            cursor.close();
        }
    }

    private void initVariables(Cursor cursor, String acct, String operType) {
        this.acct = acct;
        this.operType = operType;
        this.sign = cursor.getString(cursor.getColumnIndex("sign"));
        this.partner = getPartner(cursor.getInt(cursor.getColumnIndex("partnerid")));
        this.dateTime = getDateTime(cursor.getString(cursor.getColumnIndex("date")));
        this.list = new LinkedList<EditPaymentsRowData>();
        this.objectid = cursor.getString(cursor.getColumnIndex("objectid"));
        this.userid = cursor.getString(cursor.getColumnIndex("userid"));
        this.enddate = cursor.getString(cursor.getColumnIndex("enddate"));
    }

    public EditPaymentsData(DataTable dataTable, String acct, String operType) {
        if (dataTable.size() > 0) {
            initVariables(dataTable.getFirst(), dataTable.getColumnNames(), acct, operType);
            for (DataRow row : dataTable) {
                list.add(new EditPaymentsRowData(row, dataTable.getColumnNames()));
            }
        }
    }

    private void initVariables(DataRow row, List<String> columnNames, String acct, String operType) {
        this.acct = acct;
        this.operType = operType;
        this.sign = row.getString(columnNames.indexOf("sign"));
        this.partner = getPartner(row.getInt(columnNames.indexOf("partnerid")));
        this.dateTime = getDateTime(row.getString(columnNames.indexOf("date")));
        this.list = new LinkedList<EditPaymentsRowData>();
        this.objectid = row.getString(columnNames.indexOf("objectid"));
        this.userid = row.getString(columnNames.indexOf("userid"));
        this.enddate = row.getString(columnNames.indexOf("enddate"));
    }

    private DateTime getDateTime(String date) {
        return DateTime.fromSQLFormat(date);
    }

    private iPartner getPartner(int pid) {
        partnerid = Integer.toString(pid);
        return iPartner.getByID(partnerid);
    }

    //region getter setter
    public iPartner getPartner() {
        return partner;
    }

    public void setPartner(iPartner partner) {
        this.partner = partner;
    }

    public String getPartnerid() {
        return partnerid;
    }

    public void setPartnerid(String partnerid) {
        this.partnerid = partnerid;
    }

    public String getOperType() {
        return operType;
    }

    public void setOperType(String operType) {
        this.operType = operType;
    }

    public String getAcct() {
        return acct;
    }

    public void setAcct(String acct) {
        this.acct = acct;
    }

    public String getSign() {
        return sign;
    }

    public void setSign(String sign) {
        this.sign = sign;
    }

    public String getObjectid() {
        return objectid;
    }

    public void setObjectid(String objectid) {
        this.objectid = objectid;
    }

    public String getUserid() {
        return userid;
    }

    public void setUserid(String userid) {
        this.userid = userid;
    }

    public List<EditPaymentsRowData> getList() {
        return list;
    }

    public void setList(List<EditPaymentsRowData> list) {
        this.list = list;
    }

    public DateTime getDateTime() {
        return dateTime;
    }

    public void setDateTime(DateTime dateTime) {
        this.dateTime = dateTime;
    }

    public String getEnddate() {
        return enddate;
    }

    public void setEnddate(String date) {
        this.enddate = date;
    }

    public String getRealTimeString() {
        DateTime date = DateTime.now();
        return date.toSQLFormatDateTime();
    }
    //endregion
}
