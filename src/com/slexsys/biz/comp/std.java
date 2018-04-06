package com.slexsys.biz.comp;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;

import com.slexsys.biz.R;
import com.slexsys.biz.database.comp.Convert;
import com.slexsys.biz.database.comp.iIBase;
import com.slexsys.biz.main.comp.COPDRF.COPDRFFilters;
import com.slexsys.biz.main.comp.COPDRF.COPDRFRow;
import com.slexsys.biz.main.comp.COPDRF.COPDRFRows;
import com.slexsys.biz.main.comp.NewTypes.DateTime;
import com.slexsys.biz.main.comp.enums.PayType;
import com.slexsys.biz.database.comp.iBase;
import com.slexsys.biz.database.groups.iGroup;
import com.slexsys.biz.database.groups.iGroups;
import com.slexsys.biz.main.comp.COPDRF.COPDRFColumn;
import com.slexsys.biz.main.comp.COPDRF.COPDRFColumnAccessMode;
import com.slexsys.biz.main.comp.COPDRF.COPDRFColumnName;
import com.slexsys.biz.main.comp.COPDRF.COPDRFColumns;
import com.slexsys.biz.main.operations.json.InsertionMode;
import com.slexsys.biz.main.operations.json.OperationJSON;
import com.slexsys.biz.main.operations.json.TotalMode;
import com.slexsys.biz.main.report.comp.UIFilter;
import com.slexsys.biz.main.report.comp.UIFilters;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.Arrays;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

/**
 * Created by slexsys on 4/8/16.
 */
public class std {

    public static void InitSpinner(Context context, Spinner spinner, Object object) {
        InitSpinner(context, spinner, object, false);
    }

    public static void InitSpinner(Context context, Spinner spinner, Object object, boolean addall) {
        List<String> list = getListFromCollection(context, object, addall);
        spinner.setAdapter(new ArrayAdapter<String>(context, android.R.layout.simple_spinner_dropdown_item, list));
    }

    public static List<String> getListFromCollection(Context context, Object object, boolean addall) {
        List<String> list = new LinkedList<String>();
        if (object instanceof List<?>) {
            list = (List<String>) object;
        } else if (object.getClass().isArray()) {
            list = Arrays.asList((String[]) object);
        } else if (object.equals(PayType.class)) {
            list = PayType.names();
        }
        if (addall) {
            list.add(0, context.getString(R.string.all));
        }
        return list;
    }

    public static List<String> getIDs(List<? extends iBase> items) {
        List<String> list = new LinkedList<String>();
        for (iBase item : items) {
            list.add(Integer.toString(item.getId()));
        }
        return list;
    }

    public static List<String> getCodes(iGroups groups) {
        List<String> list = new LinkedList<String>();
        for (iGroup group : groups.getList()) {
            list.add(group.getCode());
        }
        return list;
    }

    public static void initPrivileges(COPDRFColumns columns, View parent) {
        if (columns != null) {
            for (COPDRFColumn column : columns) {
                String name = column.getColumnName().name();
                View view = parent.findViewWithTag(name);
                if (view != null) {
                    COPDRFColumnAccessMode access = column.getAccessMode();
                    std.setAccessMode4Childs(view, access);
                }
            }
        } else {
            std.setInnerViewsVisible(parent);
        }
    }

    public static void fillTotalsLinearLayout(Context context, LinearLayout linearLayout,
                                              List<String> totals, TotalMode totalMode) {
        Map<Integer, String> map = std.getTotals(totals, totalMode);
        linearLayout.removeAllViews();
        fillLinearLayout(context, map, linearLayout);
    }

    public static void fillLinearLayout(Context context, Map<Integer, String> map, LinearLayout linearLayout) {
        if (map != null) {
            linearLayout.removeAllViews();
            for(Map.Entry<Integer, String> entry : map.entrySet()) {
                Integer key = entry.getKey();
                String value = entry.getValue();
                View view = getLinearLayoutItem(context, key, value);
                linearLayout.addView(view);
            }
        }
    }

