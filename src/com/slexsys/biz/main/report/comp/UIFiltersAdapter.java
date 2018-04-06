package com.slexsys.biz.main.report.comp;

import android.app.Activity;
import android.content.Context;
import android.text.Editable;
import android.text.InputType;
import android.text.TextWatcher;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.slexsys.biz.R;
import com.slexsys.biz.GO;
import com.slexsys.biz.comp.std;
import com.slexsys.biz.main.comp.enums.ConsType;
import com.slexsys.biz.main.comp.enums.PayType;
import com.slexsys.biz.database.comp.iBase;
import com.slexsys.biz.database.comp.iItems;
import com.slexsys.biz.main.comp.dialogs.CollectionDialog;
import com.slexsys.biz.main.comp.dialogs.DateChooseDialog;
import com.slexsys.biz.main.comp.NewTypes.DateTime;
import com.slexsys.biz.main.comp.Events.OnEventListener;
import com.slexsys.biz.main.edit.Edit;
import com.slexsys.biz.main.operations.json.PayMode;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * Created by slexsys on 2/8/17.
 */

public class UIFiltersAdapter extends BaseAdapter {
    private UIFilters items;
    private Activity activity;
    private Button buttonOK;
    private List<View> views;

    public UIFiltersAdapter(UIFilters items, Activity activity) {
        this.items = items;
        this.activity = activity;
        //initViews();
    }

    private void initViews() {
        int position = 0;
        views = new LinkedList<View>();
        for (UIFilter item : items) {
            LayoutInflater vi = (LayoutInflater) activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            View view = vi.inflate(R.layout.reportfilteritem1, null);
            view.setFocusable(true);
            TextView textView = (TextView) view.findViewById(R.id.textViewreportfilteritem1Description);
            final EditText editText = (EditText) view.findViewById(R.id.editTextReportFilterItem1Value);
            Button button = (Button) view.findViewById(R.id.buttonReportFilterItem1ChooseValue);

            initEditTextEvents(editText, item);

            final View viewx = view;
            final int pos = position;
            button.setOnClickListener(new View.OnClickListener() {
                public void onClick(View v) {
                    ButtonClick(pos, viewx);
                }
            });

            initprivileges(item.getType(), view, textView, editText, button, position);
            textView.setText(item.getType().getResId());
            fillEditText(editText, item);
            Log.e("UI Filter", " pos" + position);
            views.add(view);
            position++;
        }
    }

    @Override
    public int getCount() {
        //return items.sizeOfVisibles();
        return items.size();
    }

    @Override
    public UIFilter getItem(int position) {
        //return items.getOfVisibles(position);//original
        return items.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View view = convertView;
        //if (view == null) {
            LayoutInflater vi = (LayoutInflater) activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            view = vi.inflate(R.layout.reportfilteritem1, null);
            view.setFocusable(true);
            TextView textView = (TextView) view.findViewById(R.id.textViewreportfilteritem1Description);
            final EditText editText = (EditText) view.findViewById(R.id.editTextReportFilterItem1Value);
            Button button = (Button) view.findViewById(R.id.buttonReportFilterItem1ChooseValue);

            final UIFilter item = getItem(position);
            initEditTextEvents(editText, item);

            final View viewx = view;
            final int pos = position;
            button.setOnClickListener(new View.OnClickListener() {
                public void onClick(View v) {
                    ButtonClick(pos, viewx);
                }
            });

            initprivileges(item.getType(), view, textView, editText, button, position);
            textView.setText(item.getType().getResId());
            fillEditText(editText, item);
            //Log.e("UI Filter", " pos" + position);
        //}
        return view;
    }

    public void setOKButton(Button button) {
        buttonOK = button;
    }

