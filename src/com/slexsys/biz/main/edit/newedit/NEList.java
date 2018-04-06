package com.slexsys.biz.main.edit.newedit;

import android.os.Bundle;
import android.text.InputType;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.ListView;
import com.slexsys.biz.R;
import com.slexsys.biz.database.comp.Convert;
import com.slexsys.biz.main.comp.NewTypes.SmartActivity;

import java.util.List;

/**
 * Created by slexsys on 3/19/16.
 */
public class NEList extends SmartActivity {
    //region override
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.nelist);
        Init();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.neitem, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        boolean result = true;
        switch (item.getItemId()) {
            case R.id.menuItemNEItemSave:
                ClickButtonSave();
                break;
            case R.id.menuItemNEItemCodeAuto:
                break;
            default:
                result = super.onOptionsItemSelected(item);
                break;
        }
        return result;
    }
    //endregion

    //region finals
    public static final String PUTTER_ITEMS = "items";
    public static final String PUTTER_INPUT_TYPE = "input_type";
    //endregion

    //region fields
    private int inputType;
    private List<Object> items;

    private ListView listView;
    private NEListItemAdapter adapter;
    //endregion

    //region constructor
    public NEList() {
        super(NEList.class);
    }
    //endregion

    //region methods
    private void Init() {
        initLocalFromGlobal();
        InitExtraFields();
        InitViews();
        InitFields();
    }

    private void InitExtraFields() {
        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
            String line = bundle.getString(PUTTER_ITEMS);
            items = Convert.StringToListObject(line);
            inputType = bundle.getInt(PUTTER_INPUT_TYPE, InputType.TYPE_CLASS_TEXT);
        }
    }

    private void InitViews() {
        listView = (ListView) findViewById(R.id.listViewNEList);
    }

    private void InitFields() {
        if(items != null) {
            if (items.isEmpty()) {
                items.add("");
            }
            adapter = new NEListItemAdapter(this, items, inputType);
            listView.setAdapter(adapter);
        }
    }

    private void ClickButtonSave() {
        Save();
        this.finish();
    }

    private void Save() {
        if (adapter != null){
            items = adapter.getItems();
            String result = Convert.ListObjectToString(items);
            doEvent(result);
        }
    }
    //endregion
}