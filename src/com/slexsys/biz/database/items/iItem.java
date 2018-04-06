package com.slexsys.biz.database.items;

import android.app.Activity;
import com.slexsys.biz.GO;
import com.slexsys.biz.comp.LoginInfo;
import com.slexsys.biz.database.comp.Convert;
import com.slexsys.biz.database.comp.comp.ItemType;
import com.slexsys.biz.database.comp.iIBase;
import com.slexsys.biz.database.sqls.SQLTables.SQLTablesItems;
import com.slexsys.biz.database.sqls.SQLQuerys;
import com.slexsys.biz.database.sqls.comp.QueryItemGroup;
import com.slexsys.biz.database.sqls.iSQL;
import com.slexsys.biz.main.comp.NewTypes.DataRow;
import com.slexsys.biz.main.comp.NewTypes.DataTable;
import com.slexsys.biz.main.comp.NewTypes.SmartActivity;
import com.slexsys.biz.main.operations.comp.OperItem;
import com.slexsys.biz.main.operations.comp.OperationData;
import com.slexsys.biz.main.edit.newedit.NEItem;
import com.slexsys.biz.main.operations.json.PriceUpdate;
import com.slexsys.biz.main.operations.json.TotalMode;

import java.util.LinkedList;
import java.util.List;

/**
 * Created by slexsys on 3/11/16.
 */
public class iItem extends iIBase {
    //region fields
    private double qtty;
    private List<String> barcode;
    private List<String> barcode2;
    private int plu;
    private List<String> partnerid;
    private List<String> catalog;
    private int measure;
    private int measure2;
    private double ratio;
    private List<Double> pricein;
    private List<Double> priceout;
    private double minqtty;
    private double normalqtty;
    private String description;
    private Boolean isrecipe;
    private int taxgroup;
    private ItemType type;

    private static QueryItemGroup itemQuerys;
    //endregion

    //region constructors
    static {
        itemQuerys = SQLQuerys.Edit.Items;
    }

    public iItem() { }

    public iItem(DataRow row) {
        Fill(row);
    }
    //endregion

    //region override
    @Override
    public SmartActivity newItem(Activity activity, int gid) {
        return startNEIActivity(activity, gid, null);
    }

    @Override
    public SmartActivity editItem(Activity activity, int gid) {
        return startNEIActivity(activity, gid, this);
    }

    @Override
    public String[] getShowInfo() {
        String bcode = "";
        if (barcode.size() > 0 && !barcode.get(0).isEmpty()) {
            bcode = "   {" + barcode.get(0) + "}";
        }
        String m = GO.measure1.items.get(measure).getName();
        double pin = pricein.get(GO.getPig());
        double pout = priceout.get(GO.getPog());
        return new String[] {
                name,
                "code " + code + "    " + m + bcode,
                "Price In " + pin + "        Price Out " + pout,
                qtty + ""
        };
    }

    @Override
    public iIBase createObject(DataRow row) {
        return new iItem(row);
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
            result.add(new iItem(dataTable.getRowWithNames(i)));
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
        return itemQuerys.Item.Delete.replace("#id", Convert.ToString(id));
    }

    @Override
    public boolean canDelete() {
        return false;
    }

    protected String getReplacedString(String querytemplate){
        String query = super.getReplacedString(querytemplate);
        query = query.
                replace("#partnerid", Convert.StringListToString(partnerid)).
                replace("#catalog", Convert.StringListToString(catalog)).
                replace("#measure1", GO.measure1.items.get(measure).getName()).
                replace("#measure2", GO.measure1.items.get(measure2).getName()).
                replace("#ratio", Double.toString(ratio)).
                replace("#minqtty", Double.toString(minqtty)).
                replace("#normalqtty", Double.toString(normalqtty)).
                replace("#description", description).
                replace("#isrecipe", Convert.ToString(isrecipe)).
                replace("#taxgroup", Integer.toString(GO.taxgroups.items.get(taxgroup).getId())).
                replace("#type", Integer.toString(type.value()));
        query = getListReplaced(query, "pricein", pricein);
        query = getListReplaced(query, "priceout", priceout);
        if (false) {
            query = getBarcodePluReplaced1(query, barcode, barcode2);
        } else {
            query = getBarcodePluReplaced2(query, barcode);
        }
        return query;
    }
    //endregion

