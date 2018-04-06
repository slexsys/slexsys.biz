package com.slexsys.biz.main.report.json;

import com.slexsys.biz.main.comp.COPDRF.CellIndex;
import com.slexsys.biz.main.report.comp.UIFilterType;

import java.util.Arrays;

/**
 * Created by slexsys on 2/4/17.
 */

public class ReportGenerator {
    public static ReportJSON GenerateReportOperationMySQL() {
        String join = "JOIN partners ON partners.id = operations.partnerid LEFT JOIN partnersgroups ON partners.groupid = partnersgroups.id JOIN goods ON goods.id = operations.goodid LEFT JOIN goodsgroups ON goods.groupid = goodsgroups.id JOIN objects ON objects.id = operations.objectid LEFT JOIN objectsgroups ON objects.groupid = objectsgroups.id JOIN users ON users.id = operations.userid LEFT JOIN usersgroups ON users.groupid = usersgroups.id JOIN operationtype ON operationtype.id = operations.opertype";
        String querylinestext = "SELECT date(operations.userrealtime) AS date, time(operations.userrealtime) AS time, operations.acct, operations.goodid, goods.code, goods.name, goods.groupid, goodsgroups.name AS `group`, 0 AS measureid, 0 AS measure, operations.qtty, operations.discount, operations.pricein, operations.qtty * operations.pricein AS amountin, operations.priceout, operations.qtty * operations.priceout AS amountout, (operations.priceout - operations.pricein) * operations.qtty AS amountprofit, operations.partnerid, partners.code, partners.company AS partner, operations.userid, users.code, users.name AS user, operations.objectid, objects.code, objects.name AS object, operations.opertype, operationtype.ru AS operation FROM operations #join #where";
        querylinestext = querylinestext.replace("#join", join);

        ReportColumns columns = new ReportColumns();
        columns.add(new ReportColumn(){{setName("date"); setSize(70); setFontSize(20); setFontColor(0xFFBBAACC); setCellIndex(CellIndex.fromInts(0, 0, 0));}});
        columns.add(new ReportColumn(){{setName("time"); setSize(70); setFontSize(20); setFontColor(0xFFBB00CC); setCellIndex(CellIndex.fromInts(0, 0, 1));}});
        columns.add(new ReportColumn(){{setName("acct"); setSize(50); setCellIndex(CellIndex.fromInts(0, 0, 2));}});
        columns.add(new ReportColumn(){{setName("goodid");}});
        columns.add(new ReportColumn(){{setName("code"); setSize(50); setCellIndex(CellIndex.fromInts(0, 1, 0));}}); //item code
        columns.add(new ReportColumn(){{setName("name"); setSize(20); setCellIndex(CellIndex.fromInts(0, 1, 1));}});
        columns.add(new ReportColumn(){{setName("groupid");}});
        columns.add(new ReportColumn(){{setName("group"); setSize(100); setCellIndex(CellIndex.fromInts(0, 1, 3));}});
        columns.add(new ReportColumn(){{setName("measureid");}});
        columns.add(new ReportColumn(){{setName("measure"); setSize(30); setCellIndex(CellIndex.fromInts(0, 2, 0));}});
        columns.add(new ReportColumn(){{setName("qtty"); setSize(50); setTotal(true); setCellIndex(CellIndex.fromInts(0, 2, 1));}});
        columns.add(new ReportColumn(){{setName("discount"); setSize(50); setCellIndex(CellIndex.fromInts(0, 2, 3));}});
        columns.add(new ReportColumn(){{setName("pricein"); setSize(70); setCellIndex(CellIndex.fromInts(0, 3, 0));}});
        columns.add(new ReportColumn(){{setName("amountin"); setSize(80); setTotal(true); setCellIndex(CellIndex.fromInts(0, 3, 1));}});
        columns.add(new ReportColumn(){{setName("priceout"); setSize(70); setCellIndex(CellIndex.fromInts(0, 4, 0));}});
        columns.add(new ReportColumn(){{setName("amountout"); setSize(80); setTotal(true); setCellIndex(CellIndex.fromInts(0, 4, 1));}});
        columns.add(new ReportColumn(){{setName("amountprofit"); setSize(80); setTotal(true); setCellIndex(CellIndex.fromInts(0, 3, 3));}});
        columns.add(new ReportColumn(){{setName("partnerid");}});
        columns.add(new ReportColumn(){{setName("code"); setSize(50); setCellIndex(CellIndex.fromInts(1, 0, 0));}}); //partner code
        columns.add(new ReportColumn(){{setName("partner"); setSize(100); setCellIndex(CellIndex.fromInts(1, 0, 1));}});
        columns.add(new ReportColumn(){{setName("userid");}});
        columns.add(new ReportColumn(){{setName("code"); setSize(50); setCellIndex(CellIndex.fromInts(1, 1, 0));}}); //user code
        columns.add(new ReportColumn(){{setName("user"); setSize(100); setCellIndex(CellIndex.fromInts(1, 1, 1));}});
        columns.add(new ReportColumn(){{setName("objectid");}});
        columns.add(new ReportColumn(){{setName("code"); setSize(50); setCellIndex(CellIndex.fromInts(1, 2, 0));}}); //object code
        columns.add(new ReportColumn(){{setName("object"); setSize(100); setCellIndex(CellIndex.fromInts(1, 2, 1));}});
        columns.add(new ReportColumn(){{setName("opertype");}});
        columns.add(new ReportColumn(){{setName("operation"); setSize(100); setCellIndex(CellIndex.fromInts(1, 4, 3));}});


        ReportFilters filters = new ReportFilters();
        filters.add(new ReportFilter("operations.date", ">=", UIFilterType.StartDate));
        filters.add(new ReportFilter("operations.date", "<=", UIFilterType.EndDate));

        filters.add(new ReportFilter("operations.partnerid", "=", UIFilterType.Partner));
        filters.add(new ReportFilter("operations.itemid", "=", UIFilterType.Item));
        filters.add(new ReportFilter("operations.objectid", "=", UIFilterType.Object));
        filters.add(new ReportFilter("operations.userid", "=", UIFilterType.User));

        filters.add(new ReportFilter("operations.acct", ">=", UIFilterType.StartAcct));
        filters.add(new ReportFilter("operations.acct", "<=", UIFilterType.EndAcct));
        filters.add(new ReportFilter("operations.qtty", "=", UIFilterType.Qtty));
        filters.add(new ReportFilter(Arrays.asList("operations.pricein", "operations.priceout"), "=", UIFilterType.Price));
        filters.add(new ReportFilter("operations.opertype", "=", UIFilterType.OperTypeAll));
        //filters.add(new ReportFilter("operations.paytype", "=", UIFilterType.PayType));
        filters.add(new ReportFilter("measure", "=", UIFilterType.Measure1));
        filters.add(new ReportFilter("measure2", "=", UIFilterType.Measure2));

        ReportQuery querylines = new ReportQuery(querylinestext, null, columns);

        ReportQuerys queryslines = new ReportQuerys();
        queryslines.setFilters(filters);
        queryslines.add(querylines);

        String querytotaltext1 = "SELECT MIN(operations.date) AS StartDate, MAX(operations.date) AS EndDate, SUM(operations.qtty) AS Qttys, SUM(operations.qtty * operations.pricein) AS amountin, SUM(operations.qtty * operations.priceout) AS amountout, SUM((operations.priceout - operations.pricein) * operations.qtty) AS amountprofit FROM operations #join #where";
        querytotaltext1 = querytotaltext1.replace("#join", join);
        ReportQuery querytotal1 = new ReportQuery(querytotaltext1);
        String querytotaltext2 = "SELECT users.name, SUM(operations.qtty) AS Qttys, SUM(operations.qtty * operations.pricein) AS amountin, SUM(operations.qtty * operations.priceout) AS amountout, SUM((operations.priceout - operations.pricein) * operations.qtty) AS amountprofit FROM operations #join #where GROUP BY operations.userid";
        querytotaltext2 = querytotaltext2.replace("#join", join);
        ReportQuery querytotal2 = new ReportQuery(querytotaltext2);
        String querytotaltext3 = "SELECT objects.name, SUM(operations.qtty) AS Qttys, SUM(operations.qtty * operations.pricein) AS amountin, SUM(operations.qtty * operations.priceout) AS amountout, SUM((operations.priceout - operations.pricein) * operations.qtty) AS amountprofit FROM operations #join #where GROUP BY operations.objectid";
        querytotaltext3 = querytotaltext3.replace("#join", join);
        ReportQuery querytotal3 = new ReportQuery(querytotaltext3);

        ReportQuerys querystotals = new ReportQuerys();
        querystotals.setFilters(filters);
        querystotals.add(querytotal1);
        querystotals.add(querytotal2);
        querystotals.add(querytotal3);

        ReportJSON reportJSON = new ReportJSON("Report.Operations", querystotals, queryslines);
        return reportJSON;
    }

