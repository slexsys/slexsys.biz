package com.slexsys.biz.main.finance.comp;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.view.View;
import android.widget.*;
import com.slexsys.biz.R;
import com.slexsys.biz.comp.LoginInfo;
import com.slexsys.biz.comp.std;
import com.slexsys.biz.main.comp.Events.Event;
import com.slexsys.biz.main.comp.Events.OnEventListener;
import com.slexsys.biz.main.comp.NewTypes.DateTime;
import com.slexsys.biz.main.comp.NewTypes.SmartDialog;
import com.slexsys.biz.main.comp.dialogs.DateChooseDialog;
import com.slexsys.biz.main.comp.enums.ConsType;

import java.io.Serializable;
import java.util.LinkedList;
import java.util.List;

/**
 * Created by slexsys on 3/16/16.
 */
public class NEConsumption extends Event implements SmartDialog {

    //region finals
    public static int POS_DATE;
    public static int POS_DESC;
    public static int POS_TYPE;
    public static int POS_SUM;
    public static int POS_SIGN;
    public static int POS_USER_ID;
    public static int POS_OBJECT_ID;
    public static int POS_ID;
    //endregion

    //region views
    private TextView textViewDateValue;
    private Button buttonDateChoose;
    private EditText editTextDescription;
    private Spinner spinnerType;
    private EditText editTextSum;
    private Switch switchPrint;
    //endregion

    //region fields
    private DateTime dateTime;
    private Context context;
    //endregion

    //region constructors
    public NEConsumption() {
        dateTime = DateTime.now();
    }
    //endregion

    //region public methods
    @Override
    public void show(Activity activity) {
        View view = activity.getLayoutInflater().inflate( R.layout.necashbook, null);
        Init(activity, view);
        final AlertDialog.Builder builder = new AlertDialog.Builder(activity);
        builder.setTitle(R.string.new_consumption)
                .setView(view)
                .setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int id) { }
                })
                .setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int id) { }
                });
        final AlertDialog dialog = builder.create();
        dialog.show();
        dialog.getButton(AlertDialog.BUTTON_POSITIVE).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               Save(dialog);
            }
        });
    }

    public void fillViews(List<Object> list) {
        int i = 0;
        dateTime = DateTime.fromSQLFormat((String) list.get(i++));
        setDateValue();
        editTextDescription.setText((String) list.get(i++));
        spinnerType.setSelection((Integer) list.get(i++));
        editTextSum.setText((String) list.get(i++));
    }
    //endregion

    //region private methods
    private void Init(Context context, View view){
        this.context = view.getContext();
        textViewDateValue = (TextView) view.findViewById(R.id.textViewNECashbookDateValue);
        setDateValue();

        buttonDateChoose = (Button) view.findViewById(R.id.buttonNECashbookDateChoose);
        buttonDateChoose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DateChoose();
            }
        });

        editTextDescription = (EditText) view.findViewById(R.id.editTextNECashbookDescription);
        editTextSum = (EditText) view.findViewById(R.id.editTextNECashbookSum);

        spinnerType = (Spinner) view.findViewById(R.id.spinnerNECashbookType);
        std.InitSpinner(context, spinnerType, ConsType.names());

        switchPrint = (Switch) view.findViewById(R.id.switchNECashbookPrint);
    }

    private void DateChoose() {
        DateChooseDialog dialog = DateChooseDialog.getInstance();
        dialog.setOnEventListener(new OnEventListener() {
            @Override
            public void onEvent(Serializable serializable) {
                dateTime = (DateTime) serializable;
                setDateValue();
            }
        });
        dialog.show(context);
    }

    private void Save(AlertDialog dialog) {
        if (isSave()) {
            LinkedList<String> result = new LinkedList<String>() {{
                int i = 0;

                POS_DATE = i++;
                add(dateTime.toSQLFormatDate());

                POS_DESC = i++;
                add(editTextDescription.getText().toString());

                POS_TYPE = i++;
                String id = ConsType.getIdByIndex(spinnerType.getSelectedItemPosition());
                add(id);

                POS_SUM = i++;
                add(editTextSum.getText().toString());

                POS_SIGN = i++;
                String sign = ConsType.getSignById(id);
                add(sign);

                POS_USER_ID = i++;
                String userId = Integer.toString(LoginInfo.user.getId());
                add(userId);

                POS_OBJECT_ID = i++;
                String objectId = Integer.toString(LoginInfo.object.getId());
                add(objectId);

                POS_ID = i++;
            }};
            doEvent(result);
            dialog.dismiss();
        }
    }

    private boolean isSave() {
        boolean result = true;
        if (editTextDescription.getText().toString().trim().length() == 0) {
            result = false;
            Toast.makeText(context, R.string.pls_fill_desc, Toast.LENGTH_SHORT).show();
        }
        try {
            double value = Double.parseDouble(editTextSum.getText().toString());
            if (value <= 0) {
                result = false;
                Toast.makeText(context, R.string.pls_correct_sum, Toast.LENGTH_SHORT).show();
            }
        } catch (Exception ex) {
            result = false;
            Toast.makeText(context, R.string.pls_fill_sum, Toast.LENGTH_SHORT).show();
        }
        return result;
    }

    private void setDateValue() {
        textViewDateValue.setText(dateTime.toNormalDateFormat());
    }
    //endregion
}