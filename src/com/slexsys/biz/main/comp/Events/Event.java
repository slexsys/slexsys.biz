package com.slexsys.biz.main.comp.Events;

import java.io.Serializable;

/**
 * Created by slexsys on 2/21/17.
 */

public class Event implements Serializable {

    private OnEventListener listener;

    public void setOnEventListener(OnEventListener listener) {
        this.listener = listener;
    }

    public void doEvent(Serializable serializable) {
        if (this.listener != null)
            this.listener.onEvent(serializable);
    }
}
