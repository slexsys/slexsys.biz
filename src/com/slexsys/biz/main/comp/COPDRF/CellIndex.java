package com.slexsys.biz.main.comp.COPDRF;

import com.slexsys.biz.main.comp.json.JSON_STD;

import org.json.JSONObject;

import java.io.Serializable;

/**
 * Created by slexsys on 3/8/17.
 */

public class CellIndex implements Serializable{
    //region finals
    private static final int D1 = 1;
    private static final int D2 = 4;
    private static final int D3 = 3;
    private static final String JSON_NAME_D1 = "0";
    private static final String JSON_NAME_D2 = "1";
    private static final String JSON_NAME_D3 = "2";
    //endregion

    //region fields
    private int d1;
    private int d2;
    private int d3;
    //endregion

    //region constructors
    public CellIndex() { }

    public CellIndex(int d1, int d2, int d3) {
        this.d1 = d1;
        this.d2 = d2;
        this.d3 = d3;
    }
    //endregion

    //region getters setters
    public int getD1() {
        return d1;
    }

    public void setD1(int d1) {
        this.d1 = d1;
    }

    public int getD2() {
        return d2;
    }

    public void setD2(int d2) {
        this.d2 = d2;
    }

    public int getD3() {
        return d3;
    }

    public void setD3(int d3) {
        this.d3 = d3;
    }

    public static int getCD1() {
        return D1;
    }

    public static int getCD2() {
        return D2;
    }

    public static int getCD3() {
        return D3;
    }
    //endregion

    //region public methods
    public static CellIndex fromInts(int d1, int d2, int d3) {
        return new CellIndex(d1, d2, d3);
    }

    public CellIndex getNextCellIndex() {
        CellIndex result = new CellIndex();
        if (d3 != D3) {
            result.setD3(d3 + 1);
        } else if (d2 != D2) {
            result.setD3(0);
            result.setD2(d2 + 1);
        } else if (d1 != D1) {
            result.setD3(0);
            result.setD2(0);
            result.setD1(d1 + 1);
        } else {
            result.setD3(0);
            result.setD2(0);
            result.setD1(0);
        }
        return result;
    }
    //endregion

    //region json
    public static CellIndex fromJSON(JSONObject jsonObject) {
        int d1 = (Integer) JSON_STD.getObjectFromJSONObject(jsonObject, JSON_NAME_D1);
        int d2 = (Integer) JSON_STD.getObjectFromJSONObject(jsonObject, JSON_NAME_D2);
        int d3 = (Integer) JSON_STD.getObjectFromJSONObject(jsonObject, JSON_NAME_D3);
        return new CellIndex(d1, d2, d3);
    }

    public JSONObject toJSON() {
        JSONObject result = new JSONObject();
        JSON_STD.putJSONObject(result, d1, JSON_NAME_D1);
        JSON_STD.putJSONObject(result, d2, JSON_NAME_D2);
        JSON_STD.putJSONObject(result, d3, JSON_NAME_D3);
        return result;
    }
    //endregion
}
