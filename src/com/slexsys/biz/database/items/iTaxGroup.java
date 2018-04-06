package com.slexsys.biz.database.items;


import android.app.Activity;

import com.slexsys.biz.database.comp.Convert;
import com.slexsys.biz.database.comp.iIBase;
import com.slexsys.biz.database.sqls.SQLTables.SQLTablesTaxGroups;
import com.slexsys.biz.database.sqls.SQLQuerys;
import com.slexsys.biz.database.sqls.comp.QueryItemGroup;
import com.slexsys.biz.database.sqls.iSQL;
import com.slexsys.biz.main.comp.NewTypes.DataRow;
import com.slexsys.biz.main.comp.NewTypes.SmartActivity;

import java.util.List;

/**
 * Created by slexsys on 3/19/16.
 */
public class iTaxGroup extends iIBase {

    public double value;

    public iTaxGroup() { }

    public iTaxGroup(DataRow row) {
        Fill(row);
    }

    public void Fill(DataRow row) {
        SQLTablesTaxGroups sqlTables = iSQL.getSQLTables().getTaxGroups();
        super.Fill(row, sqlTables);

        value = row.getDouble(sqlTables.getValue());
    }

    @Override
    public String getAddQuery() {
        return super.getAddQuery();
    }

    @Override
    public String getEditQuery() {
        return super.getEditQuery();
    }

    @Override
    public String getDeleteQuery() {
        return super.getDeleteQuery();
    }

    @Override
    public boolean canDelete() {
        return false;
    }

    @Override
    public SmartActivity newItem(Activity activity, int gid) {
        return startNETGActivity(activity, gid, null);
    }

    @Override
    public SmartActivity editItem(Activity activity, int gid) {
        return startNETGActivity(activity, gid, this);
    }

    private SmartActivity startNETGActivity(Activity activity, int gid, iTaxGroup taxGroup) {
        /*NETaxGroup.item = taxGroup;
        NETaxGroup.groupid = gid;
        Intent intent = new Intent(context, NETaxGroup.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startActivity(intent);*/
        return new SmartActivity();
    }

    @Override
    public String[] getShowInfo() {
        return new String[] { this.name,
                "code " + this.code,
                Convert.ToString(this.value),
                null };
    }

    @Override
    public iIBase createObject(DataRow row) {
        return new iTaxGroup(row);
    }

    @Override
    public QueryItemGroup getItemQuerys() {
        return SQLQuerys.Edit.TaxGroups;
    }

    @Override
    public List<iIBase> getLikeList(String text) {
        return null;
    }
}
