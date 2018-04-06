package com.slexsys.biz.main.operations.json;

import java.io.Serializable;

/**
 * Created by slexsys on 3/1/17.
 */
public enum PriceUpdate implements Serializable {
    None,
    PriceIn,
    PriceOut,
    PriceInOut
}
