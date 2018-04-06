package com.slexsys.biz.database.sqls.SQLTables;

/**
 * Created by slexsys on 12/2/17.
 */

public class SQLTablesItems extends SQLTablesiIBase {
    //region fields
    private String qtty;
    private String barcode;
    private String barcode2;
    private String plu;
    private String partnerid;
    private String catalog;
    private String measure;
    private String measure2;
    private String ratio;
    private String pricein;
    private String priceout;
    private String minqtty;
    private String normalqtty;
    private String description;
    private String isrecipe;
    private String taxgroup;
    private String type;
    //endregion

    //region constructors
    public SQLTablesItems(SQLTablesiIBase sqlTablesiIBase) {
        super(sqlTablesiIBase);
    }
    //endregion

    //region getters setters
    public String getQtty() {
        return qtty;
    }

    public void setQtty(String qtty) {
        this.qtty = qtty;
    }

    public String getBarcode() {
        return barcode;
    }

    public void setBarcode(String barcode) {
        this.barcode = barcode;
    }

    public String getBarcode2() {
        return barcode2;
    }

    public void setBarcode2(String barcode2) {
        this.barcode2 = barcode2;
    }

    public String getPlu() {
        return plu;
    }

    public void setPlu(String plu) {
        this.plu = plu;
    }

    public String getPartnerid() {
        return partnerid;
    }

    public void setPartnerid(String partnerid) {
        this.partnerid = partnerid;
    }

    public String getCatalog() {
        return catalog;
    }

    public void setCatalog(String catalog) {
        this.catalog = catalog;
    }

    public String getMeasure() {
        return measure;
    }

    public void setMeasure(String measure) {
        this.measure = measure;
    }

    public String getMeasure2() {
        return measure2;
    }

    public void setMeasure2(String measure2) {
        this.measure2 = measure2;
    }

    public String getRatio() {
        return ratio;
    }

    public void setRatio(String ratio) {
        this.ratio = ratio;
    }

    public String getPricein() {
        return pricein;
    }

    public void setPricein(String pricein) {
        this.pricein = pricein;
    }

    public String getPriceout() {
        return priceout;
    }

    public void setPriceout(String priceout) {
        this.priceout = priceout;
    }

    public String getMinqtty() {
        return minqtty;
    }

    public void setMinqtty(String minqtty) {
        this.minqtty = minqtty;
    }

    public String getNormalqtty() {
        return normalqtty;
    }

    public void setNormalqtty(String normalqtty) {
        this.normalqtty = normalqtty;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getIsrecipe() {
        return isrecipe;
    }

    public void setIsrecipe(String isrecipe) {
        this.isrecipe = isrecipe;
    }

    public String getTaxgroup() {
        return taxgroup;
    }

    public void setTaxgroup(String taxgroup) {
        this.taxgroup = taxgroup;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }
    //endregion
}
