package com.sparta.firstcomefirstserved.product.dto;

import lombok.Getter;

import java.util.List;

@Getter
public class CancelRequestDto {
    private Long userId;
    private List<Long> productIds;
}
