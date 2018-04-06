package com.slexsys.biz.main.operations.comp;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.*;
import com.slexsys.biz.R;
import com.slexsys.biz.GO;
import com.slexsys.biz.comp.GlobalOptions;
import com.slexsys.biz.comp.std;
import com.slexsys.biz.database.comp.Convert;
import com.slexsys.biz.database.items.iItem;
import com.slexsys.biz.main.comp.COPDRF.COPDRFColumns;
import com.slexsys.biz.main.comp.Events.Event;
import com.slexsys.biz.main.comp.NewTypes.SmartDialog;
import com.slexsys.biz.main.operations.json.TotalMode;

import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.util.LinkedList;
import java.util.List;

/**
 * Created by slexsys on 3/18/16.
 */
public class NEOperation extends Event implements SmartDialog {

    //region fields
    private iItem item;
    private OperItem operItem;
    private int pig;
    private int pog;
    Context context;

    private COPDRFColumns columns;
    private TotalMode totalMode;
    //endregion

    //region views
    private LinearLayout linearLayoutAllViews;
    private TextView textViewCodeValue;
    private EditText editTextQtty;
    private TextView textViewRealQtty;
    private Spinner spinnerMeasure;
    private EditText editTextPriceIn;
    private EditText editTextPercent;
    private EditText editTextPriceOut;
    private EditText editTextDiscount;
    private Spinner spinnerCurrency;
    private TextView textViewDescription;
    private TextView textViewAmountInValue;
    private TextView textViewAmountOutValue;
    //endregion

    //region constructors
    public NEOperation(iItem item, COPDRFColumns columns, TotalMode totalMode) {
        this.item = item;
        this.columns = columns;
        this.totalMode = totalMode;
    }

    public NEOperation(OperItem operItem, COPDRFColumns columns, TotalMode totalMode) {
        this.operItem = operItem;
        this.columns = columns;
        this.totalMode = totalMode;
    }
    //endregion

    //region getters setters
    public iItem getItem() {
        return item;
    }

    public void setItem(iItem item) {
        this.item = item;
    }

    public OperItem getOperItem() {
        return operItem;
    }

    public void setOperItem(OperItem operItem) {
        this.operItem = operItem;
    }

    public int getPig() {
        return pig;
    }

    public void setPig(int pig) {
        this.pig = pig;
    }

    public int getPog() {
        return pog;
    }

    public void setPog(int pog) {
        this.pog = pog;
    }

    public COPDRFColumns getColumns() {
        return columns;
    }

    public void setColumns(COPDRFColumns columns) {
        this.columns = columns;
    }
    //endregion

