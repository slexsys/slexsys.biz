package com.slexsys.biz.database.sqls.SQLTables;

/**
 * Created by slexsys on 12/1/17.
 */

public class SQLTables {
    //region fields
    private SQLTablesGroups groups;

    private SQLTablesPartners partners;
    private SQLTablesItems items;
    private SQLTablesObjects objects;
    private SQLTablesUsers users;

    private SQLTablesMeasures measures;
    private SQLTablesTaxGroups taxGroups;
    //endregion

    //region getters setters
    public SQLTablesGroups getGroups() {
        return groups;
    }

    public void setGroups(SQLTablesGroups groups) {
        this.groups = groups;
    }

    public SQLTablesPartners getPartners() {
        return partners;
    }

    public void setPartners(SQLTablesPartners partners) {
        this.partners = partners;
    }

    public SQLTablesItems getItems() {
        return items;
    }

    public void setItems(SQLTablesItems items) {
        this.items = items;
    }

    public SQLTablesObjects getObjects() {
        return objects;
    }

    public void setObjects(SQLTablesObjects objects) {
        this.objects = objects;
    }

    public SQLTablesUsers getUsers() {
        return users;
    }

    public void setUsers(SQLTablesUsers users) {
        this.users = users;
    }

    public SQLTablesMeasures getMeasures() {
        return measures;
    }

    public void setMeasures(SQLTablesMeasures measures) {
        this.measures = measures;
    }

    public SQLTablesTaxGroups getTaxGroups() {
        return taxGroups;
    }

    public void setTaxGroups(SQLTablesTaxGroups taxGroups) {
        this.taxGroups = taxGroups;
    }
    //endregion
}