    private static View getLinearLayoutItem(Context context, int resid, String text) {
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view = inflater.inflate(R.layout.copdrffilteritem, null);

        TextView textViewKey = (TextView) view.findViewById(R.id.textViewCOPDRFFilterItemKey);
        TextView textViewValue = (TextView) view.findViewById(R.id.textViewCOPDRFFilterItemValue);

        textViewKey.setText(resid);
        textViewValue.setText(text);

        return view;
    }

    public static Map<Integer, String> getTotals(final List<String> totals, TotalMode totalMode) {
        Map<Integer, String> map;
        switch (totalMode) {
            case TotalIn:
                map = new HashMap<Integer, String>() {{
                        put(R.string.total_in, totals.get(0));
                }};
                break;
            case TotalOut:
                map = new HashMap<Integer, String>() {{
                    put(R.string.total_out, totals.get(1));
                }};
                break;
            case TotalInOut:
                map = new HashMap<Integer, String>() {{
                    put(R.string.total_in, totals.get(0));
                    put(R.string.total_out, totals.get(1));
                    put(R.string.profit, totals.get(2));
                }};
                break;
            default:
                map = null;
                break;
        }
        return map;
    }

    public static TotalMode getTotalMode(OperationJSON operationJSON) {
        TotalMode totalMode;
        if (std.isFullTotal(operationJSON.getColumns())) {
            totalMode = TotalMode.TotalInOut;
        } else {
            totalMode = operationJSON.getTotalMode();
        }
        return totalMode;
    }

    public static boolean isFullTotal(COPDRFColumns columns) {
        boolean isTotalIn = false;
        boolean isTotalOut = false;
        if (columns != null) {
            for (COPDRFColumn column : columns) {
                if (column.getColumnName() == COPDRFColumnName.AmountIn) {
                    isTotalIn = true;
                } else if (column.getColumnName() == COPDRFColumnName.AmountOut) {
                    isTotalOut = true;
                }
            }
        }
        return isTotalIn && isTotalOut;
    }

    public static void setAccessMode4View(View view, COPDRFColumnAccessMode access) {
        switch (access) {
            case Hide:
                setViewVisibility(view, View.GONE);
                break;
            case ReadOnly:
            case ReadWrite:
                setViewVisibility(view, View.VISIBLE);
                break;
        }
    }

    public static void setAccessMode4Childs(View view, COPDRFColumnAccessMode access) {
        switch (access) {
            case Hide:
                setViewVisibility(view, View.GONE);
                break;
            case ReadOnly:
                setViewVisibility(view, View.VISIBLE);
                setInnerViewReadOnly(view);
                break;
            case ReadWrite:
                setViewVisibility(view, View.VISIBLE);
                break;
        }
    }

    public static void setViewVisibility(View view, int mode) {
        view.setVisibility(mode);
    }

    public static void setInnerViewReadOnly(View view) {
        if (view != null) {
            ViewGroup viewGroup = (ViewGroup) view;
            for (int i = 0; i < viewGroup.getChildCount(); ++i) {
                View child = viewGroup.getChildAt(i);
                setViewReadOnly(child);
            }
        }
    }

    public static void setViewReadOnly(View view) {
        if (view != null) {
            view.setEnabled(false);
            view.setClickable(false);
        }
    }

    public static void setInnerViewsVisible(View view) {
        if (view != null) {
            ViewGroup viewGroup = (ViewGroup) view;
            for (int i = 0; i < viewGroup.getChildCount(); ++i) {
                View child = viewGroup.getChildAt(i);
                setViewVisibility(child, View.VISIBLE);
            }
        }
    }

    public static void fillDPOInfo2LinearLayout(Context context, COPDRFFilters filters, InsertionMode mode, LinearLayout linearLayout) {
        if (filters != null) {
            Map<Integer, String> map;
            switch (mode) {
                case Transfer:
                    map = getTransferArray(filters);
                    break;
                case StockTacking:
                    map = getStockTackingArray(filters);
                    break;
                default:
                    map = getDefaultArray(filters);
                    break;
            }
            linearLayout.removeAllViews();
            fillLinearLayout(context, map, linearLayout);
        }
    }

