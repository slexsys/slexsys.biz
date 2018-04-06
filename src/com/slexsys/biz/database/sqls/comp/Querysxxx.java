package com.slexsys.biz.database.sqls.comp;

/**
 * Created by slexsys on 3/11/16.
 */
public class Querysxxx{

    public class SQLite{
        public static final String GET_PARTNERS_LIST = "SELECT id, code, name, mol, city, address, phone, fax, email, taxno, vatno, pricegroup, discount, userid, userrealtime, cardnumber, paymentdays, type, groupid, deleted FROM partners";
        public static final String INSERT_PARTNER = "INSERT INTO partners (code, name, mol, city, address, phone, fax, email, taxno, vatno, pricegroup, discount, userid, userrealtime, cardnumber, paymentdays, type, groupid, deleted) VALUES (#code, #name, #mol, #city, #address, #phone, #fax, #email, #taxno, #vatno, #pricegroup, #discount, #userid, #userrealtime, #cardnumber, #paymentdays, #type, #groupid, #deleted)";
        public static final String UPDATE_PARTNER = "UPDATE partners SET id = #id, code = #code, name = #name, mol = #mol, city = #city, address = #address, phone = #phone, fax = #fax, email = #email, taxno = #taxno, vatno = #vatno, pricegroup = #pricegroup, discount = #discount, userid = #userid, userrealtime = #userrealtime, cardnumber = #cardnumber, paymentdays = #paymentdays, type = #type, groupid = #groupid, deleted = #deleted WHERE id = #id";
        public static final String DELETE_PARTNER = "DELETE FROM partners WHERE id = #id";
        public static final String LIKE_PARTNER = " partners.code LIKE '%#text%' OR partners.name LIKE '%#text%' OR partners.mol LIKE '%#text%' OR partners.city LIKE '%#text%' OR partners.address LIKE '%#text%' OR partners.phone LIKE '%#text%' OR partners.fax LIKE '%#text%' OR partners.email LIKE '%#text%' OR partners.taxno LIKE '%#text%' OR partners.vatno LIKE '%#text%' OR partners.cardnumber LIKE '%#text%' ";
        public static final String GET_PARTNER_GROUPS_LIST = "SELECT id, code, name FROM partnergroups";
        public static final String PARTNER_ID = "partnerid";
        public static final String PARTNER_GROUP = "partnergroups";

        public static final String GET_ITEMS_LIST = "SELECT id, code, name, barcode, barcode2, plu, partnerid, catalog, measure, measure2, ratio, price, minqtty, normalqtty, description, isrecipe, taxgroup, type, groupid, deleted FROM items";
        public static final String INSERT_ITEMS = "INSERT INTO items (code, name, barcode, barcode2, plu, partnerid, catalog, measure, measure2, ratio, price, minqtty, normalqtty, description, isrecipe, taxgroup, type, groupid, deleted) VALUES (#code, #name, #barcode, #barcode2, #plu, #partnerid, #catalog, #measure, #measure2, #ratio, #price, #minqtty, #normalqtty, #description, #isrecipe, #taxgroup, #type, #groupid, #deleted)";
        public static final String UPDATE_ITEMS = "UPDATE items SET id = #id, code = #code, name = #name, barcode = #barcode, barcode2 = #barcode2, plu = #plu, partnerid = #partnerid, catalog = #catalog, measure = #measure, measure2 = #measure2, ratio = #ratio, price = #price, minqtty = #minqtty, normalqtty = #normalqtty, description = #description, isrecipe = #isrecipe, taxgroup = #taxgroup, type = #type, groupid = #groupid, deleted = #deleted WHERE id = #id";
        public static final String UPDATE_ITEM_PRICES = "UPDATE items SET price = #price WHERE id = #id";
        public static final String DELETE_ITEMS = "DELETE FROM items WHERE id = #id";
        public static final String LIKE_ITEMS = " items.itemscode LIKE '%#text%' OR items.name LIKE '%#text%' OR items.barcode LIKE '%#text%' OR items.barcode2 LIKE '%#text%' OR items.plu LIKE '%#text%' OR items.catalog LIKE '%#text%' OR items.description LIKE '%#text%' ";
        public static final String GET_ITEMGROUPS_LIST = "SELECT id, code, name FROM itemgroups";
        public static final String ITEM_ID = "itemid";
        public static final String ITEM_GROUP = "itemgroups";

