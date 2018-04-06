package com.slexsys.biz.main.operations.json;

import com.slexsys.biz.main.comp.COPDRF.COPDRFColumn;
import com.slexsys.biz.main.comp.COPDRF.COPDRFColumnAccessMode;
import com.slexsys.biz.main.comp.COPDRF.COPDRFColumns;
import com.slexsys.biz.main.comp.COPDRF.COPDRFColumnName;

import java.util.Arrays;

/**
 * Created by slexsys on 2/28/17.
 */

public class OperationGenerator {
    public static OperationsJSON generateOperations() {
        OperationsJSON result = new OperationsJSON(){{
            add(new OperationJSON(1, "Purchase", QttyMode.Plus, PayMode.Pay, TotalMode.TotalIn, PriceUpdate.PriceInOut, InsertionMode.Single){{
                setColumns(new COPDRFColumns(){{
                    addAll(Arrays.asList(
                            new COPDRFColumn(COPDRFColumnName.Code, COPDRFColumnAccessMode.ReadOnly),
                            new COPDRFColumn(COPDRFColumnName.Name, COPDRFColumnAccessMode.ReadWrite),
                            new COPDRFColumn(COPDRFColumnName.Measure, COPDRFColumnAccessMode.ReadOnly),
                            new COPDRFColumn(COPDRFColumnName.Qtty, COPDRFColumnAccessMode.ReadWrite),
                            new COPDRFColumn(COPDRFColumnName.RealQtty, COPDRFColumnAccessMode.ReadWrite),
                            new COPDRFColumn(COPDRFColumnName.PriceIn, COPDRFColumnAccessMode.ReadWrite),
                            new COPDRFColumn(COPDRFColumnName.PercentUp, COPDRFColumnAccessMode.ReadWrite),
                            new COPDRFColumn(COPDRFColumnName.PriceOut, COPDRFColumnAccessMode.ReadWrite),
                            new COPDRFColumn(COPDRFColumnName.Discount, COPDRFColumnAccessMode.ReadWrite),
                            new COPDRFColumn(COPDRFColumnName.DiscountedPriceIn, COPDRFColumnAccessMode.ReadWrite),
                            new COPDRFColumn(COPDRFColumnName.Currency, COPDRFColumnAccessMode.ReadOnly),
                            new COPDRFColumn(COPDRFColumnName.AmountIn, COPDRFColumnAccessMode.ReadOnly),
                            new COPDRFColumn(COPDRFColumnName.DiscountedAmountIn, COPDRFColumnAccessMode.ReadOnly),
                            new COPDRFColumn(COPDRFColumnName.AmountOut, COPDRFColumnAccessMode.ReadOnly)
                    ));
                }});
            }});
            add(new OperationJSON(2, "Sale", QttyMode.Minus, PayMode.Pay, TotalMode.TotalOut, PriceUpdate.None, InsertionMode.Single){{
                setColumns(new COPDRFColumns(){{
                    addAll(Arrays.asList(
                            new COPDRFColumn(COPDRFColumnName.Code, COPDRFColumnAccessMode.ReadOnly),
                            new COPDRFColumn(COPDRFColumnName.Name, COPDRFColumnAccessMode.ReadWrite),
                            new COPDRFColumn(COPDRFColumnName.Measure, COPDRFColumnAccessMode.ReadOnly),
                            new COPDRFColumn(COPDRFColumnName.Qtty, COPDRFColumnAccessMode.ReadWrite),
                            new COPDRFColumn(COPDRFColumnName.RealQtty, COPDRFColumnAccessMode.ReadWrite),
                            new COPDRFColumn(COPDRFColumnName.PriceOut, COPDRFColumnAccessMode.ReadWrite),
                            new COPDRFColumn(COPDRFColumnName.DiscountedPriceOut, COPDRFColumnAccessMode.ReadWrite),
                            new COPDRFColumn(COPDRFColumnName.Discount, COPDRFColumnAccessMode.ReadWrite),
                            new COPDRFColumn(COPDRFColumnName.Currency, COPDRFColumnAccessMode.ReadOnly),
                            new COPDRFColumn(COPDRFColumnName.DiscountedAmountOut, COPDRFColumnAccessMode.ReadOnly),
                            new COPDRFColumn(COPDRFColumnName.AmountOut, COPDRFColumnAccessMode.ReadOnly)
                    ));
                }});
            }});
            add(new OperationJSON(11, "WriteOut", QttyMode.Minus, PayMode.NoPay, TotalMode.TotalIn, PriceUpdate.None, InsertionMode.Single){{
                setColumns(new COPDRFColumns(){{
                    addAll(Arrays.asList(
                            new COPDRFColumn(COPDRFColumnName.Code, COPDRFColumnAccessMode.ReadOnly),
                            new COPDRFColumn(COPDRFColumnName.Name, COPDRFColumnAccessMode.ReadWrite),
                            new COPDRFColumn(COPDRFColumnName.Measure, COPDRFColumnAccessMode.ReadOnly),
                            new COPDRFColumn(COPDRFColumnName.Qtty, COPDRFColumnAccessMode.ReadWrite),
                            new COPDRFColumn(COPDRFColumnName.RealQtty, COPDRFColumnAccessMode.ReadWrite),
                            new COPDRFColumn(COPDRFColumnName.PriceIn, COPDRFColumnAccessMode.ReadOnly),
                            new COPDRFColumn(COPDRFColumnName.Currency, COPDRFColumnAccessMode.ReadOnly),
                            new COPDRFColumn(COPDRFColumnName.AmountIn, COPDRFColumnAccessMode.ReadOnly)
                    ));
                }});
            }});
            add(new OperationJSON(34, "Refund", QttyMode.Plus, PayMode.Pay, TotalMode.TotalOut, PriceUpdate.None, InsertionMode.Single){{
                setColumns(new COPDRFColumns(){{
                    addAll(Arrays.asList(
                            new COPDRFColumn(COPDRFColumnName.Code, COPDRFColumnAccessMode.ReadOnly),
                            new COPDRFColumn(COPDRFColumnName.Name, COPDRFColumnAccessMode.ReadWrite),
                            new COPDRFColumn(COPDRFColumnName.Measure, COPDRFColumnAccessMode.ReadOnly),
                            new COPDRFColumn(COPDRFColumnName.Qtty, COPDRFColumnAccessMode.ReadWrite),
                            new COPDRFColumn(COPDRFColumnName.RealQtty, COPDRFColumnAccessMode.ReadWrite),
                            new COPDRFColumn(COPDRFColumnName.PriceOut, COPDRFColumnAccessMode.ReadWrite),
                            new COPDRFColumn(COPDRFColumnName.Currency, COPDRFColumnAccessMode.ReadOnly),
                            new COPDRFColumn(COPDRFColumnName.AmountOut, COPDRFColumnAccessMode.ReadOnly)
                    ));
                }});
            }});
            add(new OperationJSON(39, "RefundToSupplier", QttyMode.Minus, PayMode.Pay, TotalMode.TotalIn, PriceUpdate.None, InsertionMode.Single){{
                setColumns(new COPDRFColumns(){{
                    addAll(Arrays.asList(
                            new COPDRFColumn(COPDRFColumnName.Code, COPDRFColumnAccessMode.ReadOnly),
                            new COPDRFColumn(COPDRFColumnName.Name, COPDRFColumnAccessMode.ReadWrite),
                            new COPDRFColumn(COPDRFColumnName.Measure, COPDRFColumnAccessMode.ReadOnly),
                            new COPDRFColumn(COPDRFColumnName.Qtty, COPDRFColumnAccessMode.ReadWrite),
                            new COPDRFColumn(COPDRFColumnName.RealQtty, COPDRFColumnAccessMode.ReadWrite),
                            new COPDRFColumn(COPDRFColumnName.PriceIn, COPDRFColumnAccessMode.ReadWrite),
                            new COPDRFColumn(COPDRFColumnName.Currency, COPDRFColumnAccessMode.ReadOnly),
                            new COPDRFColumn(COPDRFColumnName.AmountIn, COPDRFColumnAccessMode.ReadOnly)
                    ));
                }});
            }});
            add(new OperationJSON(3, "Wast", QttyMode.Minus, PayMode.NoPay, TotalMode.TotalIn, PriceUpdate.None, InsertionMode.Single){{
                setColumns(new COPDRFColumns(){{
                    addAll(Arrays.asList(
                            new COPDRFColumn(COPDRFColumnName.Code, COPDRFColumnAccessMode.ReadOnly),
                            new COPDRFColumn(COPDRFColumnName.Name, COPDRFColumnAccessMode.ReadWrite),
                            new COPDRFColumn(COPDRFColumnName.Measure, COPDRFColumnAccessMode.ReadOnly),
                            new COPDRFColumn(COPDRFColumnName.Qtty, COPDRFColumnAccessMode.ReadWrite),
                            new COPDRFColumn(COPDRFColumnName.RealQtty, COPDRFColumnAccessMode.ReadWrite),
                            new COPDRFColumn(COPDRFColumnName.PriceIn, COPDRFColumnAccessMode.ReadWrite),
                            new COPDRFColumn(COPDRFColumnName.Currency, COPDRFColumnAccessMode.ReadOnly),
                            new COPDRFColumn(COPDRFColumnName.AmountIn, COPDRFColumnAccessMode.ReadOnly)
                    ));
                }});
            }});
            add(new OperationJSON(7, 8, "Transfer", QttyMode.Minus, PayMode.NoPay, TotalMode.TotalIn, PriceUpdate.None, InsertionMode.Transfer){{
                setColumns(new COPDRFColumns(){{
                    addAll(Arrays.asList(
                            new COPDRFColumn(COPDRFColumnName.Code, COPDRFColumnAccessMode.ReadOnly),
                            new COPDRFColumn(COPDRFColumnName.Name, COPDRFColumnAccessMode.ReadWrite),
                            new COPDRFColumn(COPDRFColumnName.Measure, COPDRFColumnAccessMode.ReadOnly),
                            new COPDRFColumn(COPDRFColumnName.Qtty, COPDRFColumnAccessMode.ReadWrite),
                            new COPDRFColumn(COPDRFColumnName.RealQtty, COPDRFColumnAccessMode.ReadWrite),
                            new COPDRFColumn(COPDRFColumnName.PriceIn, COPDRFColumnAccessMode.ReadWrite),
                            new COPDRFColumn(COPDRFColumnName.Currency, COPDRFColumnAccessMode.ReadOnly),
                            new COPDRFColumn(COPDRFColumnName.AmountIn, COPDRFColumnAccessMode.ReadOnly)
                    ));
                }});
            }});
            add(new OperationJSON(4, "StockTacking", QttyMode.Equals, PayMode.Pay, TotalMode.TotalOut, PriceUpdate.None, InsertionMode.StockTacking){{
                setColumns(new COPDRFColumns(){{
                    addAll(Arrays.asList(
                            new COPDRFColumn(COPDRFColumnName.Code, COPDRFColumnAccessMode.ReadOnly),
                            new COPDRFColumn(COPDRFColumnName.Name, COPDRFColumnAccessMode.ReadWrite),
                            new COPDRFColumn(COPDRFColumnName.Measure, COPDRFColumnAccessMode.ReadOnly),
                            new COPDRFColumn(COPDRFColumnName.Qtty, COPDRFColumnAccessMode.ReadWrite),
                            new COPDRFColumn(COPDRFColumnName.RealQtty, COPDRFColumnAccessMode.ReadWrite),
                            new COPDRFColumn(COPDRFColumnName.PriceIn, COPDRFColumnAccessMode.ReadWrite),
                            new COPDRFColumn(COPDRFColumnName.PriceOut, COPDRFColumnAccessMode.ReadWrite),
                            new COPDRFColumn(COPDRFColumnName.Currency, COPDRFColumnAccessMode.ReadOnly),
                            new COPDRFColumn(COPDRFColumnName.AmountIn, COPDRFColumnAccessMode.ReadOnly),
                            new COPDRFColumn(COPDRFColumnName.AmountOut, COPDRFColumnAccessMode.ReadOnly)
                    ));
                }});
            }});
        }};
        return result;
    }
}
