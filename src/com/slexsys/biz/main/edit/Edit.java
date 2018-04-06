package com.slexsys.biz.main.edit;

import android.app.ActionBar;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.*;
import com.slexsys.biz.*;
import com.slexsys.biz.comp.LoginInfo;
import com.slexsys.biz.comp.std;
import com.slexsys.biz.database.comp.iBase;
import com.slexsys.biz.database.comp.iIBase;
import com.slexsys.biz.database.comp.iIBaseAdapter;
import com.slexsys.biz.database.comp.iItems;
import com.slexsys.biz.database.groups.iGroup;
import com.slexsys.biz.database.sqls.comp.QueryItemGroup;
import com.slexsys.biz.database.sqls.iSQL;
import com.slexsys.biz.main.comp.Events.OnEventListener;
import com.slexsys.biz.main.comp.NewTypes.DataTable;
import com.slexsys.biz.main.comp.NewTypes.ListViewItemMove;
import com.slexsys.biz.main.comp.NewTypes.SmartActivity;
import com.slexsys.biz.main.comp.NewTypes.SmartDialog;
import com.slexsys.biz.main.comp.dialogs.CollectionDialog;

import java.io.Serializable;
import java.util.LinkedList;
import java.util.List;

/**
 * Created by slexsys on 3/11/16.
 */
