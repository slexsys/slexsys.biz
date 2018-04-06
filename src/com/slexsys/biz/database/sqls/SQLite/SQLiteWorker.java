package com.slexsys.biz.database.sqls.SQLite;

import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.slexsys.biz.database.sqls.SQLTables.SQLTables;
import com.slexsys.biz.database.sqls.SQLQuerys;
import com.slexsys.biz.database.sqls.SQLWorker;
import com.slexsys.biz.main.comp.NewTypes.DataTable;
import com.slexsys.biz.main.report.json.ReportJSON;

import java.util.LinkedList;
import java.util.List;

/**
 * Created by slexsys on 12/19/17.
 */

public class SQLiteWorker extends SQLWorker {
    //region fields
    private String path;
    private SQLiteDatabase db;
    //endregion

    //region constructors
    public SQLiteWorker(String path) {
        this.path = path;
        initQuerys();
    }
    //endregion

    //region public methods
    @Override
    public boolean execQuerys(List<String> queries) {
        boolean result = true;
        try {
            db = db.openDatabase(path, null, SQLiteDatabase.OPEN_READWRITE);
            db.beginTransaction();
            for (String query : queries) {
                Log.d("iSQLite Save Oper", query);
                db.execSQL(query);
            }
            db.setTransactionSuccessful();
        } catch(SQLException e) {
            result = false;
            Log.d("iSQLite Error", e.getMessage());
        } finally {
            db.endTransaction();
            db.close();
        }
        return result;
    }

    @Override
    public boolean execQuery(String query) {
        boolean result;
        try {
            db = db.openDatabase(path, null, SQLiteDatabase.OPEN_READWRITE);
            db.execSQL(query);
            db.close();
            result = true;
            Log.d("iSQLite AED Exec", "Done");
            Log.d("iSQLite Query", query);
        }
        catch(Exception ex) {
            result = false;
            Log.d("iSQLite AED Error", ex.getMessage());
        }
        return result;
    }

    @Override
    public String execScalar(String query) {
        String result = null;
        try {
            db = db.openDatabase(path, null, SQLiteDatabase.OPEN_READONLY);
            Cursor cursor = db.rawQuery(query, null);
            if (cursor.getCount() > 0) {
                cursor.moveToFirst();
                result = cursor.getString(0);
                cursor.close();
            }
            db.close();
        } catch (Exception ex) {
            Log.d("iSQLite Scalar Error", ex.getMessage());
        }
        return result;
    }

    @Override
    public DataTable execReader(String query) {
        DataTable result = null;
        try {
            db = db.openDatabase(path, null, SQLiteDatabase.OPEN_READONLY);
            Cursor cursor = db.rawQuery(query, null);
            if (cursor.getCount() > 0) {
                cursor.moveToFirst();
                result.addNames(cursor.getColumnNames());
                do {
                    result.addRow(cursor);
                } while (cursor.moveToNext());
                cursor.close();
            }
            db.close();
        } catch (Exception ex) {
            Log.d("iSQLite Fill Error", ex.getMessage());
        }
        return result;
    }

    @Override
    public SQLTables getSqlTables() {
        return null;
    }
    //endregion

    //region Querys
    private void initQuerys() {
        initQuerysOperations();
        initQuerysEdit();
        initQuerysFinance();
        initQuerysDocuments();
        initQuerysReports();
        initQuerysOptions();
    }

