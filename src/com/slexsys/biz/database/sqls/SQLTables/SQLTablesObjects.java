package com.slexsys.biz.database.sqls.SQLTables;

/**
 * Created by slexsys on 12/2/17.
 */

public class SQLTablesObjects extends SQLTablesiIBase {
    //region fields
    private String priceGroup;
    //endregion

    //region constructors
    public SQLTablesObjects(SQLTablesiIBase sqlTablesiIBase) {
        super(sqlTablesiIBase);
    }
    //endregion

    //region getters setters
    public String getPriceGroup() {
        return priceGroup;
    }

    public void setPriceGroup(String priceGroup) {
        this.priceGroup = priceGroup;
    }
    //endregion
}
