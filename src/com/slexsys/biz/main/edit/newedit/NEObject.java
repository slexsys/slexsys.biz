package com.slexsys.biz.main.edit.newedit;

import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.*;
import com.slexsys.biz.R;
import com.slexsys.biz.database.items.iObject;
import com.slexsys.biz.database.sqls.iSQL;
import com.slexsys.biz.main.comp.NewTypes.SmartActivity;
import com.slexsys.biz.database.comp.comp.PriceGroups;

import java.io.Serializable;

/**
 * Created by slexsys on 3/11/16.
 */
public class NEObject extends SmartActivity {

    //region override
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.neobject);
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
    public static final String PUTTER_ITEM = "item";
    public static final String PUTTER_GROUP_ID = "group_id";
    public static final String PUTTER_MAX_CODE = "max_code";
    //endregion

    //region fields
    private iObject item;
    private int groupid;
    private String maxCode;
    //endregion

    //region views
    private EditText editTextCode;
    private Switch switchActive;
    private EditText editTextName;
    private Spinner spinnerPriceGroup;
    //endregion

    //region constructors
    public NEObject() {
        super(NEObject.class);
    }
    //endregion

    //region init
    private void Init() {
        initLocalFromGlobal();
        InitExtraFields();
        InitViews();
        InitFields();
    }

    private void InitExtraFields() {
        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
            Serializable serializable = bundle.getSerializable(PUTTER_ITEM);
            if (serializable != null) {
                item = (iObject) serializable;
            } else {
                groupid = bundle.getInt(PUTTER_GROUP_ID);
                maxCode = bundle.getString(PUTTER_MAX_CODE);
            }
        }
    }

    private void InitFields() {
        if(item != null) {
            FillItem();
        } else {
            FillCode();
        }
    }

    private void InitViews(){
        InitEditTexts();
        InitSpinners();
        InitSwitches();
    }

    private void InitEditTexts(){
        editTextCode = (EditText) findViewById(R.id.editTextNEObjectCode);
        editTextName = (EditText) findViewById(R.id.editTextNEObjectName);
    }

    private void InitSpinners() {
        spinnerPriceGroup = (Spinner) findViewById(R.id.spinnerNEObjectPriceGroup);
        spinnerPriceGroup.setAdapter(new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1,
                PriceGroups.getNamesTranslated(this)));
    }

    private void InitSwitches(){
        switchActive = (Switch) findViewById(R.id.switchNEObjectActive);
    }

    private void FillItem(){
        editTextCode.setText(item.getCode());
        switchActive.setChecked(!item.getDeleted());
        editTextName.setText(item.getName());
        spinnerPriceGroup.setSelection(item.getPricegroup().ordinal());
    }

    private void FillCode() {
        editTextCode.setText(maxCode);
    }
    //endregion

    //region inner methods
    private void ClickButtonSave() {
        boolean isSaved = Save();
        if (isSaved) {
            doEvent(item);
            this.finish();
        } else {
            Toast.makeText(this, R.string.save_problem, Toast.LENGTH_SHORT).show();
        }
    }

    private boolean Save() {
        boolean isNew = item == null;
        item = getItem();
        String query;
        if (isNew) {
            query = item.getAddQuery();
        } else {
            query = item.getEditQuery();
        }
        return iSQL.update(query);
    }

    private iObject getItem() {
        final boolean isNew = item == null;
        return new iObject() {{
            setId(isNew ? 0 : item.getId());
            setCode(editTextCode.getText().toString());
            setName(editTextName.getText().toString());
                setName2(editTextName.getText().toString());
            setPricegroup(PriceGroups.getValueByPos(spinnerPriceGroup.getSelectedItemPosition()));
            setGroupid(isNew ? groupid : item.getGroupid());
            setDeleted(switchActive.isChecked());
        }};
    }
    //endregion
}