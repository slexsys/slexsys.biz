package com.slexsys.biz.main.comp.Events;

import java.io.Serializable;

/**
 * Created by slexsys on 2/21/17.
 */

public interface OnEventListener extends Serializable{
    void onEvent(Serializable serializable);
}