        public static final String GET_OBJECTS_LIST = "SELECT id, code, name, pricegroup, type, groupid, deleted FROM objects";
        public static final String INSERT_OBJECTS = "INSERT INTO objects (code, name, pricegroup, type, groupid, deleted) VALUES (#code, #name, #pricegroup, #type, #groupid, #deleted)";
        public static final String UPDATE_OBJECTS = "UPDATE objects SET id = #id, code = #code, name = #name, pricegroup = #pricegroup, type = #type, groupid = #groupid, deleted = #deleted WHERE id = #id";
        public static final String DELETE_OBJECTS = "DELETE FROM objects WHERE id = #id";
        public static final String LIKE_OBJECTS = " objects.code LIKE '%#text%' OR objects.name LIKE '%#text%' ";
        public static final String GET_OBJECTGROUPS_LIST = "SELECT id, code, name FROM objectgroups";
        public static final String OBJECT_ID = "objectid";
        public static final String OBJECT_GROUP = "objectgroups";

        public static final String GET_USERS_LIST = "SELECT id, code, name, password, card, level, type, groupid, deleted FROM users";
        public static final String INSERT_USERS = "INSERT INTO users (code, name, password, card, level, type, groupid, deleted) VALUES (#code, #name, #password, #card, #level, #type, #groupid, #deleted)";
        public static final String UPDATE_USERS = "UPDATE users SET id = #id, code = #code, name = #name, password = #password, card = #card, level = #level, type = #type, groupid = #groupid, deleted = #deleted WHERE id = #id";
        public static final String DELETE_USERS = "DELETE FROM users WHERE id = #id";
        public static final String LIKE_USERS = " objects.code LIKE '%#text%' OR objects.name LIKE '%#text%' OR objects.card LIKE '%#text%' ";
        public static final String GET_USERGROUPS_LIST = "SELECT id, code, name FROM usergroups";
        public static final String USER_ID = "userid";
        public static final String USER_GROUP = "usergroups";

        public static final String GET_MEASURES1_LIST = "SELECT id, code, name, type, groupid, deleted FROM measures WHERE type = 0";
        public static final String GET_MEASURES2_LIST = "SELECT id, code, name, type, groupid, deleted FROM measures WHERE type = 1";

        public static final String GET_TAXGROUPS_LIST = "SELECT id, code, name, value, type, groupid, deleted FROM taxgroups";

        public static final String GET_CURRENCIES_LIST = "SELECT id, code, name, value, type, groupid, deleted FROM currencies";

        public static final String InsertCashBook = "INSERT INTO cashbook (date, desc, opertype, sign, profit, userid, userrealtime, objectid) VALUES " +
                                                    "(date('now'), #oname || ' No.0000' || '#acct, ' || #pname, #opr, #sign, #total, #uid, datetime('now'), #oid)";
        public static final String InsertOperations = "INSERT INTO operations (opertype, acct, itemid, partnerid, objectid, operatorid, qtty, sign, pricein, priceout, discount, currencyid, currencyrate, date, userid, userrealtime, lotid, lot, note, srcdocid) VALUES" +
                                                    "(#oper, #acct, #gid, #pid, #oid, #uid, #qtty, #sign, #pin * (1 - #disc / 100), #pout, #disc, #cid, #crate, date('now'), #uid, datetime('now'), 1, '', '', 0)";
        public static final String InsertOperationsST = "INSERT INTO operations (opertype, acct, itemid, partnerid, objectid, operatorid, qtty, sign, pricein, priceout, discount, currencyid, currencyrate, date, userid, userrealtime, lotid, lot, note, srcdocid) VALUES" +
                                                        "(#oper, #acct, #gid, #pid, #oid, #uid, #qttyR, #sign, #pin * (1 - #disc / 100), #pout, #disc, #cid, #crate, date('now'), #uid, datetime('now'), 1, '', '', 1)," +
                                                        "(#oper, #acct, #gid, #pid, #oid, #uid, #qttyW, #sign, #pin * (1 - #disc / 100), #pout, #disc, #cid, #crate, date('now'), #uid, datetime('now'), 1, '', '', 2)";
        public static final String InsertOperationsT = "INSERT INTO operations (opertype, acct, itemid, partnerid, objectid, operatorid, qtty, sign, pricein, priceout, discount, currencyid, currencyrate, date, userid, userrealtime, lotid, lot, note, srcdocid) VALUES" +
                                                        "(#oper1, #acct, #gid, #pid, #oid1, #uid, #qtty, #sign1, #pin * (1 - #disc / 100), #pout, #disc, #cid, #crate, date('now'), #uid, datetime('now'), 1, '', '', 0)," +
                                                        "(#oper2, #acct, #gid, #pid, #oid2, #uid, #qtty, #sign2, #pin * (1 - #disc / 100), #pout, #disc, #cid, #crate, date('now'), #uid, datetime('now'), 1, '', '', 0)";
        public static final String InsertPayments = "INSERT INTO payments (acct, opertype, partnerid, objectid, qtty, mode, sign, date, userid, userrealtime, type, transactionnumber, enddate) VALUES" +
                                                    "(#acct, #oper, #pid, #oid, #total, #mode, #sign, date('now'), #uid, datetime('now'), #type, '', date('now'))";
        public static final String UpdateStore = "UPDATE store SET qtty = qtty + #qtty WHERE itemid = #gid AND objectid = #oid";

