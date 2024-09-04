package com.sparta.firstcomefirstserved.userProduct.service;

import com.sparta.firstcomefirstserved.product.dto.ProductDto;
import com.sparta.firstcomefirstserved.user.UserDto;
import com.sparta.firstcomefirstserved.userProduct.dto.BuyListResponseDto;
import com.sparta.firstcomefirstserved.userProduct.dto.BuyRequestDto;
import com.sparta.firstcomefirstserved.userProduct.dto.CancelRequestDto;
import com.sparta.firstcomefirstserved.userProduct.dto.WishListResponseDto;
import com.sparta.firstcomefirstserved.userProduct.entity.DeliveryStatus;
import com.sparta.firstcomefirstserved.userProduct.entity.ProductStatus;
import com.sparta.firstcomefirstserved.userProduct.entity.UserProduct;
import com.sparta.firstcomefirstserved.userProduct.repository.UserProductRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

import java.util.List;

@Slf4j
@Service
public class UserProductService {
    private final UserProductRepository userProductRepository;
    private final WebClient webClient;

    public UserProductService(UserProductRepository userProductRepository, WebClient.Builder webClientBuilder) {
        this.userProductRepository = userProductRepository;
        this.webClient = webClientBuilder.baseUrl("http://firstcomefirstserved").build();
    }

    /**
     * wishList 전체 반환
     *
     * @return wishList 전체
     */
    public List<WishListResponseDto> getWishList() {
        return userProductRepository.findALlByProductStatus(ProductStatus.WISH).stream().map(WishListResponseDto::new).toList();
    }

    /**
     * User의 wishList에 상품 등록
     *
     * @param userId    wishList에 product를 등록하고자 하는 User id
     * @param productId wishList에 등록하고자 하는 product id
     * @return HttpStatus.OK, "Registered successful"
     */
    public ResponseEntity<String> register(Long userId, Long productId) {
        UserDto regUser = getUserById(userId);
        ProductDto regProduct = getProductById(productId);

        if (regUser == null || regProduct == null)
            return new ResponseEntity<>("Invalid user or product", HttpStatus.BAD_REQUEST);

        UserProduct userProduct = new UserProduct(userId, productId);
        userProductRepository.save(userProduct);

        return ResponseEntity.ok("Registered successful");
    }

    /**
     * 구매한 물품 리스트 API
     *
     * @return ProductStatus가 BUY인 product
     */
    public List<BuyListResponseDto> getBuyList() {
        return userProductRepository.findALlByProductStatus(ProductStatus.BUY)
                .stream().map(BuyListResponseDto::new)
                .toList();
    }

    /**
     * 구매할 productId를 받아서 productStatus를 WISH->BUY로 변경
     *
     * @param buyRequestDto userId, productId
     * @return ResponseEntity
     */
    public ResponseEntity<String> buyProduct(BuyRequestDto buyRequestDto) {
        UserDto user = getUserById(buyRequestDto.getUserId());
        ProductDto product = getProductById(buyRequestDto.getProductId());

        if (user == null || product == null)
            return new ResponseEntity<>("Invalid user or product", HttpStatus.BAD_REQUEST);

        UserProduct userProduct = userProductRepository.findByUserIdAndProductId(buyRequestDto.getUserId(), buyRequestDto.getProductId()).orElse(null);
        if (userProduct != null) {
            userProduct.setProductStatus(ProductStatus.BUY);
            return new ResponseEntity<>("BuyRequestDto is not exists in wishlist", HttpStatus.BAD_REQUEST);
        }

        return ResponseEntity.ok("Buy successful");
    }

    /**
     * 주문 상품에 대한 취소
     *
     * @param cancelRequestDto userId, productId
     * @return ResponseEntity
     */
    public ResponseEntity<String> cancelBuying(CancelRequestDto cancelRequestDto) {
        UserDto user = getUserById(cancelRequestDto.getUserId());
        ProductDto product = getProductById(cancelRequestDto.getProductId());

        if (user == null || product == null)
            return new ResponseEntity<>("Invalid user or product", HttpStatus.BAD_REQUEST);

        userProductRepository.findByUserIdAndProductId(cancelRequestDto.getUserId(), cancelRequestDto.getProductId()).ifPresent(userProduct -> {
            if(userProduct.getDeliveryStatus() == DeliveryStatus.PREPARING) {
                userProduct.setProductStatus(ProductStatus.WISH);
            }
        });

        return ResponseEntity.ok("Cancel buying successful");
    }

    /**
     * 배송된 상품에 대한 환불 요청
     *
     * @param cancelRequestDto userId, productId
     * @return ResponseEntity
     */
    public ResponseEntity<String> refund(CancelRequestDto cancelRequestDto) {
        UserDto user = getUserById(cancelRequestDto.getUserId());
        ProductDto product = getProductById(cancelRequestDto.getProductId());

        if (user == null || product == null)
            return new ResponseEntity<>("Invalid user or product", HttpStatus.BAD_REQUEST);

        userProductRepository.findByUserIdAndProductId(cancelRequestDto.getUserId(), cancelRequestDto.getProductId()).ifPresent(userProduct -> {
            if(userProduct.getDeliveryStatus() == DeliveryStatus.DONE && userProduct.isCanRefund()) {
                userProduct.setProductStatus(ProductStatus.REFUND);
            }
        });

        return ResponseEntity.ok("Refund successful");
    }

    /**
     * UserService 서버에 접속해 User 정보 return
     *
     * @param userId
     * @return UserDto
     */
    public UserDto getUserById(Long userId) {
        return this.webClient.get()
                .uri("/api/user/{id}", userId)
                .retrieve()
                .bodyToMono(UserDto.class)
                .block();
    }

    /**
     * ProductService 서버에 접속해 Product 정보 return
     *
     * @param productId
     * @return ProductDto
     */
    public ProductDto getProductById(Long productId) {
        return this.webClient.get()
                .uri("/api/product/{productId}", productId)
                .retrieve()
                .bodyToMono(ProductDto.class)
                .block();
    }
}