    //region public methods
    public void show(Activity activity) {
        View view = activity.getLayoutInflater().inflate(R.layout.neoperation, null);
        Init(activity, view);
        AlertDialog.Builder dialog = new AlertDialog.Builder(activity);
        dialog.setTitle(getItemName())//R.string.ne_operation)
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

    //region init
    private void Init(Context context, View view) {
        InitFields(context);
        InitControls(view);
        InitPrivileges();
        FillControls();
    }

    private void InitFields(Context context) {
        this.context = context;
    }

    private void InitControls(View view){
        InitLinearLayouts(view);
        InitEditTexts(view);
        InitTextViews(view);
        InitSpinners(view);
    }

    private void InitLinearLayouts(View view) {
        linearLayoutAllViews = (LinearLayout) view.findViewById(R.id.linearLayoutNEOperationAllViews);
    }

    private void InitEditTexts(View view) {
        editTextQtty = (EditText) view.findViewById(R.id.editTextNEOperationQtty);
        RefreshTotalEvent(editTextQtty);
        editTextPriceIn = (EditText) view.findViewById(R.id.editTextNEOperationPriceIn);
        RefreshTotalEvent(editTextPriceIn);
        editTextPercent = (EditText) view.findViewById(R.id.editTextNEOperationPercent);
        RefreshTotalEvent(editTextPercent);
        editTextPercent.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean b) {
                if (!b) {
                    refreshPriceOut();
                }
            }
        });
        editTextPriceOut = (EditText) view.findViewById(R.id.editTextNEOperationPriceOut);
        RefreshTotalEvent(editTextPriceOut);
        editTextPriceOut.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean b) {
                if (!b) {
                    refreshPercent();
                }
            }
        });
        editTextDiscount = (EditText) view.findViewById(R.id.editTextNEOperationDiscount);
        RefreshTotalEvent(editTextDiscount);
    }

    private void InitTextViews(View view){
        textViewCodeValue = (TextView) view.findViewById(R.id.textViewNEOperationCodeValue);
        textViewRealQtty = (TextView) view.findViewById(R.id.textViewNEOperationRealQtty);
        textViewAmountInValue = (TextView) view.findViewById(R.id.textViewNEOperationAmountInValue);
        textViewAmountOutValue = (TextView) view.findViewById(R.id.textViewNEOperationAmountOutValue);
        textViewDescription = (TextView) view.findViewById(R.id.textViewNEOperationDescription);
    }

    private void InitSpinners(View view){
        spinnerMeasure = (Spinner) view.findViewById(R.id.spinnerNEOperationMeasure);
        spinnerCurrency = (Spinner) view.findViewById(R.id.spinnerNEOperationCurrency);
    }

    private void InitPrivileges() {
        std.initPrivileges(columns, linearLayoutAllViews);
    }
    //endregion

    //region filler
    private void FillControls() {
        if (operItem != null) {
            FillByODRFItem();
        } else if (item != null) {
            FillByItem();
        }
        RefreshTotals();
    }

    private void FillByItem() {
        String pricein = Convert.ToString(item.getPricein().get(pig));
        String priceout = Convert.ToString(item.getPriceout().get(pog));
        String qtty = Double.toString(item.getQtty());

        textViewCodeValue.setText(item.getCode());
        editTextQtty.setText("1");
        editTextQtty.requestFocus();
        editTextQtty.selectAll();
        textViewRealQtty.setText(qtty);
        FillSpinner(spinnerMeasure, GetMeasures());
        editTextPriceIn.setText(pricein);
        editTextPriceOut.setText(priceout);
        FillSpinner(spinnerCurrency, GetCurrencies());
        textViewDescription.setText("");
        textViewAmountInValue.setText(pricein);
        textViewAmountOutValue.setText(priceout);
    }

    private void FillByODRFItem() {
        item = iItem.getByID(Integer.toString(operItem.ID));
        textViewCodeValue.setText(operItem.Code);
        editTextQtty.setText(Convert.ToString(operItem.Qtty));
        editTextQtty.requestFocus();
        editTextQtty.selectAll();
        textViewRealQtty.setText(Convert.ToString(operItem.QttyR));
        FillSpinner(spinnerMeasure, GetMeasures());
        spinnerMeasure.setPrompt(operItem.Measure);
        editTextPriceIn.setText(Convert.ToString(operItem.PriceIn));
        editTextPriceOut.setText(Convert.ToString(operItem.PriceOut));
        FillSpinner(spinnerCurrency, GetCurrencies());
        spinnerCurrency.setPrompt(operItem.Currency);
        textViewDescription.setText(operItem.Description);
        textViewAmountInValue.setText(Convert.ToString(operItem.PriceIn));
        textViewAmountOutValue.setText(Convert.ToString(operItem.PriceOut));
    }
    //endregion

    //region private methods
    private void Save() {
        OperItem result = new OperItem() {{
            setID(item.getId());
            setCode(item.getCode());
            setName(item.getName());
            setMeasure(spinnerMeasure.getSelectedItem().toString());
            setQtty(Convert.ToDouble(editTextQtty.getText()));
            setQttyR(Convert.ToDouble(textViewRealQtty.getText()));
            setPriceIn(Convert.ToDouble(editTextPriceIn.getText()));
            setPriceOut(Convert.ToDouble(editTextPriceOut.getText()));
            setCurrency(spinnerCurrency.getSelectedItem().toString());
            setCurrencyID(GO.currencies.items.get(spinnerCurrency.getSelectedItemPosition()).getId());
            setAmountIn(Convert.ToDouble(textViewAmountInValue.getText()));
            setAmountOut(Convert.ToDouble(textViewAmountOutValue.getText()));
            setDescription(item.getDescription()/*textViewDescription.getText().toString()*/);
        }};
        doEvent(result);
    }

    private List<String> GetMeasures(){
        List<String> list = new LinkedList<String>();
        list.add(GO.measure1.items.get(item.getMeasure()).getName());
        if (item.getRatio() != 1) {
            list.add(GO.measure2.items.get(item.getMeasure2()).getName());
        }
        return list;
    }

    private List<String> GetCurrencies() {
        return GO.currencies.getNames();
    }

    private void FillSpinner(Spinner spinner, List<String> list) {
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(context, android.R.layout.simple_spinner_dropdown_item, list);
        spinner.setAdapter(adapter);
    }

    private void RefreshTotalEvent(EditText editText) {
        editText.addTextChangedListener(new TextWatcher() {

            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (!s.equals("") ) {
                    RefreshTotals();
                }

            }

            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            public void afterTextChanged(Editable s) {

            }
        });
    }

    private void refreshPercent() {
        double pricein = getPriceIn();
        double priceout = getPriceOut();

        editTextPercent.setText(toPercentString((100 * priceout / pricein) - 100));
    }

    private void refreshPriceOut() {
        double pricein = getPriceIn();
        double percent = getDouble(editTextPercent);

        editTextPriceOut.setText(toPriceString(pricein * (100 + percent) / 100));
    }

    private void RefreshTotals() {
        double qtty = getDouble(editTextQtty);
        double pricein = getPriceIn();
        double priceout = getPriceOut();

        textViewAmountInValue.setText(toPriceString(qtty * pricein));
        textViewAmountOutValue.setText(toPriceString(qtty * priceout));
    }

    private double getPriceIn() {
        double price = getDouble(editTextPriceIn);
        if (totalMode.equals(TotalMode.TotalIn)) {
            price = getDiscountedPrice(price);
        }
        return price;
    }

    private double getPriceOut() {
        double price = getDouble(editTextPriceOut);
        if (totalMode.equals(TotalMode.TotalOut)) {
            price = getDiscountedPrice(price);
        }
        return price;
    }

    private double getDiscountedPrice(double price) {
        double discount = getDouble(editTextDiscount);
        return price * (100 - discount) / 100;
    }

    private double getDouble(EditText editText) {
        double value = 0;
        try {
            value = Double.parseDouble(editText.getText().toString());
        } catch (Exception ex) { }
        return value;
    }

    private String toPercentString(double value) {
        return toString(value, GlobalOptions.percentFormat);
    }

    private String toPriceString(double value) {
        return toString(value, GlobalOptions.priceFormat);
    }

    private String toString(double value, int count) {
        DecimalFormatSymbols decimalSymbols = DecimalFormatSymbols.getInstance();
        decimalSymbols.setDecimalSeparator('.');
        return new DecimalFormat(getFormat(count), decimalSymbols).format(value);
    }

    private String getFormat(int count) {
        String result = "0";
        if (count > 0) {
            result += ".";
            for (int i = 0; i < count; ++i) {
                result += "0";
            }
        }
        return result;
    }

    private String getItemName() {
        String result = "";
        if (operItem != null) {
            result = operItem.Name;
        } else if (item != null) {
            result = item.getName();
        }
        return result;
    }
    //endregion
}