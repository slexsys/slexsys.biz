package com.slexsys.biz.main.finance.comp;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.view.View;
import android.widget.ListView;

import com.slexsys.biz.R;
import com.slexsys.biz.main.comp.COPDRF.COPDRFAdapter;
import com.slexsys.biz.main.comp.COPDRF.COPDRFCell;
import com.slexsys.biz.main.comp.COPDRF.COPDRFRow;
import com.slexsys.biz.main.comp.COPDRF.COPDRFRows;
import com.slexsys.biz.main.comp.COPDRF.CellIndex;
import com.slexsys.biz.main.comp.Events.Event;
import com.slexsys.biz.main.comp.Events.OnEventListener;
import com.slexsys.biz.main.comp.NewTypes.ListViewItemMove;
import com.slexsys.biz.main.comp.NewTypes.SmartDialog;
import com.slexsys.biz.main.finance.comp.comp.EditPaymentsData;
import com.slexsys.biz.main.finance.comp.comp.EditPaymentsRowData;

import java.io.Serializable;

/**
 * Created by slexsys on 1/25/17.
 */
public class EditFinance extends Event implements SmartDialog {

    //region views
    private ListView listViewData;
    //endregion

    //region fields
    private EditPaymentsData data;

    private Context context;
    private COPDRFRows copdrfRows;
    private COPDRFAdapter adapter;
    //endregion

    //region constructors
    public EditFinance(EditPaymentsData data) {
        this.data = data;
    }
    //endregion

    //region init methods
    private void Init(Activity activity, View view) {
        InitViews(view);
        InitFields(activity);
        FillViews();
    }

    private void InitViews(View view) {
        listViewData = (ListView)  view.findViewById(R.id.listViewEditPaymentsData);
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

    private void InitFields(Activity activity) {
        context = activity.getApplicationContext();
        copdrfRows = new COPDRFRows();
        adapter = new COPDRFAdapter(context, copdrfRows);
        listViewData.setAdapter(adapter);
    }

    private void FillViews() {
        copdrfRows.clear();
        //head
        copdrfRows.add(new COPDRFRow(){{
            add(new COPDRFCell(R.string.date, CellIndex.fromInts(0, 0, 0)));
            add(new COPDRFCell(data.getDateTime().toNormalDateFormat(), CellIndex.fromInts(0, 0, 1)));

            add(new COPDRFCell(R.string.partner, CellIndex.fromInts(0, 1, 0)));
            add(new COPDRFCell(data.getPartner().getName(), CellIndex.fromInts(0, 1, 1)));

            add(new COPDRFCell(R.string.acct, CellIndex.fromInts(0, 2, 0)));
            add(new COPDRFCell(data.getAcct(), CellIndex.fromInts(0, 2, 1)));
        }});

        for (EditPaymentsRowData item : data.getList()) {
            copdrfRows.add(COPDRFRow.forEditPayments(item));
        }
        adapter.notifyDataSetChanged();
    }

    private void removeItem(int position) {
        if (position != 0 && position != 1) {
            int index = position - 1;
            if (data.getList().get(index).getId() != 0) {
                data.getList().get(index).setSum(0);
                data.getList().get(index).setChanged(true);
            } else {
                data.getList().remove(index);
            }
            copdrfRows.remove(position);
            adapter.notifyDataSetChanged();
        }
    }

    private void viewItem(int position) { }

    private void editItem(int position) {
        if (position != 0) {
            boolean readonly = false;
            if (position == 1) {
                readonly = true;
            }
            final int index = position - 1;
            EditPaymentsRowData rowData = data.getList().get(index);
            EditPaymentRow edit = new EditPaymentRow(readonly, rowData);
            edit.setOnEventListener(new OnEventListener() {
                @Override
                public void onEvent(Serializable serializable) {
                    EditPaymentsRowData dataRow = (EditPaymentsRowData) serializable;
                    data.getList().remove(index);
                    data.getList().add(index, dataRow);
                    FillViews();
                }
            });
        }
    }
    //endregion

    //region do methods
    @Override
    public void show(Activity activity) {
        View view = activity.getLayoutInflater().inflate( R.layout.editpayments, null);
        Init(activity, view);
        AlertDialog.Builder dialog = new AlertDialog.Builder(activity);
        dialog.setTitle(R.string.edit_payments)
                .setView(view)
                .setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int id) {
                        doEvent(data);
                    }
                })
                .setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int id) { }
                });
        dialog.show();
    }
    //endregion
}