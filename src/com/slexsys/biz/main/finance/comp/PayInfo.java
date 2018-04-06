package com.slexsys.biz.main.finance.comp;

import com.slexsys.biz.main.comp.NewTypes.DateTime;
import com.slexsys.biz.main.comp.enums.PayType;

import java.io.Serializable;

/**
 * Created by slexsys on 3/30/16.
 */
public class PayInfo implements Serializable {
    //region fields
    public int pid;
    public double total;
    public double payed;
    public DateTime date;
    public PayType type;
    public double credit;
    //endregion

    //region constructors
    public PayInfo() {
    }

    public PayInfo(double total, double credit) {
        this.total = total;
        this.credit = credit;
    }

    public PayInfo(int pid, double total, double credit) {
        this.pid = pid;
        this.total = total;
        this.credit = credit;
    }

    public PayInfo(double payed, PayType type) {
        this.payed = payed;
        this.type = type;
    }

    public PayInfo(int pid, double total, double payed, DateTime date, PayType type, double credit) {
        this.pid = pid;
        this.total = total;
        this.payed = payed;
        this.date = date;
        this.type = type;
        this.credit = credit;
    }
    //endregion
}
