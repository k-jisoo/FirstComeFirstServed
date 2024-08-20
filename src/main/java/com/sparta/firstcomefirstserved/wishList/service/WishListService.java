package com.sparta.firstcomefirstserved.wishList.service;

import com.sparta.firstcomefirstserved.product.entity.Product;
import com.sparta.firstcomefirstserved.product.repository.productRepository;
import com.sparta.firstcomefirstserved.user.entity.User;
import com.sparta.firstcomefirstserved.user.repository.UserRepository;
import com.sparta.firstcomefirstserved.wishList.dto.WishListResponseDto;
import com.sparta.firstcomefirstserved.wishList.entity.WishList;
import com.sparta.firstcomefirstserved.wishList.repository.WishListRepository;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class WishListService {
    private final WishListRepository wishListRepository;
    private final UserRepository userRepository;
    private final com.sparta.firstcomefirstserved.product.repository.productRepository productRepository;

    public WishListService(WishListRepository wishListRepository, UserRepository userRepository, productRepository productRepository) {
        this.wishListRepository = wishListRepository;
        this.userRepository = userRepository;
        this.productRepository = productRepository;
    }

    /**
     * wishList 전체 반환
     * @return wishList 전체
     */
    public List<WishListResponseDto> getWishList() {
        return wishListRepository.findAll().stream().map(WishListResponseDto::new).toList();
    }

    /**
     * User의 wishList에 상품 등록
     * @param userId wishList에 product를 등록하고자 하는 User id
     * @param productId wishList에 등록하고자 하는 product id
     * @return HttpStatus.OK, "Registered successful"
     */
    public ResponseEntity<String> register(Long userId, Long productId) {
        User regUser = userRepository.findById(userId).orElse(null);
        Product regProduct = productRepository.findById(productId).orElse(null);
        if(!regProduct.isBIsDisplayed())
            return new ResponseEntity<>("This product is not displayed", HttpStatus.BAD_REQUEST);

        WishList wishList = new WishList(regUser, regProduct);
        wishListRepository.save(wishList);

        return ResponseEntity.ok("Registered successful");
    }
}
