package com.slexsys.biz.main.report.json;

import java.io.Serializable;
import java.util.LinkedList;
import java.util.List;

/**
 * Created by slexsys on 2/27/17.
 */

public class ReportsJSON extends LinkedList<ReportJSON> implements Serializable{
    public List<String> getNames() {
        List<String> result = new LinkedList<String>();
        for (ReportJSON item : this) {
            result.add(item.getName());
        }
        return result;
    }
}