    private static Map<Integer, String> getTransferArray(final COPDRFFilters filters) {
        return new HashMap<Integer, String>() {{
            put(R.string.date, getDateString(filters.getDateTime()));
            put(R.string.from_object, getiIBaseName(filters.getObject1()));
            put(R.string.to_object, getiIBaseName(filters.getObject2()));
            put(R.string.user, getiIBaseName(filters.getUser()));
        }};
    }

    private static Map<Integer, String> getStockTackingArray(final COPDRFFilters filters) {
        return new HashMap<Integer, String>() {{
            put(R.string.object, getiIBaseName(filters.getObject1()));
            put(R.string.user, getiIBaseName(filters.getUser()));
        }};
    }

    private static Map<Integer, String> getDefaultArray(final COPDRFFilters filters) {
        return new HashMap<Integer, String>() {{
            put(R.string.date, getDateString(filters.getDateTime()));
            put(R.string.partner, getiIBaseName(filters.getPartner()));
            put(R.string.object, getiIBaseName(filters.getObject1()));
            put(R.string.user, getiIBaseName(filters.getUser()));
        }};
    }

    private static String getDateString(DateTime dateTime) {
        String result = "";
        if (dateTime != null) {
            result = dateTime.toNormalDateFormat();
        }
        return result;
    }

    private static String getiIBaseName(iIBase item) {
        String result = "";
        if (item != null) {
            result = item.getName();
        }
        return result;
    }

    public static void fillUIFilters2LinearLayout(Context context, UIFilters filters, LinearLayout linearLayoutUIInfo) {
        Map<Integer, String> map = new LinkedHashMap<Integer, String>();
        for (UIFilter filter : filters) {
            if (filter.getObject() != null) {
                map.put(filter.getResid(), filter.getShowValue());
            }
        }
        fillLinearLayout(context, map, linearLayoutUIInfo);
    }

    public static List<String> getTotalsFromCOPDRF(COPDRFRows copdrfRows, int inPos, int outPos) {
        List<String> result = new LinkedList<String>();
        double totalIn = getTotalByIndex(copdrfRows, inPos);
        double totalOut = getTotalByIndex(copdrfRows, outPos);
        double profit = totalOut - totalIn;
        result.add(Convert.ToString(totalIn));
        result.add(Convert.ToString(totalOut));
        result.add(Convert.ToString(profit));
        return result;
    }

    private static double getTotalByIndex(COPDRFRows copdrfRows, int index) {
        double result = 0;
        for (COPDRFRow row : copdrfRows) {
            String string = (String) row.get(index).getValue();
            if (string != null && !string.equals("")) {
                double value = Double.parseDouble(string);
                result += value;
            }
        }
        return result;
    }

    public static void showKeyboard(Activity activity){
        InputMethodManager imm = (InputMethodManager) activity.getSystemService(activity.INPUT_METHOD_SERVICE);
        imm.toggleSoftInput(InputMethodManager.SHOW_FORCED,InputMethodManager.HIDE_IMPLICIT_ONLY);
    }

    public static void hideKeyboard(Activity activity, EditText editText){
        InputMethodManager imm = (InputMethodManager) activity.getSystemService(activity.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(editText.getWindowToken(), 0);
    }

    public static String executeCmd(String cmd, boolean sudo){
        try {
            Process p;
            if(!sudo)
                p = Runtime.getRuntime().exec(cmd);
            else{
                p = Runtime.getRuntime().exec(new String[] { "su", "-c", cmd });
            }
            BufferedReader stdInput = new BufferedReader(new InputStreamReader(p.getInputStream()));

            String s;
            String res = "";
            while ((s = stdInput.readLine()) != null) {
                res += s + "\n";
            }
            p.destroy();
            return res;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "";

    }
}