        public static final String GET_MAX_ACCT = "SELECT value FROM accts WHERE id = #id";
        public static final String UPDATE_MAX_ACCT = "UPDATE accts SET value = value + 1 WHERE id = #id";

        public static final String GET_DOCUMENTS =
                "SELECT partners.code AS Code,\n" +
                "       partners.name AS Name,\n" +
                "       SUM(operations.qtty) AS Qtty,\n" +
                "       CASE WHEN payments.mode = 1 THEN SUM(payments.qtty) ELSE 0 END AS Pay,\n" +
                "       SUM(operations.pricein * operations.qtty) AS AmountIn,\n" +
                "       SUM(operations.priceout * operations.qtty) AS AmountOut,\n" +
                "       payments.acct AS Acct,\n" +
                "       payments.date AS Date\n" +
                "FROM payments\n" +
                "JOIN partners ON partners.id = payments.partnerid\n" +
                "JOIN operations ON operations.acct = payments.acct AND\n" +
                "       operations.opertype = payments.opertype";
        public static final String GET_REPORT_OPERATIONS = "SELECT operations.userrealtime, operations.acct, operations.itemid, items.code, items.name, items.groupid, itemgroups.name AS `group`, items.measure AS measureid, measures.name AS Measure, operations.qtty, operations.discount, operations.pricein, operations.qtty * operations.pricein AS amountin, operations.priceout, operations.qtty * operations.priceout AS amountout, (operations.priceout - operations.pricein) * operations.qtty AS amountprofit, operations.partnerid, partners.code, partners.name AS partner, operations.userid, users.code, users.name AS user, operations.objectid, objects.code, objects.name AS object, operations.opertype, opertype.en AS opertype FROM operations JOIN partners ON partners.id = operations.partnerid JOIN items ON items.id = operations.itemid JOIN itemgroups ON items.groupid = itemgroups.id JOIN measures ON items.measure = measures.id JOIN objects ON objects.id = operations.objectid JOIN users ON users.id = operations.userid JOIN opertype ON opertype.id = operations.opertype #where;";
    }

    public class MySQL{
        public static final String GET_PARTNERS_LIST = "SELECT id, code, company as name, mol, city, address, phone, fax, email, taxno, bulstat as vatno, pricegroup, discount, userid, userrealtime, cardnumber, paymentdays, type, groupid, deleted FROM partners";
        public static final String INSERT_PARTNER = SQLite.INSERT_PARTNER;
        public static final String UPDATE_PARTNER = SQLite.UPDATE_PARTNER;
        public static final String DELETE_PARTNER = SQLite.DELETE_PARTNER;
        public static final String LIKE_PARTNER = SQLite.LIKE_PARTNER;
        public static final String GET_PARTNER_GROUPS_LIST = "SELECT id, code, name FROM partnersgroups";
        public static final String PARTNER_ID = "partnerid";
        public static final String PARTNER_GROUP = "partnergroups";

        public static final String GET_ITEMS_LIST = "SELECT id, code, name, concat(barcode1, ':', barcode2, ':', replace(barcode3, ',', ':')), '' as barcode2, catalog1 as plu, catalog3 as partnerid, catalog2 as catalog, if(catalog1 != '', 2, 1) as measure, 0 as measure2, ratio, concat(pricein, ':', priceout2, ':', priceout1, ':', priceout3, ':', priceout4, ':', priceout5, ':', priceout6, ':', priceout7, ':', priceout8) as price, minqtty, normalqtty, description, isrecipe, taxgroup, type, groupid, deleted FROM goods";
        public static final String INSERT_ITEMS = SQLite.INSERT_ITEMS;
        public static final String UPDATE_ITEMS = SQLite.UPDATE_ITEMS;
        public static final String DELETE_ITEMS = SQLite.DELETE_ITEMS;
        public static final String LIKE_ITEMS = SQLite.LIKE_ITEMS;
        public static final String GET_ITEMGROUPS_LIST = "SELECT id, code, name FROM goodsgroups";
        public static final String ITEM_ID = "goodid";
        public static final String ITEM_GROUP = "goodsgroups";

