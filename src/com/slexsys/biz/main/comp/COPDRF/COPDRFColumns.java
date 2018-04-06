package com.slexsys.biz.main.comp.COPDRF;

import com.slexsys.biz.main.comp.json.JSONArrayInterface;
import com.slexsys.biz.main.comp.json.JSON_STD;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.Serializable;
import java.util.LinkedList;
import java.util.List;

/**
 * Created by slexsys on 2/27/17.
 */
public class COPDRFColumns extends LinkedList<COPDRFColumn> implements JSONArrayInterface, Serializable {
    //region constants
    //endregion

    //region constructors
    //endregion

    //region getters setters
    //endregion

    //region public methods
    public COPDRFColumn get(COPDRFColumnName columnName) {
        COPDRFColumn result = null;
        for (COPDRFColumn column : this) {
            if (column.getColumnName().equals(columnName)) {
                result = column;
                break;
            }
        }
        return result;
    }
    //endregion

    //region json
    public static COPDRFColumns fromJSONArray(JSONArray jsonArray) {
        COPDRFColumns result = null;
        List<JSONObject> list = JSON_STD.getListJSONObjectFromJSONArray(jsonArray);
        if (list != null) {
            result = new COPDRFColumns();
            for (JSONObject jsonObject : list) {
                result.add(COPDRFColumn.fromJSONObject(jsonObject));
            }
        }
        return result;
    }

    @Override
    public JSONArray toJSONArray() {
        return JSON_STD.getJSONArrayFromListJSONInterface(this);
    }
    //endregion
}
