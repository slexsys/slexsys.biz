package com.slexsys.biz.database.sqls.MySQL;

import android.app.Activity;

import com.slexsys.biz.database.sqls.SQLTables.SQLTables;
import com.slexsys.biz.database.sqls.SQLTables.SQLTablesGroups;
import com.slexsys.biz.database.sqls.SQLTables.SQLTablesItems;
import com.slexsys.biz.database.sqls.SQLTables.SQLTablesMeasures;
import com.slexsys.biz.database.sqls.SQLTables.SQLTablesObjects;
import com.slexsys.biz.database.sqls.SQLTables.SQLTablesPartners;
import com.slexsys.biz.database.sqls.SQLTables.SQLTablesTaxGroups;
import com.slexsys.biz.database.sqls.SQLTables.SQLTablesUsers;
import com.slexsys.biz.database.sqls.SQLTables.SQLTablesiBase;
import com.slexsys.biz.database.sqls.SQLTables.SQLTablesiIBase;
import com.slexsys.biz.database.sqls.SQLQuerys;
import com.slexsys.biz.database.sqls.SQLWorker;
import com.slexsys.biz.database.sqls.comp.QueryItemGroup;
import com.slexsys.biz.main.comp.NewTypes.DataTable;

import java.util.LinkedList;
import java.util.List;

/**
 * Created by slexsys on 12/14/17.
 */

public class MySQLWorker extends SQLWorker {
    //region finals
    private final int SLEEP_TIME = 10;
    //endregion
    
    //region fields
    private MySQLInfo info;
    private Activity activity;
    private SQLTables sqlTables;
    //endregion
    
    //region constructors
    public MySQLWorker(MySQLInfo info, Activity activity) {
        this.info = info;
        this.activity = activity;
        initQuerys();
        InitSQLTablesFields();
    }
    //endregion
    
    //region public methods
    @Override
    public boolean execQuerys(List<String> queries) {
        initHeader();
        MySQLExecute execute = new MySQLExecute(info, queries);
        execute.setActivity(activity);
        execute.start();
        waiter(execute);
        return execute.isok();
    }

    @Override
    public boolean execQuery(final String query) {
        initHeader();
        List<String> queries = new LinkedList<String>() {{ add(query); }};
        MySQLExecute execute = new MySQLExecute(info, queries);
        execute.setActivity(activity);
        execute.start();
        waiter(execute);
        return execute.isok();
    }

    @Override
    public String execScalar(String query) {
        String result = null;
        DataTable table = execReader(query);
        if (table != null && table.size() > 0) {
            if (table.get(0).size() > 0) {
                result = table.get(0).get(0).toString();
            }
        }
        return result;
    }

    @Override
    public DataTable execReader(String query) {
        initHeader();
        MySQLExecute execute = new MySQLExecute(info, query);
        execute.setActivity(activity);
        execute.start();
        waiter(execute);
        return execute.getDataTable();
    }

    @Override
    public SQLTables getSqlTables() {
        return sqlTables;
    }
    //endregion
    