        public static final String GET_OBJECTS_LIST = "SELECT id, code, name, pricegroup, 0 as type, groupid, deleted FROM objects";
        public static final String INSERT_OBJECTS = SQLite.INSERT_OBJECTS;
        public static final String UPDATE_OBJECTS = SQLite.UPDATE_OBJECTS;
        public static final String DELETE_OBJECTS = SQLite.DELETE_OBJECTS;
        public static final String LIKE_OBJECTS = SQLite.LIKE_OBJECTS;
        public static final String GET_OBJECTGROUPS_LIST = "SELECT id, code, name FROM objectsgroups";
        public static final String OBJECT_ID = "objectid";
        public static final String OBJECT_GROUP = "objectgroups";

        public static final String GET_USERS_LIST = "SELECT id, code, name, password, cardnumber as card, userlevel as level, 0 as type, groupid, deleted FROM users";
        public static final String INSERT_USERS = SQLite.INSERT_USERS;
        public static final String UPDATE_USERS = SQLite.UPDATE_USERS;
        public static final String DELETE_USERS = SQLite.DELETE_USERS;
        public static final String LIKE_USERS = SQLite.LIKE_USERS;
        public static final String GET_USERGROUPS_LIST = "SELECT id, code, name FROM usersgroups";
        public static final String USER_ID = "userid";
        public static final String USER_GROUP = "usergroups";

        public static final String GET_MEASURES1_LIST = "";
        public static final String GET_MEASURES2_LIST = "";

        public static final String GET_TAXGROUPS_LIST = "";

        public static final String GET_CURRENCIES_LIST = "";

        public static final String InsertCashBook = "INSERT INTO cashbook (date, desc, opertype, sign, profit, userid, userrealtime, objectid) VALUES " +
                "(CURDATE(), CONCAT(#oname, ' No.0000', '#acct, ', #pname), #opr, #sign, #total, #uid, NOW(), #oid)";
        public static final String InsertOperations = "INSERT INTO operations (opertype, acct, itemid, partnerid, objectid, operatorid, qtty, sign, pricein, priceout, discount, currencyid, currencyrate, date, userid, userrealtime, lotid, lot, note, srcdocid) VALUES" +
                "(#oper, #acct, #gid, #pid, #oid, #uid, #qtty, #sign, #pin * (1 - #disc / 100), #pout, #disc, #cid, #crate, CURDATE(), #uid, NOW(), 1, '', '', 0)";
        public static final String InsertOperationsST = "INSERT INTO operations (opertype, acct, itemid, partnerid, objectid, operatorid, qtty, sign, pricein, priceout, discount, currencyid, currencyrate, date, userid, userrealtime, lotid, lot, note, srcdocid) VALUES" +
                "(#oper, #acct, #gid, #pid, #oid, #uid, #qttyR, #sign, #pin * (1 - #disc / 100), #pout, #disc, #cid, #crate, CURDATE(), #uid, NOW(), 1, '', '', 1)," +
                "(#oper, #acct, #gid, #pid, #oid, #uid, #qttyW, #sign, #pin * (1 - #disc / 100), #pout, #disc, #cid, #crate, CURDATE(), #uid, NOW(), 1, '', '', 2)";
        public static final String InsertOperationsT = "INSERT INTO operations (opertype, acct, itemid, partnerid, objectid, operatorid, qtty, sign, pricein, priceout, discount, currencyid, currencyrate, date, userid, userrealtime, lotid, lot, note, srcdocid) VALUES" +
                "(#oper1, #acct, #gid, #pid, #oid1, #uid, #qtty, #sign1, #pin * (1 - #disc / 100), #pout, #disc, #cid, #crate, CURDATE(), #uid, NOW(), 1, '', '', 0)," +
                "(#oper2, #acct, #gid, #pid, #oid2, #uid, #qtty, #sign2, #pin * (1 - #disc / 100), #pout, #disc, #cid, #crate, CURDATE(), #uid, NOW(), 1, '', '', 0)";
        public static final String InsertPayments = "INSERT INTO payments (acct, opertype, partnerid, objectid, qtty, mode, sign, date, userid, userrealtime, type, transactionnumber, enddate) VALUES" +
                "(#acct, #oper, #pid, #oid, #total, #mode, #sign, CURDATE(), #uid, NOW(), #type, '', CURDATE())";
        public static final String UpdateStore = "UPDATE store SET qtty = qtty + #qtty WHERE itemid = #gid AND objectid = #oid";

        public static final String GET_MAX_ACCT = "";
        public static final String UPDATE_MAX_ACCT = "";

        public static final String GET_DOCUMENTS = "";
    }
}
