package com.slexsys.biz.main.edit.newedit;

import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.*;
import com.slexsys.biz.R;
import com.slexsys.biz.database.items.iUser;
import com.slexsys.biz.database.sqls.iSQL;
import com.slexsys.biz.main.comp.NewTypes.SmartActivity;
import com.slexsys.biz.database.comp.comp.UserLevel;

import java.io.Serializable;

/**
 * Created by slexsys on 3/11/16.
 */
public class NEUser extends SmartActivity {
    //region override
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.neuser);
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
    private iUser item;
    private int groupid;
    private String maxCode;
    //endregion

    //region views
    private EditText editTextCode;
    private Switch switchActive;
    private EditText editTextName;
    private EditText editTextPassword;
    private EditText editTextCard;
    private Spinner spinnerLevel;
    //endregion

    //region constructors
    public NEUser() {
        super(NEUser.class);
    }
    //endregion

    //region init
    private void Init(){
        initLocalFromGlobal();
        InitExtraFields();
        InitControls();
        InitVariables();
    }

    private void InitExtraFields() {
        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
            Serializable serializable = bundle.getSerializable(PUTTER_ITEM);
            if (serializable != null) {
                item = (iUser) serializable;
            } else {
                groupid = bundle.getInt(PUTTER_GROUP_ID);
                maxCode = bundle.getString(PUTTER_MAX_CODE);
            }
        }
    }

    private void InitVariables() {
        if(item != null) {
            FillItem();
        } else {
            FillCode();
        }
    }

    private void InitControls() {
        InitEditTexts();
        InitSpinners();
        InitSwitches();
    }

    private void InitEditTexts(){
        editTextCode = (EditText) findViewById(R.id.editTextNEUserCode);
        editTextName = (EditText) findViewById(R.id.editTextNEUserName);
        editTextPassword = (EditText) findViewById(R.id.editTextNEUserPassword);
        editTextCard = (EditText) findViewById(R.id.editTextNEUserCard);
    }

    private void InitSpinners(){
        spinnerLevel = (Spinner) findViewById(R.id.spinnerNEUserLevel);
        spinnerLevel.setAdapter(new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1,
                UserLevel.getNamesTranslated(this)));
    }

    private void InitSwitches(){
        switchActive = (Switch) findViewById(R.id.switchNEUserActive);
    }

    private void FillItem(){
        editTextCode.setText(item.getCode());
        switchActive.setChecked(!item.getDeleted());
        editTextName.setText(item.getName());
        editTextPassword.setText(item.getPassword());
        editTextCard.setText(item.getCard());
        spinnerLevel.setSelection(item.getLevel().ordinal());
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

    private iUser getItem() {
        final boolean isNew = item == null;
        return new iUser() {{
            setId(isNew ? 0 : item.getId());
            setCode(editTextCode.getText().toString());
            setName(editTextName.getText().toString());
                setName2(editTextName.getText().toString());
            setPassword(editTextPassword.getText().toString());
            setCard(editTextCard.getText().toString());
            setLevel(UserLevel.getValueByPos(spinnerLevel.getSelectedItemPosition()));
            setGroupid(isNew ? groupid : item.getGroupid());
            setDeleted(switchActive.isChecked());
        }};
    }
    //endregion
}