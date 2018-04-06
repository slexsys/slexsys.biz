package com.slexsys.biz.main.comp.COPDRF;

import com.slexsys.biz.comp.GlobalFinals;

/**
 * Created by slexsys on 2/7/17.
 */

public class COPDRFCell {
    //region fields
    private float size;
    private int color;//0xAARRGGBB
    private Object value;
    private CellIndex cellIndex;
    private COPDRFColumnName columnName;
    //endregion

    //region constructors
    public COPDRFCell() {
        size = GlobalFinals.DEFAULT_FONT_SIZE;
        color = GlobalFinals.DEFAULT_FONT_COLOR;
    }

    public COPDRFCell(float size, int color, Object value) {
        this.size = size;
        this.color = color;
        this.value = value;
    }

    public COPDRFCell(Object value) {
        this();
        this.value = value;
    }

    public COPDRFCell(Object value, CellIndex cellIndex) {
        this(value);
        this.value = value;
        this.cellIndex = cellIndex;
    }

    public COPDRFCell(Object value, CellIndex cellIndex, COPDRFColumnName columnName) {
        this(value, cellIndex);
        this.columnName = columnName;
    }
    //endregion

    //region getters settere
    public float getSize() {
        return size;
    }

    public void setSize(float size) {
        this.size = size;
    }

    public int getColor() {
        return color;
    }

    public void setColor(int color) {
        this.color = color;
    }

    public Object getValue() {
        return value;
    }

    public void setValue(Object value) {
        this.value = value;
    }

    public CellIndex getCellIndex() {
        return cellIndex;
    }

    public void setCellIndex(CellIndex cellIndex) {
        this.cellIndex = cellIndex;
    }

    public COPDRFColumnName getColumnName() {
        return columnName;
    }

    public void setColumnName(COPDRFColumnName columnName) {
        this.columnName = columnName;
    }
    //endregion
}
