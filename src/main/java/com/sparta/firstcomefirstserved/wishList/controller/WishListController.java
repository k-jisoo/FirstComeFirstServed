package com.sparta.firstcomefirstserved.wishList.controller;

import com.sparta.firstcomefirstserved.wishList.dto.WishListResponseDto;
import com.sparta.firstcomefirstserved.wishList.service.WishListService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/wishList")
public class WishListController {
    private final WishListService wishListService;

    public WishListController(WishListService wishListService) {
        this.wishListService = wishListService;
    }

    @GetMapping("/")
    public List<WishListResponseDto> getWishList() {
        return wishListService.getWishList();
    }

    @PostMapping("/register")
    public ResponseEntity<String> register(@RequestParam Long userId, @RequestParam Long productId) {
        return wishListService.register(userId, productId);
    }
}
