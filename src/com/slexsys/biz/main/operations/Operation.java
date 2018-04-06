package com.slexsys.biz.main.operations;

import android.app.ActionBar;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.slexsys.biz.GO;
import com.slexsys.biz.R;
import com.slexsys.biz.comp.std;
import com.slexsys.biz.database.comp.iIBase;
import com.slexsys.biz.database.comp.iItems;
import com.slexsys.biz.database.items.iItem;
import com.slexsys.biz.database.sqls.iSQL;
import com.slexsys.biz.main.comp.COPDRF.COPDRFColumns;
import com.slexsys.biz.main.comp.COPDRF.COPDRFRow;
import com.slexsys.biz.main.comp.COPDRF.COPDRFRows;
import com.slexsys.biz.main.comp.Events.OnEventListener;
import com.slexsys.biz.main.comp.NewTypes.ListViewItemMove;
import com.slexsys.biz.main.comp.NewTypes.SmartActivity;
import com.slexsys.biz.main.comp.dialogs.UIFiltersDialog;
import com.slexsys.biz.main.finance.comp.EditFinance;
import com.slexsys.biz.main.finance.comp.PayInfo;
import com.slexsys.biz.main.comp.dialogs.MessageDialog;
import com.slexsys.biz.main.edit.Edit;
import com.slexsys.biz.main.finance.comp.NewFinance;
import com.slexsys.biz.main.finance.comp.comp.EditPaymentsData;
import com.slexsys.biz.main.operations.comp.NEOperation;
import com.slexsys.biz.main.operations.comp.OperItem;
import com.slexsys.biz.main.operations.comp.OperationData;
import com.slexsys.biz.main.operations.json.InsertionMode;
import com.slexsys.biz.main.operations.json.OperationJSON;
import com.slexsys.biz.main.comp.COPDRF.COPDRFAdapter;
import com.slexsys.biz.main.operations.json.PayMode;
import com.slexsys.biz.main.operations.json.QttyMode;
import com.slexsys.biz.main.operations.json.TotalMode;
import com.slexsys.biz.main.report.comp.UIFilter;
import com.slexsys.biz.main.report.comp.UIFilterType;
import com.slexsys.biz.main.report.comp.UIFilters;

import java.io.Serializable;
import java.util.List;

/**
 * Created by slexsys on 3/6/17.
 */