    //region inner methods
    private void initHeader() {
        try {
            Class.forName("com.mysql.jdbc.Driver");
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
    }
    
    private void waiter(MySQLExecute execute) {
        while (!execute.isfinish()) {
            try{
                Thread.sleep(SLEEP_TIME);
            } catch (InterruptedException e) { }
        }
    }
    //endregion

    //region Queries
    private void initQuerys() {
        initQuerysOperations();
        initQuerysEdit();
        initQuerysFinance();
        initQuerysDocuments();
        initQuerysReports();
        initQuerysOptions();
    }

    private void initQuerysOperations() {
        SQLQuerys.Operations.Select.MaxAcct = "SELECT IFNULL((SELECT MAX(CAST(SUBSTR(NextAcct, LENGTH('#letter') + 1) AS SIGNED)) FROM nextacct WHERE SUBSTR(NextAcct, 1, LENGTH('#letter')) = '#letter'), 0) + 1"; //"SELECT IFNULL(MAX(Acct) + 1, 1) AS value FROM payments WHERE OperType = #id";

        SQLQuerys.Operations.Insert.Cashbook = "INSERT INTO cashbook (date, `desc`, opertype, sign, profit, userid, userrealtime, objectid) VALUES " +
                "('#date', CONCAT(#oname, ' No.0000', '#acct, ', #pname), #opr, #sign, #total, #uid, NOW(), #oid)";
        SQLQuerys.Operations.Insert.Payments = "INSERT INTO payments (acct, opertype, partnerid, objectid, qtty, mode, sign, date, userid, userrealtime, type, transactionnumber, enddate) VALUES" +
                "(#acct, #oper, #pid, #oid, #total, #mode, #sign, '#date', #uid, NOW(), #type, '', NOW())";

        SQLQuerys.Operations.Insert.Operation.Other = "INSERT INTO operations (opertype, acct, goodid, partnerid, objectid, operatorid, qtty, sign, pricein, priceout, discount, currencyid, currencyrate, date, userid, userrealtime, lotid, lot, note, srcdocid) VALUES" +
                "(#oper, #acct, #gid, #pid, #oid, #uid, #qtty, #sign, #pin * (1 - #disc / 100), #pout, #disc, #cid, #crate, '#date', #uid, NOW(), 1, '', '', 0)";
        SQLQuerys.Operations.Insert.Operation.StockTacking = "INSERT INTO operations (opertype, acct, goodid, partnerid, objectid, operatorid, qtty, sign, pricein, priceout, discount, currencyid, currencyrate, date, userid, userrealtime, lotid, lot, note, srcdocid) VALUES" +
                "(#oper, #acct, #gid, #pid, #oid, #uid, #qttyR, #sign, #pin * (1 - #disc / 100), #pout, #disc, #cid, #crate, '#date', #uid, NOW(), 1, '', '', 1)," +
                "(#oper, #acct, #gid, #pid, #oid, #uid, #qttyW, #sign, #pin * (1 - #disc / 100), #pout, #disc, #cid, #crate, '#date', #uid, NOW(), 1, '', '', 2)";
        SQLQuerys.Operations.Insert.Operation.Transfer = "INSERT INTO operations (opertype, acct, goodid, partnerid, objectid, operatorid, qtty, sign, pricein, priceout, discount, currencyid, currencyrate, date, userid, userrealtime, lotid, lot, note, srcdocid) VALUES" +
                "(#oper1, #acct, #gid, #pid, #oid1, #uid, #qtty, #sign1, #pin * (1 - #disc / 100), #pout, #disc, #cid, #crate, '#date', #uid, NOW(), 1, '', '', 0)," +
                "(#oper2, #acct, #gid, #pid, #oid2, #uid, #qtty, #sign2, #pin * (1 - #disc / 100), #pout, #disc, #cid, #crate, '#date', #uid, NOW(), 1, '', '', 0)";

        SQLQuerys.Operations.Update.MaxAcct = "INSERT INTO nextacct (NextAcct) VALUES (CONCAT('#letter', '#acct' ))";//"UPDATE accts SET value = value + 1 WHERE id = #id";
        SQLQuerys.Operations.Update.Store = "UPDATE store SET qtty = qtty + #qtty WHERE goodid = #gid AND objectid = #oid";

        SQLQuerys.Operations.Update.Payments.ByID = "UPDATE payments SET date = '#date', qtty = #qtty, type = #type, enddate = '#enddate' WHERE id = #id";
        SQLQuerys.Operations.Delete.Payments.ByID = "DELETE FROM payments WHERE id = #id";
    }

    private void initQuerysEdit() {
        initQuerysEditAll();
        initQuerysEditPartners();
        initQuerysEditItems();
        initQuerysEditObjects();
        initQuerysEditUsers();
        initQuerysEditMeasures();
        initQuerysEditTaxGroups();
        initQuerysEditCurrencies();
    }

    private void initQuerysEditAll() {
        SQLQuerys.Edit.All.Item.NextMaxCode = "SELECT IFNULL((MAX(CAST(code AS SIGNED)) + 1), 1) FROM #table";
        SQLQuerys.Edit.All.Group.Select = "SELECT id, code, name, IFNULL((SELECT name FROM #group AS gg2 WHERE gg2.code LIKE CONCAT(#group.code, '%') AND LENGTH(gg2.code) = LENGTH(#group.code) + 3 ORDER BY gg2.name LIMIT 1), '') AS subgroup, (SELECT COUNT(*) FROM #items WHERE #items.groupid = #group.id) AS count FROM #group";
        SQLQuerys.Edit.All.Group.SelectUpGroups = " WHERE code LIKE CONCAT(SUBSTRING('#code', 1, LENGTH('#code') - 6), '%') AND LENGTH(code) = LENGTH('#code') - 3 ORDER BY name";
        SQLQuerys.Edit.All.Group.SelectDownGroups = " WHERE code LIKE '#code%' AND LENGTH(code) = LENGTH('#code') + 3 ORDER BY name";
    }

    private void initQuerysEditPartners() {
        QueryItemGroup partners = SQLQuerys.Edit.Partners;
        partners.Item.Select = "SELECT id, code, company, company2, mol, mol2, city, city2, address, address2, phone, phone2, fax, email, taxno, bulstat, pricegroup, discount, userid, userrealtime, cardnumber, paymentdays, type, groupid, deleted FROM partners";
        partners.Item.SelectByID = partners.Item.Select + " WHERE id = #id";
        partners.Item.Insert = "INSERT INTO partners (code, company, mol, city, address, phone, fax, email, taxno, bulstat, pricegroup, discount, userid, userrealtime, cardnumber, paymentdays, type, groupid, deleted) VALUES ('#code', '#name', '#mol', '#city', '#address', '#phone', '#fax', '#email', '#taxno', '#vatno', #pricegroup, #discount, #userid, '#userrealtime', '#cardnumber', #paymentdays, #type, #groupid, #deleted)";
        partners.Item.Update = "UPDATE partners SET code = '#code', company = '#name', mol = '#mol', city = '#city', address = '#address', phone = '#phone', fax = '#fax', email = '#email', taxno = '#taxno', bulstat = '#vatno', pricegroup = #pricegroup, discount = #discount, userid = #userid, userrealtime = '#userrealtime', cardnumber = '#cardnumber', paymentdays = #paymentdays, type = #type, groupid = #groupid, deleted = #deleted WHERE id = #id";
        partners.Item.Delete = "DELETE FROM partners WHERE id = #id";
        partners.Item.Like = " partners.code LIKE '%#text%' OR partners.company LIKE '%#text%' OR partners.mol LIKE '%#text%' OR partners.city LIKE '%#text%' OR partners.address LIKE '%#text%' OR partners.phone LIKE '%#text%' OR partners.fax LIKE '%#text%' OR partners.email LIKE '%#text%' OR partners.taxno LIKE '%#text%' OR partners.bulstat LIKE '%#text%' OR partners.cardnumber LIKE '%#text%' ";//OR partnersgroups.name LIKE '%#text%' ";
        partners.Item.TableName = "partners";
        partners.Item.IDName = "partnerid";

        partners.Group.TableName = "partnersgroups";
        partners.Group.Select = SQLQuerys.Edit.All.Group.Select.replace("#group", partners.Group.TableName).replace("#items", partners.Item.TableName);
        partners.Group.SelectUpGroups = partners.Group.Select + SQLQuerys.Edit.All.Group.SelectUpGroups;
        partners.Group.SelectDownGroups = partners.Group.Select + SQLQuerys.Edit.All.Group.SelectDownGroups;

        partners.Item.SelectByGID = partners.Item.Select + " WHERE groupid = #gid";
        partners.Item.NextMaxCode = SQLQuerys.Edit.All.Item.NextMaxCode.replace("#table", partners.Item.TableName);
    }

    private void initQuerysEditItems() {
        QueryItemGroup items = SQLQuerys.Edit.Items;
        items.Item.Select = "SELECT goods.id, code, name, qtty, TRIM(BOTH ':' FROM CONCAT(IF(TRIM(barcode1) != '', CONCAT(barcode1, ':'), ''), IF(TRIM(barcode2) != '', CONCAT(barcode2, ':'), ''), IF(TRIM(barcode3) != '', CONCAT(REPLACE(barcode3, ',', ':'), ':'), ''))) AS barcode, '' AS barcode2, catalog1 AS plu, catalog2 AS catalog, catalog3 AS partnerid, 0 as measure1, 0 as measure2, ratio, pricein, priceout1, priceout2, priceout3, priceout4, priceout5, priceout6, priceout7, priceout8, priceout9, priceout10, minqtty, normalqtty, description, isrecipe, taxgroup - 1, type, groupid, deleted FROM goods JOIN store ON goods.id = store.goodid";
        items.Item.SelectByID = items.Item.Select + " WHERE goods.id = #id AND objectid = #oid";
        items.Item.Insert = "INSERT INTO goods (code, name, barcode1, barcode2, barcode3, catalog1, catalog2, catalog3, measure1, measure2, ratio, pricein, priceout1, priceout2, priceout3, priceout4, priceout5, priceout6, priceout7, priceout8, priceout9, priceout10, minqtty, normalqtty, description, isrecipe, taxgroup, type, groupid, deleted) VALUES ('#code', '#name', '#barcode1', '#barcode2', '#barcode3', '#plu', '#catalog', '#partnerid', '#measure', '#measure2', #ratio, #pricein, #priceout1, #priceout2, #priceout3, #priceout4, #priceout5, #priceout6, #priceout7, #priceout8, #priceout9, #priceout10, #minqtty, #normalqtty, '#description', #isrecipe, #taxgroup + 1, #type, #groupid, #deleted)";
        items.Item.Update = "UPDATE goods SET code = '#code', name = '#name', barcode1 = '#barcode1', barcode2 = '#barcode2', barcode3 = '#barcode3', catalog1 = '#plu', catalog2 = '#catalog', catalog3 = '#partnerid', measure1 = '#measure1', measure2 = '#measure2', ratio = #ratio, pricein = #pricein0, priceout1 = #priceout1, priceout2 = #priceout2, priceout3 = #priceout3, priceout4 = #priceout4, priceout5 = #priceout5, priceout6 = #priceout6, priceout7 = #priceout7, priceout8 = #priceout8, priceout9 = #priceout9, priceout10 = #priceout10, minqtty = #minqtty, normalqtty = #normalqtty, description = '#description', isrecipe = #isrecipe, taxgroup = #taxgroup + 1, type = #type, groupid = #groupid, deleted = #deleted WHERE id = #id";
        SQLQuerys.Others.Update.Items.PriceIn = "UPDATE goods SET pricein#pig = #pricein WHERE id = #id";
        SQLQuerys.Others.Update.Items.PriceOut = "UPDATE goods SET priceout#pog = #priceout WHERE id = #id";
        SQLQuerys.Others.Update.Items.PriceInOut = "UPDATE goods SET pricein#pig = #pricein, priceout#pog = #priceout WHERE id = #id";
        items.Item.Delete = "DELETE FROM goods WHERE id = #id";
        items.Item.Like = " goods.code LIKE '%#text%' OR goods.name LIKE '%#text%' OR goods.barcode1 LIKE '%#text%' OR goods.barcode2 LIKE '%#text%' OR goods.barcode3 REGEXP '[[:<:]]#text[[:>:]]' OR goods.catalog1 LIKE '%#text%' OR goods.catalog2 LIKE '%#text%' OR goods.description LIKE '%#text%' ";//OR goodsgroups.name LIKE '%#text%' ";
        items.Item.IDName = "goodid";
        items.Item.TableName = "goods";

        items.Group.TableName = "goodsgroups";
        items.Group.Select = SQLQuerys.Edit.All.Group.Select.replace("#group", items.Group.TableName).replace("#items", items.Item.TableName);
        items.Group.SelectUpGroups = items.Group.Select + SQLQuerys.Edit.All.Group.SelectUpGroups;
        items.Group.SelectDownGroups = items.Group.Select + SQLQuerys.Edit.All.Group.SelectDownGroups;

        SQLQuerys.Operations.Select.ItemByCodeBarcode = items.Item.Select + " WHERE (code = '#value' OR barcode1 = '#value') AND objectid = #oid";
        items.Item.SelectByGID = items.Item.Select + " WHERE groupid = #gid AND objectid = #oid";
        items.Item.NextMaxCode = SQLQuerys.Edit.All.Item.NextMaxCode.replace("#table", items.Item.TableName);
    }

    private void initQuerysEditObjects() {
        QueryItemGroup objects = SQLQuerys.Edit.Objects;
        objects.Item.Select = "SELECT id, code, name, pricegroup, groupid, deleted FROM objects";
        objects.Item.SelectByID = objects.Item.Select + " WHERE id = #id";
        objects.Item.Insert = "INSERT INTO objects (code, name, pricegroup, groupid, deleted) VALUES ('#code', '#name', #pricegroup, #groupid, #deleted)";
        objects.Item.Update = "UPDATE objects SET code = '#code', name = '#name', pricegroup = #pricegroup, groupid = #groupid, deleted = #deleted WHERE id = #id";
        objects.Item.Delete = "DELETE FROM objects WHERE id = #id";
        objects.Item.Like = " objects.code LIKE '%#text%' OR objects.name LIKE '%#text%' ";//OR objectsgroups.name LIKE '%#text%' ";
        objects.Item.TableName = "objects";
        objects.Item.IDName = "objectid";

        objects.Group.TableName = "objectsgroups";
        objects.Group.Select = SQLQuerys.Edit.All.Group.Select.replace("#group", objects.Group.TableName).replace("#items", objects.Item.TableName);
        objects.Group.SelectUpGroups = objects.Group.Select + SQLQuerys.Edit.All.Group.SelectUpGroups;
        objects.Group.SelectDownGroups = objects.Group.Select + SQLQuerys.Edit.All.Group.SelectDownGroups;

        objects.Item.SelectByGID = SQLQuerys.Edit.Objects.Item.Select + " WHERE groupid = #gid";
        objects.Item.NextMaxCode = SQLQuerys.Edit.All.Item.NextMaxCode.replace("#table", objects.Item.TableName);
    }

    private void initQuerysEditUsers() {
        QueryItemGroup users = SQLQuerys.Edit.Users;
        users.Item.Select = "SELECT id, code, name, password, cardnumber, userlevel, groupid, deleted FROM users";
        users.Item.SelectByID = users.Item.Select + " WHERE id = #id";
        users.Item.Insert = "INSERT INTO users (code, name, password, cardnumber, userlevel, groupid, deleted) VALUES ('#code', '#name', '#password', '#card', #level, #groupid, #deleted)";
        users.Item.Update = "UPDATE users SET code = '#code', name = '#name', password = '#password', cardnumber = '#card', userlevel = #level, groupid = #groupid, deleted = #deleted WHERE id = #id";
        users.Item.Delete = "DELETE FROM users WHERE id = #id";
        users.Item.Like = " users.code LIKE '%#text%' OR users.name LIKE '%#text%' OR users.cardnumber LIKE '%#text%' ";//OR usersgroups.name LIKE '%#text%' ";
        users.Item.TableName = "users";
        users.Item.IDName = "userid";

        users.Group.TableName = "usersgroups";
        users.Group.Select = SQLQuerys.Edit.All.Group.Select.replace("#group", users.Group.TableName).replace("#items", users.Item.TableName);
        users.Group.SelectUpGroups = users.Group.Select + SQLQuerys.Edit.All.Group.SelectUpGroups;
        users.Group.SelectDownGroups = users.Group.Select + SQLQuerys.Edit.All.Group.SelectDownGroups;

        users.Item.SelectByGID = SQLQuerys.Edit.Users.Item.Select + " WHERE groupid = #gid";
        users.Item.NextMaxCode = SQLQuerys.Edit.All.Item.NextMaxCode.replace("#table", users.Item.TableName);
    }

    private void initQuerysEditMeasures() {
        SQLQuerys.Edit.Measures1.Item.Select = "SELECT @a:=@a+1 AS id, '' AS code, measure1 AS name, '' AS name2, 0 AS type, 0 AS deleted, 0 AS groupid FROM (SELECT measure1 FROM goods GROUP BY measure1) AS m, (SELECT @a:=0) AS a";
        SQLQuerys.Edit.Measures2.Item.Select = "SELECT @a:=@a+1 AS id, '' AS code, measure2 AS name, '' AS name2, 1 AS type, 0 AS deleted, 0 AS groupid FROM (SELECT measure2 FROM goods GROUP BY measure2) AS m, (SELECT @a:=0) AS a";
    }

    private void initQuerysEditTaxGroups() {
        SQLQuerys.Edit.TaxGroups.Item.Select = "SELECT id, code, name, VATValue AS value, '' AS name2, 0 AS deleted, 0 AS groupid FROM vatgroups";
    }

    private void initQuerysEditCurrencies() {
        SQLQuerys.Edit.Currencies.Item.Select = "SELECT ID, Currency AS name, Description AS name2, ExchangeRate AS value, deleted, 0 AS groupid FROM currencies";
    }

    private void initQuerysFinance() {
        SQLQuerys.Cashbook.Select.Consumptions = "SELECT date, `desc`, IF(sign = 1, profit, '') AS SumIn, IF(sign = -1, profit, '') AS SumOut, id, opertype FROM cashbook #where";
        SQLQuerys.Cashbook.Select.Table = "cashbook";
        SQLQuerys.Cashbook.Insert.New = "INSERT INTO cashbook (date, `desc`, opertype, sign, profit, userid, userrealtime, objectid) VALUES ('#date', '#desc', #type, #sign, #sum, #uid, NOW(), #oid)";
        SQLQuerys.Cashbook.Update.ById = "UPDATE cashbook SET date = date('#date'), `desc` = '#desc', opertype = #type, sign = #sign, profit = #sum, userid = #uid, userrealtime = datetime('now'), objectid = #oid WHERE id = #id";
        SQLQuerys.Cashbook.Delete.ById = "DELETE FROM cashbook WHERE id = #id";

        SQLQuerys.Finances.Select.Payments.ByAcctAndOPerType = "SELECT id, date, mode, sign, type, qtty, partnerid, objectid, userid, enddate FROM payments WHERE acct = #acct AND opertype = #oper";
        SQLQuerys.Finances.Select.Payments.List = "SELECT payments.date, payments.acct, partners.company, operationtype.#lang, SUM(CASE WHEN payments.mode = -1 THEN qtty ELSE 0 END) AS topay, SUM(CASE WHEN payments.mode = 1 THEN qtty ELSE 0 END) AS payed, SUM(-1 * payments.mode * payments.qtty) AS difference, payments.partnerid, payments.opertype FROM payments JOIN operationtype ON operationtype.id = payments.opertype JOIN partners ON partners.id = payments.partnerid #where GROUP BY payments.opertype, payments.acct ORDER BY payments.acct";
        SQLQuerys.Finances.Select.Payments.Table = "payments";
        SQLQuerys.Finances.Insert.Payments = "INSERT INTO payments (acct, opertype, partnerid, objectid, qtty, mode, sign, date, userid, userrealtime, type, transactionnumber, enddate)" +
                " SELECT acct, opertype, partnerid, objectid, #total, #mode, sign, date('#date'), userid, NOW(), #type, '', enddate FROM payments WHERE acct = #acct AND opertype = #oper AND mode = -1 LIMIT 1";
    }

    private void initQuerysDocuments() {
        SQLQuerys.Documents.Select.Documents = "SELECT operations.date, operations.acct, partners.code, partners.company, SUM(operations.qtty) AS qtty, SUM(operations.pricein * operations.qtty) AS amountin, SUM(operations.priceout * operations.qtty) AS amountout, SUM((operations.priceout - operations.pricein) * operations.qtty) AS profit FROM operations JOIN partners ON partners.id = operations.partnerid #where GROUP BY operations.opertype, operations.acct;";
        SQLQuerys.Documents.Select.Operations = "SELECT goods.id, goods.code, goods.name, goods.measure1, operations.qtty, operations.pricein, operations.priceout, currencies.currency, currencyid, operations.qtty * operations.pricein AS amountin, operations.qtty *operations.priceout AS amountout, goods.description, operations.date, operations.partnerid, operations.objectid, operations.userid FROM operations JOIN goods ON goods.id = operations.goodid JOIN currencies ON currencies.id = operations.currencyid WHERE operations.acct = #acct AND operations.opertype = #oper";
        SQLQuerys.Documents.Select.Table = "operations";

        SQLQuerys.Documents.Update.Store.ByAcctAndOperType = "UPDATE store SET qtty = qtty - (SELECT operations.sign * operations.qtty FROM operations WHERE opertype = #oper AND acct = #acct AND operations.goodid = store.goodid) WHERE goodid IN (SELECT operations.goodid FROM operations WHERE opertype = #oper AND acct = #acct)";

        SQLQuerys.Documents.Delete.Operations.ByAcctAndOperType = "DELETE FROM operations WHERE acct = #acct AND opertype = #oper";
        SQLQuerys.Documents.Delete.Payments.ByAcctAndOperType = "DELETE FROM payments WHERE acct = #acct AND opertype = #oper";
    }

    private void initQuerysReports() {
        SQLQuerys.Reports.Select.Operations = "SELECT operations.userrealtime, operations.acct, operations.goodid, goods.code, goods.name, goods.groupid, goodsgroups.name AS `group`, 0 AS measureid, goods.measures1 AS Measure, operations.qtty, operations.discount, operations.pricein, operations.qtty * operations.pricein AS amountin, operations.priceout, operations.qtty * operations.priceout AS amountout, (operations.priceout - operations.pricein) * operations.qtty AS amountprofit, operations.partnerid, partners.code, partners.company AS partner, operations.userid, users.code, users.name AS user, operations.objectid, objects.code, objects.name AS object, operations.opertype, opertype.en AS opertype FROM operations JOIN partners ON partners.id = operations.partnerid JOIN goods ON goods.id = operations.goodid JOIN goodsgroups ON goods.groupid = goodsgroups.id JOIN objects ON objects.id = operations.objectid JOIN users ON users.id = operations.userid JOIN opertype ON opertype.id = operations.opertype #where;";
        //Limit 1000
        SQLQuerys.Reports.Select.Items.InStock = "SELECT items.id, items.code, items.name, itemgroups.name, items.catalog, items.measure, qtty, items.pricein, (qtty * items.pricein) AS amountin, items.priceout, (qtty * items.priceout) AS amountout, (qtty * (items.priceout - items.pricein)) AS profit, items.description FROM store JOIN items ON items.id = itemid JOIN itemgroups ON items.groupID = itemgroups.id #where LIMIT 1000;";
        SQLQuerys.Reports.Select.Items.Flow = "SELECT items.id, items.code, items.name, itemgroups.name AS `group`, items.pricein, items.priceout, items.measure, store.qtty, SUM(operations.qtty * sign) AS flow, SUM(CASE  WHEN opertype = 1 THEN operations.qtty ELSE 0 END) AS purchase, SUM(CASE  WHEN opertype = 2 THEN operations.qtty ELSE 0 END) AS sale, SUM(CASE  WHEN opertype = 11 THEN operations.qtty ELSE 0 END) writeout, SUM(CASE  WHEN opertype = 34 THEN operations.qtty ELSE 0 END) AS refund, SUM(CASE  WHEN opertype = 39 THEN operations.qtty ELSE 0 END) AS refundtosupplier, SUM(CASE  WHEN opertype = 3 THEN operations.qtty ELSE 0 END) AS wast, SUM(CASE  WHEN opertype = 8 THEN operations.qtty ELSE 0 END) AS transferto, SUM(CASE  WHEN opertype = 7 THEN operations.qtty ELSE 0 END) AS transferfrom, SUM(CASE  WHEN opertype = 4 THEN operations.qtty ELSE 0 END) AS stocktacking, objects.name AS object FROM operations JOIN items ON items.id = operations.itemid JOIN store ON store.itemid = operations.itemid AND store.objectid = operations.objectid JOIN itemgroups ON itemgroups.id = items.groupid JOIN objects ON objects.id = operations.objectid #where GROUP BY operations.itemid, operations.objectid;";
        SQLQuerys.Reports.Select.Payments.ByPartners = "SELECT partners.id, partners.code, partners.name, SUM(CASE  WHEN mode = -1 THEN -1 * qtty * sign ELSE 0 END) AS `all`, SUM(qtty * sign * mode) AS credit, SUM(CASE  WHEN mode = 1 AND sign = 1 THEN qtty ELSE 0 END) AS payus, SUM(CASE  WHEN mode = 1 AND sign = -1 THEN qtty ELSE 0 END) AS payto FROM payments JOIN partners ON partners.id = partnerid #where GROUP BY partnerid;";
        SQLQuerys.Reports.Select.Payments.ByDocuments = "SELECT Date AS Date, acct, partners.id, code, name, SUM(CASE  WHEN mode = -1 THEN -1 * qtty * sign ELSE 0 END) AS `all`, SUM(qtty * sign * mode) AS credit, SUM(CASE  WHEN mode = 1 AND sign = 1 THEN qtty ELSE 0 END) AS payus, SUM(CASE  WHEN mode = 1 AND sign = -1 THEN qtty ELSE 0 END) AS payto, opertype, RU FROM payments JOIN partners ON partners.id = partnerid JOIN opertype ON opertype.id = opertype #where GROUP BY acct, opertype ORDER BY date;";
        SQLQuerys.Reports.Select.Payments.Flow = "SELECT acct, date, partnerid, partners.name, partners.cardnumber, partners.groupid, partnergroups.name AS `group`, objectid, objects.name AS `object`, payments.userid, users.name AS user, opertype, opertype.RU AS `operation`, payments.type, paytypes.name AS paytype, qtty * sign AS `sum` FROM payments LEFT JOIN partners ON partnerid = partners.id LEFT JOIN users ON payments.userid = users.id LEFT JOIN objects ON objectid = objects.id LEFT JOIN opertype ON opertype = opertype.id LEFT JOIN paytypes ON payments.type = paytypes.id LEFT JOIN partnergroups ON partnergroups.id = partners.groupid #where;";
        SQLQuerys.Reports.Select.DayFlow.Sale = "SELECT 'Sales' AS operation, IFNULL(SUM(qtty), 0) AS sum FROM payments #where ";
        SQLQuerys.Reports.Select.DayFlow.Refund = " UNION ALL SELECT 'Refund', IFNULL(-1 * SUM(qtty), 0) FROM payments #where ";
        SQLQuerys.Reports.Select.DayFlow.Bonus = " UNION ALL SELECT 'Bonus', IFNULL(-1 * SUM(qtty), 0) FROM payments #where;";
        SQLQuerys.Reports.Select.FlowInPeriod = "SELECT RU AS operation, sign * ROUND(SUM(qtty * priceIn), 0) AS amountin, sign * ROUND(SUM(qtty * priceout), 0) AS amountout FROM operations JOIN opertype ON operations.opertype = opertype.id #where GROUP BY opertype UNION ALL SELECT 'Stock', SUM(qtty * pricein), SUM(qtty * priceout) FROM store JOIN items ON items.id = itemid;";
        SQLQuerys.Reports.Select.ProfitInPeriod = "SELECT RU, CASE opertype WHEN 2 THEN ROUND(SUM(qtty * (priceout - pricein)), 0) WHEN 3 THEN -1 * ROUND(SUM(qtty * priceout), 0) WHEN 4 THEN ROUND(SUM(qtty * priceout), 0) END AS sum FROM operations JOIN opertype ON operations.opertype = opertype.id #where GROUP BY opertype ";
        SQLQuerys.Reports.Select.ConsumptionInPeriod = " UNION ALL SELECT CAST(opertype AS CHAR), -1 * SUM(profit) FROM cashbook #where GROUP BY opertype";

        SQLQuerys.Reports.Select.getReports = "SELECT text FROM reports WHERE deleted = 0";
    }

    private void initQuerysOptions() { }
    //endregion

    //region TablesFields
    private void InitSQLTablesFields() {
        sqlTables = new SQLTables();
        InitSQLTableFieldsGroups(sqlTables);

        InitSQLTableFieldsPartners(sqlTables);
        InitSQLTableFieldsItems(sqlTables);
        InitSQLTableFieldsObjects(sqlTables);
        InitSQLTableFieldsUsers(sqlTables);

        InitSQLTableFieldsMeasures(sqlTables);
        InitSQLTableFieldsTaxGroups(sqlTables);
    }

    private void InitSQLTableFieldsGroups(SQLTables sqlTables) {
        SQLTablesiBase base = getSQLTablesiBase();
        sqlTables.setGroups(new SQLTablesGroups(base){{
            setFirstSubGroupName("SubGroup");
            setItemCount("Count");
        }});

    }

    private void InitSQLTableFieldsPartners(SQLTables sqlTables) {
        SQLTablesiIBase base = getSQLTablesiIBase();
        sqlTables.setPartners(new SQLTablesPartners(base){{
            setName("Company");
            setName2("Company2");
            setMol("MOL");
            setMol2("MOL2");
            setCity("City");
            setCity2("City2");
            setAddress("Address");
            setAddress2("Address2");
            setPhone("Phone");
            setPhone2("Phone2");
            setFax("Fax");
            setEmail("Email");
            setTaxno("TaxNo");
            setVatno("BulStat");
            setPricegroup("PriceGroup");
            setType("Type");
            setDiscount("Discount");
            setUserid("UserID");
            setUserrealtime("UserRealTime");
            setCardnumber("CardNumber");
            setPaymentdays("PaymentDays");
        }});
    }

    private void InitSQLTableFieldsItems(SQLTables sqlTables) {
        SQLTablesiIBase base = getSQLTablesiIBase();
        sqlTables.setItems(new SQLTablesItems(base){{
            setQtty("Qtty");
            setBarcode("Barcode");
            setBarcode2("Barcode2");
            setPlu("PLU");
            setPartnerid("PartnerID");
            setCatalog("Catalog");
            setMeasure("Measure1");
            setMeasure2("Measure2");
            setRatio("Ratio");
            setPricein("PriceIn");
            setPriceout("PriceOut");
            setMinqtty("MinQtty");
            setNormalqtty("NormalQtty");
            setDescription("Description");
            setIsrecipe("IsRecipe");
            setTaxgroup("TaxGroup");
            setType("Type");
        }});
    }

    private void InitSQLTableFieldsObjects(SQLTables sqlTables) {
        SQLTablesiIBase base = getSQLTablesiIBase();
        sqlTables.setObjects(new SQLTablesObjects(base){{
            setPriceGroup("PriceGroup");
        }});
    }

    private void InitSQLTableFieldsUsers(SQLTables sqlTables) {
        SQLTablesiIBase base = getSQLTablesiIBase();
        sqlTables.setUsers(new SQLTablesUsers(base){{
            setLevel("UserLevel");
            setPassword("Password");
            setCard("CardNumber");
        }});
    }

    private void InitSQLTableFieldsMeasures(SQLTables sqlTables) {
        SQLTablesiIBase base = getSQLTablesiIBase();
        sqlTables.setMeasures(new SQLTablesMeasures(base));
    }

    private void InitSQLTableFieldsTaxGroups(SQLTables sqlTables) {
        SQLTablesiIBase base = getSQLTablesiIBase();
        sqlTables.setTaxGroups(new SQLTablesTaxGroups(base){{
            setValue("Value");
        }});
    }

    private SQLTablesiIBase getSQLTablesiIBase() {
        SQLTablesiBase base = getSQLTablesiBase();
        return new SQLTablesiIBase(base){{
            setGroupid("GroupID");
            setIsveryused("IsVeryUsed");
            setDeleted("Deleted");
        }};
    }

    private SQLTablesiBase getSQLTablesiBase() {
        return new SQLTablesiBase(){{
            setId("ID");
            setCode("Code");
            setName("Name");
            setName2("Name2");
        }};
    }
    //endregion
}
