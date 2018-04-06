package com.slexsys.biz.main.comp.COPDRF;

import com.slexsys.biz.main.comp.json.JSONObjectInterface;
import com.slexsys.biz.main.comp.json.JSON_STD;

import org.json.JSONObject;

import java.io.Serializable;

/**
 * Created by slexsys on 2/27/17.
 */
public class COPDRFColumn implements JSONObjectInterface, Serializable {
    //region constants
    private static final String JSON_NAME_COLUMN_NAME = "0";//"columnName";
    private static final String JSON_NAME_ACCESS_MODE = "1";//"accessMode";
    //endregion

    //region fields
    private COPDRFColumnName columnName;
    private COPDRFColumnAccessMode accessMode;
    //endregion

    //region constructors
    public COPDRFColumn(COPDRFColumnName columnName, COPDRFColumnAccessMode accessMode) {
        this.columnName = columnName;
        this.accessMode = accessMode;
    }
    //endregion

    //region getters setters
    public COPDRFColumnName getColumnName() {
        return columnName;
    }

    public void setColumnName(COPDRFColumnName columnName) {
        this.columnName = columnName;
    }

    public COPDRFColumnAccessMode getAccessMode() {
        return accessMode;
    }

    public void setAccessMode(COPDRFColumnAccessMode accessMode) {
        this.accessMode = accessMode;
    }
    //endregion

    //region json
    public static COPDRFColumn fromJSONObject(JSONObject jsonObject) {
        COPDRFColumnName columnName = COPDRFColumnName.values()[JSON_STD.getInt(jsonObject, JSON_NAME_COLUMN_NAME)];
        COPDRFColumnAccessMode accessMode = COPDRFColumnAccessMode.values()[JSON_STD.getInt(jsonObject, JSON_NAME_ACCESS_MODE)];
        return new COPDRFColumn(columnName, accessMode);
    }

    @Override
    public JSONObject toJSONObject() {
        JSONObject result = new JSONObject();
        JSON_STD.putEnum(result, columnName, JSON_NAME_COLUMN_NAME);
        JSON_STD.putEnum(result, accessMode, JSON_NAME_ACCESS_MODE);
        return result;
    }
    //endregion
}