public class Operation extends SmartActivity {
    //region override
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.blanktemplate);
        Start();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.operation, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        boolean result = true;
        switch (item.getItemId()) {
            case android.R.id.home:
                super.onOptionsItemSelected(item);
                break;
            case R.id.menuItemOperationAdd:
                ClickButtonAdd();
                break;
            case R.id.menuItemOperationSave:
                ClickButtonSave();
                break;
        }
        return result;
    }
    //endregion

    //region finals
    public static final String PUTTER_POS = "pos";
    public static final String PUTTER_OPERATION_DATA = "operation_data";
    //endregion

    //region views
    private LinearLayout linearLayoutUIInfo;
    private LinearLayout linearLayoutTotals;

    private ListView listViewData;

    private EditText editTextBarCode;
    //endregion

    //region fields
    private int operJSONPos;
    private OperationJSON operationJSON;

    private OperationData operationData;
    private COPDRFRows copdrfRows;
    private COPDRFAdapter adapter;

    private iItems lastSelectedItems;
    private TotalMode totalMode;

    private UIFilters filters;
    //endregion

    //region constructors
    public Operation() {
        super(Operation.class);
        exitQuestion = true;
    }
    //endregion

    //region init methods
    @Override
    protected void initLocalFromGlobal() {
        super.initLocalFromGlobal();
    }

    private void Start() {
        Init();
        Fill();
        if (operationData.isDocument()) {
            fillOperation();
        } else {
            showB1DataDialog();
        }
    }

    private void Init() {
        initLocalFromGlobal();
        InitViews();
        InitExtraFields();
        InitFields();
        //InitPrivileges();
        InitActionBarEditText();
    }

    private void InitViews() {
        linearLayoutUIInfo = (LinearLayout) findViewById(R.id.linearLayoutOperationTemplateDPOInfo);
        linearLayoutUIInfo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showB1DataDialog();
            }
        });

        linearLayoutTotals = (LinearLayout) findViewById(R.id.linearLayoutOperationTemplateTotals);

        listViewData = (ListView) findViewById(R.id.listViewOperationTemplateList);
        ListViewItemMove lvimi = new ListViewItemMove(listViewData);
        lvimi.addRemoveListner(new ListViewItemMove.OnEventListener() {
            @Override
            public void onSlideLeftEvent(int position) {
                removeItem(position);
            }

            @Override
            public void onSlideRightEvent(int position) {
                removeItem(position);
            }

            @Override
            public void onClickEvent(int position) {
                viewItem(position);
            }

            @Override
            public void onLongClickEvent(int position) {
                editItem(position);
            }
        });
    }

    private void InitExtraFields() {
        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
            operJSONPos = bundle.getInt(PUTTER_POS);
            operationJSON = GO.operations.get(operJSONPos);
            operationData = (OperationData) bundle.getSerializable(PUTTER_OPERATION_DATA);
        }
    }

    private void InitFields() {
        if (operationData != null) {
            operationData.setOperJSONPos(operJSONPos);
        } else {
            operationData = new OperationData(operJSONPos);
        }
        copdrfRows = new COPDRFRows();
        adapter = new COPDRFAdapter(this, copdrfRows, operationJSON.getColumns());
        listViewData.setAdapter(adapter);
        totalMode = std.getTotalMode(operationJSON);
    }

    private void InitActionBarEditText() {
        ActionBar.LayoutParams layout = new ActionBar.LayoutParams(
                ActionBar.LayoutParams.MATCH_PARENT,
                ActionBar.LayoutParams.WRAP_CONTENT);

        LayoutInflater mInflater = LayoutInflater.from(this);
        View view = mInflater.inflate(R.layout.actionbaredittext, null);
        editTextBarCode = (EditText) view.findViewById(R.id.editTextMenuItemActionBarSearch);
        editTextBarCode.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (event != null && event.getKeyCode() == KeyEvent.KEYCODE_ENTER) {
                    ClickButtonAdd();
                }
                return false;
            }
        });

        ActionBar actionBar = getActionBar();
        actionBar.setCustomView(view, layout);
        actionBar.setDisplayShowCustomEnabled(true);
    }

    private void InitPrivileges() {
        COPDRFColumns columns = operationJSON.getColumns();
        std.initPrivileges(columns, linearLayoutTotals);
    }

    private void Fill() {
        fillUIFilters();
    }

    private void fillUIFilters() {
        filters = new UIFilters();
        InsertionMode insertionMode = operationJSON.getInsertionMode();
        switch (insertionMode) {
            case Transfer:
                filters.add(new UIFilter(UIFilterType.StartDate, operationData.getDate()));
                filters.add(UIFilter.fromiIBase(UIFilterType.Object1, operationData.getObject1()));
                filters.add(UIFilter.fromiIBase(UIFilterType.Object2, operationData.getObject2()));
                break;
            case StockTacking:
                filters.add(UIFilter.fromiIBase(UIFilterType.Object, operationData.getObject1()));
                break;
            case Single:
            default:
                filters.add(new UIFilter(UIFilterType.StartDate, operationData.getDate()));
                filters.add(UIFilter.fromiIBase(UIFilterType.Partner, operationData.getPartner()));
                filters.add(UIFilter.fromiIBase(UIFilterType.Object, operationData.getObject1()));
                break;
        }
        filters.add(UIFilter.fromiIBase(UIFilterType.User, operationData.getUser()));
        filters.add(new UIFilter(UIFilterType.OperTypeAll, operationJSON.getOperType(), true));
        refreshHeadData();
    }

    private void ClickButtonSave() {
        if (operationData.isDataFilled()) {
            if (operationData.isRowsFilled()) {
                boolean isDocument = operationData.isDocument();
                PayMode payMode = operationJSON.getPayMode();
                switch (payMode) {
                    case Pay:
                        if (isDocument) {
                            SaveDocumentPayed();
                        } else {
                            SaveOperationPayed();
                        }
                        break;
                    case NoPay:
                        SaveOperationWithoutPay();
                        break;
                }
            } else {
                Toast.makeText(this, R.string.list_is_empty, Toast.LENGTH_LONG).show();
            }
        } else {
            showB1DataDialog();
            Toast.makeText(this, R.string.pls_fill_add_info, Toast.LENGTH_LONG).show();
        }
    }

    private void removeItem(int position) {
        if (position != -1) {
            operationData.removeOperItem(position);
            copdrfRows.remove(position);
            adapter.notifyDataSetChanged();
            RefreshTotals();
        }
    }

    private void viewItem(int position) { }

    private void editItem(int position) {
        if (position != -1) {
            OperItem operItem = operationData.getOperItemByIndex(position);
            editNEOperationItem(operItem, position);
        }
    }
    //endregion

    //region do methods
    private void showB1DataDialog() {
        if (operationJSON.getInsertionMode() != InsertionMode.StockTacking) {
            UIFiltersDialog dialog = new UIFiltersDialog(filters);
            dialog.setOnEventListener(new OnEventListener() {
                @Override
                public void onEvent(Serializable serializable) {
                    filters = (UIFilters) serializable;
                    operationData.fill(filters);
                    operationData.setDefaultObject();
                    refreshHeadData();
                }
            });
            dialog.show(this);
        }
    }

    private void ClickButtonAdd() {
        String text = editTextBarCode.getText().toString();
        if (!text.isEmpty()) {
            List<iItem> items = iSQL.getItemByCodeBarcode(text);
            if (items != null) {
                addNEOperationItem(items.get(0));
            } else {
                showItemsActivity();
            }
        } else {
            showItemsActivity();
        }
        editTextBarCode.getText().clear();
    }

    private void addNEOperationItem(iItem item) {
        NEOperation neo = new NEOperation(item,
                operationJSON.getColumns(),
                operationJSON.getTotalMode());
        neo.setPig(GO.getPig());
        neo.setPog(GO.getPog());
        neo.setOnEventListener(new OnEventListener() {
            @Override
            public void onEvent(Serializable serializable) {
                OperItem item = (OperItem) serializable;
                insertOperItem(item);
            }
        });
        neo.show(this);
    }

    private void editNEOperationItem(OperItem operItem, final int position) {
        NEOperation neo = new NEOperation(operItem,
                operationJSON.getColumns(),
                operationJSON.getTotalMode());
        neo.setOnEventListener(new OnEventListener() {
            @Override
            public void onEvent(Serializable serializable) {
                OperItem item = (OperItem) serializable;
                updateOperItem(item, position);
            }
        });
        neo.show(this);
    }

    private void insertOperItem(OperItem item) {
        operationData.addOperItem(item);
        copdrfRows.add(item.toCOPDRFRow());
        adapter.notifyDataSetChanged();
        RefreshTotals();
    }

    private void fillOperation() {
        refreshHeadData();
        for (OperItem item : operationData.getList()) {
            copdrfRows.add(item.toCOPDRFRow());
        }
        adapter.notifyDataSetChanged();
        RefreshTotals();
    }

    private void updateOperItem(OperItem item, final int position) {
        operationData.updateOperByIndex(item, position);
        copdrfRows.remove(position);
        copdrfRows.add(position, item.toCOPDRFRow());
        adapter.notifyDataSetChanged();
        RefreshTotals();
    }

    private void showItemsActivity() {
        Edit edit = new Edit();
        edit.setOnEventListener(new OnEventListener() {
            @Override
            public void onEvent(Serializable serializable) {
                if (serializable != null) {
                    iItems items = (iItems) serializable;
                    lastSelectedItems = items;
                    fillItems(items);
                }
            }
        });
        edit.putExtra(Edit.PUTTER_AUTO_OK, true);
        edit.putExtra(Edit.PUTTER_DEFAULT_ITEM, new iItem());
        edit.setSelector(lastSelectedItems);
        edit.show(this);
    }

    public void fillItems(iItems items) {
        //groups need add
        List<iIBase> list = items.items;
        if (list.size() > 1) {
            for (iIBase base : list) {
                iItem item = (iItem) base;
                double qtty = operationJSON.getQttyMode().equals(QttyMode.Equals) ? 0.0 : 1.0;
                insertOperItem(item.toOperItem(qtty));
            }
        } else {
            iItem item = (iItem) list.get(0);
            addNEOperationItem(item);
        }
    }

    private void RefreshTotals() {
        List<String> totals = operationData.getTotals();
        std.fillTotalsLinearLayout(this, linearLayoutTotals, totals, totalMode);
    }

    private void refreshHeadData() {
        std.fillUIFilters2LinearLayout(this, filters, linearLayoutUIInfo);
    }
    //endregion

    //region save methods
    private void SaveOperationPayed() {
        TotalMode totalMode = operationJSON.getTotalMode();
        PayInfo payInfo = operationData.getPayinfoWithTotalAndCredit(totalMode);
        NewFinance nf = new NewFinance(payInfo);
        nf.setOnEventListener(new OnEventListener() {
            @Override
            public void onEvent(Serializable serializable) {
                PayInfo payInfo = (PayInfo) serializable;
                SaveOperation(payInfo);
            }
        });
        nf.show(this);
    }

    private void SaveOperation(PayInfo payInfo) {
        operationData.setPayinfo(payInfo);
        boolean result = iSQL.SaveOperation(operationData);
        if (result) {
            Toast.makeText(this, R.string.operation_save_successfully, Toast.LENGTH_SHORT).show();
            if (operationData.isDocument()) {
                COPDRFRow row = getDocumentRow();
                doEvent(row);
                finish();
            } else {
                showB1DataDialog();
                clearData();
            }
        } else {
            Toast.makeText(this, R.string.operation_save_problem, Toast.LENGTH_LONG).show();
        }
    }

    private COPDRFRow getDocumentRow() {
        return COPDRFRow.forDocuments(
                operationData.getDateString(),
                operationData.getAcct(),
                operationData.getPartner().getCode(),
                operationData.getPartner().getName(),
                Double.toString(operationData.getQttySum()),
                Double.toString(operationData.getTotalIn()),
                Double.toString(operationData.getTotalOut()),
                Double.toString(operationData.getProfit())
        );
    }

    private void SaveOperationWithoutPay() {
        MessageDialog md = new MessageDialog();
        md.setOnEventListener(new OnEventListener() {
            @Override
            public void onEvent(Serializable serializable) {
                if (operationData.isDocument()) {
                    SaveEditedOperation(null);
                } else {
                    SaveOperation(null);
                }
            }
        });
        md.show(this, R.string.save_operation);
    }

    private void SaveDocumentPayed() {
        EditPaymentsData data = getEditPaymentsData();
        EditFinance edit = new EditFinance(data);
        edit.setOnEventListener(new OnEventListener() {
            @Override
            public void onEvent(Serializable serializable) {
                EditPaymentsData data = (EditPaymentsData) serializable;
                SaveEditedOperation(data);
            }
        });
        edit.show(this);
    }

    private EditPaymentsData getEditPaymentsData() {
        String type = Integer.toString(operationJSON.getId());
        String acct = operationData.getAcct();
        EditPaymentsData data = iSQL.getPayment(type, acct);
        Double total = operationData.getTotal();
        if (data.getList().get(0).getSum() != total) {
            data.getList().get(0).setSum(total);
            data.getList().get(0).setChanged(true);
        }
        return data;
    }

    private void SaveEditedOperation(EditPaymentsData data) {
        operationData.setEditedPayment(data);
        SaveOperation(null);
    }

    private void clearData() {
        linearLayoutUIInfo.removeAllViews();
        linearLayoutTotals.removeAllViews();
        operationData.clear();
        InitFields();
    }
    //endregion
}