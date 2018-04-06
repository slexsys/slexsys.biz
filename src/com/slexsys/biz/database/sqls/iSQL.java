package com.slexsys.biz.database.sqls;

import android.app.Activity;
import android.util.Log;

import com.slexsys.biz.GO;
import com.slexsys.biz.comp.LoginInfo;
import com.slexsys.biz.database.items.iItem;
import com.slexsys.biz.database.sqls.SQLTables.SQLTables;
import com.slexsys.biz.database.sqls.MySQL.MySQLInfo;
import com.slexsys.biz.database.sqls.MySQL.MySQLWorker;
import com.slexsys.biz.database.sqls.SQLite.SQLiteWorker;
import com.slexsys.biz.main.comp.COPDRF.COPDRFRows;
import com.slexsys.biz.main.comp.NewTypes.DataRow;
import com.slexsys.biz.main.comp.NewTypes.DataTable;
import com.slexsys.biz.main.comp.enums.OperType;
import com.slexsys.biz.main.finance.comp.NEConsumption;
import com.slexsys.biz.main.finance.comp.PayInfo;
import com.slexsys.biz.main.finance.comp.comp.EditPaymentsData;
import com.slexsys.biz.main.operations.comp.OperationData;
import com.slexsys.biz.main.operations.json.OperationGenerator;
import com.slexsys.biz.main.operations.json.OperationsJSON;
import com.slexsys.biz.main.report.comp.UIFilters;
import com.slexsys.biz.main.report.json.ReportGenerator;
import com.slexsys.biz.main.report.json.ReportsJSON;

import java.util.LinkedList;
import java.util.List;

/**
 * Created by slexsys on 3/12/16.
 */
public class iSQL {
    //region fields
    private static SQLWorker worker;
    private static Activity activity;
    //endregion

    public static void Init(String path){
        worker = new SQLiteWorker(path);
    }

    public static void Init(MySQLInfo info){
        worker = new MySQLWorker(info, activity);
    }

    public static void setActivity(Activity activity) {
        iSQL.activity = activity;
    }

    public static boolean update(String query) {
        return worker.execQuery(query);
    }

    public static boolean SaveOperation(OperationData data) {
        List<String> queries = SQLQuerys.Operations.getSaveOperationsQuerys(data);
        return worker.execQuerys(queries);
    }

    public static String getNextAcct(int opertype) {
        String letter = OperType.getLetter(opertype);   //mysql
        String oper = Integer.toString(opertype);       //sqlite
        String query = SQLQuerys.Operations.Select.MaxAcct
                        .replace("#letter", letter)     //mysql
                        .replace("#id", oper);          //sqlite
        String acct = worker.execScalar(query);
        query = SQLQuerys.Operations.Update.MaxAcct
                        .replace("#letter", letter)     //mysql
                        .replace("#acct", acct)         //mysql
                        .replace("#id", oper);          //sqlite
        worker.execQuery(query);//nayel krknvelu depqy, dublicate
        return acct;
    }

    public static COPDRFRows getDocuments(UIFilters filters) {
        String table = SQLQuerys.Documents.Select.Table;
        String query = SQLQuerys.Documents.Select.Documents;
        DataTable dataTable = getQueryResult(filters, table, query);
        return COPDRFRows.forDocuments(dataTable);
    }

    public static boolean removeDocument(String opertype, String acct) {
        List<String> querys = new LinkedList<String>();
        querys.add(SQLQuerys.Documents.Update.Store.ByAcctAndOperType.replace("#oper", opertype).replace("#acct", acct));
        querys.add(SQLQuerys.Documents.Delete.Payments.ByAcctAndOperType.replace("#oper", opertype).replace("#acct", acct));
        querys.add(SQLQuerys.Documents.Delete.Operations.ByAcctAndOperType.replace("#oper", opertype).replace("#acct", acct));
        return worker.execQuerys(querys);
    }

    public static COPDRFRows getConsumption(UIFilters filters) {
        String table = SQLQuerys.Cashbook.Select.Table;
        String query = SQLQuerys.Cashbook.Select.Consumptions;
        DataTable dataTable = getQueryResult(filters, table, query);
        return COPDRFRows.forCashbook(dataTable);
    }

    public static boolean addConsumption(LinkedList<String> list) {
        String query = SQLQuerys.Cashbook.Insert.New
                .replace("#date", list.get(NEConsumption.POS_DATE))
                .replace("#desc", list.get(NEConsumption.POS_DESC))
                .replace("#type", list.get(NEConsumption.POS_TYPE))
                .replace("#sum", list.get(NEConsumption.POS_SUM))
                .replace("#sign", list.get(NEConsumption.POS_SIGN))
                .replace("#uid", list.get(NEConsumption.POS_USER_ID))
                .replace("#oid", list.get(NEConsumption.POS_OBJECT_ID));
        return worker.execQuery(query);
    }

