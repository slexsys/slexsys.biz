package com.slexsys.biz.database.groups;

import android.app.Activity;

import com.slexsys.biz.database.comp.iBase;
import com.slexsys.biz.database.sqls.SQLTables.SQLTablesGroups;
import com.slexsys.biz.database.sqls.iSQL;
import com.slexsys.biz.main.comp.NewTypes.DataRow;
import com.slexsys.biz.main.comp.NewTypes.DataTable;
import com.slexsys.biz.main.comp.NewTypes.SmartActivity;

import java.io.Serializable;
import java.util.LinkedList;
import java.util.List;

/**
 * Created by slexsys on 3/11/16.
 */
public class iGroup extends iBase implements Serializable{
    //region fields
    private String firstSubGroupName;
    private int itemCount;
    //endregion

    public iGroup() { }

    public iGroup(DataRow row) {
        Fill(row);
    }

    public void Fill(DataRow row) {
        SQLTablesGroups sqlTables = iSQL.getSQLTables().getGroups();
        super.Fill(row, sqlTables);

        firstSubGroupName = row.getString(sqlTables.getFirstSubGroupName());
        itemCount = row.getInt(sqlTables.getItemCount());
    }

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
        return startNEGActivity(activity, gid, null);
    }

    @Override
    public SmartActivity editItem(Activity activity, int gid) {
        return startNEGActivity(activity, gid, this);
    }

    @Override
    public String[] getShowInfo() {
        return new String[] {
                name,
                null,
                firstSubGroupName,
                Integer.toString(itemCount)
        };
    }

    private SmartActivity startNEGActivity(Activity activity, int gid, iGroup group) {
        /*NEGroup.item = group;
        NEGroup.groupid = gid;
        Intent intent = new Intent(context, NEGroup.class);
        context.startActivity(intent);*/
        return new SmartActivity();
    }

    public static List<iGroup> getGroupList(String query) {
        List<iGroup> result = null;
        DataTable dataTable = iSQL.getDataTable(query);
        if (dataTable != null) {
            result = new LinkedList<iGroup>();
            for (int i = 0; i < dataTable.size(); ++i) {
                result.add(new iGroup(dataTable.getRowWithNames(i)));
            }
        }
        return result;
    }
}

