package com.slexsys.biz.main.comp.NewTypes;

import android.app.ActionBar;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.view.Menu;
import android.view.MenuItem;

import com.slexsys.biz.main.comp.Events.Event;
import com.slexsys.biz.main.comp.Events.OnEventListener;
import com.slexsys.biz.main.comp.dialogs.MessageDialog;

import java.io.Serializable;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * Created by slexsys on 3/6/17.
 */

public class SmartActivity extends Activity{
    //region fields
    public static Event eventinit;

    protected Map<String, Object> extras;
    protected Event event;
    protected Class cls;
    protected boolean exitQuestion;
    protected String title;
    //endregion

    @Override
    public boolean onPrepareOptionsMenu(Menu menu){
        InitActionBar();
        super.onPrepareOptionsMenu(menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        boolean result = true;
        switch (item.getItemId()) {
            case android.R.id.home:
                exit();
                break;
            default:
                result = super.onOptionsItemSelected(item);
                break;
        }
        return result;
    }

    //region constructors
    public SmartActivity() {
        event = new Event();
        extras = new LinkedHashMap<String, Object>();
    }

    public SmartActivity(Class cls) {
        this();
        this.cls = cls;
    }
    //endregion

    //region public methods
    public void show(Activity activity) {
        Intent intent = new Intent(activity, cls);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        for (Map.Entry<String, Object> entry : extras.entrySet()) {
            put2intent(intent, entry.getKey(), entry.getValue());
        }
        activity.startActivity(intent);
    }

    public void show(Context context) {
        Intent intent = new Intent(context, cls);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        for (Map.Entry<String, Object> entry : extras.entrySet()) {
            put2intent(intent, entry.getKey(), entry.getValue());
        }
        context.startActivity(intent);
    }

    private void put2intent(Intent intent, String key, Object value) {
        if (value instanceof Integer) {
            intent.putExtra(key, (Integer) value);
        } else if (value instanceof Double) {
            intent.putExtra(key, (Double) value);
        } else if (value instanceof String) {
            intent.putExtra(key, (String) value);
        } else if (value instanceof Boolean) {
            intent.putExtra(key, (Boolean) value);
        } else if (value instanceof Serializable) {
            intent.putExtra(key, (Serializable) value);
        }
    }

    public void putExtra(String name, Object object) {
        extras.put(name, object);
    }

    protected void initLocalFromGlobal() {
        event = eventinit;
        eventinit = null;
    }

    public void setOnEventListener(OnEventListener listener) {
        if (listener != null) {
            eventinit = new Event();
            eventinit.setOnEventListener(listener);
        }
    }

    private void InitActionBar() {
        ActionBar actionBar = getActionBar();
        // Enabling Up / Back navigation
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setTitle(title);
        //actionBar.setBackgroundDrawable(new ColorDrawable(Color.GRAY));
    }

    public void doEvent(Serializable serializable) {
        event.doEvent(serializable);
    }

    private void exit() {
        if (exitQuestion) {
            MessageDialog md = new MessageDialog();
            md.setOnEventListener(new OnEventListener() {
                @Override
                public void onEvent(Serializable serializable) {
                    finish();
                }
            });
            md.show(this);
        } else {
            finish();
        }
    }
    //endregion
}
