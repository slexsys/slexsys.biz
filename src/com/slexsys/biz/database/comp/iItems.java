package com.slexsys.biz.database.comp;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;

import com.slexsys.biz.database.groups.iGroup;
import com.slexsys.biz.database.groups.iGroups;
import com.slexsys.biz.database.items.iItem;
import com.slexsys.biz.database.items.iPartner;
import com.slexsys.biz.database.sqls.iSQL;
import com.slexsys.biz.main.comp.NewTypes.DataTable;
import com.slexsys.biz.main.edit.Edit;

import java.io.Serializable;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

/**
 * Created by slexsys on 3/25/16.
 */
public class iItems<T extends iIBase> implements Serializable {
    public List<T> items = new LinkedList<T>();
    public iGroups groups = new iGroups();
    private T defaultObject;

    public iItems() { }

    public iItems(T defaultObject){
        this.defaultObject = defaultObject;
    }

    public void Fill(){
        fillGroups();
        fillItems();
    }

    private void fillGroups() {
        String query = defaultObject.getItemQuerys().Group.Select;
        if (query != null && query != "") {
            DataTable dataTable = iSQL.getDataTable(query);
            for (int i = 0; i < dataTable.size(); ++i) {
                groups.add(new iGroup(dataTable.getRowWithNames(i)));
            }
        }
    }

    private void fillItems() {
        String query = defaultObject.getItemQuerys().Item.Select;
        if (query != null && query != "") {
            DataTable dataTable = iSQL.getDataTable(query);
            for (int i = 0; i < dataTable.size(); ++i) {
                items.add((T) defaultObject.createObject(dataTable.getRowWithNames(i)));
            }
        }
    }

    public List<T> getIListByGID(int gid){
        List<T> result = new LinkedList<T>();
        for (T i : items){
            if(i.groupid == gid){
                result.add(i);
            }
        }
        return result;
    }

    public List<iGroup> getIGUpList(int gid){
        return groups.getUpGroupsByID(gid);
    }

    public List<iGroup> getIGDownList(int gid){
        return groups.getSubGroupsByID(gid);
    }

    public String getNextMaxCode() {
        int max = 0;
        for (T item : items) {
            int code = Convert.ToInteger(item.code);
            if(code > max){
                max = code;
            }
        }
        return Convert.ToString(max + 1);
    }

    public List<String> getNames(){
        List<String> result = new LinkedList<String>();
        for (T m : items){
            result.add(m.name);
        }
        return result;
    }

    public Map<String, String> getMap(){
        Map<String, String> map = new LinkedHashMap<String, String>();
        for (T m : items){
            map.put(Integer.toString(m.id), m.name);
        }
        return map;
    }

    public void Add(T item) {
        AddToDB(item);
        items.add(item);
    }

    public void Edit(T item) {
        EditInDB(item);
        int pos = items.indexOf(item);
        items.remove(pos);
        items.add(pos, item);
    }

    public void Delete(iIBase item) {
        DeleteFromDB(item);
        items.remove(item);
    }

    private void AddToDB(T item) {
        iSQL.update(item.getAddQuery());
    }

    private void EditInDB(T item) {
        iSQL.update(item.getEditQuery());
    }

    private void DeleteFromDB(iIBase item) {
        iSQL.update(item.getDeleteQuery());
    }

    public List<T> GetItemsLike(String text) {
        List<T> result = new LinkedList<T>();
        for (T item : items){
            if(item.code.contains(text) ||
                    item.name.contains(text)) {
                result.add(item);
            } else if(IsEternItem(item, text)) {
                result.add(item);
            }
        }
        return result;
    }

    private boolean IsEternItem(T item, String text) {
        boolean result = false;
        if (item instanceof iPartner) {
            result = IsLikePartner(item, text);
        } else if (item instanceof iItem) {
            result = IsLikeItem(item, text);
        }
        return result;
    }

    private boolean IsLikePartner(T item, String text) {
        boolean result = false;
        iPartner pitem = (iPartner) item;
        if(pitem.getMol().contains(text) ||
                pitem.getCity().contains(text) ||
                pitem.getAddress().contains(text) ||
                pitem.getPhone().contains(text) ||
                pitem.getFax().contains(text) ||
                pitem.getEmail().contains(text) ||
                pitem.getTaxno().contains(text) ||
                pitem.getVatno().contains(text) ||
                pitem.getCardnumber().contains(text))
            result = true;
        return result;
    }

    private boolean IsLikeItem(T item, String text) {
        boolean result = false;
        iItem iitem = (iItem) item;
        if(iitem.getBarcode().contains(text) ||
                iitem.getBarcode2().contains(text) ||
                iitem.getCatalog().contains(text) ||
                Convert.ToString(iitem.getPlu()).contains(text) ||
                IsPriceContain(iitem, text) ||
                iitem.getDescription().contains(text))
            result = true;
        return result;
    }

    private boolean IsPriceContain(iItem item, String text) {
        boolean result = false;
        try {
            double value = Double.parseDouble(text);
            if(item.getPricein().contains(value)){
                result = true;
            } else if(item.getPriceout().contains(value)){
                result = true;
            }
        } catch (Exception ex){}
        return result;
    }

    public T getItemByID(int id) {
        T result = null;
        for (T t : items) {
            if (t.id == id) {
                result = t;
                break;
            }
        }
        return result;
    }

    public iGroup getGroupByID(int id) {
        iGroup result = null;
        if (groups.items.containsKey(id)) {
            result = groups.items.get(id);
        }
        return result;
    }

    public void setDefaultObject(T defaultObject) {
        this.defaultObject = defaultObject;
    }

    public void showActivity(Activity activity) {
        showActivity(activity, false);
    }

    public void showActivity(Activity activity, boolean autoOK) {
        Intent intent = new Intent(activity, Edit.class);
        intent.putExtra(Edit.PUTTER_AUTO_OK, autoOK);
        intent.putExtra(Edit.PUTTER_DEFAULT_ITEM, defaultObject);
        activity.startActivityForResult(intent, defaultObject.getClass().hashCode());
    }
}
