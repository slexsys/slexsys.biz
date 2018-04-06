package com.slexsys.biz.main.comp.NewTypes;

import android.database.Cursor;

import com.slexsys.biz.GO;
import com.slexsys.biz.database.comp.iIBase;
import com.slexsys.biz.database.items.iItem;

import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

/**
 * Created by slexsys on 2/1/17.
 */

public class DataTable extends LinkedList<DataRow>{
    private List<String> columnNames;
    private List<Integer> columnSizes;

    public DataTable() {
        columnNames = new LinkedList<String>();
        columnSizes = new LinkedList<Integer>();
    }

    public Object getCell(int column, int row) {
        return this.get(row).get(column);
    }

    public void setCell(int column, int row, Object item) {
        this.get(row).set(column, item);
    }

    public void addName(String name) {
        columnNames.add(name);
    }

    public void addNames(String[] names) {
        columnNames = Arrays.asList(names);
    }

    public String getName(int i) {
        return columnNames.get(i);
    }

    public void addSize(Integer size) {
        columnSizes.add(size);
    }

    public Integer getSize(int i) {
        return columnSizes.get(i);
    }

    public int getColumnsCount() {
        int count = 0;
        if (this.size() > 0) {
            count = this.get(0).size();
        }
        return count;
    }

    public void addRow(Cursor cursor) {
        this.add(new DataRow(cursor));
    }

    public List<String> getColumnNames() {
        return columnNames;
    }

    public DataRow getRowWithNames(int index) {
        DataRow row = get(index);
        row.setNames(columnNames);
        return row;
    }

    public DataRow getFirstWithNames() {
        return getRowWithNames(0);
    }
}
