package com.slexsys.biz.main.comp.dialogs;

import android.app.DatePickerDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.widget.DatePicker;

import com.slexsys.biz.R;
import com.slexsys.biz.main.comp.NewTypes.DateTime;
import com.slexsys.biz.main.comp.Events.Event;

/**
 * Created by slexsys on 2/20/17.
 */

public class DateChooseDialog extends Event {

    private static DateTime dateTime;

    private static DateChooseDialog ourInstance = new DateChooseDialog();

    public static DateChooseDialog getInstance() {
        return ourInstance;
    }

    static {
        dateTime = DateTime.now();
    }

    private DateChooseDialog() { }

    public void show(Context context) {
        String text = context.getResources().getString(R.string.done);
        final DatePickerDialog dpd = new DatePickerDialog(context, null,
                                dateTime.getYear(),
                                dateTime.getMonth() - 1,
                                dateTime.getDay());
        dpd.setButton(DialogInterface.BUTTON_POSITIVE, text, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                if (which == DialogInterface.BUTTON_POSITIVE) {
                    DatePicker dp = dpd.getDatePicker();
                    dateTime = new DateTime(
                            dp.getYear(),
                            dp.getMonth() + 1,
                            dp.getDayOfMonth()
                    );
                    doEvent(dateTime);
                }
            }
        });
        dpd.show();
    }
}
