package com.slexsys.biz.main.report.json;

import com.slexsys.biz.main.comp.COPDRF.CellIndex;
import com.slexsys.biz.comp.GlobalFinals;
import com.slexsys.biz.main.comp.json.JSON_STD;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by slexsys on 2/4/17.
 */

public class ReportColumn {
    //region finals
    private static final String JSON_NAME_NAME = "name";
    private static final String JSON_NAME_SIZE = "size";
    private static final String JSON_NAME_ISTOTAL = "isTotal";
    private static final String JSON_NAME_FONT_SIZE = "fontSize";
    private static final String JSON_NAME_FONT_COLOR = "fontColor";
    private static final String JSON_NAME_INDEX = "index";
    //endregion

    //region fields
    private String name;
    private int size;
    private boolean isTotal;
    private float fontSize;
    private int fontColor;
    private CellIndex cellIndex;
    //endregion

    //region constructors
    public ReportColumn() {
        fontSize = GlobalFinals.DEFAULT_FONT_SIZE;
        fontColor = GlobalFinals.DEFAULT_FONT_COLOR;
    }

    public ReportColumn(String name, int size, float fontSize, int fontColor, CellIndex cellIndex, boolean isTotal) {
        this.name = name;
        this.size = size;
        this.fontSize = fontSize;
        this.fontColor = fontColor;
        this.isTotal = isTotal;
        this.cellIndex = cellIndex;
    }
    //endregion

    // region getters setters

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getSize() {
        return size;
    }

    public void setSize(int size) {
        this.size = size;
    }

    public float getFontSize() {
        return fontSize;
    }

    public void setFontSize(float fontSize) {
        this.fontSize = fontSize;
    }

    public int getFontColor() {
        return fontColor;
    }

    public void setFontColor(int fontColor) {
        this.fontColor = fontColor;
    }

    public CellIndex getCellIndex() {
        return cellIndex;
    }

    public void setCellIndex(CellIndex cellIndex) {
        this.cellIndex = cellIndex;
    }

    public boolean isTotal() {
        return isTotal;
    }

    public void setTotal(boolean total) {
        isTotal = total;
    }
    //endregion

    //region json
    public static ReportColumn fromJSON(JSONObject jsonObject) {
        ReportColumn result = null;
        if (jsonObject != null) {

            try {
                String name = "";
                if (!jsonObject.isNull(JSON_NAME_NAME)) {
                    name = jsonObject.getString(JSON_NAME_NAME);
                }
                int size = 0;
                if (!jsonObject.isNull(JSON_NAME_SIZE)) {
                    size = jsonObject.getInt(JSON_NAME_SIZE);
                }
                float fontSize = GlobalFinals.DEFAULT_FONT_SIZE;
                if (!jsonObject.isNull(JSON_NAME_FONT_SIZE)) {
                    fontSize = (float) jsonObject.getDouble(JSON_NAME_FONT_SIZE);
                }
                int fontColor = GlobalFinals.DEFAULT_FONT_COLOR;
                if (!jsonObject.isNull(JSON_NAME_FONT_COLOR)) {
                    fontColor = jsonObject.getInt(JSON_NAME_FONT_COLOR);
                }
                CellIndex cellIndex = CellIndex.fromJSON(JSON_STD.getInnerJSONObject(jsonObject, JSON_NAME_INDEX));
                boolean isTotal = false;
                if (!jsonObject.isNull(JSON_NAME_INDEX)) {
                    isTotal = jsonObject.getBoolean(JSON_NAME_ISTOTAL);
                }
                result = new ReportColumn(name, size, fontSize, fontColor, cellIndex, isTotal);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        return result;
    }

    public JSONObject toJSON() {
        JSONObject result = null;
        try {
            result = new JSONObject();
            if (name != null && name != "") {
                result.put(JSON_NAME_NAME, name);
            }
            result.put(JSON_NAME_SIZE, size);
            result.put(JSON_NAME_FONT_SIZE, fontSize);
            result.put(JSON_NAME_FONT_COLOR, fontColor);
            JSON_STD.putJSONObject(result, cellIndex, JSON_NAME_INDEX);
            result.put(JSON_NAME_ISTOTAL, isTotal);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return result;
    }
    //endregion
}