    private void initEditTextEvents(final EditText editText, final UIFilter item) {
        editText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) { }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if (activity.getCurrentFocus() == editText) {
                    setTextChangedValue(item, charSequence.toString());
                }
            }

            @Override
            public void afterTextChanged(Editable editable) { }
        });

        editText.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (event != null && event.getKeyCode() == KeyEvent.KEYCODE_ENTER) {
                    std.hideKeyboard(activity, editText);
                    buttonOK.performClick();
                    return true;
                }
                return false;
            }
        });
    }

    public void setTextChangedValue(UIFilter item, String value) {
        UIFilterType type = item.getType();
        Log.d("Text changed", "+");
        if(type.isAcct() || type.isQttyPriceType() || type.isItemType()) {
            item.setValue(value);
            item.setObject(null);
        }
    }

    private void initprivileges(UIFilterType type, final View view, TextView textView,
                                EditText editText, final Button button, final int position) {
        if (type.isNoButton()) {
            InputType1(editText, button);
        } else if (type.isQttyPriceType()) {
            InputType2(editText, button, position);
        } else if (type.isDateType() || type.isMultiValuesType()) {
            InputType3(editText, view, position);
        }
    }

    private void InputType1(EditText editText, Button button) {
        editText.setInputType(InputType.TYPE_CLASS_NUMBER);
        button.setVisibility(View.GONE);
    }

    private void InputType2(EditText editText, Button button, int position) {
        UIFilter item = getItem(position);
        editText.setInputType(InputType.TYPE_CLASS_NUMBER);
        String operator = item.getOperator();
        if (operator == null || operator == "") {
            operator = "=";
        }
        button.setText(operator);
    }

    private void InputType3(EditText editText, final View view, final int position) {
        editText.setFocusable(false);
        editText.setClickable(false);
        editText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ButtonClick(position, view);
            }
        });
    }

    private void fillEditText(EditText editText, UIFilter item) {
        item.setIgnoreEditTextchanges(true);
        UIFilterType type = item.getType();
        Object object = item.getObject();
        if(type.isAcct() || type.isQttyPriceType()) {
            editText.setText(item.getValue());
        } else if (type.isItemType()) {
            if (object != null) {
                iItems items = (iItems) object;
                setFilterValue(editText, getItemValues(items));
            } else {
                editText.setText(getValues(item.getValues()));
            }
        } else if (type.isDateType()) {
            if (object != null) {
                DateTime dateTime = (DateTime) object;
                String value = dateTime.toNormalDateFormat();
                setFilterValue(editText, value);
            }
        }
        item.setIgnoreEditTextchanges(false);
    }

    private void ButtonClick(int position, View view) {
        UIFilterType type = getItem(position).getType();
        if (type.isDateType()) {
            showDateDialog(type, position, view);
        } else if (type.isItemType()) {
            showPIOUDialog(type, position, view);
        } else if (type.isQttyPriceType()) {
            showOperatorDialog(type, position, view);
        } else if (type.isMultiValuesType()) {
            showCollectionDialog(type, position, view);
        }
    }

    private void showDateDialog(final UIFilterType type, final int position, final View view) {
        DateChooseDialog dcd = DateChooseDialog.getInstance();
        dcd.setOnEventListener(new OnEventListener() {
            public void onEvent(Serializable serializable) {
                if (serializable != null) {
                    DateTime dateTime = (DateTime) serializable;
                    setDateFilter(view, position, type, dateTime);
                }
            }
        });
        dcd.show(activity);
    }

    private void showPIOUDialog(final UIFilterType type, final int position, final View view) {
        Edit edit = new Edit();
        edit.setOnEventListener(new OnEventListener() {
            @Override
            public void onEvent(Serializable serializable) {
                if (serializable != null) {
                    iItems items = (iItems) serializable;
                    setItemFilter(view, position, type, items);
                }
            }
        });
        UIFilter item = getItem(position);
        iItems bases = (iItems) item.getObject();
        edit.setSelector(bases);
        edit.putExtra(Edit.PUTTER_AUTO_OK, true);
        edit.putExtra(Edit.PUTTER_DEFAULT_ITEM, type.getiIbase());
        edit.show(activity);
    }

    private void showOperatorDialog(final UIFilterType type, final int position, final View view) {
        final List<String> list = Arrays.asList("=", ">=", "<=", "!=");
        CollectionDialog cd = new CollectionDialog();
        cd.setOnEventListener(new OnEventListener() {
            @Override
            public void onEvent(Serializable serializable) {
                if (serializable != null) {
                    int index = (Integer) serializable;
                    String operator = list.get(index);
                    setItemFilterOperator(view, position, type, operator);
                }
            }
        });
        cd.showSingleChoice(activity, R.string.operator, list);
    }

    private void showCollectionDialog(final UIFilterType type, final int position, final View view) {
        final Map<String, String> map = getMap(type);
        if (type != null) {
            int title = type.getResId();
            CollectionDialog cd = new CollectionDialog();
            cd.setOnEventListener(new OnEventListener() {
                @Override
                public void onEvent(Serializable serializable) {
                    if (serializable != null) {
                        ArrayList selectedItems = (ArrayList) serializable;
                        setCollectionFilter(view, position, type, selectedItems, map);
                    }
                }
            });
            UIFilter item = getItem(position);
            ArrayList arrayList = (ArrayList) item.getObject();
            cd.showMultiChoice(activity, title, map.values(), arrayList);
        }
    }

    private void setDateFilter(View view, int position, UIFilterType type, DateTime dateTime) {
        UIFilter item = getItem(position);
        item.setObject(dateTime);
        item.setValueSQLString(dateTime.toSQLFormatDate());
        String value = dateTime.toNormalDateFormat();
        setFilterValue(view, value);
    }

    private void setItemFilter(View view, int position, UIFilterType type, iItems items) {
        UIFilter item = this.getItem(position);
        item.setObject(items);

        item.setIgnoreEditTextchanges(true);
        setFilterValue(view, getItemValues(items));
        item.setIgnoreEditTextchanges(false);
    }

    private void setItemFilterOperator(View view, int position, UIFilterType type, String operator) {
        UIFilter item = getItem(position);
        item.setOperator(operator);
        Button button = (Button) view.findViewById(R.id.buttonReportFilterItem1ChooseValue);
        button.setText(operator);
    }

    private void setCollectionFilter(View view, int position, UIFilterType type, ArrayList selectedItems, Map<String, String> map) {
        UIFilter item = getItem(position);
        item.setObject(selectedItems);
        item.setValues(selected2IDs(selectedItems, map.keySet()));
        setFilterValue(view, getValues(selectedItems, map.values()));
    }

    private List<String> selected2IDs(ArrayList selectedItems, Set<String> set) {
        List<String> list = new LinkedList<String>();
        String[] array = set.toArray(new String[set.size()]);
        for (Object object : selectedItems) {
            int index = (Integer) object;
            list.add(array[index]);
        }
        return list;
    }

    private void setFilterValue(View view, String text) {
        EditText editText = (EditText) view.findViewById(R.id.editTextReportFilterItem1Value);
        editText.setText(text);
    }

    private String getValues(ArrayList selectedItems, Collection<String> collection) {
        String result = "";

        Object[] array = selectedItems.toArray();
        Arrays.sort(array);

        String[] colarray = collection.toArray(new String[collection.size()]);

        for (Object i : array) {
            result += getValueItem(colarray[(Integer) i]);
        }

        return result;
    }

    private String getValues(List<String> list) {
        String result = "";
        if (list != null) {
            for (String item : list) {
                result += getValueItem(item);
            }
        }
        return result;
    }

    private String getItemValues(iItems items) {
        String result = "";
        if (items.groups != null) {
            String title = activity.getString(R.string.groups);
            result += getItemValue(items.groups.getItems(), title);
        }
        if (items.items != null) {
            String title = activity.getString(R.string.item);
            result += getItemValue(items.items, title);
        }
        return result;
    }

    private String getItemValue(List<? extends iBase> items, String title) {
        String result = "";
        if (items.size() > 0) {
            if (items.size() != 1) {
                result = getValueItem(title + " : " + items.size());
            } else {
                result = getValueItem(items.get(0).getName());
            }
        }
        return result;
    }

    private String getValueItem(String value) {
        return "[" + value + "]";
    }

    private Map<String, String> getMap(UIFilterType type) {
        Map<String, String> map = null;
        switch (type) {
            case DateInterval:
                break;
            case OperTypeAll:
                map = GO.operations.getMap(null);
                break;
            case OperTypePay:
                map = GO.operations.getMap(PayMode.Pay);
                break;
            case OperTypeNoPay:
                map = GO.operations.getMap(PayMode.NoPay);
                break;
            case PayType:
                map = PayType.getMap();
                break;
            case ConsType:
                map = ConsType.getMap();
                break;
            case Measure1:
                map = GO.measure1.getMap();
                break;
            case Measure2:
                map = GO.measure2.getMap();
                break;
        }
        return map;
    }
}