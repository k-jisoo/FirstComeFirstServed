package com.sparta.firstcomefirstserved.product.dto;

import com.sparta.firstcomefirstserved.product.entity.Product;
import jakarta.persistence.Column;
import lombok.Getter;

@Getter
public class ProductInfoResponseDto {
    private Long id;
    private String productName;
    private String description;
    private String imageUrl;
    private int price;
    private int stock;

    public ProductInfoResponseDto(Product product) {
        this.id = product.getId();
        this.productName = product.getProductName();
        this.description = product.getDescription();
        this.imageUrl = product.getImageUrl();
        this.price = product.getPrice();
        this.stock = product.getStock();
    }
}