    private void initQuerysOperations() {
        SQLQuerys.Operations.Select.MaxAcct = "SELECT value + 1 FROM accts WHERE id = #id";
        SQLQuerys.Operations.Select.ItemByCodeBarcode = "SELECT id FROM items WHERE code = '#value' OR barcode = '#value'";

        SQLQuerys.Operations.Insert.Cashbook = "INSERT INTO cashbook (date, desc, opertype, sign, profit, userid, userrealtime, objectid) VALUES " +
                "(date('#date'), #oname || ' No.0000' || '#acct, ' || #pname, #opr, #sign, #total, #uid, datetime('#realtime'), #oid)";
        SQLQuerys.Operations.Insert.Payments = "INSERT INTO payments (acct, opertype, partnerid, objectid, qtty, mode, sign, date, userid, userrealtime, type, transactionnumber, enddate) VALUES" +
                "(#acct, #oper, #pid, #oid, #total, #mode, #sign, date('#date'), #uid, datetime('#realtime'), #type, '', date('#enddate'))";

        SQLQuerys.Operations.Insert.Operation.Other = "INSERT INTO operations (opertype, acct, itemid, partnerid, objectid, operatorid, qtty, sign, pricein, priceout, discount, currencyid, currencyrate, date, userid, userrealtime, lotid, lot, note, srcdocid) VALUES" +
                "(#oper, #acct, #gid, #pid, #oid, #uid, #qtty, #sign, #pin * (1 - #disc / 100), #pout, #disc, #cid, #crate, date('#date'), #uid, datetime('#realtime'), 1, '', '', 0)";
        SQLQuerys.Operations.Insert.Operation.StockTacking = "INSERT INTO operations (opertype, acct, itemid, partnerid, objectid, operatorid, qtty, sign, pricein, priceout, discount, currencyid, currencyrate, date, userid, userrealtime, lotid, lot, note, srcdocid) VALUES" +
                "(#oper, #acct, #gid, #pid, #oid, #uid, #qttyR, #sign, #pin * (1 - #disc / 100), #pout, #disc, #cid, #crate, date('#date'), #uid, datetime('#realtime'), 1, '', '', 1)," +
                "(#oper, #acct, #gid, #pid, #oid, #uid, #qttyW, #sign, #pin * (1 - #disc / 100), #pout, #disc, #cid, #crate, date('#date'), #uid, datetime('#realtime'), 1, '', '', 2)";
        SQLQuerys.Operations.Insert.Operation.Transfer = "INSERT INTO operations (opertype, acct, itemid, partnerid, objectid, operatorid, qtty, sign, pricein, priceout, discount, currencyid, currencyrate, date, userid, userrealtime, lotid, lot, note, srcdocid) VALUES" +
                "(#oper1, #acct, #gid, #pid, #oid1, #uid, #qtty, #sign1, #pin * (1 - #disc / 100), #pout, #disc, #cid, #crate, date('#date'), #uid, datetime('#realtime'), 1, '', '', 0)," +
                "(#oper2, #acct, #gid, #pid, #oid2, #uid, #qtty, #sign2, #pin * (1 - #disc / 100), #pout, #disc, #cid, #crate, date('#date'), #uid, datetime('#realtime'), 1, '', '', 0)";

        SQLQuerys.Operations.Update.MaxAcct = "UPDATE accts SET value = value + 1 WHERE id = #id";
        SQLQuerys.Operations.Update.Store = "UPDATE store SET qtty = qtty + #qtty WHERE itemid = #gid AND objectid = #oid";

        SQLQuerys.Operations.Update.Payments.ByID = "UPDATE payments SET date = date('#date'), qtty = #qtty, type = #type, enddate = date('#enddate') WHERE id = #id";
        SQLQuerys.Operations.Delete.Payments.ByID = "DELETE FROM payments WHERE id = #id";
    }

    private void initQuerysEdit() {
        initQuerysEditPartners();
        initQuerysEditItems();
        initQuerysEditObjects();
        initQuerysEditUsers();
        initQuerysEditMeasures();
        initQuerysEditTaxGroups();
        initQuerysEditCurrencies();
    }

