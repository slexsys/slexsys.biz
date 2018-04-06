package com.slexsys.biz.main.operations.comp;

import com.slexsys.biz.main.comp.COPDRF.COPDRFCell;
import com.slexsys.biz.main.comp.COPDRF.COPDRFColumnName;
import com.slexsys.biz.main.comp.COPDRF.CellIndex;
import com.slexsys.biz.main.comp.COPDRF.COPDRFRow;
import com.slexsys.biz.main.comp.NewTypes.DataRow;
import com.slexsys.biz.main.comp.NewTypes.DataTable;

import java.io.Serializable;
import java.util.LinkedList;
import java.util.List;

/**
 * Created by slexsys on 3/26/16.
 */
public class OperItem implements Serializable {
    //region fields
    public int ID;
    public String Code;
    public String Name;
    public String Measure;
    public double Qtty;
    public double PriceIn;
    public double PriceOut;
    public String Currency;
    public int CurrencyID;
    public double AmountIn;
    public double AmountOut;
    public String Description;
    public double Discount;
    public double QttyR;
    //endregion

    //region constructors
    public OperItem() { }

    public OperItem(DataRow row) {
        if (row != null) {
            int i = 0;
            ID = row.getInt(i++);
            Code = row.getString(i++);
            Name = row.getString(i++);
            Measure = row.getString(i++);
            Qtty = row.getDouble(i++);
            PriceIn = row.getDouble(i++);
            PriceOut = row.getDouble(i++);
            Currency = row.getString(i++);
            CurrencyID = row.getInt(i++);
            AmountIn = row.getDouble(i++);
            AmountOut = row.getDouble(i++);
            Description = row.getString(i++);
        }
    }
    //endregion

    private List<OperItem> getListByDataTable(DataTable dataTable) {
        List<OperItem> list = new LinkedList<OperItem>();
        if (dataTable != null) {
            for (DataRow row : dataTable) {
                list.add(new OperItem(row));
            }
        }
        return list;
    }

    public static List<OperItem> getOperItemList(DataTable dataTable) {
        OperItem operItem = new OperItem();
        return operItem.getListByDataTable(dataTable);
    }

    public COPDRFRow toCOPDRFRow() {
        return new COPDRFRow() {{
            add(new COPDRFCell(Code, CellIndex.fromInts(0, 0, 0), COPDRFColumnName.Code));
            add(new COPDRFCell(Name, CellIndex.fromInts(0, 0, 2), COPDRFColumnName.Name));

            add(new COPDRFCell(Measure, CellIndex.fromInts(0, 1, 0), COPDRFColumnName.Measure));
            add(new COPDRFCell(Qtty, CellIndex.fromInts(0, 1, 1), COPDRFColumnName.Qtty));
            add(new COPDRFCell(PriceIn, CellIndex.fromInts(0, 1, 2), COPDRFColumnName.PriceIn));
            add(new COPDRFCell(PriceOut, CellIndex.fromInts(0, 1, 3), COPDRFColumnName.PriceOut));

            add(new COPDRFCell(Currency, CellIndex.fromInts(0, 2, 0), COPDRFColumnName.Currency));
            add(new COPDRFCell(AmountIn, CellIndex.fromInts(0, 2, 2), COPDRFColumnName.AmountIn));
            add(new COPDRFCell(AmountOut, CellIndex.fromInts(0, 2, 3), COPDRFColumnName.AmountOut));
        }};
    }

    //region getters setters
    public int getID() {
        return ID;
    }

    public void setID(int ID) {
        this.ID = ID;
    }

    public String getCode() {
        return Code;
    }

    public void setCode(String code) {
        Code = code;
    }

    public String getName() {
        return Name;
    }

    public void setName(String name) {
        Name = name;
    }

    public String getMeasure() {
        return Measure;
    }

    public void setMeasure(String measure) {
        Measure = measure;
    }

    public double getQtty() {
        return Qtty;
    }

    public void setQtty(double qtty) {
        Qtty = qtty;
    }

    public double getPriceIn() {
        return PriceIn;
    }

    public void setPriceIn(double priceIn) {
        PriceIn = priceIn;
    }

    public double getPriceOut() {
        return PriceOut;
    }

    public void setPriceOut(double priceOut) {
        PriceOut = priceOut;
    }

    public String getCurrency() {
        return Currency;
    }

    public void setCurrency(String currency) {
        Currency = currency;
    }

    public int getCurrencyID() {
        return CurrencyID;
    }

    public void setCurrencyID(int currencyID) {
        CurrencyID = currencyID;
    }

    public double getAmountIn() {
        return AmountIn;
    }

    public void setAmountIn(double amountIn) {
        AmountIn = amountIn;
    }

    public double getAmountOut() {
        return AmountOut;
    }

    public void setAmountOut(double amountOut) {
        AmountOut = amountOut;
    }

    public String getDescription() {
        return Description;
    }

    public void setDescription(String description) {
        Description = description;
    }

    public double getDiscount() {
        return Discount;
    }

    public void setDiscount(double discount) {
        Discount = discount;
    }

    public double getQttyR() {
        return QttyR;
    }

    public void setQttyR(double qttyR) {
        QttyR = qttyR;
    }
    //endregion
}
