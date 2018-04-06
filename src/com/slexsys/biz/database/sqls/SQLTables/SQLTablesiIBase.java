package com.slexsys.biz.database.sqls.SQLTables;

/**
 * Created by slexsys on 12/2/17.
 */

public class SQLTablesiIBase extends SQLTablesiBase {
    //region fields
    private String groupid;
    private String deleted;
    private String isveryused;
    //endregion

    //region constructors
    public SQLTablesiIBase() { }

    public SQLTablesiIBase(SQLTablesiBase sqlTablesiBase) {
        super(sqlTablesiBase);
    }

    public SQLTablesiIBase(SQLTablesiIBase sqlTablesiIBase) {
        super(sqlTablesiIBase);
        this.groupid = sqlTablesiIBase.getGroupid();
        this.deleted = sqlTablesiIBase.getDeleted();
        this.isveryused = sqlTablesiIBase.getIsveryused();
    }
    //endregion

    //region getters setters
    public String getGroupid() {
        return groupid;
    }

    public void setGroupid(String groupid) {
        this.groupid = groupid;
    }

    public String getDeleted() {
        return deleted;
    }

    public void setDeleted(String deleted) {
        this.deleted = deleted;
    }

    public String getIsveryused() {
        return isveryused;
    }

    public void setIsveryused(String isveryused) {
        this.isveryused = isveryused;
    }
    //endregion
}