    private void initQuerysEditPartners() {
        SQLQuerys.Edit.Partners.Item.Select = "SELECT id, code, name, mol, city, address, phone, fax, email, taxno, vatno, pricegroup, discount, userid, userrealtime, cardnumber, paymentdays, type, groupid, deleted FROM partners";
        SQLQuerys.Edit.Partners.Item.Insert = "INSERT INTO partners (code, name, mol, city, address, phone, fax, email, taxno, vatno, pricegroup, discount, userid, userrealtime, cardnumber, paymentdays, type, groupid, deleted) VALUES (#code, #name, #mol, #city, #address, #phone, #fax, #email, #taxno, #vatno, #pricegroup, #discount, #userid, #userrealtime, #cardnumber, #paymentdays, #type, #groupid, #deleted)";
        SQLQuerys.Edit.Partners.Item.Update = "UPDATE partners SET id = #id, code = #code, name = #name, mol = #mol, city = #city, address = #address, phone = #phone, fax = #fax, email = #email, taxno = #taxno, vatno = #vatno, pricegroup = #pricegroup, discount = #discount, userid = #userid, userrealtime = #userrealtime, cardnumber = #cardnumber, paymentdays = #paymentdays, type = #type, groupid = #groupid, deleted = #deleted WHERE id = #id";
        SQLQuerys.Edit.Partners.Item.Delete = "DELETE FROM partners WHERE id = #id";
        SQLQuerys.Edit.Partners.Item.Like = " partners.code LIKE '%#text%' OR partners.name LIKE '%#text%' OR partners.mol LIKE '%#text%' OR partners.city LIKE '%#text%' OR partners.address LIKE '%#text%' OR partners.phone LIKE '%#text%' OR partners.fax LIKE '%#text%' OR partners.email LIKE '%#text%' OR partners.taxno LIKE '%#text%' OR partners.vatno LIKE '%#text%' OR partners.cardnumber LIKE '%#text%' OR partnergroups.name LIKE '%#text%' ";
        SQLQuerys.Edit.Partners.Item.IDName = "partnerid";

        SQLQuerys.Edit.Partners.Group.Select = "SELECT id, code, name FROM partnergroups";
        SQLQuerys.Edit.Partners.Group.TableName = "partnergroups";
    }

