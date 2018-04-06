package com.slexsys.biz.main.finance.comp.comp;

import android.database.Cursor;

import com.slexsys.biz.main.comp.NewTypes.DataRow;
import com.slexsys.biz.main.comp.enums.PayType;
import com.slexsys.biz.main.comp.NewTypes.DateTime;

import java.io.Serializable;
import java.util.List;

/**
 * Created by slexsys on 1/25/17.
 */

public class EditPaymentsRowData implements Serializable{

    private int id;
    private DateTime dateTime;
    private String type;
    private PayType paytype;
    private double sum;
    private boolean changed;

    public EditPaymentsRowData(Cursor cursor) {
        if (cursor.getColumnCount() > 0) {
            this.id = cursor.getInt(cursor.getColumnIndex("id"));
            this.dateTime = DateTime.fromSQLFormat(cursor.getString(cursor.getColumnIndex("date")));
            this.type = cursor.getString(cursor.getColumnIndex("mode"));
            this.paytype = PayType.fromValue(cursor.getInt(cursor.getColumnIndex("type")));
            this.sum = cursor.getDouble(cursor.getColumnIndex("qtty"));
            this.changed = false;
        }
    }

    public EditPaymentsRowData(DataRow row, List<String> columnNames) {
        if (row.size() > 0) {
            this.id = row.getInt(columnNames.indexOf("id"));
            this.dateTime = DateTime.fromSQLFormat(row.getString(columnNames.indexOf("date")));
            this.type = row.getString(columnNames.indexOf("mode"));
            this.paytype = PayType.fromValue(row.getInt(columnNames.indexOf("type")));
            this.sum = row.getDouble(columnNames.indexOf("qtty"));
            this.changed = false;
        }
    }

    //region getter setter
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public DateTime getDateTime() {
        return dateTime;
    }

    public void setDateTime(DateTime dateTime) {
        this.dateTime = dateTime;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public PayType getPaytype() {
        return paytype;
    }

    public void setPaytype(PayType paytype) {
        this.paytype = paytype;
    }

    public double getSum() {
        return sum;
    }

    public void setSum(double sum) {
        this.sum = sum;
    }

    public boolean getChanged() {
        return changed;
    }

    public void setChanged(boolean state) {
        this.changed = state;
    }
    //endregion
}