public class Edit extends SmartActivity {
    //region overrides methods
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.edit);
        Init();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.edit, menu);
        return true;
    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu){
        if (searchMode) {
            for (int i = 0; i < menu.size(); ++i) {
                menu.getItem(i).setVisible(false);
            }
            editTextSearch.setVisibility(View.VISIBLE);
            editTextSearch.requestFocus();
        } else {
            MenuItem item = menu.findItem(R.id.menuItemEditAutoOK);
            item.setChecked(autok);
            editTextSearch.setVisibility(View.GONE);
        }
        super.onPrepareOptionsMenu(menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        boolean result = true;
        switch (item.getItemId()) {
            case android.R.id.home:
                if (searchMode) {
                    invalidateSearch();
                } else {
                    result = super.onOptionsItemSelected(item);
                }
                break;
            case R.id.menuItemEditShowHideGroups:
                ClickButtonShowHideGroups();
                break;
            case R.id.menuItemEditAdd:
                ClickButtonNew();
                break;
            case R.id.menuItemEditSearch:
                initSearch();
                break;
            case R.id.menuItemEditSelect:
                ClickButtonOK();
                break;
            case R.id.menuItemEditAutoOK:
                autok = !item.isChecked();
                item.setChecked(autok);
                break;
            case R.id.menuItemEditFillItem:
                fillItem = !item.isChecked();
                item.setChecked(fillItem);
                break;
            default:
                result = super.onOptionsItemSelected(item);
                break;
        }
        return result;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        UpdateCurrentGroupItems();
    }

    @Override
    protected void initLocalFromGlobal() {
        super.initLocalFromGlobal();
    }
    //endregion

    //region finals
    public static final String PUTTER_AUTO_OK = "auto_ok";
    public static final String PUTTER_SELECTOR = "selector";
    public static final String PUTTER_DEFAULT_ITEM = "default_item";
    //endregion

    //region views
    private LinearLayout linearLayoutGroups;
    private ListView listGroups;
    private ListView listViewItems;
    private EditText editTextSearch;
    private Button btnUp;
    //endregion

    //region fields
    private boolean searchMode;
    private boolean fillItem;
    private boolean autok;
    //private iItems<? extends iIBase> objects;
    private QueryItemGroup itemsQuerys;
    private int lastgrouppos;
    private int lastitempos;
    private iIBase defaultItem;

    private List<iGroup> groups;
    private List<? extends iIBase> items;
    private iItems selector;
    //endregion

    //region constructors
    public Edit() {
        super(Edit.class);
    }
    //endregion

    //region inner methods
    private void Init() {
        initLocalFromGlobal();
        InitExtraMembers();
        InitFields();
        InitViews();
        InitButtons();
        InitGroups();
        InitActionBarEditText();
    }

    private void InitExtraMembers() {
        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
            autok = bundle.getBoolean(PUTTER_AUTO_OK, false);
            Serializable serializable = bundle.getSerializable(PUTTER_SELECTOR);
            if (serializable != null) {
                selector = (iItems) serializable;
            }
            defaultItem = (iIBase) bundle.getSerializable(PUTTER_DEFAULT_ITEM);
            itemsQuerys = defaultItem.getItemQuerys();
        }
    }

    private void InitFields() {
        lastgrouppos = 0;
        lastitempos = 0;
        selector = new iItems();
    }

    private void InitGroups() {
        ListGroupsClick(-1);
    }

    private void InitViews(){
        linearLayoutGroups = (LinearLayout) findViewById(R.id.linearLayoutEditGroups);
        listGroups = (ListView) findViewById(R.id.listViewGroups);
        ListViewItemMove lvimg = new ListViewItemMove(listGroups, true);
        lvimg.addRemoveListner(new ListViewItemMove.OnEventListener() {
            @Override
            public void onSlideLeftEvent(int position) {
                addremoveSelectedGroup(position);
            }

            @Override
            public void onSlideRightEvent(int position) {
                removeGroup(position);
            }

            @Override
            public void onClickEvent(int position) {
                ListGroupsClick(position);
            }

            @Override
            public void onLongClickEvent(int position) {
                ListGroupLongClick(position);
            }
        });

        listViewItems = (ListView) findViewById(R.id.listViewItems);
        ListViewItemMove lvimi = new ListViewItemMove(listViewItems, true);
        lvimi.addRemoveListner(new ListViewItemMove.OnEventListener() {
            @Override
            public void onSlideLeftEvent(int position) {
                addremoveSelectedItem(position);
            }

            @Override
            public void onSlideRightEvent(int position) {
                removeItem(position);
            }

            @Override
            public void onClickEvent(int position) {
                ListItemsClick(position);
            }

            @Override
            public void onLongClickEvent(int position) {
                ClickButtonEdit(position);
            }
        });
    }

    private void InitButtons(){
        btnUp = (Button) findViewById(R.id.buttonEditUp);
        btnUp.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                ClickButtonUp();
            }
        });
    }

    private void InitActionBarEditText() {
        ActionBar.LayoutParams layout = new ActionBar.LayoutParams(
                ActionBar.LayoutParams.MATCH_PARENT,
                ActionBar.LayoutParams.WRAP_CONTENT);

        LayoutInflater mInflater = LayoutInflater.from(this);
        View view = mInflater.inflate(R.layout.actionbaredittext, null);
        editTextSearch = (EditText) view.findViewById(R.id.editTextMenuItemActionBarSearch);
        editTextSearch.requestFocus();
        editTextSearch.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                    performSearch();
                    return true;
                }
                return false;
            }
        });
        editTextSearch.setVisibility(View.GONE);

        ActionBar actionBar = getActionBar();
        actionBar.setCustomView(view, layout);
        actionBar.setDisplayShowCustomEnabled(true);
    }

    private void initSearch() {
        searchMode = true;
        invalidateOptionsMenu();
        ClickButtonShowHideGroups(false);
        listViewItems.setAdapter(null);
    }

    private void invalidateSearch() {
        searchMode = false;
        invalidateOptionsMenu();
        ClickButtonShowHideGroups(true);
        ListGroupsClick(lastgrouppos);
        std.hideKeyboard(this, editTextSearch);
    }

    public void setSelector(iItems bases) {
        this.selector = bases;
    }

    private void ClickButtonShowHideGroups(boolean... values) {
        if (values.length > 0) {
            if (values[0]) {
                linearLayoutGroups.setVisibility(View.VISIBLE);
            } else {
                linearLayoutGroups.setVisibility(View.GONE);
            }
        } else {
            int visible = linearLayoutGroups.getVisibility();
            if (visible == View.GONE) {
                linearLayoutGroups.setVisibility(View.VISIBLE);
            } else {
                linearLayoutGroups.setVisibility(View.GONE);
            }
        }
    }

    private void ClickButtonNew() {
        NEDButtonClick(groups.get(lastgrouppos).getId(), -1);
    }

    private void ClickButtonEdit(int pos) {
        lastitempos = pos;
        NEDButtonClick(groups.get(lastgrouppos).getId(), pos);
    }

    private void addremoveSelectedGroup(int position) {
        iGroup group = groups.get(position);
        iGroup result = selector.getGroupByID(group.getId());
        if (result != null) {
            selector.groups.remove(result);
        } else {
            selector.groups.add(group);
        }
    }

    private void addremoveSelectedItem(int position) {
        iIBase item = items.get(position);
        iIBase result = selector.getItemByID(item.getId());
        if (result != null) {
            selector.items.remove(result);
        } else {
            selector.items.add(item);
        }
    }

    private void removeGroup(int position) {
        boolean done = itemRemover(groups, position);
        if (done) {
            removeGroupSelection(position);
            listGroups.removeViewAt(position);
        }
    }

    private void removeItem(int position) {
        boolean done = itemRemover(items, position);
        if (done) {
            removeItemSelection(position);
            listViewItems.removeViewAt(position);
        }
    }

    private void removeGroupSelection(int position) {
        iGroup group = groups.get(position);
        selector.groups.remove(group);
    }

    private void removeItemSelection(int position) {
        iIBase item = items.get(position);
        selector.items.remove(item);
    }

    private boolean itemRemover(List<? extends iBase> list, int pos) {
        boolean result = false;
        iBase item = list.get(pos);
        if (item.canDelete()) {
            boolean done = iSQL.update(item.getDeleteQuery());
            if (done) {
                list.remove(item);
                result = true;
            } else {
                Toast.makeText(this, R.string.not_deleted, Toast.LENGTH_SHORT).show();
            }
        } else {
            Toast.makeText(this, R.string.cant_remove, Toast.LENGTH_SHORT).show();
        }
        return result;
    }

    private void NEDButtonClick(int gid, int pos){
        SmartActivity smartActivity;
        if (pos == -1){
            smartActivity = defaultItem.newItem(this, gid);
        } else {
            smartActivity = items.get(pos).editItem(this, gid);
        }
        smartActivity.setOnEventListener(new OnEventListener() {
            @Override
            public void onEvent(Serializable serializable) {
                iIBase item = (iIBase) serializable;
                if (item != null) {
                    UpdateCurrentGroupItems();
                }
            }
        });
    }

    private void FindItemsByText(String text) {
        items = defaultItem.getLikeList(text);
        FillItems();
    }

    private void ClickButtonOK() {
        doEvent(selector);
        this.finish();
    }

    private void ListGroupsClick(int pos) {
        int gid = 0;
        String code = "";
        if(pos != -1) {
            lastgrouppos = pos;
            //lastgroup = groups.get(pos);
            gid = groups.get(pos).getId();
            code = groups.get(pos).getCode();
        }
        String query = itemsQuerys.Group.SelectDownGroups.replace("#code", code);
        Log.e("Query", query);
        List<iGroup> list = iGroup.getGroupList(query);//objects.getIGDownList(gid);
        if (list.size() > 0) {
            UpButtonSH(list.get(0));
            groups = list;
            FillGroups();
        } else {
            FillItems(gid);
        }
    }

    private void ListGroupLongClick(int pos) {
        if(pos != -1) {
            lastgrouppos = pos;
            final iGroup group = groups.get(pos);
            CollectionDialog dialog = new CollectionDialog();
            dialog.setOnEventListener(new OnEventListener() {
                @Override
                public void onEvent(Serializable serializable) {
                    int pos = (Integer) serializable;
                    doOperation(group, pos);
                }
            });
            dialog.showSingleChoice(this, R.string.operations, getMenuItems());
        }
    }

    private void doOperation(iGroup group, int pos) {
        switch (pos) {  // avelacnel nayev move_hear ev move_to
            case 0:     //add group
                addGroup(group);
                break;
            case 1:     //add subgroup
                addSubGroup(group);
                break;
            case 2:     //edit
                editGroup(group);
                break;
            case 3:     //delete
                removeGroup(groups.indexOf(group));
                break;
        }
    }

    private void addGroup(iGroup group) {

    }

    private void addSubGroup(iGroup group) {

    }

    private void editGroup(iGroup group) {

    }

    private List<String> getMenuItems() {
        return new LinkedList<String>() {{
            add(Edit.this.getString(R.string.add_group));
            add(Edit.this.getString(R.string.add_sub_group));
            add(Edit.this.getString(R.string.edit));
            add(Edit.this.getString(R.string.delete));
        }};
    }

    private void ClickButtonUp() {
        String code = groups.get(0).getCode();
        String query = itemsQuerys.Group.SelectUpGroups.replace("#code", code);
        List<iGroup> list = iGroup.getGroupList(query);//objects.getIGUpList(gid);

        if(list.size() > 0) {
            UpButtonSH(list.get(0));
            groups = list;
            FillGroups();
        }
    }

    private void UpButtonSH(iGroup group){
        if(group.getCode().length() != 3) {
            btnUp.setVisibility(View.VISIBLE);
        } else {
            btnUp.setVisibility(View.GONE);
        }
    }

    private void ListItemsClick(int pos) {
        lastitempos = pos;
        addLastSelectedItem(items.get(pos));
        if (autok) {
            ClickButtonOK();
        }
    }

    private void addLastSelectedItem(iIBase item) {
        if (selector.items.isEmpty() && selector.groups.isEmpty()) {
            selector.items.add(item);
        }
    }

    private void UpdateCurrentGroupItems(){
        FillItems(groups.get(lastgrouppos).getId());
    }

    private void FillGroups(){
        listGroups.setAdapter(new iIBaseAdapter(this, groups));
        int gid = groups.get(0).getId();
        FillItems(gid);
        lastgrouppos = 0;
    }

    private void FillItems(int gid){
        lastitempos = 0;
        FilliItems(gid);
    }

    private void FilliItems(int gid) {
        //items = objects.getIListByGID(gid); offline work
        String oid = Integer.toString(LoginInfo.object.getId()); //default object
        String sgid = Integer.toString(gid);
        String query = itemsQuerys.Item.SelectByGID
                                .replace("#gid", sgid)
                                .replace("#oid", oid); //if item, for qtty
        DataTable dataTable = iSQL.getDataTable(query);
        items = getItemList(dataTable);
        FillItems();
    }

    private void FillItems() {
        if (items != null) {
            listViewItems.setAdapter(new iIBaseAdapter(this, items));
        }
    }

    private void performSearch() {
        String text = editTextSearch.getText().toString().trim();
        if(!text.isEmpty()){
            FindItemsByText(text);
            std.hideKeyboard(this, editTextSearch);
        }
    }

    private List<iIBase> getItemList(DataTable dataTable) {
        List<iIBase> result = new LinkedList<iIBase>();
        for (int i = 0; i < dataTable.size(); ++i) {
            result.add(defaultItem.createObject(dataTable.getRowWithNames(i)));
        }
        return result;
    }
    //endregion
}
