package com.slexsys.biz.database.sqls.MySQL;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.view.View;

import com.slexsys.biz.R;
import com.slexsys.biz.main.comp.Events.Event;
import com.slexsys.biz.main.comp.NewTypes.SmartDialog;

/**
 * Created by slexsys on 1/15/18.
 */

public class MySQLConnectionDialog extends Event implements SmartDialog {
    @Override
    public void show(Activity activity) {
        View view = activity.getLayoutInflater().inflate(R.layout.connection_dialog, null);
        Init(activity, view);
        AlertDialog.Builder dialog = new AlertDialog.Builder(activity);
        dialog.setTitle(R.string.connection_error)
                .setView(view)
                .setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int id) {
                        //Save();
                    }
                })
                .setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int id) { }
                });
        dialog.show();
    }

    private void Init(Activity activity, View view) {

    }
}
