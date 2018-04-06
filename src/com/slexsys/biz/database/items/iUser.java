package com.slexsys.biz.database.items;

import android.app.Activity;

import com.slexsys.biz.database.comp.comp.UserLevel;
import com.slexsys.biz.database.comp.iIBase;
import com.slexsys.biz.database.sqls.SQLTables.SQLTablesUsers;
import com.slexsys.biz.database.sqls.SQLQuerys;
import com.slexsys.biz.database.sqls.comp.QueryItemGroup;
import com.slexsys.biz.database.sqls.iSQL;
import com.slexsys.biz.main.comp.NewTypes.DataRow;
import com.slexsys.biz.main.comp.NewTypes.DataTable;
import com.slexsys.biz.main.comp.NewTypes.SmartActivity;
import com.slexsys.biz.main.edit.newedit.NEUser;

import java.util.LinkedList;
import java.util.List;

/**
 * Created by slexsys on 3/11/16.
 */
public class iUser extends iIBase {

    //region fields
    private String password;
    private String card;
    private UserLevel level;

    private static QueryItemGroup itemQuerys;
    //endregion

    //region constructors
    static {
        itemQuerys = SQLQuerys.Edit.Users;
    }

    public iUser() { }

    public iUser(DataRow row) {
        Fill(row);
    }
    //endregion

    //region override
    @Override
    public SmartActivity newItem(Activity activity, int gid) {
        return startNEUActivity(activity, gid, null);
    }

    @Override
    public SmartActivity editItem(Activity activity, int gid) {
        return startNEUActivity(activity, gid, this);
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
        query = query.replace("#password", password).
                replace("#card", card).
                replace("#level", Integer.toString(level.value()));
        return query;
    }

    @Override
    public String[] getShowInfo() {
        return new String[] { this.name,
                "code " + this.code,
                "level : " + this.level,
                ""};
    }

    @Override
    public iIBase createObject(DataRow row) {
        return new iUser(row);
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
            result.add(new iUser(dataTable.getRowWithNames(i)));
        }
        return result;
    }
    //endregion

    //region public methods
    public static iUser getByID(String id) {
        String query = itemQuerys.Item.SelectByID.replace("#id", id);
        DataRow row = iSQL.getDataRow(query);
        return new iUser(row);
    }
    //endregion

    //region private methods
    private SmartActivity startNEUActivity(Activity activity, int gid, iUser user) {
        NEUser neUser = new NEUser();
        if (user != null) {
            neUser.putExtra(NEUser.PUTTER_ITEM, user);
        } else {
            String maxCode = iSQL.getScalar(itemQuerys.Item.NextMaxCode);
            neUser.putExtra(NEUser.PUTTER_MAX_CODE, maxCode);
            neUser.putExtra(NEUser.PUTTER_GROUP_ID, gid);
        }
        neUser.show(activity);
        return neUser;
    }

    private void Fill(DataRow row) {
        SQLTablesUsers sqlTables = iSQL.getSQLTables().getUsers();
        super.Fill(row, sqlTables);

        password = row.getString(sqlTables.getPassword());
        card = row.getString(sqlTables.getCard());
        level = UserLevel.fromValue(row.getInt(sqlTables.getLevel()));
    }
    //endregion

    //region getters setters
    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getCard() {
        return card;
    }

    public void setCard(String card) {
        this.card = card;
    }

    public UserLevel getLevel() {
        return level;
    }

    public void setLevel(UserLevel level) {
        this.level = level;
    }
    //endregion
}

