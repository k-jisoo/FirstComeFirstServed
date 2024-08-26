package com.sparta.firstcomefirstserved.userProduct.dto;

import com.sparta.firstcomefirstserved.product.entity.Product;
import com.sparta.firstcomefirstserved.user.entity.User;
import com.sparta.firstcomefirstserved.userProduct.entity.DeliveryStatus;
import com.sparta.firstcomefirstserved.userProduct.entity.ProductStatus;
import com.sparta.firstcomefirstserved.userProduct.entity.UserProduct;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
public class BuyListResponseDto {
    private Long id;
    private User user;
    private Product product;
    private ProductStatus productStatus;
    private LocalDateTime purchaseDate;
    private DeliveryStatus deliveryStatus;
    private LocalDateTime deliveredDate;



    public BuyListResponseDto(UserProduct userProduct) {
        this.id = userProduct.getId();
        this.user = userProduct.getUser();
        this.product = userProduct.getProduct();
        this.productStatus = userProduct.getProductStatus();
        this.purchaseDate = userProduct.getPurchaseDate();
        this.deliveryStatus = userProduct.getDeliveryStatus();
        this.deliveredDate = userProduct.getDeliveredDate();
    }
}
