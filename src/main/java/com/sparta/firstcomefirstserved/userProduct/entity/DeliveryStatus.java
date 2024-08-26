package com.sparta.firstcomefirstserved.userProduct.entity;

public enum DeliveryStatus {
    NONE("NONE"),
    PREPARING("PREPARING"),
    INTRASIT("INTRASIT"),
    DONE("DONE");


    private final String status;

    DeliveryStatus(String status) {
        this.status = status;
    }

    public String getStatus() {
        return status;
    }
}
