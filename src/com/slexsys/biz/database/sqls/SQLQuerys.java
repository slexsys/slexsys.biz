package com.slexsys.biz.database.sqls;

import com.slexsys.biz.database.comp.Convert;
import com.slexsys.biz.database.items.iItem;
import com.slexsys.biz.database.sqls.comp.QueryItemGroup;
import com.slexsys.biz.main.comp.NewTypes.DateTime;
import com.slexsys.biz.main.comp.enums.PayType;
import com.slexsys.biz.main.finance.comp.PayInfo;
import com.slexsys.biz.main.finance.comp.comp.EditPaymentsData;
import com.slexsys.biz.main.finance.comp.comp.EditPaymentsRowData;
import com.slexsys.biz.main.operations.comp.OperItem;
import com.slexsys.biz.main.operations.comp.OperationData;
import com.slexsys.biz.main.operations.json.InsertionMode;
import com.slexsys.biz.main.operations.json.PriceUpdate;
import com.slexsys.biz.main.operations.json.QttyMode;

import java.util.LinkedList;
import java.util.List;

/**
 * Created by slexsys on 12/19/17.
 */


public class SQLQuerys {
    public static class Operations {
        public static class Select {
            public static String MaxAcct;
            public static String ItemByCodeBarcode;
        }

        public static class Insert {
            public static class Operation {
                public static String Other;
                public static String StockTacking;
                public static String Transfer;
            }

            public static String Cashbook;
            public static String Payments;
        }

        public static class Update {
            public static String MaxAcct;
            public static String Store;
            public static class Payments {
                public static String ByID;
            }
        }

        public static class Delete {
            public static class Payments {
                public static String ByID;
            }
        }

        public static List<String> getSaveOperationsQuerys(OperationData data) {
            List<String> queries = new LinkedList<String>();
            queries.addAll(GetQueryInsertIntoCashbook(data));
            queries.addAll(GetQueryInsertIntoOperation(data));
            queries.addAll(GetQueryInsertIntoPayments(data));
            queries.addAll(GetQueryUpdateStore(data));
            return queries;
        }

        private static List<String> GetQueryInsertIntoCashbook(OperationData data) {
            List<String> query = new LinkedList<String>();
            if (data.isFinancialOperation()) {
                if (data.getPayinfo() != null &&
                        data.getPayinfo().type.equals(PayType.Cash) &&
                        data.getPayinfo().payed != 0) {
                    query.add(getQueryInsertIntoCashBook(data,
                            data.getDateString(),
                            data.getPayed()));
                } else if (data.getEditedPayment() != null) {
                    for (EditPaymentsRowData row : data.getEditedPayment().getList()) {
                        if (row.getPaytype().equals(PayType.Cash) && row.getType() == "1") {
                            query.add(getQueryInsertIntoCashBook(data,
                                    row.getDateTime().toSQLFormatDate(),
                                    Double.toString(row.getSum())));
                        }
                    }
                }
            }
            return query;
        }

        private static String getQueryInsertIntoCashBook(OperationData data, String date, String sum) {
            return Insert.Cashbook
                    .replace("#date", date)
                    .replace("#realtime", data.getRealTimeString())
                    .replace("#opr", data.getOperTypeCashBook())
                    .replace("#oper", data.getOperType())
                    .replace("#oname", Convert.ToSQLString(data.getOperationName()))
                    .replace("#acct", data.getAcct())
                    .replace("#pid", data.getPartnerID())
                    .replace("#pname", Convert.ToSQLString(data.getPartnerName()))
                    .replace("#oid", data.getObjectID())
                    .replace("#uid", data.getUserID())
                    .replace("#sign", data.getPaySign())
                    .replace("#total", sum);
        }

