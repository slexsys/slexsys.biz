package com.slexsys.biz.main.comp.COPDRF;

import com.slexsys.biz.main.comp.NewTypes.DataRow;
import com.slexsys.biz.main.comp.NewTypes.DataTable;

import java.util.LinkedList;

/**
 * Created by slexsys on 3/8/17.
 */

public class COPDRFRows extends LinkedList<COPDRFRow> {
    //region public methods
    public static COPDRFRows forCashbook(DataTable dataTable) {
        COPDRFRows result = new COPDRFRows();
        for (DataRow row : dataTable) {
            String[] array = row.toArray(new String[] {});
            result.add(COPDRFRow.forCashbook(array));
        }
        return result;
    }

    public static COPDRFRows forDocuments(DataTable dataTable) {
        COPDRFRows result = new COPDRFRows();
        if (dataTable != null) {
            for (DataRow row : dataTable) {
                String[] array = row.toArray(new String[]{});
                result.add(COPDRFRow.forDocuments(array));
            }
        }
        return result;
    }

    public static COPDRFRows forPayments(DataTable dataTable) {
        COPDRFRows result = new COPDRFRows();
        for (DataRow row : dataTable) {
            String[] array = row.toArray(new String[] {});
            result.add(COPDRFRow.forPayments(array));
        }
        return result;
    }
    //endregion
}
