package com.sparta.firstcomefirstserved.product.dto;

import lombok.Getter;

@Getter
public class ProductDto {
    private Long id;
    private String productName;
    private String description;
    private String imageUrl;
    private int price;
    private int stock;
}
