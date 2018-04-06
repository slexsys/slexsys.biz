package com.slexsys.biz.database.comp;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import com.slexsys.biz.R;

import java.util.List;

/**
 * Created by slexsys on 3/26/16.
 */
public class iIBaseAdapter extends BaseAdapter {
    private Context context;
    private List<? extends iBase> items;

    public iIBaseAdapter(Context context, List<? extends iBase> items){
        this.context = context;
        this.items = items;
    }

    @Override
    public int getCount() {
        return items.size();
    }

    @Override
    public Object getItem(int position) {
        return items.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View v = convertView;
        if (v == null) {
            LayoutInflater vi = (LayoutInflater) context
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            v = vi.inflate(R.layout.listitem, null);
        }
        ImageView iv = (ImageView) v.findViewById(R.id.imageView);
        TextView tv1 = (TextView) v.findViewById(R.id.textViewLine1);
        TextView tv2 = (TextView) v.findViewById(R.id.textViewLine2);
        TextView tv3 = (TextView) v.findViewById(R.id.textViewLine3);
        TextView tv4 = (TextView) v.findViewById(R.id.textViewLine4);

        String[] array = items.get(position).getShowInfo();
        FillViews(array, new TextView[] { tv1, tv2, tv3, tv4 });
        return v;
    }

    private void FillViews(String[] array, TextView[] textViews) {
        for (int i = 0; i < array.length; ++i) {
            if(array[i] != null){
                textViews[i].setText(array[i]);
            } else {
                textViews[i].setVisibility(View.GONE);
            }
        }
    }
}