    public static ReportJSON GenerateReportItemInStockMySQL() {
        String join = //"JOIN partners ON partners.id = items.partnerid " +
                        "JOIN goods ON goods.id = store.goodid " +
                        "LEFT JOIN goodsgroups ON goods.groupid = goodsgroups.id " +
                        //"JOIN measures ON items.measure = measures.id " +
                        "JOIN objects ON objects.id = store.objectid " +
                        "LEFT JOIN objectsgroups ON objects.groupid = objectsgroups.id";
        String querylinestext = "SELECT goods.id, goods.code, goods.name, goods.groupid, goodsgroups.name, goods.catalog2, 0 AS measures, qtty, goods.pricein, (qtty * goods.pricein) AS amountin, goods.priceout2, (qtty * goods.priceout2) AS amountout, (qtty * (goods.priceout2 - goods.pricein)) AS profit, goods.description, objects.id, objects.name AS object FROM store #join #where";
        querylinestext = querylinestext.replace("#join", join);

        ReportColumns columns = new ReportColumns();
        columns.add(new ReportColumn(){{setName("goodid");}});
        columns.add(new ReportColumn(){{setName("code"); setSize(50); setCellIndex(CellIndex.fromInts(0, 1, 0));}}); //item code
        columns.add(new ReportColumn(){{setName("name"); setSize(20); setCellIndex(CellIndex.fromInts(0, 1, 1));}});
        columns.add(new ReportColumn(){{setName("groupid");}});
        columns.add(new ReportColumn(){{setName("group"); setSize(100); setCellIndex(CellIndex.fromInts(0, 1, 3));}});
        columns.add(new ReportColumn(){{setName("catalog"); setSize(70); setCellIndex(CellIndex.fromInts(0, 2, 0));}});
        columns.add(new ReportColumn(){{setName("measure"); setSize(30); setCellIndex(CellIndex.fromInts(0, 2, 0));}});
        columns.add(new ReportColumn(){{setName("qtty"); setSize(50); setTotal(true); setCellIndex(CellIndex.fromInts(0, 2, 1));}});
        columns.add(new ReportColumn(){{setName("pricein"); setSize(70); setCellIndex(CellIndex.fromInts(0, 3, 0));}});
        columns.add(new ReportColumn(){{setName("amountin"); setSize(80); setTotal(true); setCellIndex(CellIndex.fromInts(0, 3, 1));}});
        columns.add(new ReportColumn(){{setName("priceout"); setSize(70); setCellIndex(CellIndex.fromInts(0, 4, 0));}});
        columns.add(new ReportColumn(){{setName("amountout"); setSize(80); setTotal(true); setCellIndex(CellIndex.fromInts(0, 4, 1));}});
        columns.add(new ReportColumn(){{setName("amountprofit"); setSize(80); setTotal(true); setCellIndex(CellIndex.fromInts(0, 3, 3));}});
        columns.add(new ReportColumn(){{setName("description"); setSize(100); setCellIndex(CellIndex.fromInts(1, 0, 1));}});
        columns.add(new ReportColumn(){{setName("objectid");}});
        columns.add(new ReportColumn(){{setName("code"); setSize(50); setCellIndex(CellIndex.fromInts(1, 2, 0));}}); //object code
        columns.add(new ReportColumn(){{setName("object"); setSize(100); setCellIndex(CellIndex.fromInts(1, 2, 1));}});


        ReportFilters filters = new ReportFilters();
        //filters.add(new ReportFilter("items.partnerid", "=", UIFilterType.Partner));
        filters.add(new ReportFilter("store.goodid", "=", UIFilterType.Item));
        filters.add(new ReportFilter("store.objectid", "=", UIFilterType.Object));

        filters.add(new ReportFilter("store.qtty", "=", UIFilterType.Qtty));
        filters.add(new ReportFilter(Arrays.asList("goods.pricein", "items.priceout"), "=", UIFilterType.Price));
        filters.add(new ReportFilter("measure", "=", UIFilterType.Measure1));
        filters.add(new ReportFilter("measure2", "=", UIFilterType.Measure2));

        ReportQuery querylines = new ReportQuery(querylinestext, null, columns);

        ReportQuerys queryslines = new ReportQuerys();
        queryslines.setFilters(filters);
        queryslines.add(querylines);

        String querytotaltext1 = "SELECT COUNT(*) AS count, SUM(qtty) AS Qttys, SUM(qtty * goods.pricein) AS amountin, SUM(qtty * goods.priceout2) AS amountout, SUM((goods.priceout2 - goods.pricein) * qtty) AS amountprofit FROM store #join #where";
        querytotaltext1 = querytotaltext1.replace("#join", join);
        ReportQuery querytotal1 = new ReportQuery(querytotaltext1);
        String querytotaltext2 = "SELECT objects.name, COUNT(*) AS count, SUM(qtty) AS Qttys, SUM(qtty * goods.pricein) AS amountin, SUM(qtty * goods.priceout2) AS amountout, SUM((goods.priceout2 - goods.pricein) * qtty) AS amountprofit FROM store #join #where GROUP BY store.objectid";
        querytotaltext2 = querytotaltext2.replace("#join", join);
        ReportQuery querytotal2 = new ReportQuery(querytotaltext2);

        ReportQuerys querystotals = new ReportQuerys();
        querystotals.setFilters(filters);
        querystotals.add(querytotal1);
        querystotals.add(querytotal2);

        ReportJSON reportJSON = new ReportJSON("Report.Item.ItemsInStock", querystotals, queryslines);
        return reportJSON;
    }