        private static List<String> GetQueryInsertIntoOperation(OperationData data) {
            List<String> queries = new LinkedList<String>();
            InsertionMode insertionMode = data.getOperJSON().getInsertionMode();
            if (data.isDocument()) {
                queries.add(Documents.Update.Store.ByAcctAndOperType
                        .replace("#acct", data.getAcct())
                        .replace("#oper", data.getOperType()));
                queries.add(Documents.Delete.Operations.ByAcctAndOperType
                        .replace("#acct", data.getAcct())
                        .replace("#oper", data.getOperType()));
            }
            for (OperItem item : data.getList()) {
                String operation;
                switch (insertionMode) {
                    case StockTacking:
                        operation = Insert.Operation.StockTacking
                                .replace("#oper", data.getOperType())
                                .replace("#oid", data.getObjectID())
                                .replace("#qttyW", Convert.ToString(item.Qtty))
                                .replace("#qttyR", Convert.ToString(item.QttyR))
                                .replace("#sign", data.getSignOper());
                        break;
                    case Transfer:
                        operation = Insert.Operation.Transfer
                                .replace("#oper1", data.getOperType())
                                .replace("#oper2", data.getOperType2())
                                .replace("#oid1", data.getObjectID())
                                .replace("#oid2", data.getObjectID2())
                                .replace("#qtty", Convert.ToString(item.Qtty))
                                .replace("#sign1", data.getSignOper())
                                .replace("#sign2", data.getSignOper2());
                        break;
                    case Single:
                    default:
                        operation = Insert.Operation.Other
                                .replace("#oper", data.getOperType())
                                .replace("#oid", data.getObjectID())
                                .replace("#qtty", Convert.ToString(item.Qtty))
                                .replace("#sign", data.getSignOper());
                        break;
                }
                queries.add(operation
                        .replace("#date", data.getDateString())
                        .replace("#realtime", data.getRealTimeString())
                        .replace("#acct", data.getAcct())
                        .replace("#gid", Convert.ToString(item.ID))
                        .replace("#pid", data.getPartnerID())
                        .replace("#uid", data.getUserID())
                        .replace("#pin", Convert.ToString(item.PriceIn))
                        .replace("#pout", Convert.ToString(item.PriceOut))
                        .replace("#disc", Convert.ToString(item.Discount))
                        .replace("#cid", Convert.ToString(item.CurrencyID))
                        .replace("#crate", data.getCurrencyRate()));
            }
            queries.addAll(getPricesUpdateQuerys(data));
            return queries;
        }

        private static List<String> getPricesUpdateQuerys(OperationData data) {
            List<String> result = new LinkedList<String>();
            PriceUpdate priceUpdate = data.getOperJSON().getPriceUpdate();
            if (priceUpdate != null) {
                switch (priceUpdate) {
                    case PriceIn:
                        result.addAll(iItem.getPricesUpdateQuerys(data,
                                Others.Update.Items.PriceIn));
                        break;
                    case PriceOut:
                        result.addAll(iItem.getPricesUpdateQuerys(data,
                                Others.Update.Items.PriceOut));
                        break;
                    case PriceInOut:
                        result.addAll(iItem.getPricesUpdateQuerys(data,
                                Others.Update.Items.PriceInOut));
                        break;
                }
            }
            return result;
        }

        private static List<String> GetQueryInsertIntoPayments(OperationData data) {
            List<String> queries;
            if (data.getPayinfo() != null) {
                queries = GetQueryInsertIntoPaymentsPayInfo(data);
            } else {
                queries = GetQueryInsertIntoPaymentsEditedPayment(data.getEditedPayment());
            }
            return queries;
        }

        private static List<String> GetQueryInsertIntoPaymentsPayInfo(OperationData data) {
            List<String> queries = new LinkedList<String>();
            if (data.isFinancialOperation()) {
                queries.add(getPaymentInsert(data, data.getDateString(), data.getDateEndString(), "-1",
                        data.getPayType(), data.getTotalString()));

                if (data.getPayinfo().payed != 0) {
                    queries.add(getPaymentInsert(data, data.getDateString(), data.getDateEndString(), "1",
                            data.getPayType(), data.getPayed()));
                }
            }
            return queries;
        }

        public static List<String> GetQueryInsertIntoPaymentsEditedPayment(EditPaymentsData data) {
            List<String> queries = new LinkedList<String>();
            for (EditPaymentsRowData row : data.getList()) {
                if (row.getChanged()) {
                    if (row.getSum() != 0) {
                        if (row.getId() != 0) {
                            queries.add(Update.Payments.ByID.replace("#id", Integer.toString(row.getId()))
                                    .replace("#date", row.getDateTime().toSQLFormatDate())
                                    .replace("#enddate", data.getEnddate())
                                    .replace("#type", row.getPaytype().getValueString())
                                    .replace("#qtty", Double.toString(row.getSum())));
                        } else {
                            queries.add(getPaymentInsert(data, "1",
                                    row.getDateTime().toSQLFormatDate(),
                                    data.getEnddate(),
                                    row.getPaytype().getValueString(),
                                    Double.toString(row.getSum())));
                        }
                    } else {
                        queries.add(Delete.Payments.ByID.replace("#id", Integer.toString(row.getId())));
                    }
                }
            }
            return queries;
        }

        private static List<String> GetQueryUpdateStore(OperationData data) {
            List<String> queries = new LinkedList<String>();
            QttyMode qttyMode = data.getOperJSON().getQttyMode();
            if (qttyMode != QttyMode.None) {
                InsertionMode insertionMode = data.getOperJSON().getInsertionMode();
                for (OperItem item : data.getList()) {

                    queries.add(Update.Store
                            .replace("#oid", data.getObjectID())
                            .replace("#gid", Convert.ToString(item.ID))
                            .replace("#qtty", data.getQttySigned(item.Qtty, item.QttyR)));

                    if (insertionMode.equals(InsertionMode.Transfer)) {
                        queries.add(Update.Store
                                .replace("#oid", data.getObjectID2())
                                .replace("#gid", Convert.ToString(item.ID))
                                .replace("#qtty", Convert.ToString(item.Qtty)));
                    }
                }
            }
            return queries;
        }

