package com.slexsys.biz.database.comp.comp;

import com.slexsys.biz.database.comp.iIBase;
import com.slexsys.biz.database.sqls.comp.QueryItemGroup;
import com.slexsys.biz.main.comp.NewTypes.DataRow;

import java.util.List;

/**
 * Created by slexsys on 12/21/17.
 */

public interface BaseMethods {
    iIBase createObject(DataRow row);
    QueryItemGroup getItemQuerys();
    List<iIBase> getLikeList(String text);
}
