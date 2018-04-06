package com.slexsys.biz.database.items;

import android.app.Activity;
import com.slexsys.biz.database.comp.Convert;
import com.slexsys.biz.database.comp.iIBase;
import com.slexsys.biz.database.sqls.SQLTables.SQLTablesMeasures;
import com.slexsys.biz.database.sqls.SQLQuerys;
import com.slexsys.biz.database.sqls.comp.QueryItemGroup;
import com.slexsys.biz.database.sqls.iSQL;
import com.slexsys.biz.main.comp.NewTypes.DataRow;
import com.slexsys.biz.main.comp.NewTypes.SmartActivity;

import java.util.List;

/**
 * Created by slexsys on 3/19/16.
 */
public class iMeasure extends iIBase {

    public iMeasure(){
    }

    public iMeasure(DataRow row){
        Fill(row);
    }

    public void Fill(DataRow row) {
        SQLTablesMeasures sqlTables = iSQL.getSQLTables().getMeasures();
        super.Fill(row, sqlTables);
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
        return startNEMActivity(activity, gid, null);
    }

    @Override
    public SmartActivity editItem(Activity activity, int gid) {
        return startNEMActivity(activity, gid, this);
    }

    private SmartActivity startNEMActivity(Activity activity, int gid, iMeasure measure) {
        /*NEMeasure.item = measure;
        NEMeasure.groupid = gid;
        Intent intent = new Intent(context, NEMeasure.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startActivity(intent);*/
        return null;
    }

    @Override
    public String[] getShowInfo() {
        return super.getShowInfo();
    }

    @Override
    public iIBase createObject(DataRow row) {
        return new iMeasure(row);
    }

    @Override
    public QueryItemGroup getItemQuerys() {
        return SQLQuerys.Edit.Measures1;////////////????????????
    }

    @Override
    public List<iIBase> getLikeList(String text) {
        return null;
    }
}
