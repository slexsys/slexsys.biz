package com.slexsys.biz.main.operations.comp;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.view.View;
import android.widget.ListView;

import com.slexsys.biz.R;
import com.slexsys.biz.main.comp.COPDRF.COPDRFFilters;
import com.slexsys.biz.main.comp.Events.Event;
import com.slexsys.biz.main.operations.json.InsertionMode;
import com.slexsys.biz.main.report.comp.UIFiltersAdapter;
import com.slexsys.biz.main.report.comp.UIFilters;

/**
 * Created by slexsys on 3/14/17.
 */

public class OperationDPO2UInfo extends Event {

    //region views
    private ListView listView;
    //endregion

    //region fields
    private COPDRFFilters filters;
    private InsertionMode mode;

    private UIFilters uiFilters;
    private UIFiltersAdapter adapterFilters;
    //endregion

    //region constructors
    public OperationDPO2UInfo() { }

    public OperationDPO2UInfo(COPDRFFilters filters, InsertionMode mode) {
        this.filters = filters;
        this.mode = mode;
    }
    //endregion

    //region public methods
    public void show(Activity activity) {
        View view = activity.getLayoutInflater().inflate( R.layout.uifilterslayout, null);
        Init(activity, view);
        AlertDialog.Builder dialog = new AlertDialog.Builder(activity);
        dialog.setTitle(R.string.add_info)
                .setView(view)
                .setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int id) {
                        Save();
                    }
                })
                .setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int id) { }
                });
        dialog.show();
    }
    //endregion

    //region private methods
    private void Init(Activity activity, View view) {
        initViews(view);
        initFields(activity);
        fillViews();
    }

    private void initFields(Activity activity) {
        uiFilters = new UIFilters();
        adapterFilters = new UIFiltersAdapter(uiFilters, activity);
        listView.setAdapter(adapterFilters);
    }

    private void initViews(View view) {
        listView = (ListView) view.findViewById(R.id.listViewOperationDPOInfo);
    }

    private void fillViews() {
        UIFilters list = filters.getUIFilters(mode);
        uiFilters.addAll(list);
        adapterFilters.notifyDataSetChanged();
    }

    private void Save() {
        COPDRFFilters filters = COPDRFFilters.fromUIFilters(uiFilters);
        doEvent(filters);
    }
    //endregion
}
