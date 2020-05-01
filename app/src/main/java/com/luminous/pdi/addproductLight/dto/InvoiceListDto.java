package com.luminous.pdi.addproductLight.dto;

public class InvoiceListDto {

    private String InvoiceNo_;

    public String getInvoiceNo_() {
        return InvoiceNo_;
    }

    public InvoiceListDto(String invoiceNo_, String availableQuantity_, String PDIQnty_) {
        InvoiceNo_ = invoiceNo_;
        this.availableQuantity_ = availableQuantity_;
        this.PDIQnty_ = PDIQnty_;
    }

    public void setInvoiceNo_(String invoiceNo_) {
        InvoiceNo_ = invoiceNo_;
    }

    public String getAvailableQuantity_() {
        return availableQuantity_;
    }

    public void setAvailableQuantity_(String availableQuantity_) {
        this.availableQuantity_ = availableQuantity_;
    }

    public String getPDIQnty_() {
        return PDIQnty_;
    }

    public void setPDIQnty_(String PDIQnty_) {
        this.PDIQnty_ = PDIQnty_;
    }

    private String availableQuantity_;
    private String PDIQnty_;
}
