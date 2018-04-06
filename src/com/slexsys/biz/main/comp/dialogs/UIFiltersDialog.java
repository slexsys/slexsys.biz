package com.slexsys.biz.main.comp.dialogs;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.view.View;
import android.widget.ListView;

import com.slexsys.biz.R;
import com.slexsys.biz.main.comp.Events.Event;
import com.slexsys.biz.main.report.comp.UIFiltersAdapter;
import com.slexsys.biz.main.report.comp.UIFilters;

/**
 * Created by slexsys on 3/24/17.
 */
public class UIFiltersDialog extends Event {

    //region views
    private ListView listView;
    //endregion

    //region fields
    private UIFilters uiFilters;
    private UIFiltersAdapter adapterFilters;
    //endregion

    //region constructors
    public UIFiltersDialog(UIFilters uiFilters) {
        this.uiFilters = uiFilters;
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
    }

    private void initViews(View view) {
        listView = (ListView) view.findViewById(R.id.listViewOperationDPOInfo);
    }

    private void initFields(Activity activity) {
        adapterFilters = new UIFiltersAdapter(uiFilters, activity);
        listView.setAdapter(adapterFilters);
        adapterFilters.notifyDataSetChanged();
    }

    private void Save() {
        doEvent(uiFilters);
    }
    //endregion
}