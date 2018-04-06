package com.slexsys.biz;

import com.slexsys.biz.database.comp.*;
import com.slexsys.biz.database.items.*;
import com.slexsys.biz.database.sqls.MySQL.MySQLInfo;
import com.slexsys.biz.database.sqls.iSQL;
import com.slexsys.biz.main.operations.json.OperationsJSON;
import com.slexsys.biz.main.report.json.ReportsJSON;

/**
 * Created by slexsys on 3/25/16.
 */
public class GO {
    //region fields
    public static iItems<iObject> objects;
    public static iItems<iUser> users;

    public static iItems<iMeasure> measure1;
    public static iItems<iMeasure> measure2;
    public static iItems<iTaxGroup> taxgroups;
    public static iItems<iTaxGroup> currencies;

    public static OperationsJSON operations;
    public static ReportsJSON reports;

    private static int pig;
    private static int pog;
    private static int currencyId;
    //endregion

    //region methods
    static {
        objects = new iItems<iObject>(new iObject());
        users = new iItems<iUser>(new iUser());

        measure1 = new iItems<iMeasure>(new iMeasure());
        measure2 = new iItems<iMeasure>(new iMeasure());
        taxgroups = new iItems<iTaxGroup>(new iTaxGroup());
        currencies = new iItems<iTaxGroup>(new iTaxGroup());
    }

    public static void Fill(String file) {
        iSQL.Init(file);
        Fill();
    }

    public static void Fill(MySQLInfo info) {
        iSQL.Init(info);
        Fill();
    }

    private static void Fill() {
        GO.objects.Fill();
        GO.users.Fill();
        GO.measure1.Fill();
        GO.measure2.Fill();
        GO.taxgroups.Fill();
        GO.currencies.Fill();
        GO.operations = iSQL.getOperationsJSON();
        GO.reports = iSQL.getReportsJSON();
    }
    //endregion

    //region getters setters
    public static int getPig() {
        return pig;
    }

    public static void setPig(int pig) {
        GO.pig = pig;
    }

    public static int getPog() {
        return pog;
    }

    public static void setPog(int pog) {
        GO.pog = pog;
    }

    public static int getCurrencyId() {
        return currencyId;
    }

    public static void setCurrencyId(int currencyId) {
        GO.currencyId = currencyId;
    }
    //endregion
}
