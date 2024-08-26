package com.sparta.firstcomefirstserved.userProduct.controller;

import com.sparta.firstcomefirstserved.userProduct.dto.BuyListResponseDto;
import com.sparta.firstcomefirstserved.userProduct.dto.WishListResponseDto;
import com.sparta.firstcomefirstserved.userProduct.service.UserProductService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/userProduct")
public class UserProductController {
    private final UserProductService userProductService;

    public UserProductController(UserProductService userProductService) {
        this.userProductService = userProductService;
    }

    @GetMapping("/wishLists")
    public List<WishListResponseDto> getWishList() {
        return userProductService.getWishList();
    }

    @PostMapping("/wishList/register")
    public ResponseEntity<String> register(@RequestParam Long userId, @RequestParam Long productId) {
        return userProductService.register(userId, productId);
    }

    @GetMapping("/buyLists")
    public List<BuyListResponseDto> getBuyList() {
        return userProductService.getBuyList();
    }
}
