package com.slexsys.biz.database.sqls.comp;

/**
 * Created by slexsys on 1/16/17.
 */

public class QueryItemGroup {
    public QueryItemGroupInside Group;
    public QueryItemGroupInside Item;

    public QueryItemGroup() {
        Group = new QueryItemGroupInside();
        Item = new QueryItemGroupInside();
    }
}