        private static String getPaymentInsert(OperationData data, String date, String enddate, String mode, String paytype, String total) {
            String[] array = new String[] {
                    date,
                    enddate,
                    data.getRealTimeString(),
                    data.getOperType(),
                    data.getAcct(),
                    data.getPartnerID(),
                    data.getObjectID(),
                    data.getUserID(),
                    mode,
                    data.getPaySign(),
                    paytype,
                    total
            };
            return getPaymentInsert(array);
        }

        private static String getPaymentInsert(EditPaymentsData data, String date, String enddate, String mode, String paytype, String total) {
            String[] array = new String[] {
                    date,
                    enddate,
                    data.getRealTimeString(),
                    data.getOperType(),
                    data.getAcct(),
                    data.getPartnerid(),
                    data.getObjectid(),
                    data.getUserid(),
                    mode,
                    data.getSign(),
                    paytype,
                    total
            };
            return getPaymentInsert(array);
        }

        private static String getPaymentInsert(String... array) {
            int i = 0;
            return Insert.Payments
                    .replace("#date", array[i++])
                    .replace("#enddate", array[i++])
                    .replace("#realtime", array[i++])
                    .replace("#oper", array[i++])
                    .replace("#acct", array[i++])
                    .replace("#pid", array[i++])
                    .replace("#oid", array[i++])
                    .replace("#uid", array[i++])
                    .replace("#mode", array[i++])
                    .replace("#sign", array[i++])
                    .replace("#type", array[i++])
                    .replace("#total", array[i++]);
        }
    }

    public static class Edit {
        public static QueryItemGroup All;
        public static QueryItemGroup Partners;
        public static QueryItemGroup Items;
        public static QueryItemGroup Objects;
        public static QueryItemGroup Users;
        public static QueryItemGroup Measures1;
        public static QueryItemGroup Measures2;
        public static QueryItemGroup TaxGroups;
        public static QueryItemGroup Currencies;

        static {
            All = new QueryItemGroup();
            Partners = new QueryItemGroup();
            Items = new QueryItemGroup();
            Objects = new QueryItemGroup();
            Users = new QueryItemGroup();
            Measures1 = new QueryItemGroup();
            Measures2 = new QueryItemGroup();
            TaxGroups = new QueryItemGroup();
            Currencies = new QueryItemGroup();
        }
    }

    public static class Documents {

        public static class Select {
            public static String Documents;
            public static String Operations;
            public static String Table;
        }

        public static class Update {
            public static class Store {

                public static String ByAcctAndOperType;
            }
        }

        public static class Delete {
            public static class Operations {
                public static String ByAcctAndOperType;
            }
            public static class Payments {
                public static String ByAcctAndOperType;
            }
        }
    }

    public static class Finances {
        public static class Select {
            public static class Payments {
                public static String List;
                public static String ByAcctAndOPerType;
                public static String Table;
            }
        }
        public static class Insert {
            public static String Payments;
        }

        public static String getPaymentInsert(PayInfo payInfo, String type, String acct) {
            return Finances.Insert.Payments
                    .replace("#date", payInfo.date.toSQLFormatDate())
                    .replace("#realtime", DateTime.now().toSQLFormatDateTime())
                    .replace("#oper", type)
                    .replace("#acct", acct)
                    .replace("#mode", "1")
                    .replace("#type", payInfo.type.getValueString())
                    .replace("#total", Double.toString(payInfo.payed));
        }
    }

    public static class Reports {
        public static class Select {
            public static String Operations;
            public static String getReports;

            public static class Items {
                public static String InStock;
                public static String Flow;
            }
            public static class Payments {
                public static String ByPartners;
                public static String ByDocuments;
                public static String Flow;
            }
            public static class DayFlow {
                public static String Sale;
                public static String Refund;
                public static String Bonus;
            }
            public static String FlowInPeriod;
            public static String ProfitInPeriod;
            public static String ConsumptionInPeriod;
        }
    }

    public static class Options { }

    public static class Others {
        public static class Update {
            public static class Items {
                public static String PriceIn;
                public static String PriceOut;
                public static String PriceInOut;
            }
        }
    }

    public static class Cashbook {
        public static class Select {
            public static String Consumptions;
            public static String Table;
        }
        public static class Insert {
            public static String New;
        }
        public static class Update {
            public static String ById;
        }
        public static class Delete {
            public static String ById;
        }
    }
}

