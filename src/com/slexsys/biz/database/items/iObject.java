package com.slexsys.biz.database.items;

import android.app.Activity;

import com.slexsys.biz.database.comp.comp.PriceGroups;
import com.slexsys.biz.database.comp.iIBase;
import com.slexsys.biz.database.sqls.SQLTables.SQLTablesObjects;
import com.slexsys.biz.database.sqls.SQLQuerys;
import com.slexsys.biz.database.sqls.comp.QueryItemGroup;
import com.slexsys.biz.database.sqls.iSQL;
import com.slexsys.biz.main.comp.NewTypes.DataRow;
import com.slexsys.biz.main.comp.NewTypes.DataTable;
import com.slexsys.biz.main.comp.NewTypes.SmartActivity;
import com.slexsys.biz.main.edit.newedit.NEObject;

import java.util.LinkedList;
import java.util.List;

/**
 * Created by slexsys on 3/11/16.
 */
public class iObject extends iIBase {
    //region fields
    private PriceGroups pricegroup;

    private static QueryItemGroup itemQuerys;
    //endregion

    //region constructors
    static {
        itemQuerys = SQLQuerys.Edit.Objects;
    }

    public iObject() { }

    public iObject(DataRow row) {
        Fill(row);
    }
    //endregion

    //region override
    @Override
    public SmartActivity newItem(Activity activity, int gid) {
        return startNEOActivity(activity, gid, null);
    }

    @Override
    public SmartActivity editItem(Activity activity, int gid) {
        return startNEOActivity(activity, gid, this);
    }

    @Override
    public String getAddQuery() {
        String query = itemQuerys.Item.Insert;
        return getReplacedString(query);
    }

    @Override
    public String getEditQuery() {
        String query = itemQuerys.Item.Update;
        return getReplacedString(query);
    }

    @Override
    public String getDeleteQuery() {
        String query = itemQuerys.Item.Delete;
        return getReplacedString(query);
    }

    @Override
    public boolean canDelete() {
        return false;
    }

    protected String getReplacedString(String querytemplate){
        String query = super.getReplacedString(querytemplate);
        query = query.replace("#pricegroup", Integer.toString(pricegroup.value()));
        return query;
    }

    @Override
    public String[] getShowInfo() {
        return new String[] { name,
                "code " + code,
                pricegroup.toString(),
                ""};
    }

    @Override
    public iIBase createObject(DataRow row) {
        return new iObject(row);
    }

    @Override
    public QueryItemGroup getItemQuerys() {
        return itemQuerys;
    }

    @Override
    public String getItemLike(String text) {
        return itemQuerys.Item.Like.replace("#text", text);
    }

    @Override
    public String getItemTableName() {
        return itemQuerys.Group.TableName;
    }

    @Override
    public List<iIBase> getLikeList(String text) {
        String query = itemQuerys.Item.Select + " WHERE " +
                itemQuerys.Item.Like.replace("#text", text);
        List<iIBase> result = new LinkedList<iIBase>();
        DataTable dataTable = iSQL.getDataTable(query);
        for (int i = 0; i < dataTable.size(); ++i) {
            result.add(new iObject(dataTable.getRowWithNames(i)));
        }
        return result;
    }
    //endregion

    //region public methods
    public static iObject getByID(String id) {
        String query = itemQuerys.Item.SelectByID.replace("#id", id);
        DataRow row = iSQL.getDataRow(query);
        return new iObject(row);
    }
    //endregion

    //region private methods
    private void Fill(DataRow row) {
        SQLTablesObjects sqlTables = iSQL.getSQLTables().getObjects();
        super.Fill(row, sqlTables);

        pricegroup = PriceGroups.fromValue(row.getInt(sqlTables.getPriceGroup()));
    }

    private SmartActivity startNEOActivity(Activity activity, int gid, iObject object) {
        NEObject neObject = new NEObject();
        if (object != null) {
            neObject.putExtra(NEObject.PUTTER_ITEM, object);
        } else {
            String maxCode = iSQL.getScalar(itemQuerys.Item.NextMaxCode);
            neObject.putExtra(NEObject.PUTTER_MAX_CODE, maxCode);
            neObject.putExtra(NEObject.PUTTER_GROUP_ID, gid);
        }
        neObject.show(activity);
        return neObject;
    }
    //endregion

    //region getters setters
    public PriceGroups getPricegroup() {
        return pricegroup;
    }

    public void setPricegroup(PriceGroups pricegroup) {
        this.pricegroup = pricegroup;
    }
    //endregion
}

