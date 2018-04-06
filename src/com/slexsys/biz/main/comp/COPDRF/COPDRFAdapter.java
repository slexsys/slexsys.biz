package com.slexsys.biz.main.comp.COPDRF;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.ViewFlipper;

import com.slexsys.biz.R;
import com.slexsys.biz.comp.std;

/**
 * Created by slexsys on 2/6/17.
 */

public class COPDRFAdapter extends BaseAdapter {
    private Context context;
    private COPDRFRows rows;
    private COPDRFColumns columns;

    public COPDRFAdapter(Context context, COPDRFRows rows) {
        this.context = context;
        this.rows = rows;
    }

    public COPDRFAdapter(Context context, COPDRFRows rows, COPDRFColumns columns) {
        this.context = context;
        this.rows = rows;
        this.columns = columns;
    }

    @Override
    public int getCount() {
        return rows.size();
    }

    @Override
    public Object getItem(int position) {
        return rows.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(final int position, View convertView, final ViewGroup parent) {
        View view = convertView;
        if (view == null) {
            LayoutInflater vi = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            view = vi.inflate(R.layout.copdrfrowtemplate1, null);
        }

        initViewFlipper(view, parent, position);

        TextView[][][] textView3d = get3DTextViews(view);

        hideViews(textView3d);

        fillTextView(textView3d, position);

        return view;
    }

    private void fillTextView(TextView[][][] textView3d, int position) {
        COPDRFRow row = rows.get(position);
        for (COPDRFCell cell : row) {
            CellIndex index = cell.getCellIndex();
            if (index != null) {
                int d1 = index.getD1();
                int d2 = index.getD2();
                int d3 = index.getD3();
                TextView textView = textView3d[d1][d2][d3];
                fillTextView(textView, cell);
            }
        }
    }

    private void fillTextView(TextView textView, COPDRFCell cell) {
        if (cell.getValue() != null) {
            if (cell.getColumnName() == null) {
                textView.setVisibility(View.VISIBLE);
            } else {
                COPDRFColumn column = columns.get(cell.getColumnName());
                if (column != null) {
                    std.setAccessMode4View(textView, column.getAccessMode());
                }
            }
            textView.setText(cell.getValue().toString());
            textView.setTextSize(cell.getSize());
            textView.setTextColor(cell.getColor());
        }
    }

    private void initViewFlipper(View view, final ViewGroup parent, final int position) {
        final ViewFlipper viewFlipper = (ViewFlipper) view.findViewById(R.id.viewFlipperCOPDRFRowTemplate1);
        //new SwipSlider(viewFlipper); //operations OperItem edit not work

        LinearLayout linearLayoutNext = (LinearLayout) view.findViewById(R.id.linearLayoutCOPDRFRowTemplate1P1Swich);
        linearLayoutNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showNextFlip(position, viewFlipper, parent);
            }
        });

        LinearLayout linearLayoutBack = (LinearLayout) view.findViewById(R.id.linearLayoutCOPDRFRowTemplate1P2Swich);
        linearLayoutBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showPreviousFlip(position, viewFlipper, parent);
            }
        });
    }

    private void hideViews(View[][][] views) {
        if (columns != null) {
            for (int i = 0; i < views.length; ++i) {
                for (int j = 0; j < views[i].length; ++j) {
                    for (int k = 0; k < views[i][j].length; ++k) {
                        views[i][j][k].setVisibility(View.INVISIBLE);
                    }
                }
            }
        }
    }

    private TextView[][][] get3DTextViews(View view) {
        return new TextView[][][]{
        {
            {
                    (TextView) view.findViewById(R.id.textViewCOPDRFRowTemplate1P111),
                    (TextView) view.findViewById(R.id.textViewCOPDRFRowTemplate1P112),
                    (TextView) view.findViewById(R.id.textViewCOPDRFRowTemplate1P113),
                    (TextView) view.findViewById(R.id.textViewCOPDRFRowTemplate1P114)
            }, {
                    (TextView) view.findViewById(R.id.textViewCOPDRFRowTemplate1P121),
                    (TextView) view.findViewById(R.id.textViewCOPDRFRowTemplate1P122),
                    (TextView) view.findViewById(R.id.textViewCOPDRFRowTemplate1P123),
                    (TextView) view.findViewById(R.id.textViewCOPDRFRowTemplate1P124)
            }, {
                    (TextView) view.findViewById(R.id.textViewCOPDRFRowTemplate1P131),
                    (TextView) view.findViewById(R.id.textViewCOPDRFRowTemplate1P132),
                    (TextView) view.findViewById(R.id.textViewCOPDRFRowTemplate1P133),
                    (TextView) view.findViewById(R.id.textViewCOPDRFRowTemplate1P134)
            }, {
                    (TextView) view.findViewById(R.id.textViewCOPDRFRowTemplate1P141),
                    (TextView) view.findViewById(R.id.textViewCOPDRFRowTemplate1P142),
                    (TextView) view.findViewById(R.id.textViewCOPDRFRowTemplate1P143),
                    (TextView) view.findViewById(R.id.textViewCOPDRFRowTemplate1P144)
            }, {
                    (TextView) view.findViewById(R.id.textViewCOPDRFRowTemplate1P151),
                    (TextView) view.findViewById(R.id.textViewCOPDRFRowTemplate1P152),
                    (TextView) view.findViewById(R.id.textViewCOPDRFRowTemplate1P153),
                    (TextView) view.findViewById(R.id.textViewCOPDRFRowTemplate1P154)
            }
        }, {
            {
                    (TextView) view.findViewById(R.id.textViewCOPDRFRowTemplate1P211),
                    (TextView) view.findViewById(R.id.textViewCOPDRFRowTemplate1P212),
                    (TextView) view.findViewById(R.id.textViewCOPDRFRowTemplate1P213),
                    (TextView) view.findViewById(R.id.textViewCOPDRFRowTemplate1P214)
            }, {
                    (TextView) view.findViewById(R.id.textViewCOPDRFRowTemplate1P221),
                    (TextView) view.findViewById(R.id.textViewCOPDRFRowTemplate1P222),
                    (TextView) view.findViewById(R.id.textViewCOPDRFRowTemplate1P223),
                    (TextView) view.findViewById(R.id.textViewCOPDRFRowTemplate1P224)
            }, {
                    (TextView) view.findViewById(R.id.textViewCOPDRFRowTemplate1P231),
                    (TextView) view.findViewById(R.id.textViewCOPDRFRowTemplate1P232),
                    (TextView) view.findViewById(R.id.textViewCOPDRFRowTemplate1P233),
                    (TextView) view.findViewById(R.id.textViewCOPDRFRowTemplate1P234)
            }, {
                    (TextView) view.findViewById(R.id.textViewCOPDRFRowTemplate1P241),
                    (TextView) view.findViewById(R.id.textViewCOPDRFRowTemplate1P242),
                    (TextView) view.findViewById(R.id.textViewCOPDRFRowTemplate1P243),
                    (TextView) view.findViewById(R.id.textViewCOPDRFRowTemplate1P244)
            }, {
                    (TextView) view.findViewById(R.id.textViewCOPDRFRowTemplate1P251),
                    (TextView) view.findViewById(R.id.textViewCOPDRFRowTemplate1P252),
                    (TextView) view.findViewById(R.id.textViewCOPDRFRowTemplate1P253),
                    (TextView) view.findViewById(R.id.textViewCOPDRFRowTemplate1P254)
            }
        }};
    }

    private void showNextFlip(int position, ViewFlipper viewFlipper, final ViewGroup parent) {
        if (position != 0) {
            viewFlipper.showNext();
        } else {
            showAllNextFlip(parent);
        }
    }

    private void showPreviousFlip(int position, ViewFlipper viewFlipper, final ViewGroup parent) {
        if (position != 0) {
            viewFlipper.showPrevious();
        } else {
            showAllPreviousFlip(parent);
        }
    }

    private void showAllNextFlip(final ViewGroup parent) {
        for (int i = 0; i < parent.getChildCount(); ++i) {
            ViewFlipper viewFlipper = (ViewFlipper) parent.getChildAt(i).findViewById(R.id.viewFlipperCOPDRFRowTemplate1);
            viewFlipper.showNext();
        }
    }

    private void showAllPreviousFlip(final ViewGroup parent) {
        for (int i = 0; i < parent.getChildCount(); ++i) {
            ViewFlipper viewFlipper = (ViewFlipper) parent.getChildAt(i).findViewById(R.id.viewFlipperCOPDRFRowTemplate1);
            viewFlipper.showPrevious();
        }
    }
}