    public static boolean editConsumption(LinkedList<String> list) {
        String query = SQLQuerys.Cashbook.Update.ById
                .replace("#date", list.get(NEConsumption.POS_DATE))
                .replace("#desc", list.get(NEConsumption.POS_DESC))
                .replace("#type", list.get(NEConsumption.POS_TYPE))
                .replace("#sum", list.get(NEConsumption.POS_SUM))
                .replace("#sign", list.get(NEConsumption.POS_SIGN))
                .replace("#uid", list.get(NEConsumption.POS_USER_ID))
                .replace("#oid", list.get(NEConsumption.POS_OBJECT_ID))
                .replace("#id", list.get(NEConsumption.POS_ID));
        return worker.execQuery(query);
    }

    public static boolean removeConsumption(String id) {
        String query = SQLQuerys.Cashbook.Delete.ById.replace("#id", id);
        return worker.execQuery(query);
    }

    public static COPDRFRows getPayments(UIFilters filters) {
        String table = SQLQuerys.Finances.Select.Payments.Table;
        String query = SQLQuerys.Finances.Select.Payments.List
                .replace("#lang", LoginInfo.lang);
        DataTable dataTable = getQueryResult(filters, table, query);
        return COPDRFRows.forPayments(dataTable);
    }

    public static EditPaymentsData getPayment(String operType, String acct) {
        String query = SQLQuerys.Finances.Select.Payments.ByAcctAndOPerType
                .replace("#oper", operType)
                .replace("#acct", acct);
        DataTable dataTable = worker.execReader(query);
        return new EditPaymentsData(dataTable, acct, operType);
    }

    public static boolean addPayment(PayInfo payInfo, String type, String acct) {
        String query = SQLQuerys.Finances.getPaymentInsert(payInfo, type, acct);
        return worker.execQuery(query);
    }

    public static boolean doGroupPayment(PayInfo payInfo, DataTable data) {
        return false;
    }

    public static boolean updateEditedPayment(EditPaymentsData data) {
        List<String> queries = SQLQuerys.Operations.GetQueryInsertIntoPaymentsEditedPayment(data);
        return worker.execQuerys(queries);
    }

    public static OperationData getOperationDataByAcctAndOperType(int operJSONPos, String acct) {
        String operType = GO.operations.get(operJSONPos).getOperType();
        String query = SQLQuerys.Documents.Select.Operations
                                    .replace("#oper", operType)
                                    .replace("#acct", acct);
        DataTable dataTable = worker.execReader(query);
        return new OperationData(dataTable, operJSONPos, acct);
    }

    public static List<iItem> getItemByCodeBarcode(String text) {
        List<iItem> result = new LinkedList<iItem>();
        String oid = Integer.toString(LoginInfo.object.getId());
        String query = SQLQuerys.Operations.Select.ItemByCodeBarcode
                                .replace("#value", text)
                                .replace("#oid", oid);
        DataTable dataTable = worker.execReader(query);
        if (dataTable != null) {
            for (int i = 0; i < dataTable.size(); ++i) {
                result.add(new iItem(dataTable.getRowWithNames(i)));
            }
        }
        return result;
    }

    public static OperationsJSON getOperationsJSON() {
        return OperationGenerator.generateOperations();
    }

    public static ReportsJSON getReportsJSON() {
        //return iSQLite.getReportsJSON();
        return ReportGenerator.getReportsMySQL();
    }

    public static DataTable getDataTable(String query) {
        return worker.execReader(query);
    }

    public static DataRow getDataRow(String query) {
        DataRow row = null;
        DataTable dataTable = worker.execReader(query);
        if (dataTable != null) {
            row = dataTable.getFirstWithNames();
        }
        return row;
    }

    public static String getScalar(String query) {
        return worker.execScalar(query);
    }

    public static SQLTables getSQLTables() {
        return worker.getSqlTables();
    }

    //region private methods
    private static DataTable getQueryResult(UIFilters filters, String table, String query) {
        filters.setMainTable(table);
        String where = filters.getWhereFilters();
        Log.e("WHERE", where);
        query = query.replace("#where", where);
        Log.e("Query - > ", "[" + query + "]");
        return worker.execReader(query);
    }
    //endregion
}
