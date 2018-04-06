package com.slexsys.biz.main.operations.json;

import java.io.Serializable;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

/**
 * Created by slexsys on 2/27/17.
 */

public class OperationsJSON extends LinkedList<OperationJSON> implements Serializable {
    //region public methods
    public List<String> getNames() {
        List<String> result = new LinkedList<String>();
        for (OperationJSON item : this) {
            result.add(item.getName());
        }
        return result;
    }

    public Map<String, String> getMap(PayMode payMode) {
        Map<String, String> result = new HashMap<String, String>();
        for (OperationJSON item : this) {
            if (item.getPayMode().equals(payMode)) {
                result.put(Integer.toString(item.getId()), item.getName());
            } else if (payMode == null) {
                result.put(Integer.toString(item.getId()), item.getName());
            }
        }
        return result;
    }

    public int getSignPayById(int id) {
        int result = 0;
        OperationJSON item = getItemById(id);
        if (item.getPayMode() == PayMode.Pay) {
            switch (item.getQttyMode()) {
                case Equals:
                case Minus:
                    result = 1;
                    break;
                case Plus:
                    result = -1;
                    break;
            }
        }
        return result;
    }

    private OperationJSON getItemById(int id) {
        OperationJSON result = null;
        for (OperationJSON item : this) {
            if (item.getId() == id) {
                result = item;
                break;
            }
        }
        return result;
    }
    //endregion
}
