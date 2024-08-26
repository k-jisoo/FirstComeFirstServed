package com.sparta.firstcomefirstserved.userProduct.dto;

import com.sparta.firstcomefirstserved.userProduct.entity.UserProduct;
import lombok.Getter;

@Getter
public class WishListResponseDto {
    private Long id;
    private Long userId;
    private Long productId;



    public WishListResponseDto(UserProduct userProduct) {
        this.id = userProduct.getId();
        this.userId = userProduct.getUser().getId();
        this.productId = userProduct.getProduct().getId();
    }
}
