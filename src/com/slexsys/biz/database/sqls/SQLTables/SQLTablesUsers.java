package com.slexsys.biz.database.sqls.SQLTables;

/**
 * Created by slexsys on 12/1/17.
 */

public class SQLTablesUsers extends SQLTablesiIBase {
    //region fields
    private String password;
    private String level;
    private String card;
    //endregion

    //region constructors
    public SQLTablesUsers(SQLTablesiIBase sqlTablesiIBase) {
        super(sqlTablesiIBase);
    }
    //endregion

    //region getters setters
    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getLevel() {
        return level;
    }

    public void setLevel(String level) {
        this.level = level;
    }

    public String getCard() {
        return card;
    }

    public void setCard(String card) {
        this.card = card;
    }
    //endregion
}
