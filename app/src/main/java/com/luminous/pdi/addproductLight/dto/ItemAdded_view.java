package com.luminous.pdi.addproductLight.dto;

public class ItemAdded_view {

    private String fault, qty, batch,mfg,wty,Accepted,rejected;

    public ItemAdded_view() {

    }

    public ItemAdded_view(String fault, String qty, String batch, String mfg, String wty, String accepted, String rejected) {
        this.fault = fault;
        this.qty = qty;
        this.batch = batch;
        this.mfg = mfg;
        this.wty = wty;
        Accepted = accepted;
        this.rejected = rejected;
    }

    public String getFault() {

        return fault;

    }

    public void setFault(String fault) {
        this.fault = fault;
    }

    public String getQty() {
        return qty;
    }

    public void setQty(String qty) {
        this.qty = qty;
    }

    public String getBatch() {
        return batch;
    }

    public void setBatch(String batch) {
        this.batch = batch;
    }

    public String getMfg() {
        return mfg;
    }

    public void setMfg(String mfg) {
        this.mfg = mfg;
    }

    public String getWty() {
        return wty;
    }

    public void setWty(String wty) {
        this.wty = wty;
    }

    public String getAccepted() {
        return Accepted;
    }

    public void setAccepted(String accepted) {
        Accepted = accepted;
    }

    public String getRejected() {
        return rejected;
    }

    public void setRejected(String rejected) {
        this.rejected = rejected;
    }
}
