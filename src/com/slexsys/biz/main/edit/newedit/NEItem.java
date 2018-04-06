package com.slexsys.biz.main.edit.newedit;

import android.os.Bundle;
import android.text.InputType;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.*;
import com.slexsys.biz.R;
import com.slexsys.biz.database.comp.Convert;
import com.slexsys.biz.GO;
import com.slexsys.biz.database.comp.comp.ItemType;
import com.slexsys.biz.database.items.iItem;
import com.slexsys.biz.database.sqls.iSQL;
import com.slexsys.biz.main.comp.Events.OnEventListener;
import com.slexsys.biz.main.comp.NewTypes.SmartActivity;

import java.io.Serializable;
import java.util.Arrays;
import java.util.List;

/**
 * Created by slexsys on 3/11/16.
 */
public class NEItem extends SmartActivity {
    //region override
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.neitem);
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
    public static final String PUTTER_PIG = "pig";
    public static final String PUTTER_POG = "pog";
    public static final String PUTTER_BG = "bg";
    public static final String PUTTER_BG2 = "bg2";
    public static final String PUTTER_CG = "cg";
    //endregion

    //region fields
    private iItem item;
    private int groupid;
    private String maxCode;

    private int pig;
    private int pog;
    private int bg;
    private int bg2;
    private int cg;
    //endregion

    //region views
    private EditText editTextCode;
    private Switch switchActive;
    private EditText editTextName;
    private EditText editTextBarcode;
    private Button buttonBarcode;
    private EditText editTextPriceIn;
    private Button buttonPriceIn;
    private EditText editTextPriceOut;
    private Button buttonPriceOut;
    private Spinner spinnerMeasure;
    private EditText editTextPLU;
    private EditText editTextCatalog;
    private EditText editTextBarcode2;
    private Button buttonBarcode2;
    private Spinner spinnerMeasure2;
    private EditText editTextRatio;
    private Button buttonCatalog;
    private EditText editTextMinimalQtty;
    private EditText editTextNominalQtty;
    private EditText editTextDescription;
    private Spinner spinnerTaxGroup;
    private Spinner spinnerType;
    //endregion

    //region constructors
    public NEItem() {
        super(NEItem.class);
    }
    //endregion

    //region init
    private void Init(){
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
                item = (iItem) serializable;
            } else {
                groupid = bundle.getInt(PUTTER_GROUP_ID);
                maxCode = bundle.getString(PUTTER_MAX_CODE);
                pig = bundle.getInt(PUTTER_PIG, GO.getPig());
                pog = bundle.getInt(PUTTER_POG, GO.getPog());
                bg = bundle.getInt(PUTTER_BG, 0);
                bg2 = bundle.getInt(PUTTER_BG2, 0);
                cg = bundle.getInt(PUTTER_CG, 0);
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
        InitButtons();
        InitSwitches();
    }

    private void InitEditTexts(){
        editTextCode = (EditText) findViewById(R.id.editTextNEItemCode);
        editTextName = (EditText) findViewById(R.id.editTextNEItemName);
        editTextBarcode = (EditText) findViewById(R.id.editTextNEItemBarcode);
        editTextPriceIn = (EditText) findViewById(R.id.editTextPriceIn);
        editTextPriceOut = (EditText) findViewById(R.id.editTextNEItemPriceOut);
        editTextPLU = (EditText) findViewById(R.id.editTextNEItemPLU);
        editTextCatalog = (EditText) findViewById(R.id.editTextNEItemCatalog);
        editTextBarcode2 = (EditText) findViewById(R.id.editTextNEItemBarcode2);
        editTextRatio = (EditText) findViewById(R.id.editTextNEItemRatio);
        editTextMinimalQtty = (EditText) findViewById(R.id.editTextNEItemMinimalQtty);
        editTextNominalQtty = (EditText) findViewById(R.id.editTextNEItemNominalQtty);
        editTextDescription = (EditText) findViewById(R.id.editTextNEItemDescription);
    }

    private void InitSpinners(){
        spinnerMeasure = (Spinner) findViewById(R.id.spinnerNEItemMeasure);
        spinnerMeasure2 = (Spinner) findViewById(R.id.spinnerNEItemMeasure2);
        spinnerTaxGroup = (Spinner) findViewById(R.id.spinnerNEItemTaxGroup);
        spinnerType = (Spinner) findViewById(R.id.spinnerNEItemType);

        FillSpinner(spinnerMeasure, GO.measure1.getNames());
        FillSpinner(spinnerMeasure2, GO.measure2.getNames());
        FillSpinner(spinnerTaxGroup, GO.taxgroups.getNames());
        FillSpinner(spinnerType, Arrays.asList(ItemType.getNamesTranslated(this)));
    }

    private void InitButtons(){
        buttonBarcode = (Button) findViewById(R.id.buttonNEItemBarcode);
        buttonBarcode2 = (Button) findViewById(R.id.buttonNEItemBarcode2);
        buttonPriceIn = (Button) findViewById(R.id.buttonNEItemPriceIn);
        buttonPriceOut = (Button) findViewById(R.id.buttonNEItemPriceOut);
        buttonCatalog = (Button) findViewById(R.id.buttonNEItemCatalog);

        buttonBarcode.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                OpenNEListString(item.getBarcode(), editTextBarcode, bg);
            }
        });
        buttonBarcode2.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                OpenNEListString(item.getBarcode2(), editTextBarcode2, bg2);
            }
        });
        buttonPriceIn.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                OpenNEListDouble(item.getPricein(), editTextPriceIn, pig);
            }
        });
        buttonPriceOut.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                OpenNEListDouble(item.getPriceout(), editTextPriceOut, pog);
            }
        });
        buttonCatalog.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                OpenNEListString(item.getCatalog(), editTextCatalog, cg);
            }
        });
    }

    private void InitSwitches() {
        switchActive = (Switch) findViewById(R.id.switchNEItemActive);
    }

    private void FillItem(){
        editTextCode.setText(item.getCode());
        switchActive.setChecked(!item.getDeleted());
        editTextName.setText(item.getName());
        editTextBarcode.setText(item.getBarcode().get(bg));
        editTextPriceIn.setText(Double.toString(item.getPricein().get(pig)));
        editTextPriceOut.setText(Double.toString(item.getPriceout().get(pog)));
        spinnerMeasure.setSelection(item.getMeasure());
        editTextPLU.setText(Convert.ToString(item.getPlu()));
        editTextCatalog.setText(item.getCatalog().get(cg));
        editTextBarcode2.setText(item.getBarcode2().get(bg2));
        spinnerMeasure2.setSelection(item.getMeasure2());
        editTextRatio.setText(Convert.ToString(item.getRatio()));
        editTextMinimalQtty.setText(Convert.ToString(item.getMinqtty()));
        editTextNominalQtty.setText(Convert.ToString(item.getNormalqtty()));
        editTextDescription.setText(item.getDescription());
        spinnerTaxGroup.setSelection(item.getTaxgroup());
        spinnerType.setSelection(item.getType().ordinal());
    }

    private void FillCode() {
        editTextCode.setText(maxCode);
    }
    //endregion

    //region inner methods
    private void OpenNEListString(final List<String> list, final EditText editText, final int index) {
        NEList neList = new NEList();
        neList.setOnEventListener(new OnEventListener() {
            @Override
            public void onEvent(Serializable serializable) {
                List<String> result = Convert.ToSplitedText(serializable);
                list.removeAll(list);
                list.addAll(result);
                editText.setText(list.get(index));
            }
        });
        String items = Convert.StringListToString(list);
        neList.putExtra(NEList.PUTTER_ITEMS, items);
        neList.show(this);
    }

    private void OpenNEListDouble(final List<Double> list, final EditText editText, final int index) {
        NEList neList = new NEList();
        neList.setOnEventListener(new OnEventListener() {
            @Override
            public void onEvent(Serializable serializable) {
                List<Double> result = Convert.ToPriceList(serializable);
                list.removeAll(list);
                list.addAll(result);
                editText.setText(Double.toString(list.get(index)));
            }
        });
        String items = Convert.DoubleListToString(list);
        neList.putExtra(NEList.PUTTER_ITEMS, items);
        neList.putExtra(NEList.PUTTER_INPUT_TYPE, InputType.TYPE_CLASS_NUMBER);
        neList.show(this);
    }

    private void ClickButtonSave() {
        boolean isSaved = Save();
        if (isSaved) {
            doEvent(item);
            this.finish();
        } else {
            Toast.makeText(this, R.string.save_problem, Toast.LENGTH_SHORT).show();
        }
    }

    private void FillSpinner(Spinner spinner, List<String> list) {
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_dropdown_item, list);
        spinner.setAdapter(adapter);
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

    private iItem getItem() {
        final boolean isNew = item == null;
        return new iItem() {{
            setId(isNew ? 0 : item.getId());
            setCode(editTextCode.getText().toString());
            setName(editTextName.getText().toString());
            setName2(editTextName.getText().toString());
            setBarcode(item.getBarcode());
            setPricein(item.getPricein());
            setPriceout(item.getPriceout());
            setMeasure(spinnerMeasure.getSelectedItemPosition());
            setPlu(Integer.parseInt(editTextPLU.getText().toString()));
            setCatalog(item.getCatalog());
            setBarcode2(item.getBarcode2());
            setMeasure2(spinnerMeasure2.getSelectedItemPosition());
            setRatio(Double.parseDouble(editTextRatio.getText().toString()));
            setMinqtty(Double.parseDouble(editTextMinimalQtty.getText().toString()));
            setNormalqtty(Double.parseDouble(editTextNominalQtty.getText().toString()));
            setDescription(editTextDescription.getText().toString());
            setTaxgroup(spinnerTaxGroup.getSelectedItemPosition());
            setType(ItemType.getValueByPos(spinnerType.getSelectedItemPosition()));
            setIsrecipe(isNew ? false : item.getIsrecipe());
            setGroupid(isNew ? groupid : item.getGroupid());
            setDeleted(!switchActive.isChecked());
        }};
    }
    //endregion
}