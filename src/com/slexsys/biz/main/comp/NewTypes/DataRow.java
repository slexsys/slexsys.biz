package com.slexsys.biz.main.comp.NewTypes;

import android.database.Cursor;

import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.LinkedList;
import java.util.List;

/**
 * Created by slexsys on 2/1/17.
 */
public class DataRow extends LinkedList<Object>{
    private List<String> names;

    public DataRow() { }

    public DataRow(Cursor cursor) {
        for (int i = 0; i < cursor.getColumnCount(); ++i) {
            this.add(cursor.getString(i));
        }
    }

    public DataRow(Object... objects) {
        for (int i = 0; i < objects.length; ++i) {
            this.add(objects[i]);
        }
    }

    public DataRow(ResultSet resultSet) {
        this();
        try {
            ResultSetMetaData rsmd = resultSet.getMetaData();
            for (int i = 1; i <= rsmd.getColumnCount(); ++i) {
                this.add(resultSet.getString(i));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public List<String> getNames() {
        return names;
    }

    public void setNames(List<String> names) {
        this.names = names;
    }

    public String getString(String name) {
        return getString(getIndex(name));
    }

    public String getString(int index) {
        return getValue(index);
    }

    public int getInt(String name) {
        return getInt(getIndex(name));
    }

    public int getInt(int index) {
        int result;
        try {
            result = Integer.parseInt(getValue(index));
        } catch (Exception ex) {
            result = 0;
        }
        return result;
    }

    public double getDouble(String name) {
        return getDouble(getIndex(name));
    }

    public double getDouble(int index) {
        return Double.parseDouble(getValue(index));
    }

    public boolean getBoolean(String name) {
        return getBoolean(getIndex(name));
    }

    public boolean getBoolean(int index) {
        boolean result;
        String value = this.get(index).toString();
        try {
            result = Boolean.parseBoolean(value);
        } catch (Exception ex) {
            try {
                result = getInt(index) != 0;
            } catch (Exception ex1) {
                result = false;
            }
        }
        return result;
    }

    public boolean isNameExist(String name) {
        return getIndex(name) != -1;
    }

    private int getIndex(String name) {
        return names.indexOf(name.toLowerCase());
    }

    private String getValue(int index) {
        return this.get(index).toString();
    }
}
