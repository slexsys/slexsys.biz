package com.slexsys.biz.main.finance;

import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.ListView;

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
import com.slexsys.biz.main.comp.enums.ConsType;
import com.slexsys.biz.main.finance.comp.NEConsumption;
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
public class Cashbook extends SmartActivity {
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.blanktemplate);
        Start();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.cashbook, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        boolean result = true;
        switch (item.getItemId()) {
            case android.R.id.home:
                super.onOptionsItemSelected(item);
                break;
            case R.id.menuItemCashbookAdd:
                ClickButtonAdd();
                break;
        }
        return result;
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

    private void InitFields() {
        copdrfRows = new COPDRFRows();
        adapter = new COPDRFAdapter(this, copdrfRows);
        listViewData.setAdapter(adapter);
    }

    private void Fill() {
        fillUIFilters();
        fillListView();
    }

    private void fillUIFilters() {
        filters = new UIFilters();
        filters.add(new UIFilter(UIFilterType.StartDate, DateTime.now()));
        filters.add(new UIFilter(UIFilterType.EndDate, DateTime.now()));
        filters.add(new UIFilter(UIFilterType.Description));
        filters.add(new UIFilter(UIFilterType.ConsType));
        filters.add(new UIFilter(UIFilterType.Object));
        filters.add(new UIFilter(UIFilterType.User));
        refreshHeadData();
    }

    private void fillListView() {
        copdrfRows.clear();
        copdrfRows.addAll(iSQL.getConsumption(filters));
        adapter.notifyDataSetChanged();
        RefreshTotals();
    }

    private void removeItem(int position) {
        if (position != -1) {
            String id = getConsumptionId(position);
            boolean deleted = iSQL.removeConsumption(id);
            if (deleted) {
                copdrfRows.remove(position);
                adapter.notifyDataSetChanged();
                RefreshTotals();
            }
        }
    }

    private void viewItem(int position) { }

    private void editItem(int position) {
        if (position != -1) {
            editConsumption(position);
        }
    }
    //endregion

    //region do methods
    private void ClickButtonAdd() {
        addConsumption();
    }

    private void addConsumption() {
        NEConsumption dialog = new NEConsumption();
        dialog.setOnEventListener(new OnEventListener() {
            @Override
            public void onEvent(Serializable serializable) {
                LinkedList<String> list = (LinkedList<String>) serializable;
                saveAdd(list);
            }
        });
        dialog.show(this);
    }

    private void saveAdd(LinkedList<String> list) { //list without id
        iSQL.addConsumption(list);
        fillListView(); // update only row for fast
    }

    private void editConsumption(final int position) {
        final COPDRFRow row = copdrfRows.get(position);

        NEConsumption dialog = new NEConsumption();
        dialog.setOnEventListener(new OnEventListener() {
            @Override
            public void onEvent(Serializable serializable) {
                LinkedList<String> list = (LinkedList<String>) serializable;
                String id = getValue(row, COPDRFRow.POS_CONSUMPTION_ID);
                list.add(id);
                saveEdit(list, position);
            }
        });
        dialog.show(this);
        dialog.fillViews(getDataList(row));
    }

    private void saveEdit(LinkedList<String> list, int pos) { //list with id at last
        iSQL.editConsumption(list);
        fillListView(); // update only row for fast
    }

    private List<Object> getDataList(final COPDRFRow row) {
        String sid = getValue(row, COPDRFRow.POS_CONSUMPTION_OPER_TYPE);
        int id = Integer.parseInt(sid);
        final int index = ConsType.getIndexById(id);
        List<Object> result = new LinkedList<Object>() {{
            add(getValue(row, COPDRFRow.POS_CONSUMPTION_DATE_SQL));
            add(getValue(row, COPDRFRow.POS_CONSUMPTION_DESC));
            add(index);
            add(getValue(row, COPDRFRow.POS_CONSUMPTION_SUM_IN) +
                getValue(row, COPDRFRow.POS_CONSUMPTION_SUM_OUT));
        }};
        return result;
    }

    private String getValue(COPDRFRow row, int pos) {
        String result;
        COPDRFCell cell = row.get(pos);
        Object object = cell.getValue();
        if (object != null) {
            result = object.toString();
        } else {
            result = "";
        }
        return result;
    }

    private void showB1DataDialog() {
        UIFiltersDialog dialog = new UIFiltersDialog(filters);
        dialog.setOnEventListener(new OnEventListener() {
            @Override
            public void onEvent(Serializable serializable) {
                filters = (UIFilters) serializable;
                refreshHeadData();
                fillListView();
            }
        });
        dialog.show(this);
    }

    private void refreshHeadData() {
        std.fillUIFilters2LinearLayout(this, filters, linearLayoutUIInfo);
    }

    private void RefreshTotals() {
        int posIn = COPDRFRow.POS_CONSUMPTION_SUM_IN;
        int posOut = COPDRFRow.POS_CONSUMPTION_SUM_OUT;
        List<String> totals = std.getTotalsFromCOPDRF(copdrfRows, posIn, posOut);
        std.fillTotalsLinearLayout(this, linearLayoutTotals, totals, TotalMode.TotalInOut);
    }

    private String getConsumptionId(int position) {
        COPDRFRow row = copdrfRows.get(position);
        COPDRFCell cell = row.get(COPDRFRow.POS_CONSUMPTION_ID);
        String id = (String) cell.getValue();
        return id;
    }
    //endregion
}