package com.slexsys.biz.main.edit.newedit;

import android.content.Context;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.*;
import com.slexsys.biz.R;
import com.slexsys.biz.database.comp.Convert;

import java.util.LinkedList;
import java.util.List;

/**
 * Created by slexsys on 3/19/16.
 */
public class NEListItemAdapter  extends BaseAdapter {

    private Context context;
    private List<Object> items;
    private int inputType;

    public NEListItemAdapter(Context context, List<Object> items, int inputType){
        this.context = context;
        this.items = items;
        this.inputType = inputType;
    }

    @Override
    public int getCount() {
        return items.size();
    }

    @Override
    public Object getItem(int position) {
        return items.get(position);
    }

    public List<Object> getItems() {
        List<Object> result = new LinkedList<Object>();
        for (Object item : items){
            if(item.toString() != null && item.toString().trim().length() > 0)
                result.add(item);
        }
        return result;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        final ViewHolder holder;
        if (convertView == null) {
            holder = new ViewHolder();
            LayoutInflater vi = (LayoutInflater) context
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = vi.inflate(R.layout.nelistitem, null);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        TextView tv = (TextView) convertView.findViewById(R.id.textViewNEListItem);
        EditText et = (EditText) convertView.findViewById(R.id.editTextNEListItem);
        Button btn = (Button) convertView.findViewById(R.id.buttonNEListItemAdd);

        holder.textView = tv;
        holder.editText = et;
        holder.button = btn;
        holder.index = position;

        convertView.setTag(holder);

        Object item = items.get(position);
        holder.textView.setText(Convert.ToString(position + 1));
        holder.editText.setInputType(inputType);
        holder.editText.setText(Convert.ToString(item));
        holder.editText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                items.remove(holder.index);
                items.add(holder.index, editable.toString());
            }
        });
        holder.editText.setOnKeyListener(new View.OnKeyListener() {
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                // If the event is a key-up event on the "enter" button
                if ((event.getAction() == KeyEvent.ACTION_UP) &&
                        (keyCode == KeyEvent.KEYCODE_ENTER) &&
                        holder.index == items.size() - 1) {
                    ClickButtonAdd(holder.button);
                    return true;
                }
                return false;
            }
        });

        if (position == items.size() - 1){
            holder.button.setVisibility(View.VISIBLE);
            holder.editText.requestFocus();
            holder.button.setOnClickListener(new View.OnClickListener() {
                public void onClick(View v) {
                    ClickButtonAdd(v);
                }
            });
        }
        return convertView;
    }

    private void ClickButtonAdd(View v) {
        v.setVisibility(View.GONE);
        items.add("");
        this.notifyDataSetChanged();
    }

    private class ViewHolder {
        TextView textView;
        EditText editText;
        Button button;
        int index;
    }
}
