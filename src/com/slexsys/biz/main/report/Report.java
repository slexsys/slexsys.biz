package com.slexsys.biz.main.report;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.GridLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.ViewFlipper;

import com.slexsys.biz.GO;
import com.slexsys.biz.R;
import com.slexsys.biz.database.sqls.iSQL;
import com.slexsys.biz.main.comp.COPDRF.COPDRFAdapter;
import com.slexsys.biz.main.comp.COPDRF.COPDRFCell;
import com.slexsys.biz.main.comp.COPDRF.CellIndex;
import com.slexsys.biz.main.comp.COPDRF.COPDRFRow;
import com.slexsys.biz.main.comp.COPDRF.COPDRFRows;
import com.slexsys.biz.main.comp.NewTypes.SmartActivity;
import com.slexsys.biz.main.comp.NewTypes.SwipSlider;
import com.slexsys.biz.main.comp.NewTypes.DataTable;
import com.slexsys.biz.main.report.comp.UIFiltersAdapter;
import com.slexsys.biz.main.report.comp.UIFilterType;
import com.slexsys.biz.main.report.comp.UIFilter;
import com.slexsys.biz.main.report.comp.UIFilters;
import com.slexsys.biz.main.report.json.ReportColumn;
import com.slexsys.biz.main.report.json.ReportJSON;

import java.util.List;

/**
 * Created by slexsys on 1/30/17.
 */