    public static ReportsJSON getReportsMySQL() {
        return new ReportsJSON(){{
            add(GenerateReportOperationMySQL());
            add(GenerateReportItemInStockMySQL());
        }};
    }

    public static ReportJSON GenerateReportOperationSQLite() {
        String join = "JOIN partners ON partners.id = operations.partnerid JOIN partnergroups ON partners.groupid = partnergroups.id JOIN items ON items.id = operations.itemid JOIN itemgroups ON items.groupid = itemgroups.id JOIN measures ON items.measure = measures.id JOIN objects ON objects.id = operations.objectid JOIN objectgroups ON objects.groupid = objectgroups.id JOIN users ON users.id = operations.userid JOIN usergroups ON users.groupid = usergroups.id JOIN opertype ON opertype.id = operations.opertype";
        String querylinestext = "SELECT date(operations.userrealtime) AS date, time(operations.userrealtime) AS time, operations.acct, operations.itemid, items.code, items.name, items.groupid, itemgroups.name AS `group`, items.measure AS measureid, measures.name AS measure, operations.qtty, operations.discount, operations.pricein, operations.qtty * operations.pricein AS amountin, operations.priceout, operations.qtty * operations.priceout AS amountout, (operations.priceout - operations.pricein) * operations.qtty AS amountprofit, operations.partnerid, partners.code, partners.name AS partner, operations.userid, users.code, users.name AS user, operations.objectid, objects.code, objects.name AS object, operations.opertype, opertype.en AS operation FROM operations #join #where";
        querylinestext = querylinestext.replace("#join", join);

        ReportColumns columns = new ReportColumns();
        columns.add(new ReportColumn(){{setName("date"); setSize(70); setFontSize(20); setFontColor(0xFFBBAACC); setCellIndex(CellIndex.fromInts(0, 0, 0));}});
        columns.add(new ReportColumn(){{setName("time"); setSize(70); setFontSize(20); setFontColor(0xFFBB00CC); setCellIndex(CellIndex.fromInts(0, 0, 1));}});
        columns.add(new ReportColumn(){{setName("acct"); setSize(50); setCellIndex(CellIndex.fromInts(0, 0, 2));}});
        columns.add(new ReportColumn(){{setName("itemid");}});
        columns.add(new ReportColumn(){{setName("code"); setSize(50); setCellIndex(CellIndex.fromInts(0, 1, 0));}}); //item code
        columns.add(new ReportColumn(){{setName("name"); setSize(20); setCellIndex(CellIndex.fromInts(0, 1, 1));}});
        columns.add(new ReportColumn(){{setName("groupid");}});
        columns.add(new ReportColumn(){{setName("group"); setSize(100); setCellIndex(CellIndex.fromInts(0, 1, 3));}});
        columns.add(new ReportColumn(){{setName("measureid");}});
        columns.add(new ReportColumn(){{setName("measure"); setSize(30); setCellIndex(CellIndex.fromInts(0, 2, 0));}});
        columns.add(new ReportColumn(){{setName("qtty"); setSize(50); setTotal(true); setCellIndex(CellIndex.fromInts(0, 2, 1));}});
        columns.add(new ReportColumn(){{setName("discount"); setSize(50); setCellIndex(CellIndex.fromInts(0, 2, 3));}});
        columns.add(new ReportColumn(){{setName("pricein"); setSize(70); setCellIndex(CellIndex.fromInts(0, 3, 0));}});
        columns.add(new ReportColumn(){{setName("amountin"); setSize(80); setTotal(true); setCellIndex(CellIndex.fromInts(0, 3, 1));}});
        columns.add(new ReportColumn(){{setName("priceout"); setSize(70); setCellIndex(CellIndex.fromInts(0, 4, 0));}});
        columns.add(new ReportColumn(){{setName("amountout"); setSize(80); setTotal(true); setCellIndex(CellIndex.fromInts(0, 4, 1));}});
        columns.add(new ReportColumn(){{setName("amountprofit"); setSize(80); setTotal(true); setCellIndex(CellIndex.fromInts(0, 3, 3));}});
        columns.add(new ReportColumn(){{setName("partnerid");}});
        columns.add(new ReportColumn(){{setName("code"); setSize(50); setCellIndex(CellIndex.fromInts(1, 0, 0));}}); //partner code
        columns.add(new ReportColumn(){{setName("partner"); setSize(100); setCellIndex(CellIndex.fromInts(1, 0, 1));}});
        columns.add(new ReportColumn(){{setName("userid");}});
        columns.add(new ReportColumn(){{setName("code"); setSize(50); setCellIndex(CellIndex.fromInts(1, 1, 0));}}); //user code
        columns.add(new ReportColumn(){{setName("user"); setSize(100); setCellIndex(CellIndex.fromInts(1, 1, 1));}});
        columns.add(new ReportColumn(){{setName("objectid");}});
        columns.add(new ReportColumn(){{setName("code"); setSize(50); setCellIndex(CellIndex.fromInts(1, 2, 0));}}); //object code
        columns.add(new ReportColumn(){{setName("object"); setSize(100); setCellIndex(CellIndex.fromInts(1, 2, 1));}});
        columns.add(new ReportColumn(){{setName("opertype");}});
        columns.add(new ReportColumn(){{setName("operation"); setSize(100); setCellIndex(CellIndex.fromInts(1, 4, 3));}});


        ReportFilters filters = new ReportFilters();
        filters.add(new ReportFilter("operations.date", ">=", UIFilterType.StartDate));
        filters.add(new ReportFilter("operations.date", "<=", UIFilterType.EndDate));

        filters.add(new ReportFilter("operations.partnerid", "=", UIFilterType.Partner));
        filters.add(new ReportFilter("operations.itemid", "=", UIFilterType.Item));
        filters.add(new ReportFilter("operations.objectid", "=", UIFilterType.Object));
        filters.add(new ReportFilter("operations.userid", "=", UIFilterType.User));

        filters.add(new ReportFilter("operations.acct", ">=", UIFilterType.StartAcct));
        filters.add(new ReportFilter("operations.acct", "<=", UIFilterType.EndAcct));
        filters.add(new ReportFilter("operations.qtty", "=", UIFilterType.Qtty));
        filters.add(new ReportFilter(Arrays.asList("operations.pricein", "operations.priceout"), "=", UIFilterType.Price));
        filters.add(new ReportFilter("operations.opertype", "=", UIFilterType.OperTypeAll));
        //filters.add(new ReportFilter("operations.paytype", "=", UIFilterType.PayType));
        filters.add(new ReportFilter("measure", "=", UIFilterType.Measure1));
        filters.add(new ReportFilter("measure2", "=", UIFilterType.Measure2));

        ReportQuery querylines = new ReportQuery(querylinestext, null, columns);

        ReportQuerys queryslines = new ReportQuerys();
        queryslines.setFilters(filters);
        queryslines.add(querylines);

        String querytotaltext1 = "SELECT MIN(operations.date) AS StartDate, MAX(operations.date) AS EndDate, SUM(operations.qtty) AS Qttys, SUM(operations.qtty * operations.pricein) AS amountin, SUM(operations.qtty * operations.priceout) AS amountout, SUM((operations.priceout - operations.pricein) * operations.qtty) AS amountprofit FROM operations #join #where";
        querytotaltext1 = querytotaltext1.replace("#join", join);
        ReportQuery querytotal1 = new ReportQuery(querytotaltext1);
        String querytotaltext2 = "SELECT users.name, SUM(operations.qtty) AS Qttys, SUM(operations.qtty * operations.pricein) AS amountin, SUM(operations.qtty * operations.priceout) AS amountout, SUM((operations.priceout - operations.pricein) * operations.qtty) AS amountprofit FROM operations #join #where GROUP BY operations.userid";
        querytotaltext2 = querytotaltext2.replace("#join", join);
        ReportQuery querytotal2 = new ReportQuery(querytotaltext2);
        String querytotaltext3 = "SELECT objects.name, SUM(operations.qtty) AS Qttys, SUM(operations.qtty * operations.pricein) AS amountin, SUM(operations.qtty * operations.priceout) AS amountout, SUM((operations.priceout - operations.pricein) * operations.qtty) AS amountprofit FROM operations #join #where GROUP BY operations.objectid";
        querytotaltext3 = querytotaltext3.replace("#join", join);
        ReportQuery querytotal3 = new ReportQuery(querytotaltext3);

        ReportQuerys querystotals = new ReportQuerys();
        querystotals.setFilters(filters);
        querystotals.add(querytotal1);
        querystotals.add(querytotal2);
        querystotals.add(querytotal3);

        ReportJSON reportJSON = new ReportJSON("Report.Operations", querystotals, queryslines);
        return reportJSON;
    }

