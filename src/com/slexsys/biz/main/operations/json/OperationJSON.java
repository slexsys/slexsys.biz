package com.slexsys.biz.main.operations.json;

import com.slexsys.biz.main.comp.COPDRF.COPDRFColumns;
import com.slexsys.biz.main.comp.json.JSON_STD;

import org.json.JSONObject;

import java.io.Serializable;

/**
 * Created by slexsys on 2/27/17.
 */

public class OperationJSON implements Serializable{
    //region constants
    private static final String JSON_NAME_ID = "0";//"id";
    private static final String JSON_NAME_SID = "1";//"sid";
    private static final String JSON_NAME_NAME = "2";//"name";
    private static final String JSON_NAME_QTTY_MODE = "3";//"qttyMode";
    private static final String JSON_NAME_PAY_MODE = "4";//"payMode";
    private static final String JSON_NAME_TOTAL_MODE = "5";//"totalMode";
    private static final String JSON_NAME_PRICE_UPDATE = "6";//"priceUpdate";
    private static final String JSON_NAME_INSERTION_MODE = "7";//"insertionMode";
    private static final String JSON_NAME_OPERATIONS_COLUMNS = "8";//"columns";
    //endregion

    //region fields
    private int id;
    private int sid;
    private String name;
    private QttyMode qttyMode;
    private PayMode payMode;
    private TotalMode totalMode;
    private PriceUpdate priceUpdate;
    private InsertionMode insertionMode;
    private COPDRFColumns columns;
    //endregion

    //region constructors
    public OperationJSON() { }

    public OperationJSON(int id, String name, QttyMode qttyMode, PayMode payMode, TotalMode totalMode, PriceUpdate priceUpdate, InsertionMode insertionMode) {
        this.id = id;
        this.name = name;
        this.qttyMode = qttyMode;
        this.payMode = payMode;
        this.totalMode = totalMode;
        this.priceUpdate = priceUpdate;
        this.insertionMode = insertionMode;
    }

    public OperationJSON(int id, int sid, String name, QttyMode qttyMode, PayMode payMode, TotalMode totalMode, PriceUpdate priceUpdate, InsertionMode insertionMode) {
        this.id = id;
        this.sid = sid;
        this.name = name;
        this.qttyMode = qttyMode;
        this.payMode = payMode;
        this.totalMode = totalMode;
        this.priceUpdate = priceUpdate;
        this.insertionMode = insertionMode;
    }

    public OperationJSON(int id, String name, QttyMode qttyMode, PayMode payMode, TotalMode totalMode, PriceUpdate priceUpdate, InsertionMode insertionMode, COPDRFColumns columns) {
        this.name = name;
        this.qttyMode = qttyMode;
        this.payMode = payMode;
        this.totalMode = totalMode;
        this.columns = columns;
        this.priceUpdate = priceUpdate;
        this.insertionMode = insertionMode;
        this.id = id;
    }

    public OperationJSON(int id, int sid, String name, QttyMode qttyMode, PayMode payMode, TotalMode totalMode, PriceUpdate priceUpdate, InsertionMode insertionMode, COPDRFColumns columns) {
        this.id = id;
        this.sid = sid;
        this.name = name;
        this.qttyMode = qttyMode;
        this.payMode = payMode;
        this.totalMode = totalMode;
        this.columns = columns;
        this.priceUpdate = priceUpdate;
        this.insertionMode = insertionMode;
    }
    //endregion

    //region getters setters
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getSid() {
        return sid;
    }

    public void setSid(int sid) {
        this.sid = sid;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public QttyMode getQttyMode() {
        return qttyMode;
    }

    public void setQttyMode(QttyMode qttyMode) {
        this.qttyMode = qttyMode;
    }

    public PayMode getPayMode() {
        return payMode;
    }

    public void setPayMode(PayMode payMode) {
        this.payMode = payMode;
    }

    public TotalMode getTotalMode() {
        return totalMode;
    }

    public void setTotalMode(TotalMode totalMode) {
        this.totalMode = totalMode;
    }

    public PriceUpdate getPriceUpdate() {
        return priceUpdate;
    }

    public void setPriceUpdate(PriceUpdate priceUpdate) {
        this.priceUpdate = priceUpdate;
    }

    public InsertionMode getInsertionMode() {
        return insertionMode;
    }

    public void setInsertionMode(InsertionMode insertionMode) {
        this.insertionMode = insertionMode;
    }

    public COPDRFColumns getColumns() {
        return columns;
    }

    public void setColumns(COPDRFColumns columns) {
        this.columns = columns;
    }

    public String getOperType() {
        return Integer.toString(id);
    }
    //endregion

    //region json
    public static OperationJSON fromJSON(JSONObject jsonObject) {
        int id = (Integer) JSON_STD.getObjectFromJSONObject(jsonObject, JSON_NAME_ID);
        int sid = (Integer) JSON_STD.getObjectFromJSONObject(jsonObject, JSON_NAME_SID);
        String name = (String) JSON_STD.getObjectFromJSONObject(jsonObject, JSON_NAME_NAME);
        QttyMode qttyMode = QttyMode.values()[JSON_STD.getInt(jsonObject, JSON_NAME_QTTY_MODE)];
        PayMode payMode = PayMode.values()[JSON_STD.getInt(jsonObject, JSON_NAME_PAY_MODE)];
        TotalMode totalMode = TotalMode.values()[JSON_STD.getInt(jsonObject, JSON_NAME_TOTAL_MODE)];
        PriceUpdate priceUpdate = PriceUpdate.values()[JSON_STD.getInt(jsonObject, JSON_NAME_PRICE_UPDATE)];
        InsertionMode insertionMode = InsertionMode.values()[JSON_STD.getInt(jsonObject, JSON_NAME_INSERTION_MODE)];
        COPDRFColumns columns = COPDRFColumns.fromJSONArray(JSON_STD.getJSONArrayFromJSONObject(jsonObject, JSON_NAME_OPERATIONS_COLUMNS));
        return new OperationJSON(id, sid, name, qttyMode, payMode, totalMode, priceUpdate, insertionMode, columns);
    }

    public JSONObject toJSON() {
        JSONObject result = new JSONObject();
        JSON_STD.putJSONObject(result, id, JSON_NAME_ID);
        JSON_STD.putJSONObject(result, sid, JSON_NAME_SID);
        JSON_STD.putString(result, name, JSON_NAME_NAME);
        JSON_STD.putEnum(result, qttyMode, JSON_NAME_QTTY_MODE);
        JSON_STD.putEnum(result, payMode, JSON_NAME_PAY_MODE);
        JSON_STD.putEnum(result, totalMode, JSON_NAME_TOTAL_MODE);
        JSON_STD.putEnum(result, priceUpdate, JSON_NAME_PRICE_UPDATE);
        JSON_STD.putEnum(result, insertionMode, JSON_NAME_INSERTION_MODE);
        JSON_STD.putJSONObject(result, columns.toJSONArray(), JSON_NAME_OPERATIONS_COLUMNS);
        return result;
    }
    //endregion
}
