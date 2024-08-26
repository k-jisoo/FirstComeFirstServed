package com.sparta.firstcomefirstserved.product.dto;

import lombok.Getter;

import java.util.List;

@Getter
public class UserProductRequestDto {
    private Long userId;
    private List<Long> productIds;
}