    public static ReportJSON GenerateReportItemInStockSQLite() {
        String join = //"JOIN partners ON partners.id = items.partnerid " +
                "JOIN items ON items.id = store.itemid " +
                        "JOIN itemgroups ON items.groupid = itemgroups.id " +
                        "JOIN measures ON items.measure = measures.id " +
                        "JOIN objects ON objects.id = store.objectid " +
                        "JOIN objectgroups ON objects.groupid = objectgroups.id";
        String querylinestext = "SELECT items.id, items.code, items.name, items.groupid, itemgroups.name, items.catalog, measures.name AS measures, qtty, items.pricein, (qtty * items.pricein) AS amountin, items.priceout, (qtty * items.priceout) AS amountout, (qtty * (items.priceout - items.pricein)) AS profit, items.description, objects.id, objects.name AS object FROM store #join #where";
        querylinestext = querylinestext.replace("#join", join);

        ReportColumns columns = new ReportColumns();
        columns.add(new ReportColumn(){{setName("itemid");}});
        columns.add(new ReportColumn(){{setName("code"); setSize(50); setCellIndex(CellIndex.fromInts(0, 1, 0));}}); //item code
        columns.add(new ReportColumn(){{setName("name"); setSize(20); setCellIndex(CellIndex.fromInts(0, 1, 1));}});
        columns.add(new ReportColumn(){{setName("groupid");}});
        columns.add(new ReportColumn(){{setName("group"); setSize(100); setCellIndex(CellIndex.fromInts(0, 1, 3));}});
        columns.add(new ReportColumn(){{setName("catalog"); setSize(70); setCellIndex(CellIndex.fromInts(0, 2, 0));}});
        columns.add(new ReportColumn(){{setName("measure"); setSize(30); setCellIndex(CellIndex.fromInts(0, 2, 0));}});
        columns.add(new ReportColumn(){{setName("qtty"); setSize(50); setTotal(true); setCellIndex(CellIndex.fromInts(0, 2, 1));}});
        columns.add(new ReportColumn(){{setName("pricein"); setSize(70); setCellIndex(CellIndex.fromInts(0, 3, 0));}});
        columns.add(new ReportColumn(){{setName("amountin"); setSize(80); setTotal(true); setCellIndex(CellIndex.fromInts(0, 3, 1));}});
        columns.add(new ReportColumn(){{setName("priceout"); setSize(70); setCellIndex(CellIndex.fromInts(0, 4, 0));}});
        columns.add(new ReportColumn(){{setName("amountout"); setSize(80); setTotal(true); setCellIndex(CellIndex.fromInts(0, 4, 1));}});
        columns.add(new ReportColumn(){{setName("amountprofit"); setSize(80); setTotal(true); setCellIndex(CellIndex.fromInts(0, 3, 3));}});
        columns.add(new ReportColumn(){{setName("description"); setSize(100); setCellIndex(CellIndex.fromInts(1, 0, 1));}});
        columns.add(new ReportColumn(){{setName("objectid");}});
        columns.add(new ReportColumn(){{setName("code"); setSize(50); setCellIndex(CellIndex.fromInts(1, 2, 0));}}); //object code
        columns.add(new ReportColumn(){{setName("object"); setSize(100); setCellIndex(CellIndex.fromInts(1, 2, 1));}});


        ReportFilters filters = new ReportFilters();
        //filters.add(new ReportFilter("items.partnerid", "=", UIFilterType.Partner));
        filters.add(new ReportFilter("store.itemid", "=", UIFilterType.Item));
        filters.add(new ReportFilter("store.objectid", "=", UIFilterType.Object));

        filters.add(new ReportFilter("store.qtty", "=", UIFilterType.Qtty));
        filters.add(new ReportFilter(Arrays.asList("items.pricein", "items.priceout"), "=", UIFilterType.Price));
        filters.add(new ReportFilter("measure", "=", UIFilterType.Measure1));
        filters.add(new ReportFilter("measure2", "=", UIFilterType.Measure2));

        ReportQuery querylines = new ReportQuery(querylinestext, null, columns);

        ReportQuerys queryslines = new ReportQuerys();
        queryslines.setFilters(filters);
        queryslines.add(querylines);

        String querytotaltext1 = "SELECT COUNT(*) AS count, SUM(qtty) AS Qttys, SUM(qtty * items.pricein) AS amountin, SUM(qtty * items.priceout) AS amountout, SUM((items.priceout - items.pricein) * qtty) AS amountprofit FROM store #join #where";
        querytotaltext1 = querytotaltext1.replace("#join", join);
        ReportQuery querytotal1 = new ReportQuery(querytotaltext1);
        String querytotaltext2 = "SELECT objects.name, COUNT(*) AS count, SUM(qtty) AS Qttys, SUM(qtty * items.pricein) AS amountin, SUM(qtty * items.priceout) AS amountout, SUM((items.priceout - items.pricein) * qtty) AS amountprofit FROM store #join #where GROUP BY store.objectid";
        querytotaltext2 = querytotaltext2.replace("#join", join);
        ReportQuery querytotal2 = new ReportQuery(querytotaltext2);

        ReportQuerys querystotals = new ReportQuerys();
        querystotals.setFilters(filters);
        querystotals.add(querytotal1);
        querystotals.add(querytotal2);

        ReportJSON reportJSON = new ReportJSON("Report.Item.ItemsInStock", querystotals, queryslines);
        return reportJSON;
    }

    public static ReportsJSON getReportsSQLite() {
        return new ReportsJSON(){{
            add(GenerateReportOperationSQLite());
            add(GenerateReportItemInStockSQLite());
        }};
    }
}
