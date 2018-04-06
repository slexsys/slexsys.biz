package com.slexsys.biz.main.comp.dialogs;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;

import com.slexsys.biz.R;
import com.slexsys.biz.main.comp.Events.Event;

/**
 * Created by slexsys on 3/10/17.
 */

public class MessageDialog extends Event {

    public void show(Activity activity, int... array) {
        int i = 0;
        int[] texts = getTexts(array);
        AlertDialog.Builder builder = new AlertDialog.Builder(activity);
        builder.setTitle(texts[i++])
                .setPositiveButton(texts[i++], new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int id) {
                        doEvent(id);
                    }
                })
                .setNegativeButton(texts[i++], new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int id) { }
                });
        AlertDialog alert = builder.create();
        alert.show();
    }

    private int[] getTexts(int[] array) {
        int[] result = new int[]{
                R.string.exit_without_save,
                R.string.ok,
                R.string.cancel
        };
        for (int i = 0; i < array.length; ++i) {
            result[i] = array[i];
        }
        return result;
    }
}
