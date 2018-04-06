package com.slexsys.biz.database.sqls.SQLTables;

/**
 * Created by slexsys on 12/2/17.
 */

public class SQLTablesPartners extends SQLTablesiIBase {
    //region fields
    private String mol;
    private String mol2;
    private String city;
    private String city2;
    private String address;
    private String address2;
    private String phone;
    private String phone2;
    private String fax;
    private String email;
    private String taxno;
    private String vatno;
    private String pricegroup;
    private String type;
    private String discount;
    private String userid;
    private String userrealtime;
    private String cardnumber;
    private String paymentdays;
    //endregion

    //region constructors
    public SQLTablesPartners(SQLTablesiIBase sqlTablesiIBase) {
        super(sqlTablesiIBase);
    }
    //endregion

    //region getters setters
    public String getMol() {
        return mol;
    }

    public void setMol(String mol) {
        this.mol = mol;
    }

    public String getMol2() {
        return mol2;
    }

    public void setMol2(String mol2) {
        this.mol2 = mol2;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getCity2() {
        return city2;
    }

    public void setCity2(String city2) {
        this.city2 = city2;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getAddress2() {
        return address2;
    }

    public void setAddress2(String address2) {
        this.address2 = address2;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getPhone2() {
        return phone2;
    }

    public void setPhone2(String phone2) {
        this.phone2 = phone2;
    }

    public String getFax() {
        return fax;
    }

    public void setFax(String fax) {
        this.fax = fax;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getTaxno() {
        return taxno;
    }

    public void setTaxno(String taxno) {
        this.taxno = taxno;
    }

    public String getVatno() {
        return vatno;
    }

    public void setVatno(String vatno) {
        this.vatno = vatno;
    }

    public String getPricegroup() {
        return pricegroup;
    }

    public void setPricegroup(String pricegroup) {
        this.pricegroup = pricegroup;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getDiscount() {
        return discount;
    }

    public void setDiscount(String discount) {
        this.discount = discount;
    }

    public String getUserid() {
        return userid;
    }

    public void setUserid(String userid) {
        this.userid = userid;
    }

    public String getUserrealtime() {
        return userrealtime;
    }

    public void setUserrealtime(String userrealtime) {
        this.userrealtime = userrealtime;
    }

    public String getCardnumber() {
        return cardnumber;
    }

    public void setCardnumber(String cardnumber) {
        this.cardnumber = cardnumber;
    }

    public String getPaymentdays() {
        return paymentdays;
    }

    public void setPaymentdays(String paymentdays) {
        this.paymentdays = paymentdays;
    }
    //endregion
}