    //region public methods
    public static List<String> getPricesUpdateQuerys(OperationData data, String queryTempalte) {
        List<String> list = new LinkedList<String>();
        PriceUpdate priceUpdate = data.getOperJSON().getPriceUpdate();
        TotalMode totalMode = data.getOperJSON().getTotalMode();
        for (OperItem item : data.getList()) {
            int id = item.ID,
                    pg = data.getObject1().getPricegroup().value();
            double pin = item.PriceIn;
            double pout = item.PriceOut;
            switch (totalMode) {
                case TotalIn:
                    pin = item.PriceIn * (100 - item.Discount) / 100;
                    break;
                case TotalOut:
                    pout = item.PriceOut * (100 - item.Discount) / 100;
                    break;
            }
            list.add(UpdatePrices(id, pin, pout, pg, queryTempalte, priceUpdate));
        }
        return list;
    }

    public OperItem toOperItem(final double qtty) {
        final double qttyR = this.qtty;
        OperItem item = new OperItem() {{
            setID(id);
            setCode(code);
            setName(name);
            //setMeasure(GO.measure1.getItemByID(measure).getName());
            setQtty(qtty);
            setQttyR(qttyR);
            setPriceIn(pricein.get(GO.getPig()));
            setPriceOut(priceout.get(GO.getPog()));
            setCurrency(GO.currencies.getItemByID(GO.getCurrencyId()).getName());
            setCurrencyID(GO.getCurrencyId());
            setAmountIn(qtty * pricein.get(GO.getPig()));
            setAmountOut(qtty * priceout.get(GO.getPog()));
            setDescription("");
        }};
        return item;
    }

    public static iItem getByID(String id) {
        String oid = Integer.toString(LoginInfo.object.getId());
        String query = itemQuerys.Item.SelectByID
                                .replace("#id", id)
                                .replace("#oid", oid);
        DataRow row = iSQL.getDataRow(query);
        return new iItem(row);
    }
    //endregion

    //region private methods
    private void Fill(DataRow row) {
        SQLTablesItems sqlTables = iSQL.getSQLTables().getItems();
        super.Fill(row, sqlTables);

        qtty = row.getDouble(sqlTables.getQtty());
        barcode = Convert.ToSplitedText(row.getString(sqlTables.getBarcode()));
        barcode2 = Convert.ToSplitedText(row.getString(sqlTables.getBarcode2()));
        plu = row.getInt(sqlTables.getPlu());
        partnerid = Convert.ToSplitedText(row.getString(sqlTables.getPartnerid()));
        catalog = Convert.ToSplitedText(row.getString(sqlTables.getCatalog()));
        measure = row.getInt(sqlTables.getMeasure());
        measure2 = row.getInt(sqlTables.getMeasure2());
        ratio = row.getDouble(sqlTables.getRatio());
        pricein = getPriceList(row, sqlTables.getPricein());
        priceout = getPriceList(row, sqlTables.getPriceout());
        minqtty = row.getDouble(sqlTables.getMinqtty());
        normalqtty = row.getDouble(sqlTables.getNormalqtty());
        description = row.getString(sqlTables.getDescription());
        isrecipe = row.getBoolean(sqlTables.getIsrecipe());
        taxgroup = row.getInt(sqlTables.getTaxgroup());
        type = ItemType.fromValue(row.getInt(sqlTables.getType()));
    }

    private SmartActivity startNEIActivity(Activity activity, int gid, iItem item) {
        NEItem neItem = new NEItem();
        if (item != null) {
            neItem.putExtra(NEItem.PUTTER_ITEM, item);
        } else {
            String maxCode = iSQL.getScalar(itemQuerys.Item.NextMaxCode);
            neItem.putExtra(NEItem.PUTTER_MAX_CODE, maxCode);
            neItem.putExtra(NEItem.PUTTER_GROUP_ID, gid);
        }
        neItem.show(activity);
        return neItem;
    }

    private String getBarcodePluReplaced1(String query, List<String> barcode, List<String> barcode2) {
        return query.replace("#plu", Integer.toString(plu))
                .replace("#barcode2", Convert.StringListToString(barcode2))
                .replace("#barcode", Convert.StringListToString(barcode));
    }

    private String getBarcodePluReplaced2(String query, List<String> barcode) {
        if (barcode.size() > 0) {
            query = query.replace("#barcode1", barcode.get(0));
        }
        if (barcode.size() > 1) {
            query = query.replace("#barcode2", barcode.get(1));
        }
        if (barcode.size() > 2) {
            query = query.replace("#barcode3", getBarcode3(barcode));
        }
        if (plu != 0) {
            query = query.replace("#plu", Integer.toString(plu));
        } else {
            query = query.replace("#plu", "");
        }
        return query;
    }