public class Report extends SmartActivity {
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.reporttemplate1);
        Init();
    }

    //region finals
    public static final String PUTTER = "putter";
    //endregion

    //region views
    private ViewFlipper viewFlipper;
    private GridLayout gridLayoutTotals;
    private ListView listViewLines;
    private ListView listViewFiltersMain;
    private ListView listViewFiltersAdvanced;

    private Button buttonOK;
    private Button buttonTotals;
    private Button buttonLines;
    private Button buttonFiltersMain;
    private Button buttonFiltersAdvanced;
    private Button buttonExit;
    //endregion

    //region fields
    Context context;
    ReportJSON report;

    //lines
    COPDRFRows listLines;
    COPDRFAdapter adapterLines;

    //filters main
    UIFilters uiFiltersMain;
    UIFiltersAdapter adapterFiltersMain;

    //filters main
    UIFilters uiFiltersAdvanced;
    UIFiltersAdapter adapterFiltersAdvanced;
    //endregion

    //region initialization methods
    private void Init() {
        InitViews();
        InitFields();
        //InitReport();
        InitExtraMembers();
    }

    private void InitViews() {
        InitViewFlipper();
        InitGridLayout();
        InitListView();
        InitButtons();
    }

    private void InitViewFlipper() {
        viewFlipper = (ViewFlipper) findViewById(R.id.viewFlipperReportTemplate1);
        new SwipSlider(viewFlipper);
    }

    private void InitGridLayout() {
        gridLayoutTotals = (GridLayout) findViewById(R.id.gridLayoutReportTemplate1Totals);
    }

    private void InitListView() {
        listViewLines = (ListView) findViewById(R.id.listViewReportTemplate1Lines);
        listViewFiltersMain = (ListView) findViewById(R.id.listViewReportTemplate1FiltersMain);
        listViewFiltersAdvanced = (ListView) findViewById(R.id.listViewReportTemplate1FiltersAdvanced);
    }

    private void InitButtons() {
        buttonOK = (Button) findViewById(R.id.buttonReportTemplate1OK);
        buttonTotals = (Button) findViewById(R.id.buttonReportTemplate1Totals);
        buttonLines = (Button) findViewById(R.id.buttonReportTemplate1Lines);
        buttonFiltersMain = (Button) findViewById(R.id.buttonReportTemplate1FiltersMain);
        buttonFiltersAdvanced = (Button) findViewById(R.id.buttonReportTemplate1FiltersAdvanced);
        buttonExit = (Button) findViewById(R.id.buttonReportTemplate1Exit);

        buttonOK.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                viewFlipper.setDisplayedChild(viewFlipper.indexOfChild(findViewById(R.id.linearLayoutReportTemplate1Totals)));
                ClickOK();
            }
        });

        buttonTotals.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                viewFlipper.setDisplayedChild(viewFlipper.indexOfChild(findViewById(R.id.linearLayoutReportTemplate1Totals)));
            }
        });

        buttonLines.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                viewFlipper.setDisplayedChild(viewFlipper.indexOfChild(findViewById(R.id.linearLayoutReportTemplate1Lines)));
            }
        });

        buttonFiltersMain.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                viewFlipper.setDisplayedChild(viewFlipper.indexOfChild(findViewById(R.id.linearLayoutReportTemplate1FilterMain)));
            }
        });

        buttonFiltersAdvanced.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                viewFlipper.setDisplayedChild(viewFlipper.indexOfChild(findViewById(R.id.linearLayoutReportTemplate1FilterAdvanced)));
            }
        });

        buttonExit.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                finish();
            }
        });
    }

    private void InitFields() {
        context = this.getApplicationContext();

        //lines
        listLines = new COPDRFRows();
        adapterLines = new COPDRFAdapter(context, listLines);
        listViewLines.setAdapter(adapterLines);

        //filters main
        uiFiltersMain = new UIFilters();
        adapterFiltersMain = new UIFiltersAdapter(uiFiltersMain, this);
        listViewFiltersMain.setAdapter(adapterFiltersMain);
        adapterFiltersMain.setOKButton(buttonOK);

        //filters advanced
        uiFiltersAdvanced = new UIFilters();
        adapterFiltersAdvanced = new UIFiltersAdapter(uiFiltersAdvanced, this);
        listViewFiltersAdvanced.setAdapter(adapterFiltersAdvanced);
        adapterFiltersAdvanced.setOKButton(buttonOK);
    }

    private void InitExtraMembers() {
        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
            int pos = bundle.getInt(PUTTER);
            report = GO.reports.get(pos);
            fillfilters(report);
        }
    }

    private void InitReport() {
        Intent intent = getIntent();
        String json = intent.getStringExtra("JSON");
        report = ReportJSON.fromJSON(json);
        fillfilters(report);
    }

    private void fillfilters(ReportJSON report) {
        List<UIFilterType> items = report.getActiveFilters();
        items = UIFilterType.normalize(items);
        for (UIFilterType item : items) {
            UIFilter filter = new UIFilter(item);
            if (item.isMain()) {
                uiFiltersMain.add(filter);
            } else {
                uiFiltersAdvanced.add(filter);
            }
        }
        adapterFiltersMain.notifyDataSetChanged();
        adapterFiltersAdvanced.notifyDataSetChanged();
    }
    //endregion

    private void ClickOK() {
        filler();
    }

    private void filler() {
        UIFilters filters = getUIFilters();
        filltotals(report, filters);
        filllines(report, filters);
    }

    private UIFilters getUIFilters() {
        UIFilters filters = new UIFilters();
        filters.addAll(uiFiltersMain);
        filters.addAll(uiFiltersAdvanced);
        return filters;
    }

    private void filltotals(ReportJSON report, UIFilters filters) {
        List<String> querys = report.buildSQLQueryTotals(filters);
        gridLayoutTotals.removeAllViews();
        for (String query : querys) {
            Log.d(">", query);
            DataTable data = iSQL.getDataTable(query);
            if (data != null) {
                for (int r = 0; r < data.size(); ++r) {
                    for (int c = 0; c < data.getColumnsCount(); ++c) {
                        String name = data.getName(c);
                        Object cell = data.getCell(c, r);
                        if (cell != null) {
                            String value = cell.toString();
                            if (value != "#") {
                                View view = getTotalView(name, value, "", "", "");
                                gridLayoutTotals.addView(view);
                            }
                        }
                    }
                }
            }
        }
    }

    private void filllines(ReportJSON report, UIFilters filters) {
        String query = report.buildSQLQueryLines(filters);
        Log.d(">", query);
        listLines.clear();
        DataTable data = iSQL.getDataTable(query);
        if (data != null) {
            fillColumnNames(data);
            fillCellsValues(data);
        }
        adapterLines.notifyDataSetChanged();
    }

    private void fillColumnNames(DataTable data) {
        CellIndex cellIndex = null;
        COPDRFRow cells = new COPDRFRow();
        for (int c = 0; c < data.getColumnsCount(); ++c) {
            String name = data.getName(c);
            ReportColumn column = report.getQueryLines().get(0).indexOf(c);
            if (column != null && column.getSize() > 0) {
                COPDRFCell cell = new COPDRFCell();

                cell.setSize(column.getFontSize());
                cell.setColor(column.getFontColor());
                cell.setValue(name);
                if (column.getCellIndex() != null) {
                    cellIndex = column.getCellIndex();
                } else {
                    cellIndex = cellIndex.getNextCellIndex();
                }
                cell.setCellIndex(cellIndex);
                cells.add(cell);
            }
        }
        listLines.add(cells);
    }

    private void fillCellsValues(DataTable data) {
        CellIndex cellIndex = null;
        for (int r = 0; r < data.size(); ++r) {
            COPDRFRow cells = new COPDRFRow();
            for (int c = 0; c < data.getColumnsCount(); ++c) {
                ReportColumn column = report.getQueryLines().get(0).indexOf(c);
                if (column != null && column.getSize() > 0) {
                    COPDRFCell cell = new COPDRFCell();

                    cell.setSize(column.getFontSize());
                    cell.setColor(column.getFontColor());
                    cell.setValue(data.getCell(c, r));
                    if (column.getCellIndex() != null) {
                        cellIndex = column.getCellIndex();
                    } else {
                        cellIndex = cellIndex.getNextCellIndex();
                    }
                    cell.setCellIndex(cellIndex);
                    cells.add(cell);
                }
            }
            listLines.add(cells);
        }
    }

    private View getTotalView(String t1, String t2, String t3, String t41, String t42) {
        View view = LayoutInflater.from(this).inflate(R.layout.reportitemtemplatetotal, null);
        TextView p1 = (TextView) view.findViewById(R.id.textViewreportitemtemplatetotalP1);
        TextView p2 = (TextView) view.findViewById(R.id.textViewreportitemtemplatetotalP2);
        TextView p3 = (TextView) view.findViewById(R.id.textViewreportitemtemplatetotalP3);
        TextView p41 = (TextView) view.findViewById(R.id.textViewreportitemtemplatetotalP41);
        TextView p42 = (TextView) view.findViewById(R.id.textViewreportitemtemplatetotalP42);

        p1.setText(t1);
        p2.setText(t2);
        p3.setText(t3);
        p41.setText(t41);
        p42.setText(t42);

        return view;
    }
}