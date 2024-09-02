package com.sparta.firstcomefirstserved.userProduct.dto;

import lombok.Getter;

import java.util.List;

@Getter
public class CancelRequestDto {
    private Long userId;
    private Long productId;
}
