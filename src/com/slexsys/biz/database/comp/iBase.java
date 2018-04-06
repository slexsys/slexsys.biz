package com.slexsys.biz.database.comp;

import android.app.Activity;

import com.slexsys.biz.database.sqls.SQLTables.SQLTablesiBase;
import com.slexsys.biz.main.comp.NewTypes.DataRow;
import com.slexsys.biz.main.comp.NewTypes.SmartActivity;

import java.io.Serializable;

/**
 * Created by slexsys on 3/11/16.
 */
public abstract class iBase implements Comparable<iBase>, Serializable {
    //region fields
    protected int id;
    protected String code;
    protected String name;
    protected String name2;
    //endregion

    //region abstract methods
    public abstract String getAddQuery();
    public abstract String getEditQuery();
    public abstract String getDeleteQuery();
    public abstract boolean canDelete();
    public abstract SmartActivity newItem(Activity activity, int gid);
    public abstract SmartActivity editItem(Activity activity, int gid);
    public abstract String[] getShowInfo();
    //endregion

    //region override
    @Override
    public int compareTo(iBase ibase) {
        return name.compareTo(ibase.name);
    }

    @Override public boolean equals(Object other) {
        if (this == other)
            return true;
        if (other == null)
            return false;
        if (getClass() != other.getClass())
            return false;
        iBase otherx = (iBase) other;
        if (id != otherx.id)
            return false;
        return true;
    }

    @Override public int hashCode() {
        return id * 31; // same values should hash to the same number
    }
    //endregion

    //region inner methods
    protected void Fill(DataRow row, SQLTablesiBase sqlTablesiBase){
        id = row.getInt(sqlTablesiBase.getId());
        code = row.getString(sqlTablesiBase.getCode());
        name = row.getString(sqlTablesiBase.getName());
        name2 = row.getString(sqlTablesiBase.getName());
    }

    protected String getReplacedString(String querytemplate) {
        return querytemplate.replace("#id", Convert.ToString(id)).
                replace("#code", code).
                replace("#name2", name2).
                replace("#name", name);
    }
    //endregion

    //region getters setters
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getName2() {
        return name2;
    }

    public void setName2(String name2) {
        this.name2 = name2;
    }
    //endregion
}

