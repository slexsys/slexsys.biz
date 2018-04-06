package com.slexsys.biz.main.comp.COPDRF;

import android.database.Cursor;

import com.slexsys.biz.main.comp.NewTypes.DataRow;
import com.slexsys.biz.main.comp.NewTypes.DateTime;
import com.slexsys.biz.main.finance.comp.comp.EditPaymentsRowData;

import java.util.LinkedList;

/**
 * Created by slexsys on 3/8/17.
 */

public class COPDRFRow extends LinkedList<COPDRFCell> {

    //region positions
    //region documents
    //endregion

    //region payments
    public static int POS_PAYMENTS_DATE;
    public static int POS_PAYMENTS_ACCT;
    public static int POS_PAYMENTS_PARTNER_NAME;
    public static int POS_PAYMENTS_OPER_NAME;
    public static int POS_PAYMENTS_TO_PAY;
    public static int POS_PAYMENTS_PAYED;
    public static int POS_PAYMENTS_DIFFERENCE;
    public static int POS_PAYMENTS_PARTNER_ID;
    public static int POS_PAYMENTS_OPER_TYPE;
    //endregion

    //region consumption
    public static int POS_CONSUMPTION_DATE;
    public static int POS_CONSUMPTION_DESC;
    public static int POS_CONSUMPTION_SUM_IN;
    public static int POS_CONSUMPTION_SUM_OUT;
    public static int POS_CONSUMPTION_DATE_SQL;
    public static int POS_CONSUMPTION_ID;
    public static int POS_CONSUMPTION_OPER_TYPE;
    //endregion
    //endregion

    //region public methods
    public static COPDRFRow forEditPayments(EditPaymentsRowData item) {
        COPDRFRow result = new COPDRFRow();
        result.add(new COPDRFCell(item.getDateTime().toNormalDateFormat(), CellIndex.fromInts(0, 0, 0)));

        result.add(new COPDRFCell(item.getType(), CellIndex.fromInts(0, 1, 1)));
        result.add(new COPDRFCell(item.getPaytype().name(), CellIndex.fromInts(0, 1, 2)));

        result.add(new COPDRFCell(Double.toString(item.getSum()), CellIndex.fromInts(0, 2, 3)));
        return result;
    }

    public static COPDRFRow forCashbook(Cursor cursor) {
        int i = 0;
        String[] array = new String[] {
                cursor.getString(i++),

                cursor.getString(i++),

                cursor.getString(i++),
                cursor.getString(i++),

                cursor.getString(i++),
                cursor.getString(i++)
        };
        return forCashbook(array);
    }

    public static COPDRFRow forDocuments(Cursor cursor) {
        int i = 0;
        String[] array = new String[] {
                cursor.getString(i++),
                cursor.getString(i++),

                cursor.getString(i++),
                cursor.getString(i++),

                cursor.getString(i++),
                cursor.getString(i++),
                cursor.getString(i++),
                cursor.getString(i++)
        };
        return forDocuments(array);
    }

    public static COPDRFRow forPayments(Cursor cursor) {
        int i = 0;
        String[] array = new String[] {
                cursor.getString(i++),
                cursor.getString(i++),
                cursor.getString(i++),
                cursor.getString(i++),
                cursor.getString(i++),
                cursor.getString(i++),
                cursor.getString(i++),

                cursor.getString(i++),
                cursor.getString(i++)
        };
        return forPayments(array);
    }

    public static COPDRFRow forCashbook(String... array) {
        COPDRFRow result = new COPDRFRow();
        int i = 0;
        POS_CONSUMPTION_DATE = i;
        String date = array[i++];
        String normalDate = DateTime.fromSQLFormat(date).toNormalDateFormat();
        result.add(new COPDRFCell(normalDate, CellIndex.fromInts(0, 0, 0)));

        POS_CONSUMPTION_DESC = i;
        result.add(new COPDRFCell(array[i++], CellIndex.fromInts(0, 1, 0)));

        POS_CONSUMPTION_SUM_IN = i;
        result.add(new COPDRFCell(array[i++], CellIndex.fromInts(0, 2, 2)));
        POS_CONSUMPTION_SUM_OUT = i;
        result.add(new COPDRFCell(array[i++], CellIndex.fromInts(0, 2, 3)));

        POS_CONSUMPTION_ID = i;
        result.add(new COPDRFCell(array[i++]));
        POS_CONSUMPTION_OPER_TYPE = i;
        result.add(new COPDRFCell(array[i++]));
        POS_CONSUMPTION_DATE_SQL = i;
        result.add(new COPDRFCell(date));
        return result;
    }

    public static COPDRFRow forDocuments(String... array) {
        COPDRFRow result = new COPDRFRow();
        int i = 0;
        result.add(new COPDRFCell(array[i++], CellIndex.fromInts(0, 0, 0)));
        result.add(new COPDRFCell(array[i++], CellIndex.fromInts(0, 0, 1)));

        result.add(new COPDRFCell(array[i++], CellIndex.fromInts(0, 1, 0)));
        result.add(new COPDRFCell(array[i++], CellIndex.fromInts(0, 1, 1)));

        result.add(new COPDRFCell(array[i++], CellIndex.fromInts(0, 2, 0)));
        result.add(new COPDRFCell(array[i++], CellIndex.fromInts(0, 2, 2), COPDRFColumnName.AmountIn));
        result.add(new COPDRFCell(array[i++], CellIndex.fromInts(0, 3, 2), COPDRFColumnName.AmountOut));
        result.add(new COPDRFCell(array[i++], CellIndex.fromInts(0, 2, 3), COPDRFColumnName.Profit));
        return result;
    }

    public static COPDRFRow forPayments(String... array) {
        COPDRFRow result = new COPDRFRow();
        int i = 0;

        POS_PAYMENTS_DATE = i;
        result.add(new COPDRFCell(array[i++], CellIndex.fromInts(0, 0, 0)));
        POS_PAYMENTS_ACCT = i;
        result.add(new COPDRFCell(array[i++], CellIndex.fromInts(0, 0, 2)));
        POS_PAYMENTS_PARTNER_NAME = i;
        result.add(new COPDRFCell(array[i++], CellIndex.fromInts(0, 1, 1)));
        POS_PAYMENTS_OPER_NAME = i;
        result.add(new COPDRFCell(array[i++], CellIndex.fromInts(0, 1, 3)));
        POS_PAYMENTS_TO_PAY = i;
        result.add(new COPDRFCell(array[i++], CellIndex.fromInts(0, 2, 1)));
        POS_PAYMENTS_PAYED = i;
        result.add(new COPDRFCell(array[i++], CellIndex.fromInts(0, 2, 2)));
        POS_PAYMENTS_DIFFERENCE = i;
        result.add(new COPDRFCell(array[i++], CellIndex.fromInts(0, 2, 3)));

        POS_PAYMENTS_PARTNER_ID = i;
        result.add(new COPDRFCell(array[i++]));
        POS_PAYMENTS_OPER_TYPE = i;
        result.add(new COPDRFCell(array[i++]));

        return result;
    }
    //endregion
}
