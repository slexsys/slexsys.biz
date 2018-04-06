package com.slexsys.biz.main.comp.enums;

import java.io.Serializable;

/**
 * Created by slexsys on 1/14/17.
 */

public enum OperType implements Serializable{
    Purchase(1),
    Sale(2),
    Wast(3),
    StockTacking(4),
    Production1(5),
    Production2(6),
    Transfer(7),
    Transfer2(8),
    Point_of_Sale(9),
    Touch_Screen(10),
    WriteOut(11),
    Request(12),
    Offer(13),
    Proform_Invoice(14),
    Consign(15),
    Sales_on_consignment(16),
    Return_consignment(17),
    Purchase_on_consignment(18),
    Order(19),
    Raw_material1(20),
    Product1(21),
    Raw_material2(22),
    Product2(23),
    Complex_Production1(24),
    Complex_Production2(25),
    Debit_Note(26),
    Credit_Note(27),
    Warranty_Card(28),
    Packing_raw_material(29),
    Packing_product(30),
    Packing_give(31),
    Packing_return(32),
    Order_restaurant(33),
    Refund(34),
    Booking(35),
    Advance_payment(36),
    Supplier_Debit_Note(37),
    Supplier_Credit_Note(38),
    RefundToSupplier(39);

    private int value;

    OperType(int i) {
        value = i;
    }

    public int getValue() {
        return value;
    }

    public String getStringValue() {
        return Integer.toString(value);
    }

    public static int ConvertEnumToInt(Enum type) {
        int result = 0;
        if (type != null) {
            OperType oper = OperType.valueOf(type.name());
            result = oper.getValue();
        }
        return result;
    }

    public static String ConvertEnumToIntString(Enum type) {
        String result = null;
        if (type != null) {
            int oper = ConvertEnumToInt(type);
            result = Integer.toString(oper);
        }
        return result;
    }

    public static OperType ConvertEnumToOperType(Enum type) {
        return OperType.valueOf(type.name());
    }

    public static String getLetter(int value) {
        String result = null;
        switch (OperType.fromValue(value)) {
            case Purchase:
                result = "D";
                break;
            case Sale:
                result = "S";
                break;
            case Refund:
                result = "Rf";
                break;
            case RefundToSupplier:
                result = "Rs";
                break;
            case WriteOut:
                result = "WO";
                break;
            case Wast:
                result = "WA";
                break;
            case Transfer:
            case Transfer2:
                result = "T";
                break;
            case StockTacking:
                result = "Rev";
                break;
        }
        return result;
    }

    public static OperType fromValue(int value) {
        OperType result = null;
        for (OperType type : OperType.values()) {
            if (type.getValue() == value) {
                result = type;
                break;
            }
        }
        return result;
    }
}
