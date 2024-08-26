package com.sparta.firstcomefirstserved.userProduct.entity;

public enum ProductStatus {
    WISH("WISH"),
    BUY("BUY"),
    REFUND("REFUND");

    private final String status;

    ProductStatus(String status) {
        this.status = status;
    }

    public String getStatus() {
        return status;
    }
}