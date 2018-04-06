package com.slexsys.biz.database.sqls.SQLTables;

/**
 * Created by slexsys on 12/2/17.
 */

public class SQLTablesiBase {
    //region fields
    private String id;
    private String code;
    private String name;
    private String name2;
    //endregion

    //region constructors
    public SQLTablesiBase() {
    }

    public SQLTablesiBase(SQLTablesiBase sqlTablesiBase) {
        this.id = sqlTablesiBase.getId();
        this.code = sqlTablesiBase.getCode();
        this.name = sqlTablesiBase.getName();
        this.name2 = sqlTablesiBase.getName2();
    }
    //endregion

    //region getters setters
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getName2() {
        return name2;
    }

    public void setName2(String name2) {
        this.name2 = name2;
    }
    //endregion
}
