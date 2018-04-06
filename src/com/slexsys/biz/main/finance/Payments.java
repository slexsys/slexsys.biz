package com.slexsys.biz.main.finance;

import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.Toast;

import com.slexsys.biz.GO;
import com.slexsys.biz.R;
import com.slexsys.biz.comp.std;
import com.slexsys.biz.database.sqls.iSQL;
import com.slexsys.biz.main.comp.COPDRF.COPDRFAdapter;
import com.slexsys.biz.main.comp.COPDRF.COPDRFRow;
import com.slexsys.biz.main.comp.COPDRF.COPDRFRows;
import com.slexsys.biz.main.comp.Events.OnEventListener;
import com.slexsys.biz.main.comp.NewTypes.DataRow;
import com.slexsys.biz.main.comp.NewTypes.DataTable;
import com.slexsys.biz.main.comp.NewTypes.DateTime;
import com.slexsys.biz.main.comp.NewTypes.ListViewItemMove;
import com.slexsys.biz.main.comp.NewTypes.SmartActivity;
import com.slexsys.biz.main.comp.dialogs.UIFiltersDialog;
import com.slexsys.biz.main.finance.comp.EditFinance;
import com.slexsys.biz.main.finance.comp.NewFinance;
import com.slexsys.biz.main.finance.comp.PayInfo;
import com.slexsys.biz.main.finance.comp.comp.EditPaymentsData;
import com.slexsys.biz.main.operations.json.TotalMode;
import com.slexsys.biz.main.report.comp.UIFilter;
import com.slexsys.biz.main.report.comp.UIFilterType;
import com.slexsys.biz.main.report.comp.UIFilters;

import java.io.Serializable;
import java.util.LinkedList;
import java.util.List;

/**
 * Created by slexsys on 3/11/16.
 */