    private void initQuerysEditItems() {
        SQLQuerys.Edit.Items.Item.Select = "SELECT id, code, name, barcode, barcode2, plu, partnerid, catalog, measure, measure2, ratio, pricein, pricein1, pricein2, pricein3, pricein4, pricein5, pricein6, pricein7, pricein8, pricein9, priceout, priceout1, priceout2, priceout3, priceout4, priceout5, priceout6, priceout7, priceout8, priceout9, minqtty, normalqtty, description, isrecipe, taxgroup, type, groupid, deleted FROM items";
        SQLQuerys.Edit.Items.Item.Insert = "INSERT INTO items (code, name, barcode, barcode2, plu, partnerid, catalog, measure, measure2, ratio, pricein, pricein1, pricein2, pricein3, pricein4, pricein5, pricein6, pricein7, pricein8, pricein9, priceout, priceout1, priceout2, priceout3, priceout4, priceout5, priceout6, priceout7, priceout8, priceout9, minqtty, normalqtty, description, isrecipe, taxgroup, type, groupid, deleted) VALUES (#code, #name, #barcode, #barcode2, #plu, #partnerid, #catalog, #measure, #measure2, #ratio, #pricein0, #pricein1, #pricein2, #pricein3, #pricein4, #pricein5, #pricein6, #pricein7, #pricein8, #pricein9, #priceout0, #priceout1, #priceout2, #priceout3, #priceout4, #priceout5, #priceout6, #priceout7, #priceout8, #priceout9, #minqtty, #normalqtty, #description, #isrecipe, #taxgroup, #type, #groupid, #deleted)";
        SQLQuerys.Edit.Items.Item.Update = "UPDATE items SET id = #id, code = #code, name = #name, barcode = #barcode, barcode2 = #barcode2, plu = #plu, partnerid = #partnerid, catalog = #catalog, measure = #measure, measure2 = #measure2, ratio = #ratio, pricein0 = #pricein0, pricein1 = #pricein1, pricein2 = #pricein2, pricein3 = #pricein3, pricein4 = #pricein4, pricein5 = #pricein5, pricein6 = #pricein6, pricein7 = #pricein7, pricein8 = #pricein8, pricein9 = #pricein9, priceout0 = #priceout0, priceout1 = #priceout1, priceout2 = #priceout2, priceout3 = #priceout3, priceout4 = #priceout4, priceout5 = #priceout5, priceout6 = #priceout6, priceout7 = #priceout7, priceout8 = #priceout8, priceout9 = #priceout9, minqtty = #minqtty, normalqtty = #normalqtty, description = #description, isrecipe = #isrecipe, taxgroup = #taxgroup, type = #type, groupid = #groupid, deleted = #deleted WHERE id = #id";
        SQLQuerys.Others.Update.Items.PriceIn = "UPDATE items SET pricein = #pricein WHERE id = #id";
        SQLQuerys.Others.Update.Items.PriceOut = "UPDATE items SET priceout = #priceout WHERE id = #id";
        SQLQuerys.Others.Update.Items.PriceInOut = "UPDATE items SET pricein = #pricein, priceout = #priceout WHERE id = #id";
        SQLQuerys.Edit.Items.Item.Delete = "DELETE FROM items WHERE id = #id";
        SQLQuerys.Edit.Items.Item.Like = " items.code LIKE '%#text%' OR items.name LIKE '%#text%' OR items.barcode LIKE '#text' OR items.barcode LIKE '#text:%' OR items.barcode LIKE '%:#text:%' OR items.barcode LIKE '%:#text' OR items.barcode2 LIKE '#text' OR items.barcode2 LIKE '#text:%' OR items.barcode2 LIKE '%:#text:%' OR items.barcode2 LIKE '%:#text' OR items.plu LIKE '%#text%' OR items.catalog LIKE '%#text%' OR items.description LIKE '%#text%' OR itemgroups.name LIKE '%#text%' ";
        SQLQuerys.Edit.Items.Item.IDName = "itemid";

        SQLQuerys.Edit.Items.Group.Select = "SELECT id, code, name FROM itemgroups";
        SQLQuerys.Edit.Items.Group.TableName = "itemgroups";
    }

    private void initQuerysEditObjects() {
        SQLQuerys.Edit.Objects.Item.Select = "SELECT id, code, name, pricegroup, type, groupid, deleted FROM objects";
        SQLQuerys.Edit.Objects.Item.Insert = "INSERT INTO objects (code, name, pricegroup, type, groupid, deleted) VALUES (#code, #name, #pricegroup, #type, #groupid, #deleted)";
        SQLQuerys.Edit.Objects.Item.Update = "UPDATE objects SET id = #id, code = #code, name = #name, pricegroup = #pricegroup, type = #type, groupid = #groupid, deleted = #deleted WHERE id = #id";
        SQLQuerys.Edit.Objects.Item.Delete = "DELETE FROM objects WHERE id = #id";
        SQLQuerys.Edit.Objects.Item.Like = " objects.code LIKE '%#text%' OR objects.name LIKE '%#text%' OR objectgroups.name LIKE '%#text%' ";
        SQLQuerys.Edit.Objects.Item.IDName = "objectid";

        SQLQuerys.Edit.Objects.Group.Select = "SELECT id, code, name FROM objectgroups";
        SQLQuerys.Edit.Objects.Group.TableName = "objectgroups";
    }

