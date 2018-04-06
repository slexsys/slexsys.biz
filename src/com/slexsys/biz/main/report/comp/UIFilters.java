package com.slexsys.biz.main.report.comp;

import com.slexsys.biz.database.sqls.comp.stdsql;

import java.io.Serializable;
import java.util.LinkedList;

/**
 * Created by slexsys on 2/25/17.
 */

public class UIFilters extends LinkedList<UIFilter> implements Serializable{
    //region fields
    private String mainTable;
    //endregion

    //region getters setters
    public String getMainTable() {
        return mainTable;
    }

    public void setMainTable(String mainTable) {
        this.mainTable = mainTable;
    }
    //endregion

    public UIFilter get(UIFilterType type) {
        UIFilter result = null;
        for (UIFilter item : this) {
            if (item.getType() == type) {
                result = item;
                break;
            }
        }
        return result;
    }

    public int sizeOfVisibles() {
        int size = 0;
        for (UIFilter filter : this) {
            if (!filter.isInvisible()) {
                size++;
            }
        }
        return size;
    }

    public UIFilter getOfVisibles(int position) {
        UIFilter result = null;
        int count = 0;
        for (UIFilter filter : this) {
            if (!filter.isInvisible()) {
                if (count == position) {
                    result = this.get(count);
                    break;
                }
                count++;
            }
        }
        return result;
    }

    public String getWhereFilters() {
        String result = "";
        for (UIFilter filter : this) {
            result += filter.getFilterField(mainTable);
        }
        result = stdsql.getFinalResult(result);
        return result;
    }
}
