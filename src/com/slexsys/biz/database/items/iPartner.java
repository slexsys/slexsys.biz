package com.slexsys.biz.database.items;

import android.app.Activity;

import com.slexsys.biz.database.comp.comp.PartnerType;
import com.slexsys.biz.database.comp.comp.PriceGroups;
import com.slexsys.biz.database.comp.iIBase;
import com.slexsys.biz.database.sqls.SQLTables.SQLTablesPartners;
import com.slexsys.biz.database.sqls.SQLQuerys;
import com.slexsys.biz.database.sqls.comp.QueryItemGroup;
import com.slexsys.biz.database.sqls.iSQL;
import com.slexsys.biz.main.comp.NewTypes.DataRow;
import com.slexsys.biz.main.comp.NewTypes.DataTable;
import com.slexsys.biz.main.comp.NewTypes.SmartActivity;
import com.slexsys.biz.main.edit.newedit.NEPartner;

import java.util.LinkedList;
import java.util.List;

/**
 * Created by slexsys on 3/11/16.
 */
public class iPartner extends iIBase {
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
    private PriceGroups pricegroup;
    private PartnerType type;
    private double discount;
    private int userid;
    private String userrealtime;
    private String cardnumber;
    private int paymentdays;

    private static QueryItemGroup itemQuerys;
    //endregion

    //region constructors
    static {
        itemQuerys = SQLQuerys.Edit.Partners;
    }

    public iPartner() { }

    public iPartner(DataRow row){
        Fill(row);
    }
    //endregion

    //region override
    @Override
    public SmartActivity newItem(Activity activity, int gid) {
        return startNEPActivity(activity, gid, null);
    }

    @Override
    public SmartActivity editItem(Activity activity, int gid) {
        return startNEPActivity(activity, gid, this);
    }

    @Override
    public String[] getShowInfo() {
        return new String[] { this.name,
                "code " + this.code,
                "phone : " + this.phone,
                String.valueOf(this.type)};
    }

    @Override
    public iIBase createObject(DataRow row) {
        return new iPartner(row);
    }

    @Override
    public QueryItemGroup getItemQuerys() {
        return itemQuerys;
    }

    @Override
    public String getItemLike(String text) {
        return itemQuerys.Item.Like.replace("#text", text);
    }

    @Override
    public String getItemTableName() {
        return itemQuerys.Group.TableName;
    }

    @Override
    public List<iIBase> getLikeList(String text) {
        String query = itemQuerys.Item.Select + " WHERE " +
                itemQuerys.Item.Like.replace("#text", text);
        List<iIBase> result = new LinkedList<iIBase>();
        DataTable dataTable = iSQL.getDataTable(query);
        for (int i = 0; i < dataTable.size(); ++i) {
            result.add(new iPartner(dataTable.getRowWithNames(i)));
        }
        return result;
    }

    @Override
    public String getAddQuery() {
        String query = itemQuerys.Item.Insert;
        return getReplacedString(query);
    }

    @Override
    public String getEditQuery() {
        String query = itemQuerys.Item.Update;
        return getReplacedString(query);
    }

    @Override
    public String getDeleteQuery() {
        String query = itemQuerys.Item.Delete;
        return getReplacedString(query);
    }

    @Override
    public boolean canDelete() {
        return false;
    }

    protected String getReplacedString(String querytemplate){
        String query = super.getReplacedString(querytemplate);
        query = query.replace("#mol", mol).
                replace("#city", city).
                replace("#address", address).
                replace("#phone", phone).
                replace("#fax", fax).
                replace("#email", email).
                replace("#taxno", taxno).
                replace("#vatno", vatno).
                replace("#pricegroup", Integer.toString(pricegroup.value())).
                replace("#type", Integer.toString(type.value())).
                replace("#discount", Double.toString(discount)).
                replace("#userid", Integer.toString(userid)).
                replace("#userrealtime", userrealtime).
                replace("#cardnumber", cardnumber).
                replace("#paymentdays", Integer.toString(paymentdays));
        return query;
    }
    //endregion

    //region public methods
    public static iPartner getByID(String id) {
        String query = itemQuerys.Item.SelectByID.replace("#id", id);
        DataRow row = iSQL.getDataRow(query);
        return new iPartner(row);
    }
    //endregion

    //region private methods
    private SmartActivity startNEPActivity(Activity activity, int gid, iPartner partner) {
        NEPartner nePartner = new NEPartner();
        if (partner != null) {
            nePartner.putExtra(NEPartner.PUTTER_ITEM, partner);
        } else {
            String maxCode = iSQL.getScalar(itemQuerys.Item.NextMaxCode);
            nePartner.putExtra(NEPartner.PUTTER_MAX_CODE, maxCode);
            nePartner.putExtra(NEPartner.PUTTER_GROUP_ID, gid);
        }
        nePartner.show(activity);
        return nePartner;
    }

    private void Fill(DataRow row) {
        SQLTablesPartners sqlTables = iSQL.getSQLTables().getPartners();
        super.Fill(row, sqlTables);

        mol = row.getString(sqlTables.getMol());
        mol2 = row.getString(sqlTables.getMol2());
        city = row.getString(sqlTables.getCity());
        city2 = row.getString(sqlTables.getCity2());
        address = row.getString(sqlTables.getAddress());
        address2 = row.getString(sqlTables.getAddress2());
        phone = row.getString(sqlTables.getPhone());
        phone2 = row.getString(sqlTables.getPhone2());
        fax = row.getString(sqlTables.getFax());
        email = row.getString(sqlTables.getEmail());
        taxno = row.getString(sqlTables.getTaxno());
        vatno = row.getString(sqlTables.getVatno());
        pricegroup = PriceGroups.fromValue(row.getInt(sqlTables.getPricegroup()));
        type = PartnerType.fromValue(row.getInt(sqlTables.getType()));
        discount = row.getDouble(sqlTables.getDiscount());
        userid = row.getInt(sqlTables.getUserid());
        userrealtime = row.getString(sqlTables.getUserrealtime());
        cardnumber = row.getString(sqlTables.getCardnumber());
        paymentdays = row.getInt(sqlTables.getPaymentdays());
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

    public PriceGroups getPricegroup() {
        return pricegroup;
    }

    public void setPricegroup(PriceGroups pricegroup) {
        this.pricegroup = pricegroup;
    }

    public PartnerType getType() {
        return type;
    }

    public void setType(PartnerType type) {
        this.type = type;
    }

    public double getDiscount() {
        return discount;
    }

    public void setDiscount(double discount) {
        this.discount = discount;
    }

    public int getUserid() {
        return userid;
    }

    public void setUserid(int userid) {
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

    public int getPaymentdays() {
        return paymentdays;
    }

    public void setPaymentdays(int paymentdays) {
        this.paymentdays = paymentdays;
    }
    //endregion
}

