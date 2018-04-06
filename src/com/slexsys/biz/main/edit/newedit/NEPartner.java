package com.slexsys.biz.main.edit.newedit;

import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.*;
import com.slexsys.biz.R;
import com.slexsys.biz.database.items.iPartner;
import com.slexsys.biz.database.sqls.iSQL;
import com.slexsys.biz.main.comp.NewTypes.DateTime;
import com.slexsys.biz.main.comp.NewTypes.SmartActivity;
import com.slexsys.biz.database.comp.comp.PartnerType;
import com.slexsys.biz.database.comp.comp.PriceGroups;

import java.io.Serializable;

/**
 * Created by slexsys on 3/11/16.
 */
public class NEPartner extends SmartActivity {

    //region override
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.nepartner);
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
    private iPartner item;
    private int groupid;
    private String maxCode;
    //endregion

    //region views
    private EditText editTextCode;
    private Switch switchActive;
    private EditText editTextName;
    private EditText editTextMOL;
    private EditText editTextCity;
    private EditText editTextAddress;
    private EditText editTextPhone;
    private EditText editTextFax;
    private EditText editTextEMail;
    private EditText editTextTaxNo;
    private EditText editTextVATNo;
    private EditText editTextCard;
    private EditText editTextDiscount;
    private EditText editTextPayDay;
    private Spinner spinnerType;
    private Spinner spinnerPriceGroup;
    //endregion

    //region constructors
    public NEPartner() {
        super(NEPartner.class);
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
                item = (iPartner) serializable;
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

    private void InitViews() {
        InitEditTexts();
        InitSpinners();
        InitSwitches();
    }

    private void InitEditTexts() {
        editTextCode = (EditText) findViewById(R.id.editTextNEPartnerCode);
        editTextName = (EditText) findViewById(R.id.editTextNEPartnerName);
        editTextMOL = (EditText) findViewById(R.id.editTextNEPartnerMOL);
        editTextCity = (EditText) findViewById(R.id.editTextNEPartnerCity);
        editTextAddress = (EditText) findViewById(R.id.editTextNEPartnerAddress);
        editTextPhone = (EditText) findViewById(R.id.editTextNEPartnerPhone);
        editTextFax = (EditText) findViewById(R.id.editTextNEPartnerFax);
        editTextEMail = (EditText) findViewById(R.id.editTextNEPartnerEMail);
        editTextTaxNo = (EditText) findViewById(R.id.editTextNEPartnerTaxNo);
        editTextVATNo = (EditText) findViewById(R.id.editTextNEPartnerVATNo);
        editTextCard = (EditText) findViewById(R.id.editTextNEPartnerCard);
        editTextDiscount = (EditText) findViewById(R.id.editTextNEPartnerDiscount);
        editTextPayDay = (EditText) findViewById(R.id.editTextNEPartnerPayDay);
    }

    private void InitSpinners() {
        spinnerType = (Spinner) findViewById(R.id.spinnerNEPartnerType);
        spinnerType.setAdapter(new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1,
                                                PartnerType.getNamesTranslated(this)));
        spinnerPriceGroup = (Spinner) findViewById(R.id.spinnerNEPartnerPriceGroup);
        spinnerPriceGroup.setAdapter(new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1,
                                                PriceGroups.getNamesTranslated(this)));
    }

    private void InitSwitches() {
        switchActive = (Switch) findViewById(R.id.switchNEPartnerActive);
    }

    private void FillItem() {
        editTextCode.setText(item.getCode());
        editTextName.setText(item.getName());
        editTextMOL.setText(item.getMol());
        editTextCity.setText(item.getCity());
        editTextAddress.setText(item.getAddress());
        editTextPhone.setText(item.getPhone());
        editTextFax.setText(item.getFax());
        editTextEMail.setText(item.getEmail());
        editTextTaxNo.setText(item.getTaxno());
        editTextVATNo.setText(item.getVatno());
        editTextCard.setText(item.getCardnumber());
        editTextDiscount.setText(Double.toString(item.getDiscount()));
        editTextPayDay.setText(Integer.toString(item.getPaymentdays()));
        spinnerPriceGroup.setSelection(item.getPricegroup().ordinal());
        spinnerType.setSelection(item.getType().ordinal());
        switchActive.setChecked(!item.getDeleted());
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

    private iPartner getItem() {
        final boolean isNew = item == null;
        return new iPartner() {{
            setId(isNew ? 0 : item.getId());
            setCode(editTextCode.getText().toString());
            setName(editTextName.getText().toString());
                setName2(editTextName.getText().toString());
            setMol(editTextMOL.getText().toString());
                setMol2(editTextMOL.getText().toString());
            setCity(editTextCity.getText().toString());
                setCity2(editTextCity.getText().toString());
            setAddress(editTextAddress.getText().toString());
                setAddress2(editTextAddress.getText().toString());
            setPhone(editTextPhone.getText().toString());
                setPhone2(editTextPhone.getText().toString());
            setFax(editTextFax.getText().toString());
            setEmail(editTextEMail.getText().toString());
            setTaxno(editTextTaxNo.getText().toString());
            setVatno(editTextVATNo.getText().toString());
            setCardnumber(editTextCard.getText().toString());
            setDiscount(Double.parseDouble(editTextDiscount.getText().toString()));
            setPaymentdays(Integer.parseInt(editTextPayDay.getText().toString()));
            setPricegroup(PriceGroups.getValueByPos(spinnerPriceGroup.getSelectedItemPosition()));
            setType(PartnerType.getValueByPos(spinnerType.getSelectedItemPosition()));
            setGroupid(isNew ? groupid : item.getGroupid());
            setDeleted(switchActive.isChecked());
            setUserrealtime(isNew ? DateTime.now().toSQLFormatDateTime() : item.getUserrealtime());
        }};
    }
    //endregion
}