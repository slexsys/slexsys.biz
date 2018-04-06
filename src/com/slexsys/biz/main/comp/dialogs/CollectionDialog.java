package com.slexsys.biz.main.comp.dialogs;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;

import com.slexsys.biz.R;
import com.slexsys.biz.main.comp.Events.Event;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * Created by slexsys on 2/22/17.
 */

public class CollectionDialog extends Event {

    public CollectionDialog() { }

    public void showSingleChoice(Activity activity, int title, List<String> list) {
        CharSequence[] cs = list.toArray(new CharSequence[list.size()]);
        AlertDialog.Builder builder = new AlertDialog.Builder(activity);
        builder.setTitle(title)
                .setItems(cs, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        doEvent(which);
                    }
                })
                .setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int id) { }
                });
        AlertDialog alert = builder.create();
        alert.show();
    }

    public void showMultiChoice(Activity activity, int title, Collection<String> collection, ArrayList arrayList) {
        CharSequence[] cs = collection.toArray(new CharSequence[collection.size()]);
        final ArrayList selectedItems = getSelectedItems(arrayList);
        final boolean[] checkeds = getCheckeds(arrayList, collection.size());
        AlertDialog.Builder builder = new AlertDialog.Builder(activity);
        builder.setTitle(title)
                .setMultiChoiceItems(cs, checkeds, new DialogInterface.OnMultiChoiceClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which, boolean isChecked) {
                        if (isChecked) {
                            selectedItems.add(which);
                        } else if (selectedItems.contains(which)) {
                            selectedItems.remove(Integer.valueOf(which));
                        }
                    }
                })
                .setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int id) {
                        doEvent(selectedItems);
                    }
                })
                .setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int id) { }
                });
        AlertDialog alert = builder.create();
        alert.show();
    }

    private ArrayList getSelectedItems(ArrayList arrayList) {
        ArrayList result;
        if (arrayList != null) {
            result = arrayList;
        } else {
            result = new ArrayList();
        }
        return result;
    }

    private boolean[] getCheckeds(ArrayList arrayList, int size) {
        boolean[] array = new boolean[size];
        if (arrayList != null) {
            for (Object o : arrayList) {
                int index = (Integer) o;
                array[index] = true;
            }
        }
        return array;
    }
}
