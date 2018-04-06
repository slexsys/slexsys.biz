package com.slexsys.biz.main.comp.NewTypes;

import android.view.MotionEvent;
import android.view.View;
import android.widget.ViewFlipper;

/**
 * Created by slexsys on 4/8/16.
 */
public class SwipSlider {

    public int mindelta = 5;
    public int maxdelta = 40;

    private float historicX = 0;
    private float historicY = 0;

    private ViewFlipper viewFlipper;

    public SwipSlider(ViewFlipper viewFlipper) {
        this.viewFlipper = viewFlipper;
        viewFlipper.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event)
            {
                return OnTouch(v, event);
            }
        });
    }

    private boolean OnTouch(View view, MotionEvent event) {
        boolean result = true;
        float diffX = event.getX() - historicX;
        switch (event.getAction())
        {
            case MotionEvent.ACTION_DOWN:
                historicX = event.getX();
                historicY = event.getY();
                break;
            case MotionEvent.ACTION_UP:
                if (diffX < -maxdelta) {
                    viewFlipper.showNext();
                    result = true;
                } else if (diffX > maxdelta) {
                    viewFlipper.showPrevious();
                    result = true;
                }
                if (diffX < -mindelta || diffX > mindelta)
                    result = true;
                break;
        }
        return result;
    }
}
