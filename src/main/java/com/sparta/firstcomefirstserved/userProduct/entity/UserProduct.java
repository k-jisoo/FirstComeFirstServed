package com.sparta.firstcomefirstserved.userProduct.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.Duration;
import java.time.LocalDateTime;

@Getter
@Entity
@NoArgsConstructor
public class UserProduct {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "userId", nullable = false)
    private Long userId;

    @Column(name = "productId", nullable = false)
    private Long productId;

    @Column(name = "productStatus", nullable = false)
    private ProductStatus productStatus;

    @Column(name = "purchaseDate")
    private LocalDateTime purchaseDate = null;

    @Column(name = "deliveryStatus")
    private DeliveryStatus deliveryStatus;

    @Column(name = "deliveredDate")
    private LocalDateTime deliveredDate;


    public UserProduct(Long userId, Long productId) {
        this.userId = userId;
        this.productId = productId;
        this.productStatus = ProductStatus.WISH;
        this.deliveryStatus = DeliveryStatus.NONE;
    }

    public void setProductStatus(ProductStatus productStatus) {
        if(productStatus == ProductStatus.BUY) {
            purchaseDate = LocalDateTime.now();
        }
        else{
            purchaseDate = null;
        }

        this.productStatus = productStatus;
    }

    public void setDeliveryStatus(DeliveryStatus deliveryStatus) {
        if(deliveryStatus == DeliveryStatus.DONE) {
            deliveredDate = LocalDateTime.now();
        }

        this.deliveryStatus = deliveryStatus;
    }

    public boolean isCanRefund(){
        LocalDateTime now = LocalDateTime.now();

        Duration duration = Duration.between(deliveredDate, now);

        return duration.toHours() >= 24;
    }
}