    private String getBarcode3(List<String> barcode) {
        String result = "";
        for (int i = 2; i < barcode.size(); ++i) {
            String item = barcode.get(i);
            if (item != "") {
                result += item + ",";
            }
        }
        if (result != "") {
            result = result.substring(0, result.length() - 1);
        }
        return result;
    }

    private List<Double> getPriceList(DataRow row, String column) {
        List<Double> result = new LinkedList<Double>();
        if (row.isNameExist(column)) {
            result.add(row.getDouble(column));
        }
        for (int i = 1; i <= 10; ++i ) {
            String name = column + i;
            if (row.isNameExist(name)) {
                result.add(row.getDouble(name));
            }
        }
        return result;
    }

    private String getListReplaced(String query, String text, List<Double> list) {
        for (int i = list.size() - 1; i >= 0; --i) {
            String texti = "#" + text;
            if (i != 0) {
                texti = texti + i;
            }
            query = query.replace(texti, Convert.ToString(list.get(i)));
        }
        return query;
    }

    private static String UpdatePrices(int id, double pin, double pout, int pg, String queryTempalte, PriceUpdate priceUpdate) {
        String query;
        String pig = GO.getPig() > 0 ? Integer.toString(GO.getPig()) : "";
        String pog = GO.getPog() > 0 ? Integer.toString(GO.getPog()) : "";
        switch (priceUpdate) {
            case PriceIn:
                query = queryTempalte.replace("#id", Convert.ToString(id))
                        .replace("#pig", pig)
                        .replace("#pricein", Double.toString(pin));
                break;
            case PriceOut:
                query = queryTempalte.replace("#id", Convert.ToString(id))
                        .replace("#pog", pog)
                        .replace("#priceout", Double.toString(pout));
                break;
            case PriceInOut:
            default:
                query = queryTempalte.replace("#id", Convert.ToString(id))
                        .replace("#pig", pig)
                        .replace("#pog", pog)
                        .replace("#pricein", Double.toString(pin))
                        .replace("#priceout", Double.toString(pout));
                break;
        }
        return query;
    }
    //endregion

    //region getters setters
    public double getQtty() {
        return qtty;
    }

    public void setQtty(double qtty) {
        this.qtty = qtty;
    }

    public List<String> getBarcode() {
        return barcode;
    }

    public void setBarcode(List<String> barcode) {
        this.barcode = barcode;
    }

    public List<String> getBarcode2() {
        return barcode2;
    }

    public void setBarcode2(List<String> barcode2) {
        this.barcode2 = barcode2;
    }

    public int getPlu() {
        return plu;
    }

    public void setPlu(int plu) {
        this.plu = plu;
    }

    public List<String> getPartnerid() {
        return partnerid;
    }

    public void setPartnerid(List<String> partnerid) {
        this.partnerid = partnerid;
    }

    public List<String> getCatalog() {
        return catalog;
    }

    public void setCatalog(List<String> catalog) {
        this.catalog = catalog;
    }

    public int getMeasure() {
        return measure;
    }

    public void setMeasure(int measure) {
        this.measure = measure;
    }

    public int getMeasure2() {
        return measure2;
    }

    public void setMeasure2(int measure2) {
        this.measure2 = measure2;
    }

    public double getRatio() {
        return ratio;
    }

    public void setRatio(double ratio) {
        this.ratio = ratio;
    }

    public List<Double> getPricein() {
        return pricein;
    }

    public void setPricein(List<Double> pricein) {
        this.pricein = pricein;
    }

    public List<Double> getPriceout() {
        return priceout;
    }

    public void setPriceout(List<Double> priceout) {
        this.priceout = priceout;
    }

    public double getMinqtty() {
        return minqtty;
    }

    public void setMinqtty(double minqtty) {
        this.minqtty = minqtty;
    }

    public double getNormalqtty() {
        return normalqtty;
    }

    public void setNormalqtty(double normalqtty) {
        this.normalqtty = normalqtty;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Boolean getIsrecipe() {
        return isrecipe;
    }

    public void setIsrecipe(Boolean isrecipe) {
        this.isrecipe = isrecipe;
    }

    public int getTaxgroup() {
        return taxgroup;
    }

    public void setTaxgroup(int taxgroup) {
        this.taxgroup = taxgroup;
    }

    public ItemType getType() {
        return type;
    }

    public void setType(ItemType type) {
        this.type = type;
    }
    //endregion
}
