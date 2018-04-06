package com.slexsys.biz.database.sqls;

import com.slexsys.biz.database.sqls.SQLTables.SQLTables;
import com.slexsys.biz.main.comp.NewTypes.DataTable;

import java.util.List;

/**
 * Created by slexsys on 12/19/17.
 */

public abstract class SQLWorker {

    public abstract boolean execQuerys(List<String> queries);
    public abstract boolean execQuery(final String query);
    public abstract String execScalar(String query);
    public abstract DataTable execReader(String query);
    public abstract SQLTables getSqlTables();
}
