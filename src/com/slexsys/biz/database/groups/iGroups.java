package com.slexsys.biz.database.groups;

import com.slexsys.biz.database.comp.iMap;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

/**
 * Created by slexsys on 3/11/16.
 */
public class iGroups extends iMap<iGroup> implements Serializable{
    public void add(iGroup item){
        super.add(item.getId(), item);
    }

    public void edit(iGroup item){
        super.edit(item.getId(), item);
    }

    public int size() {
        return super.size();
    }

    public boolean isEmpty() {
        return super.isEmpty();
    }

    public List<iGroup> getItems() {
        return new ArrayList<iGroup>(super.getList());
    }

    public List<iGroup> getSubGroupsByID(int gid){
        List<iGroup> result = new LinkedList<iGroup>();
        if(gid != 0) {
            String code = items.get(gid).getCode();
            int len = items.get(gid).getCode().length() + 3;
            for (iGroup g : items.values()) {
                if (g.getCode().startsWith(code) && g.getCode().length() == len && g.getId() != gid) {
                    result.add(g);
                }
            }
        } else {
            for (iGroup g : items.values()) {
                if (g.getCode().length() == 3) {
                    result.add(g);
                }
            }
        }
        if (result.size() > 0)
            Collections.sort(result);
        return result;
    }

    public List<iGroup> getUpGroupsByID(int gid){
        List<iGroup> result = new LinkedList<iGroup>();
        String code = items.get(gid).getCode();
        if(code.length() > 6){
            code = code.substring(0, code.length() - 6);
            for (iGroup g : items.values()){
                if (g.getCode().equals(code)){
                    result = getSubGroupsByID(g.getId());
                    break;
                }
            }
        } else if(code.length() == 6){
            result = getSubGroupsByID(0);
        }
        if (result.size() > 0)
            Collections.sort(result);
        return result;
    }
}

