package com.luminous.pdi.addproductLight.dto;

public class Items_quanitySHow {

    private String invoiceno, availableqty, pdiqty;

    public Items_quanitySHow() {
    }

    public Items_quanitySHow(String invoiceno, String availableqty, String pdiqty) {
        this.invoiceno = invoiceno;
        this.availableqty = availableqty;
        this.pdiqty = pdiqty;
    }

    public String getInvoiceno() {
        return invoiceno;
    }

    public void setInvoiceno(String invoiceno) {
        this.invoiceno = invoiceno;
    }

    public String getAvailableqty() {
        return availableqty;
    }

    public void setAvailableqty(String availableqty) {
        this.availableqty = availableqty;
    }

    public String getPdiqty() {
        return pdiqty;
    }

    public void setPdiqty(String pdiqty) {
        this.pdiqty = pdiqty;
    }
}
