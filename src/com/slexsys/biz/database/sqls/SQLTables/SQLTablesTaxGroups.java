package com.slexsys.biz.database.sqls.SQLTables;

/**
 * Created by slexsys on 12/2/17.
 */

public class SQLTablesTaxGroups extends SQLTablesiIBase {
    //region fields
    private String value;
    //endregion

    //region constructors
    public SQLTablesTaxGroups(SQLTablesiIBase sqlTablesiIBase) {
        super(sqlTablesiIBase);
    }
    //endregion

    //region getters setters
    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }
    //endregion
}
