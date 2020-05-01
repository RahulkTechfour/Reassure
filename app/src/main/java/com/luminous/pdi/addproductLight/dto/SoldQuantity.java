package com.luminous.pdi.addproductLight.dto;

public class SoldQuantity {
    public SoldQuantity(String soldValue) {
        this.soldQuantity = soldValue;

    }

    public String getSoldQuantity() {
        return soldQuantity;
    }

    public void setSoldQuantity(String soldQuantity) {
        this.soldQuantity = soldQuantity;
    }

    private String soldQuantity;



}
