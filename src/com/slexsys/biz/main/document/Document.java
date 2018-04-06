package com.slexsys.biz.main.document;

import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.ListView;

import com.slexsys.biz.GO;
import com.slexsys.biz.R;
import com.slexsys.biz.comp.std;
import com.slexsys.biz.database.sqls.iSQL;
import com.slexsys.biz.main.comp.COPDRF.COPDRFAdapter;
import com.slexsys.biz.main.comp.COPDRF.COPDRFCell;
import com.slexsys.biz.main.comp.COPDRF.COPDRFRow;
import com.slexsys.biz.main.comp.COPDRF.COPDRFRows;
import com.slexsys.biz.main.comp.Events.OnEventListener;
import com.slexsys.biz.main.comp.NewTypes.DateTime;
import com.slexsys.biz.main.comp.NewTypes.ListViewItemMove;
import com.slexsys.biz.main.comp.NewTypes.SmartActivity;
import com.slexsys.biz.main.comp.dialogs.UIFiltersDialog;
import com.slexsys.biz.main.operations.comp.OperationData;
import com.slexsys.biz.main.operations.Operation;
import com.slexsys.biz.main.operations.json.OperationJSON;
import com.slexsys.biz.main.operations.json.TotalMode;
import com.slexsys.biz.main.report.comp.UIFilter;
import com.slexsys.biz.main.report.comp.UIFilterType;
import com.slexsys.biz.main.report.comp.UIFilters;

import java.io.Serializable;
import java.util.List;

/**
 * Created by slexsys on 3/22/17.
 */
public class Document extends SmartActivity {
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.blanktemplate);
        Start();
    }

    //region finals
    public static final String PUTTER_POS = "pos";
    private static final int POS_ACCT = 1;
    private static final int POS_TOTAL_IN = 5;
    private static final int POS_TOTAL_OUT = 6;
    //endregion

    //region views
    private LinearLayout linearLayoutUIInfo;
    private LinearLayout linearLayoutTotals;

    private ListView listViewData;
    //endregion

    //region fields
    private UIFilters filters;
    private COPDRFRows copdrfRows;
    private COPDRFAdapter adapter;
    private int operJSONPos;
    private TotalMode totalMode;
    //endregion

    //region init methods
    private void Start() {
        Init();
        fill();
    }

    private void Init() {
        InitViews();
        InitExtraFields();
        InitFields();
    }

    private void InitViews() {
        linearLayoutUIInfo = (LinearLayout) findViewById(R.id.linearLayoutOperationTemplateDPOInfo);
        linearLayoutUIInfo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showB1DataDialog();
            }
        });

        linearLayoutTotals = (LinearLayout) findViewById(R.id.linearLayoutOperationTemplateTotals);

        listViewData = (ListView) findViewById(R.id.listViewOperationTemplateList);
        ListViewItemMove lvimi = new ListViewItemMove(listViewData);
        lvimi.addRemoveListner(new ListViewItemMove.OnEventListener() {
            @Override
            public void onSlideLeftEvent(int position) {
                removeItem(position);
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

    private void InitExtraFields() {
        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
            operJSONPos = bundle.getInt(PUTTER_POS);
        }
    }

    private void InitFields() {
        copdrfRows = new COPDRFRows();
        adapter = new COPDRFAdapter(this, copdrfRows, getOperJSON().getColumns());
        listViewData.setAdapter(adapter);
        totalMode = std.getTotalMode(getOperJSON());
    }

    private void removeItem(int position) {
        if (position != -1) {
            String operType = getOperJSON().getOperType();
            String acct = getAcct(position);
            boolean result = iSQL.removeDocument(operType, acct);
            if (result) {
                copdrfRows.remove(position);
                adapter.notifyDataSetChanged();
                RefreshTotals();
            }
        }
    }

    private void viewItem(int position) { }

    private void editItem(int position) {
        if (position != -1) {
            String acct = getAcct(position);
            editOperation(acct, position);
        }
    }

    private void fill() {
        fillUIFilters();
        fillDocuments();
    }

    private void fillUIFilters() {
        filters = new UIFilters();
        filters.add(new UIFilter(UIFilterType.StartDate, DateTime.now()));
        filters.add(new UIFilter(UIFilterType.EndDate, DateTime.now()));
        filters.add(new UIFilter(UIFilterType.Partner));
        filters.add(new UIFilter(UIFilterType.Object));
        filters.add(new UIFilter(UIFilterType.User));
        //filters.add(new UIFilter(UIFilterType.OperTypeAll, getOperJSON().getOperType(), true));
        refreshHeadData();
    }

    private void fillDocuments() {
        copdrfRows.clear();
        copdrfRows.addAll(iSQL.getDocuments(filters));
        adapter.notifyDataSetChanged();
        RefreshTotals();
    }
    //endregion

    //region methods
    private void showB1DataDialog() {
        UIFiltersDialog dialog = new UIFiltersDialog(filters);
        dialog.setOnEventListener(new OnEventListener() {
            @Override
            public void onEvent(Serializable serializable) {
                filters = (UIFilters) serializable;
                refreshHeadData();
                fillDocuments();
            }
        });
        dialog.show(this);
    }

    private void refreshHeadData() {
        std.fillUIFilters2LinearLayout(this, filters, linearLayoutUIInfo);
    }

    private void editOperation(String acct, final int pos) {
        OperationData operationData = iSQL.getOperationDataByAcctAndOperType(operJSONPos, acct);
        operationData.setIsDocument(true);

        Operation operation = new Operation();
        operation.setOnEventListener(new OnEventListener() {
            @Override
            public void onEvent(Serializable serializable) {
                COPDRFRow row = (COPDRFRow) serializable;
                copdrfRows.remove(pos);
                copdrfRows.add(pos, row);
                adapter.notifyDataSetChanged();
            }
        });
        operation.putExtra(Operation.PUTTER_POS, operJSONPos);
        operation.putExtra(Operation.PUTTER_OPERATION_DATA, operationData);
        operation.show(this);
    }

    private void RefreshTotals() {
        //List<String> totals = getTotals();//old style
        List<String> totals = std.getTotalsFromCOPDRF(copdrfRows, POS_TOTAL_IN, POS_TOTAL_OUT);
        std.fillTotalsLinearLayout(this, linearLayoutTotals, totals, totalMode);
    }

    private String getAcct(int index) {
        COPDRFCell cell = copdrfRows.get(index).get(POS_ACCT);
        String acct = cell.getValue().toString();
        return acct;
    }

    private OperationJSON getOperJSON() {
        return GO.operations.get(operJSONPos);
    }
    //endregion
}