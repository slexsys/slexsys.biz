package com.slexsys.biz.database.sqls.SQLTables;

/**
 * Created by slexsys on 12/2/17.
 */

public class SQLTablesGroups extends SQLTablesiBase{
    //region fields
    private String firstSubGroupName;
    private String itemCount;
    //endregion

    //region constructors
    public SQLTablesGroups(SQLTablesiBase sqlTablesiBase) {
        super(sqlTablesiBase);
    }
    //endregion

    //region getters setters
    public String getFirstSubGroupName() {
        return firstSubGroupName;
    }

    public void setFirstSubGroupName(String firstSubGroupName) {
        this.firstSubGroupName = firstSubGroupName;
    }

    public String getItemCount() {
        return itemCount;
    }

    public void setItemCount(String itemCount) {
        this.itemCount = itemCount;
    }
    //endregion
}
