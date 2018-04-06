package com.slexsys.biz.database.sqls.MySQL;

/**
 * Created by slexsys on 12/14/17.
 */

public class MySQLInfo {
    //region fields
    private String server = "slexsysnotebook";
    private String port = "3306";
    private String database = "database";
    private String user = "root";
    private String password = "192.168.1.1";
    //endregion

    //region constructors
    public MySQLInfo() { }

    public MySQLInfo(String server, String database, String user, String password) {
        this.server = server;
        this.database = database;
        this.user = user;
        this.password = password;
    }

    public MySQLInfo(String server, String port, String database, String user, String password) {
        this.server = server;
        this.port = port;
        this.database = database;
        this.user = user;
        this.password = password;
    }
    //endregion

    //region getters setters
    public String getServer() {
        return server;
    }

    public void setServer(String server) {
        this.server = server;
    }

    public String getDatabase() {
        return database;
    }

    public void setDatabase(String database) {
        this.database = database;
    }

    public String getUser() {
        return user;
    }

    public void setUser(String user) {
        this.user = user;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
    //endregion

    //region public methods
    public String getUrl() {
        return "jdbc:mysql://" + server +
                ":" + port +
                "/" + database +
                "?user=" + user +
                "&password=" + password +
                "&useUnicode=true&characterEncoding=utf-8";
    }
    //endregion
}