    private void initQuerysEditUsers() {
        SQLQuerys.Edit.Users.Item.Select = "SELECT id, code, name, password, card, level, type, groupid, deleted FROM users";
        SQLQuerys.Edit.Users.Item.Insert = "INSERT INTO users (code, name, password, card, level, type, groupid, deleted) VALUES (#code, #name, #password, #card, #level, #type, #groupid, #deleted)";
        SQLQuerys.Edit.Users.Item.Update = "UPDATE users SET id = #id, code = #code, name = #name, password = #password, card = #card, level = #level, type = #type, groupid = #groupid, deleted = #deleted WHERE id = #id";
        SQLQuerys.Edit.Users.Item.Delete = "DELETE FROM users WHERE id = #id";
        SQLQuerys.Edit.Users.Item.Like = " users.code LIKE '%#text%' OR users.name LIKE '%#text%' OR users.card LIKE '%#text%' OR usergroups.name LIKE '%#text%' ";
        SQLQuerys.Edit.Users.Item.IDName = "userid";

        SQLQuerys.Edit.Users.Group.Select = "SELECT id, code, name FROM usergroups";
        SQLQuerys.Edit.Users.Group.TableName = "usergroups";
    }

    private void initQuerysEditMeasures() {
        String query = "SELECT id, code, name, type, groupid, deleted FROM measures WHERE type = #type";
        SQLQuerys.Edit.Measures1.Item.Select = query.replace("#type", "0");
        SQLQuerys.Edit.Measures2.Item.Select = query.replace("#type", "1");
    }

    private void initQuerysEditTaxGroups() {
        SQLQuerys.Edit.TaxGroups.Item.Select = "SELECT id, code, name, value, type, groupid, deleted FROM taxgroups";
    }

    private void initQuerysEditCurrencies() {
        SQLQuerys.Edit.Currencies.Item.Select = "SELECT id, code, name, value, type, groupid, deleted FROM currencies";
    }

    private void initQuerysFinance() {
        SQLQuerys.Cashbook.Select.Consumptions = "SELECT date, desc, (CASE WHEN sign = 1 THEN profit ELSE '' END) AS SumIn, (CASE WHEN sign = -1 THEN profit ELSE '' END) AS SumOut, id, opertype FROM Cashbook #where";
        SQLQuerys.Cashbook.Select.Table = "cashbook";
        SQLQuerys.Cashbook.Insert.New = "INSERT INTO cashbook (date, desc, opertype, sign, profit, userid, userrealtime, objectid) VALUES (date('#date'), '#desc', #type, #sign, #sum, #uid, datetime('now'), #oid)";
        SQLQuerys.Cashbook.Update.ById = "UPDATE cashbook SET date = date('#date'), desc = '#desc', opertype = #type, sign = #sign, profit = #sum, userid = #uid, userrealtime = datetime('now'), objectid = #oid WHERE id = #id";
        SQLQuerys.Cashbook.Delete.ById = "DELETE FROM Cashbook WHERE id = #id";

        SQLQuerys.Finances.Select.Payments.ByAcctAndOPerType = "SELECT id, date, mode, sign, type, qtty, partnerid, objectid, userid, enddate FROM payments WHERE acct = #acct AND opertype = #oper";
        SQLQuerys.Finances.Select.Payments.List = "SELECT payments.date, payments.acct, partners.name, opertype.#lang, SUM(CASE WHEN payments.mode = -1 THEN qtty ELSE 0 END) AS topay, SUM(CASE WHEN payments.mode = 1 THEN qtty ELSE 0 END) AS payed, SUM(-1 * payments.mode * payments.qtty) AS difference, payments.partnerid, payments.opertype FROM payments JOIN opertype ON opertype.id = payments.opertype JOIN partners ON partners.id = payments.partnerid #where GROUP BY payments.opertype, payments.acct ORDER BY payments.acct";
        SQLQuerys.Finances.Select.Payments.Table = "payments";
        SQLQuerys.Finances.Insert.Payments = "INSERT INTO payments (acct, opertype, partnerid, objectid, qtty, mode, sign, date, userid, userrealtime, type, transactionnumber, enddate)" +
                " SELECT acct, opertype, partnerid, objectid, #total, #mode, sign, date('#date'), userid, datetime('#realtime'), #type, '', enddate FROM payments WHERE acct = #acct AND opertype = #oper AND mode = -1 LIMIT 1";
    }

