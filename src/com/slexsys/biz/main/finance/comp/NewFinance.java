package com.slexsys.biz.main.finance.comp;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.*;
import com.slexsys.biz.R;
import com.slexsys.biz.database.comp.Convert;
import com.slexsys.biz.main.comp.NewTypes.DateTime;
import com.slexsys.biz.main.comp.enums.PayType;
import com.slexsys.biz.main.comp.Events.Event;
import com.slexsys.biz.main.comp.NewTypes.SwipSlider;

/**
 * Created by slexsys on 3/16/16.
 */
public class NewFinance extends Event {

    //region views
    private TextView textViewTotalValue;
    private EditText editTextPay;
    private TextView textViewBalanceValue;
    private Spinner spinnerType;
    private TextView textViewCreditBeforeValue;
    private TextView textViewCreditAfterValue;
    private Switch switchPrintItemsList;
    private Switch switchPrintPayments;
    private Switch switchPrintRealQtty;
    private ViewFlipper viewFlipper;
    private DatePicker datePicker;
    private NumberPicker numberPickerPPI;
    private NumberPicker numberPickerPPO;
    private NumberPicker numberPickerPPIO;
    //endregion

    //region fields
    private PayInfo payInfo;
    //endregion

    //region constructors
    public NewFinance(PayInfo payInfo) {
        this.payInfo = payInfo;
    }
    //endregion

    //region initialization
    private void Init(Activity activity, View view) {
        InitControls(activity, view);
        FillControls();
    }

    private void InitControls(Activity activity, View view){
        InitEditTexts(view);
        InitTextViews(view);
        InitSpinners(activity, view);
        InitSwitchs(view);
        InitOther(view);
    }

    private void InitEditTexts(View view){
        editTextPay = (EditText) view.findViewById(R.id.editTextNEFinancePay);
        editTextPay.addTextChangedListener(new TextWatcher(){
            public void afterTextChanged(Editable s) {
                RefreshTotals();
            }

            public void beforeTextChanged(CharSequence s, int start, int count, int after){}

            public void onTextChanged(CharSequence s, int start, int before, int count){}
        });
    }

    private void InitTextViews(View view){
        textViewTotalValue = (TextView) view.findViewById(R.id.textViewNEFinanceTotalValue);
        textViewBalanceValue = (TextView) view.findViewById(R.id.textViewNEFinanceBalanceValue);
        textViewCreditBeforeValue = (TextView) view.findViewById(R.id.textViewNEFinanceCreditBeforeValue);
        textViewCreditAfterValue = (TextView) view.findViewById(R.id.textViewNEFinanceCreditAfterValue);
    }

    private void InitSpinners(Activity activity, View view){
        spinnerType = (Spinner) view.findViewById(R.id.spinnerNEFinanceType);
        spinnerType.setAdapter(new ArrayAdapter<PayType>(activity, android.R.layout.simple_spinner_dropdown_item, PayType.values()));
    }

    private void InitSwitchs(View view){
        switchPrintItemsList = (Switch) view.findViewById(R.id.switchNEFinancePrintItemsList);
        switchPrintPayments = (Switch) view.findViewById(R.id.switchNEFinancePrintPayments);
        switchPrintRealQtty = (Switch) view.findViewById(R.id.switchNEFinancePrintRealQtty);
    }

    private void InitOther(View view) {
        viewFlipper = (ViewFlipper) view.findViewById(R.id.viewFlipperNEFinance);
        new SwipSlider(viewFlipper);

        datePicker = (DatePicker) view.findViewById(R.id.datePickerNEFinanceDate);

        numberPickerPPI = (NumberPicker) view.findViewById(R.id.numberPickerNEFinancePrintPriceIn);
        numberPickerPPO = (NumberPicker) view.findViewById(R.id.numberPickerNEFinancePrintPriceOut);
        numberPickerPPIO = (NumberPicker) view.findViewById(R.id.numberPickerNEFinancePrintPriceInOut);

        numberPickerPPI.setMinValue(0);
        numberPickerPPO.setMinValue(0);
        numberPickerPPIO.setMinValue(0);

        numberPickerPPI.setMaxValue(10);
        numberPickerPPO.setMaxValue(10);
        numberPickerPPIO.setMaxValue(10);
    }
    //endregion

    //region public methods
    public void show(Activity activity) {
        View view = activity.getLayoutInflater().inflate( R.layout.newfinance, null);
        Init(activity, view);
        AlertDialog.Builder dialog = new AlertDialog.Builder(activity);
        dialog.setTitle(R.string.payments)
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
    private void RefreshTotals() {
        double pay = Convert.ToDouble(editTextPay.getText());
        double balance = payInfo.total - pay;
        textViewBalanceValue.setText(Convert.ToString(balance));
        textViewCreditAfterValue.setText(Convert.ToString(payInfo.credit + balance));
    }

    private void FillControls() {
        textViewTotalValue.setText(Convert.ToString(payInfo.total));
        editTextPay.setText(Convert.ToString(payInfo.total));
        textViewCreditBeforeValue.setText(Convert.ToString(payInfo.credit));
        textViewCreditAfterValue.setText(Convert.ToString(payInfo.credit));
    }

    private void Save() {
        payInfo.date = DateTime.now();
        payInfo.payed = Convert.ToDouble(editTextPay.getText());
        payInfo.type = PayType.fromIndex(spinnerType.getSelectedItemPosition());
        doEvent(payInfo);
    }
    //endregion
}