package com.luminous.pdi.addproductLight.Interface;

import android.view.View;

import com.luminous.pdi.addproductLight.LightRes.InvoiceQntyEntity;

public interface LightInvoiceClick {

    void onItemInvoiceListCLicked(InvoiceQntyEntity inoiceListData, int Position, String InvoiceNumberValue, String AvaialbleQuantity,
                                  String SoldQuantity);



}
