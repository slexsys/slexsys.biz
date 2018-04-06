package com.slexsys.biz.main.finance.comp;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.view.View;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;

import com.slexsys.biz.R;
import com.slexsys.biz.comp.std;
import com.slexsys.biz.main.comp.Events.Event;
import com.slexsys.biz.main.comp.NewTypes.SmartDialog;
import com.slexsys.biz.main.comp.enums.PayType;
import com.slexsys.biz.main.comp.NewTypes.DateTime;
import com.slexsys.biz.main.finance.comp.comp.EditPaymentsRowData;

/**
 * Created by slexsys on 1/27/17.
 */
public class EditPaymentRow extends Event implements SmartDialog {
    //region members views
    private EditText editTextSum;
    private Spinner spinnerType;
    private DatePicker datePicker;
    //endregion

    //region members
    private EditPaymentsRowData data;
    private boolean readonly;
    //endregion

    //region constructors
    public EditPaymentRow(boolean readonly, EditPaymentsRowData data) {
        this.readonly = readonly;
        this.data = data;
    }
    //endregion

    //region init methods
    private void Init(Activity activity, View view) {
        InitViews(view);
        FillViews(activity);
    }

    private void InitViews(View view) {
        editTextSum = (EditText) view.findViewById(R.id.editTextEditPaymentSum);
        spinnerType = (Spinner) view.findViewById(R.id.spinnerEditPaymentsRowPayType);
        datePicker = (DatePicker) view.findViewById(R.id.datePickerEditPaymentRow);

        editTextSum.setEnabled(!readonly);
    }

    private void FillViews(Activity activity) {
        editTextSum.setText(Double.toString(data.getSum()));
        if (!readonly) {
            editTextSum.selectAll();
        }
        std.InitSpinner(activity, spinnerType, PayType.class);
        spinnerType.setSelection(data.getPaytype().toIndex());
        datePicker.updateDate(data.getDateTime().getYear(),
                data.getDateTime().getMonth(),
                data.getDateTime().getDay());
    }
    //endregion

    //region do methods
    @Override
    public void show(Activity activity) {
        View view = activity.getLayoutInflater().inflate( R.layout.editpaymentrow, null);
        Init(activity, view);
        AlertDialog.Builder dialog = new AlertDialog.Builder(activity);
        dialog.setTitle(R.string.edit_payment_row)
                .setView(view)
                .setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int id) {
                        save();
                    }
                })
                .setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int id) { }
                });
        dialog.show();
    }

    private void save() {
        fillData();
        doEvent(data);
    }

    private void fillData() {
        data.setSum(Double.parseDouble(editTextSum.getText().toString()));
        data.setPaytype(PayType.fromIndex(spinnerType.getSelectedItemPosition()));
        data.setDateTime(new DateTime(datePicker.getYear(), datePicker.getMonth() + 1, datePicker.getDayOfMonth()));
        data.setChanged(true);
    }
    //endregion
}