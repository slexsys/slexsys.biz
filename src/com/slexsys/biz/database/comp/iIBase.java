package com.slexsys.biz.database.comp;

import android.app.Activity;
import android.util.Log;

import com.slexsys.biz.database.comp.comp.BaseMethods;
import com.slexsys.biz.database.sqls.SQLTables.SQLTablesiIBase;
import com.slexsys.biz.database.sqls.comp.QueryItemGroup;
import com.slexsys.biz.main.comp.NewTypes.DataRow;
import com.slexsys.biz.main.comp.NewTypes.SmartActivity;

import java.io.Serializable;
import java.util.List;

/**
 * Created by slexsys on 3/11/16.
 */
public class iIBase extends iBase implements BaseMethods, Serializable {
    //region fields
    protected int groupid;
    protected boolean deleted;
    //endregion

    //region public methods
    public String getItemLike(String text) {
        return null;
    }

    public String getItemTableName() {
        return null;
    }
    //endregion

    //region inner methods
    protected void Fill(DataRow row, SQLTablesiIBase sqlTablesiIBase) {
        super.Fill(row, sqlTablesiIBase);
        groupid = row.getInt(sqlTablesiIBase.getGroupid());
        deleted = row.getBoolean(sqlTablesiIBase.getDeleted());
    }

    protected String getReplacedString(String querytemplate) {
        String query = super.getReplacedString(querytemplate);
        return query.replace("#groupid", Convert.ToString(groupid)).
                replace("#deleted", Convert.ToString(deleted));
    }
    //endregion

    //region override methods
    @Override
    public String getAddQuery() {
        return null;
    }

    @Override
    public String getEditQuery() {
        return null;
    }

    @Override
    public String getDeleteQuery() {
        return null;
    }

    @Override
    public boolean canDelete() {
        return false;
    }

    @Override
    public SmartActivity newItem(Activity activity, int gid) {
        Log.d("Error", "iIBase - newItem");
        return null;
    }

    @Override
    public SmartActivity editItem(Activity activity, int gid) {
        Log.d("Error", "iIBase - editItem");
        return null;
    }

    @Override
    public String[] getShowInfo() {
        return new String[] { this.name,
                "code " + this.code,
                null,
                null };
    }

    @Override
    public iIBase createObject(DataRow row) {
        return null;
    }

    @Override
    public QueryItemGroup getItemQuerys() {
        return null;
    }

    @Override
    public List<iIBase> getLikeList(String text) {
        return null;
    }
    //endregion

    //region getters setters
    public int getGroupid() {
        return groupid;
    }

    public void setGroupid(int groupid) {
        this.groupid = groupid;
    }

    public boolean getDeleted() {
        return deleted;
    }

    public void setDeleted(boolean deleted) {
        this.deleted = deleted;
    }
    //endregion
}
