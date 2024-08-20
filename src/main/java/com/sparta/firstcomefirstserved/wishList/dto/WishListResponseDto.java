package com.sparta.firstcomefirstserved.wishList.dto;

import com.sparta.firstcomefirstserved.wishList.entity.WishList;
import lombok.Getter;

@Getter
public class WishListResponseDto {
    private Long id;
    private Long userId;
    private Long productId;

    public WishListResponseDto(WishList wishList) {
        this.id = wishList.getId();
        this.userId = wishList.getUser().getId();
        this.productId = wishList.getProduct().getId();
    }
}