    private void initQuerysDocuments() {
        SQLQuerys.Documents.Select.Documents = "SELECT operations.date, operations.acct, partners.code, partners.name, SUM(operations.qtty) AS qtty, SUM(operations.pricein * operations.qtty) AS amountin, SUM(operations.priceout * operations.qtty) AS amountout, SUM((operations.priceout - operations.pricein) * operations.qtty) AS profit FROM operations JOIN partners ON partners.id = operations.partnerid #where GROUP BY operations.opertype, operations.acct;";
        SQLQuerys.Documents.Select.Operations = "SELECT items.id, items.code, items.name, measures.name AS measure, operations.qtty, operations.pricein, operations.priceout, currencies.name AS currency, currencyid, operations.qtty * operations.pricein AS amountin, operations.qtty *operations.priceout AS amountout, items.description, operations.date, operations.partnerid, operations.objectid, operations.userid FROM operations JOIN items ON items.id = operations.itemid JOIN measures ON measures.id = items.measure JOIN currencies ON currencies.id = operations.currencyid WHERE operations.acct = #acct AND operations.opertype = #oper";
        SQLQuerys.Documents.Select.Table = "operations";

        SQLQuerys.Documents.Update.Store.ByAcctAndOperType = "UPDATE store SET qtty = qtty - (SELECT operations.sign * operations.qtty FROM operations WHERE opertype = #oper AND acct = #acct AND operations.itemid = store.itemid) WHERE itemid IN (SELECT operations.itemid FROM operations WHERE opertype = #oper AND acct = #acct)";

        SQLQuerys.Documents.Delete.Operations.ByAcctAndOperType = "DELETE FROM operations WHERE acct = #acct AND opertype = #oper";
        SQLQuerys.Documents.Delete.Payments.ByAcctAndOperType = "DELETE FROM payments WHERE acct = #acct AND opertype = #oper";
    }

    private void initQuerysReports() {
        SQLQuerys.Reports.Select.Operations = "SELECT operations.userrealtime, operations.acct, operations.itemid, items.code, items.name, items.groupid, itemgroups.name AS `group`, items.measure AS measureid, measures.name AS Measure, operations.qtty, operations.discount, operations.pricein, operations.qtty * operations.pricein AS amountin, operations.priceout, operations.qtty * operations.priceout AS amountout, (operations.priceout - operations.pricein) * operations.qtty AS amountprofit, operations.partnerid, partners.code, partners.name AS partner, operations.userid, users.code, users.name AS user, operations.objectid, objects.code, objects.name AS object, operations.opertype, opertype.en AS opertype FROM operations JOIN partners ON partners.id = operations.partnerid JOIN items ON items.id = operations.itemid JOIN itemgroups ON items.groupid = itemgroups.id JOIN measures ON items.measure = measures.id JOIN objects ON objects.id = operations.objectid JOIN users ON users.id = operations.userid JOIN opertype ON opertype.id = operations.opertype #where;";
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

    //region ReportJSON
    public List<ReportJSON> getReportsJSON() {
        List<ReportJSON> list = new LinkedList<ReportJSON>();
        try {
            db = db.openDatabase(path, null, SQLiteDatabase.OPEN_READONLY);
            Cursor cursor = db.rawQuery(SQLQuerys.Reports.Select.getReports, null);
            if (cursor.getCount() > 0) {
                cursor.moveToFirst();
                do {
                    list.add(ReportJSON.fromJSON(cursor));
                } while (cursor.moveToNext());
            }
            db.close();
            Log.d("Load Reports", " count : " + cursor.getCount());
        } catch (Exception ex) {
            Log.d("iSQLite Reports Error", ex.getMessage());
        }
        return list;
    }
    //endregion
}