public class Payments extends SmartActivity {
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.blanktemplate);
        Start();
    }

    //region views
    private LinearLayout linearLayoutUIInfo;
    private LinearLayout linearLayoutTotals;

    private ListView listViewData;
    //endregion

    //region fields
    private COPDRFRows copdrfRows;
    private COPDRFAdapter adapter;

    private UIFilters filters;
    private List<Integer> selectedList;
    //endregion

    //region init methods
    private void Start() {
        Init();
        Fill();
    }

    private void Init() {
        InitViews();
        InitFields();
    }

    private void InitViews() {
        linearLayoutUIInfo = (LinearLayout) findViewById(R.id.linearLayoutOperationTemplateDPOInfo);
        linearLayoutUIInfo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showFiltersDialog();
            }
        });

        linearLayoutTotals = (LinearLayout) findViewById(R.id.linearLayoutOperationTemplateTotals);

        listViewData = (ListView) findViewById(R.id.listViewOperationTemplateList);
        ListViewItemMove lvimi = new ListViewItemMove(listViewData, true);
        lvimi.addRemoveListner(new ListViewItemMove.OnEventListener() {
            @Override
            public void onSlideLeftEvent(int position) {
                selectItem(position);
            }

            @Override
            public void onSlideRightEvent(int position) {
                removeItem(position);
            }

            @Override
            public void onClickEvent(int position) {
                viewItem(position);
            }

            @Override
            public void onLongClickEvent(int position) {
                editItem(position);
            }
        });
    }

    private void InitFields() {
        copdrfRows = new COPDRFRows();
        adapter = new COPDRFAdapter(this, copdrfRows);
        listViewData.setAdapter(adapter);
        selectedList = new LinkedList<Integer>();
    }

    private void Fill() {
        fillUIFilters();
        fillListView();
    }

    private void fillUIFilters() {
        filters = new UIFilters();
        filters.add(new UIFilter(UIFilterType.StartDate, DateTime.now()));
        filters.add(new UIFilter(UIFilterType.EndDate, DateTime.now()));
        filters.add(new UIFilter(UIFilterType.OperTypePay));
        filters.add(new UIFilter(UIFilterType.Object));
        filters.add(new UIFilter(UIFilterType.User));
        refreshFiltersInfo();
    }

    private void fillListView() {
        copdrfRows.clear();
        copdrfRows.addAll(iSQL.getPayments(filters));
        adapter.notifyDataSetChanged();
        refreshTotals();
    }

    private void selectItem(int position) {
        if (position != -1) {
            selectedList.add(position);
        }
    }

    private void removeItem(int position) { }

    private void viewItem(int position) {
        if (position != -1) {
            doPayment(position);
        }
    }

    private void editItem(int position) {
        if (position != -1) {
            editPayment(position);
        }
    }
    //endregion

    //region do methods
    private void doPayment(int position) {
        COPDRFRow row = copdrfRows.get(position);
        double sum = Double.parseDouble((String) row.get(COPDRFRow.POS_PAYMENTS_DIFFERENCE).getValue());
        if (sum > 0) {
            double credit = 0;
            String type = (String) row.get(COPDRFRow.POS_PAYMENTS_OPER_TYPE).getValue();
            String acct = (String) row.get(COPDRFRow.POS_PAYMENTS_ACCT).getValue();
            PayInfo payInfo = new PayInfo(sum, credit);
            showNewFinance(payInfo, type, acct, null);
        }
    }

    private void doGroupPayment(int position) {
        double sum = getGroupPaySum(position);
        if (sum > 0) {
            COPDRFRow row = copdrfRows.get(position);
            int pid = Integer.parseInt((String) row.get(COPDRFRow.POS_PAYMENTS_PARTNER_ID).getValue());
            double credit = 0;
            PayInfo payInfo = new PayInfo(pid, sum, credit);
            DataTable data = getPaymentsData(position);
            showNewFinance(payInfo, null, null, data);
        }
    }

    private double getGroupPaySum(int position) {
        double total = 0;
        COPDRFRow row = copdrfRows.get(position);
        String pid = (String) row.get(COPDRFRow.POS_PAYMENTS_PARTNER_ID).getValue();
        for (COPDRFRow item : copdrfRows) {
            String id = (String) item.get(COPDRFRow.POS_PAYMENTS_PARTNER_ID).getValue();
            if (pid == id) {
                String oper = (String) item.get(COPDRFRow.POS_PAYMENTS_OPER_TYPE).getValue();
                int opertype = Integer.parseInt(oper);
                int sign = GO.operations.getSignPayById(opertype);
                double sum = Double.parseDouble((String) row.get(COPDRFRow.POS_PAYMENTS_DIFFERENCE).getValue());
                total += sign * sum;
            }
        }
        return total;
    }

    private DataTable getPaymentsData(int position) {
        DataTable data = new DataTable();
        COPDRFRow row = copdrfRows.get(position);
        String pid = (String) row.get(COPDRFRow.POS_PAYMENTS_PARTNER_ID).getValue();
        for (COPDRFRow item : copdrfRows) {
            String id = (String) item.get(COPDRFRow.POS_PAYMENTS_PARTNER_ID).getValue();
            if (pid == id) {
                String opertype = (String) item.get(COPDRFRow.POS_PAYMENTS_OPER_TYPE).getValue();
                String acct = (String) item.get(COPDRFRow.POS_PAYMENTS_ACCT).getValue();
                String sum = (String) row.get(COPDRFRow.POS_PAYMENTS_DIFFERENCE).getValue();
                int oper = Integer.parseInt(opertype);
                int sign = GO.operations.getSignPayById(oper);
                data.add(new DataRow(opertype, acct, sum, Integer.toString(sign)));
            }
        }
        return data;
    }

    private void showNewFinance(PayInfo payInfo, final String type, final String acct, final DataTable data) {
        NewFinance nf = new NewFinance(payInfo);
        nf.setOnEventListener(new OnEventListener() {
            @Override
            public void onEvent(Serializable serializable) {
                PayInfo payInfo = (PayInfo) serializable;
                boolean isOK;
                if (type != null && acct != null) {
                    isOK = iSQL.addPayment(payInfo, type, acct);//cashbook no add
                } else {
                    isOK = iSQL.doGroupPayment(payInfo, data);
                }
                if (isOK) {
                    Toast.makeText(Payments.this, R.string.operation_save_successfully, Toast.LENGTH_LONG).show();
                    fillListView(); // update only row for fast
                } else {
                    Toast.makeText(Payments.this, R.string.operation_save_problem, Toast.LENGTH_SHORT).show();
                    showNewFinance(payInfo, type, acct, data);
                }
            }
        });
        nf.show(this);
    }

    private void editPayment(final int position) {
        final COPDRFRow row = copdrfRows.get(position);
        String type = (String) row.get(COPDRFRow.POS_PAYMENTS_OPER_TYPE).getValue();
        String acct = (String) row.get(COPDRFRow.POS_PAYMENTS_ACCT).getValue();
        EditPaymentsData data = iSQL.getPayment(type, acct);
        showEditFinance(data);
    }

    private void showEditFinance(EditPaymentsData data) {
        EditFinance edit = new EditFinance(data);
        edit.setOnEventListener(new OnEventListener() {
            @Override
            public void onEvent(Serializable serializable) {
                EditPaymentsData data = (EditPaymentsData) serializable;
                boolean isok = iSQL.updateEditedPayment(data);
                if (isok) {
                    Toast.makeText(Payments.this, R.string.operation_save_successfully, Toast.LENGTH_LONG).show();
                    fillListView(); // update only row for fast
                } else {
                    Toast.makeText(Payments.this, R.string.operation_save_problem, Toast.LENGTH_SHORT).show();
                    showEditFinance(data);
                }
            }
        });
        edit.show(this);
    }

    private void showFiltersDialog() {
        UIFiltersDialog dialog = new UIFiltersDialog(filters);
        dialog.setOnEventListener(new OnEventListener() {
            @Override
            public void onEvent(Serializable serializable) {
                filters = (UIFilters) serializable;
                refreshFiltersInfo();
                fillListView();
            }
        });
        dialog.show(this);
    }

    private void refreshFiltersInfo() {
        std.fillUIFilters2LinearLayout(this, filters, linearLayoutUIInfo);
    }

    private void refreshTotals() {
        int posIn = COPDRFRow.POS_PAYMENTS_TO_PAY;
        int posOut = COPDRFRow.POS_PAYMENTS_PAYED;
        List<String> totals = std.getTotalsFromCOPDRF(copdrfRows, posIn, posOut);
        std.fillTotalsLinearLayout(this, linearLayoutTotals, totals, TotalMode.TotalInOut);
    }
    //endregion
}