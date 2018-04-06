package com.slexsys.biz.main.comp.NewTypes;

import android.graphics.Color;
import android.graphics.Rect;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

import com.slexsys.biz.R;

/**
 * Created by slexsys on 4/8/16.
 */
public class ListViewItemMove {

    public OnEventListener editRemoveEvent;

    private ListView listView;
    private Drawable drawable;

    private boolean isSelectable;
    private int mindelta = 5;
    private int precentdeltaX = 40;

    private float historicX = 0;
    private float historicY = 0;
    private boolean moving = false;

    private static int lasttouch = -1;

    public interface OnEventListener {
        void onSlideLeftEvent(int position);
        void onSlideRightEvent(int position);
        void onClickEvent(int position);
        void onLongClickEvent(int position);
    }

    public ListViewItemMove(ListView listView) {
        this.listView = listView;
        listView.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                Log.e(">>>>>>>", "moving : >" + moving);
                return OnTouch(v, event);
            }
        });
        listView.setOnItemLongClickListener (new AdapterView.OnItemLongClickListener() {
            public boolean onItemLongClick(AdapterView parent, View view, int position, long id) {
                if (!moving && position != -1) {
                    editRemoveEvent.onLongClickEvent(position);
                    Log.e(">>>>>>>", "long click : moving >" + moving);
                }
                return true;
            }
        });
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if (!moving && position != -1) {
                    editRemoveEvent.onClickEvent(position);
                }
            }
        });
        drawable = listView.getSelector();
    }

    public ListViewItemMove(ListView listView, boolean isSelectable) {
        this(listView);
        this.isSelectable = isSelectable;
    }

    private boolean OnTouch(View view, MotionEvent event) {
        boolean result = false;
        if (view != null) {
            int deltaX = getDeltaX();
            double precSel = getPrecSel();
            float diffX = event.getX() - historicX;
            switch (event.getAction()) {
                case MotionEvent.ACTION_DOWN:
                    historicX = event.getX();
                    historicY = event.getY();
                    result = OnDown(view, event);
                    break;
                case MotionEvent.ACTION_UP:
                    result = OnUp(view, event);
                    if (diffX < -deltaX * precSel) {
                        result = OnLeftUp(view, event);
                    } else if (diffX > deltaX) {
                        result = OnRightUp(view, event);
                    } else {
                        result = OnUpAlter(view, event);
                    }
                    if (diffX < -mindelta || diffX > mindelta)
                        result = OnMoveNitral(view, event);
                    movingSetter();
                    break;
                case MotionEvent.ACTION_MOVE:
                    if (diffX < -3 * mindelta || diffX > 3 * mindelta) {
                        result = OnMove(view, event);
                        moving = true;
                    }
                    break;
                default:
                    result = OnEnd(view, event);
                    break;
            }
        }
        return result;
    }

    private void movingSetter() {
        Thread run = new Thread() {
            @Override
            public void run() {
                try {
                    Thread.sleep(500);
                } catch (InterruptedException e) { }
                moving = false;
            }
        };
        run.start();
    }

    private boolean OnDown(View view, MotionEvent event) {
        SetLastTouch(event);
        return false;
    }

    private boolean OnUp(View view, MotionEvent event) {
        listView.setSelector(drawable);
        SetScrollItem(0, 1f);
        return false;
    }

    private boolean OnLeftUp(View view, MotionEvent event) {
        if (lasttouch != -1) {
            if (isSelectable) {
                selectItem(lasttouch);
            }
            editRemoveEvent.onSlideLeftEvent(lasttouch);
        }
        return true;
    }

    private boolean OnRightUp(View view, MotionEvent event) {
        if (lasttouch != -1) {
            editRemoveEvent.onSlideRightEvent(lasttouch);
        }
        return true;
    }

    private boolean OnUpAlter(View view, MotionEvent event) {
        return false;
    }

    private boolean OnMoveNitral(View view, MotionEvent event) {
        return true;
    }

    private boolean OnMove(View view, MotionEvent event) {
        listView.setSelector(new ColorDrawable(Color.TRANSPARENT));
        float diffX = event.getX() - historicX;
        SetScrollItem(-(int)diffX, 0.5f);
        return true;
    }

    private boolean OnEnd(View view, MotionEvent event) {
        return false;
    }

    private int getDeltaX() {
        return (int)(listView.getMeasuredWidth() * precentdeltaX / 100);
    }

    private double getPrecSel() {
        double result = 1;
        if (isSelectable) {
            result = 0.2;
        }
        return result;
    }

    private void SetLastTouch(MotionEvent event) {
        Rect rect = new Rect();
        int childCount = listView.getChildCount();
        int[] listViewCoords = new int[2];
        listView.getLocationOnScreen(listViewCoords);
        int x = (int) event.getRawX() - listViewCoords[0];
        int y = (int) event.getRawY() - listViewCoords[1];
        View child, touched = null;
        for (int i = 0; i < childCount; i++) {
            child = listView.getChildAt(i);
            child.getHitRect(rect);
            if (rect.contains(x, y)) {
                //touched = child;
                lasttouch = i;
                break;
            }
        }

        if (touched != null) {
            //lasttouch = Blankx.listViewData.getPositionForView(touched);
        }
    }

    private void SetScrollItem(int scroll, float opacity) {
        if (lasttouch != -1) {
            View v = listView.getChildAt(lasttouch);
            if (v != null) {
                v.setScrollX(scroll);
                v.setAlpha(opacity);
            }
        }
    }

    public void addRemoveListner(OnEventListener listener) {
        editRemoveEvent = listener;
    }

    private void selectItem(int lasttouch) {
        View v = listView.getChildAt(lasttouch);
        TextView textView = (TextView) v.findViewById(R.id.textViewListItemSelection);
        if (textView.getVisibility() != View.GONE) {
            textView.setVisibility(View.GONE);
        } else {
            textView.setVisibility(View.VISIBLE);
        }
    }
}

