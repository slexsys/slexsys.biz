package com.slexsys.biz.main.menu;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ExpandableListView;
import android.widget.SimpleExpandableListAdapter;

import com.slexsys.biz.GO;
import com.slexsys.biz.R;
import com.slexsys.biz.database.comp.iIBase;
import com.slexsys.biz.database.items.iItem;
import com.slexsys.biz.database.items.iObject;
import com.slexsys.biz.database.items.iPartner;
import com.slexsys.biz.database.items.iUser;
import com.slexsys.biz.main.comp.NewTypes.SmartActivity;
import com.slexsys.biz.main.document.Document;
import com.slexsys.biz.main.edit.Edit;
import com.slexsys.biz.main.finance.Cashbook;
import com.slexsys.biz.main.finance.Payments;
import com.slexsys.biz.main.operations.Operation;
import com.slexsys.biz.main.option.Option;
import com.slexsys.biz.main.report.Report;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

public class MainMenu extends SmartActivity {
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.mainmenu);
        Init();
    }

    //region constants
    private static final String GROUP_NAME = "groupName";
    private static final String SUB_GROUP_NAME = "subGroupName";
    //endregion

    //region views
    private ExpandableListView listView;
    private SimpleExpandableListAdapter adapter;
    //endregion

    //region fields
    ArrayList<Map<String, String>> groupData;
    ArrayList<ArrayList<Map<String, String>>> childData;
    //endregion

    //region init methods
    private void Init() {
        InitViews();
        InitFields();
        fillFields();
        fillViews();
    }

    private void InitViews() {
        listView = (ExpandableListView) findViewById(R.id.expandableListViewMainMenu);
        listView.setOnChildClickListener(new ExpandableListView.OnChildClickListener() {
            @Override
            public boolean onChildClick(ExpandableListView parent, View v,
                                        int groupPosition, int childPosition, long id) {
                clickSubMenu(groupPosition, childPosition);
                return false;
            }
        });
    }

    private void InitFields() {
        groupData = new ArrayList<Map<String, String>>();
        childData = new ArrayList<ArrayList<Map<String, String>>>();
    }
    //endregion

    //region fill methods
    private void fillFields() {
        List<String> items = new LinkedList<String>(){{
            add(getRString(R.string.operation));
            add(getRString(R.string.edit));
            add(getRString(R.string.finances));
            add(getRString(R.string.documents));
            add(getRString(R.string.reports));
            add(getRString(R.string.other));
        }};
        groupData = getChildItem(items, GROUP_NAME);

        //Operations
        List<String> subItems = GO.operations.getNames();
        childData.add(getChildItem(subItems, SUB_GROUP_NAME));

        //Edit
        items = new LinkedList<String>(){{
            add(getRString(R.string.partners));
            add(getRString(R.string.items));
            add(getRString(R.string.objects));
            add(getRString(R.string.users));
        }};
        childData.add(getChildItem(items, SUB_GROUP_NAME));

        //Finance
        items = new LinkedList<String>(){{
            add(getRString(R.string.payments));
            add(getRString(R.string.cashbook));
        }};
        childData.add(getChildItem(items, SUB_GROUP_NAME));

        //Documents
        childData.add(getChildItem(subItems, SUB_GROUP_NAME));

        subItems = GO.reports.getNames();
        //Report
        childData.add(getChildItem(subItems, SUB_GROUP_NAME));

        //Options
        items = new LinkedList<String>(){{
            add(getRString(R.string.options));
        }};
        childData.add(getChildItem(items, SUB_GROUP_NAME));
    }

    private void fillViews() {
        String groupFrom[] = new String[] {GROUP_NAME};
        int groupTo[] = new int[] {android.R.id.text1};

        String childFrom[] = new String[] {SUB_GROUP_NAME};
        int childTo[] = new int[] {android.R.id.text1};

        adapter = new SimpleExpandableListAdapter(this,
                groupData, android.R.layout.simple_expandable_list_item_1,
                groupFrom, groupTo,
                childData, R.layout.my_list_item_1,
                childFrom, childTo);
        listView.setAdapter(adapter);
    }

    private String getRString(int id) {
        return getResources().getString(id);
    }

    private ArrayList<Map<String, String>> getChildItem(List<String> groups, String groupName) {
        ArrayList<Map<String, String>> result = new ArrayList<Map<String, String>>();
        for (String group : groups) {
            Map<String, String> m = new HashMap<String, String>();
            m.put(groupName, group);
            result.add(m);
        }
        return result;
    }
    //endregion

    //region private methods
    private void clickSubMenu(int group, int child) {
        switch (group) {
            case 0:
                Operations(child);
                break;
            case 1:
                Edit(child);
                break;
            case 2:
                Finances(child);
                break;
            case 3:
                Documents(child);
                break;
            case 4:
                Reports(child);
                break;
            case 5:
                Others(child);
                break;
        }
    }

    private void Operations(int pos) {
        Intent intent = new Intent(this, Operation.class);
        intent.putExtra(Operation.PUTTER_POS, pos);
        startActivity(intent);
    }

    private void Edit(int pos) {
        iIBase item = getItemByPos(pos);
        Intent intent = new Intent(this, Edit.class);
        intent.putExtra(Edit.PUTTER_DEFAULT_ITEM, item);
        startActivity(intent);
    }

    private iIBase getItemByPos(int pos) {
        iIBase result;
        switch (pos) {
            case 0:
                result = new iPartner();
                break;
            case 1:
                result = new iItem();
                break;
            case 2:
                result = new iObject();
                break;
            case 3:
                result = new iUser();
                break;
            default:
                result = null;
                break;
        }
        return result;
    }

    private void Finances(int pos) {
        switch (pos){
            case 0:
                startActivity(new Intent(this, Payments.class));
                break;
            case 1:
                startActivity(new Intent(this, Cashbook.class));
                break;
        }
    }

    private void Documents(int pos) {
        Intent intent = new Intent(this, Document.class);
        intent.putExtra(Document.PUTTER_POS, pos);
        startActivity(intent);
    }

    private void Reports(int pos) {
        Intent intent = new Intent(this, Report.class);
        intent.putExtra(Report.PUTTER, pos);
        startActivity(intent);
    }

    private void Others(int pos) {
        startActivity(new Intent(this, Option.class));
    }
    //endregion
}