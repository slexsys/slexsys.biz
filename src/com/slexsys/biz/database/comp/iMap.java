package com.slexsys.biz.database.comp;

import java.io.Serializable;
import java.util.Collection;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * Created by slexsys on 3/11/16.
 */
public class iMap<T> implements Serializable{
    protected Map<Integer, T> items;

    public iMap(){
        items = new LinkedHashMap<Integer, T>();
    }

    public void add(int id, T item){
        items.put(id, item);
    }

    public void edit(int id, T item){
        items.remove(item);
        items.put(id, item);
    }

    public void remove(T item){
        items.remove(item);
    }

    public int size() {
        return items.size();
    }

    public boolean isEmpty() {
        return items.size() > 0 ? false : true;
    }

    public T get(int gid){
        return items.get(gid);
    }

    public Collection<T> getList(){
        return items.values();
    }
}
