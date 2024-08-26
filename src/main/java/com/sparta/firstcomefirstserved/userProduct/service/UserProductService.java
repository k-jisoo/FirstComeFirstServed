package com.sparta.firstcomefirstserved.userProduct.service;

import com.sparta.firstcomefirstserved.product.dto.CancelRequestDto;
import com.sparta.firstcomefirstserved.product.entity.Product;
import com.sparta.firstcomefirstserved.product.repository.productRepository;
import com.sparta.firstcomefirstserved.user.entity.User;
import com.sparta.firstcomefirstserved.user.repository.UserRepository;
import com.sparta.firstcomefirstserved.userProduct.dto.BuyListResponseDto;
import com.sparta.firstcomefirstserved.userProduct.dto.WishListResponseDto;
import com.sparta.firstcomefirstserved.userProduct.entity.DeliveryStatus;
import com.sparta.firstcomefirstserved.userProduct.entity.ProductStatus;
import com.sparta.firstcomefirstserved.userProduct.entity.UserProduct;
import com.sparta.firstcomefirstserved.userProduct.repository.UserProductRepository;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserProductService {
    private final UserProductRepository userProductRepository;
    private final UserRepository userRepository;
    private final com.sparta.firstcomefirstserved.product.repository.productRepository productRepository;

    public UserProductService(UserProductRepository userProductRepository, UserRepository userRepository, productRepository productRepository) {
        this.userProductRepository = userProductRepository;
        this.userRepository = userRepository;
        this.productRepository = productRepository;
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
        User regUser = userRepository.findById(userId).orElse(null);
        Product regProduct = productRepository.findById(productId).orElse(null);
        if(regUser == null || regProduct == null)
            return new ResponseEntity<>("Invalid user or product", HttpStatus.BAD_REQUEST);

        if (!regProduct.isDisplayed())
            return new ResponseEntity<>("This product is not displayed", HttpStatus.BAD_REQUEST);

        UserProduct userProduct = new UserProduct(regUser, regProduct);
        userProductRepository.save(userProduct);

        return ResponseEntity.ok("Registered successful");
    }

    /**
     * 구매한 물품 리스트 API
     * @return ProductStatus가 BUY인 product
     */
    public List<BuyListResponseDto> getBuyList() {
        return userProductRepository.findALlByProductStatus(ProductStatus.BUY)
                .stream().map(BuyListResponseDto::new)
                .toList();
    }

    /**
     * 구매할 productId 리스트를 받아서 productStatus를 WISH->BUY로 변경
     * @param cancelRequestDtoList userId, productId list
     * @return ResponseEntity
     */
    public ResponseEntity<String> buyProduct(CancelRequestDto cancelRequestDtoList) {
        User user = findUserById(cancelRequestDtoList.getUserId());
        List<Product> products = findProductsByIds(cancelRequestDtoList.getProductIds());

        if (user == null || products.isEmpty())
            return new ResponseEntity<>("Invalid user or product", HttpStatus.BAD_REQUEST);

        products.stream()
                .map(product -> userProductRepository.findByUserIdAndProductId(user.getId(), product.getId()).orElse(null))
                .filter(userProduct -> userProduct != null && userProduct.getProduct().isDisplayed())
                .forEach(userProduct -> userProduct.setProductStatus(ProductStatus.BUY));

        return ResponseEntity.ok("Buy successful");
    }

    /**
     * 주문 상품에 대한 취소
     * @param cancelRequestDto userId, productId list
     * @return ResponseEntity
     */
    public ResponseEntity<String> cancelBuying(CancelRequestDto cancelRequestDto) {
        User user = findUserById(cancelRequestDto.getUserId());
        List<Product> products = findProductsByIds(cancelRequestDto.getProductIds());

        if (user == null || products.isEmpty())
            return new ResponseEntity<>("Invalid user or product", HttpStatus.BAD_REQUEST);

        products.stream()
                .map(product -> userProductRepository.findByUserIdAndProductId(user.getId(), product.getId()).orElse(null))
                .filter(userProduct -> userProduct.getDeliveryStatus() == DeliveryStatus.PREPARING)
                .forEach(userProduct -> userProduct.setProductStatus(ProductStatus.WISH));

        return ResponseEntity.ok("CancelBuying successful");
    }

    /**
     *
     * @param cancelRequestDto
     * @return
     */
    public ResponseEntity<String> refund(CancelRequestDto cancelRequestDto) {
        User user = findUserById(cancelRequestDto.getUserId());
        List<Product> products = findProductsByIds(cancelRequestDto.getProductIds());

        if (user == null || products.isEmpty())
            return new ResponseEntity<>("Invalid user or product", HttpStatus.BAD_REQUEST);

        products.stream()
                .map(product -> userProductRepository.findByUserIdAndProductId(user.getId(), product.getId()).orElse(null))
                .filter(userProduct -> userProduct.getDeliveryStatus() == DeliveryStatus.DONE && userProduct.isCanRefund())
                .forEach(userProduct -> userProduct.setProductStatus(ProductStatus.REFUND));

        return ResponseEntity.ok("Refund successful");
    }

    /**
     * Helper
     * @param userId
     * @return
     */
    private User findUserById(Long userId) {
        return userRepository.findById(userId).orElse(null);
    }

    /**
     * Helper
     * @param productIds
     * @return
     */
    private List<Product> findProductsByIds(List<Long> productIds) {
        return productIds.stream()
                .map(productId -> productRepository.findById(productId).orElse(null))
                .toList();
    }
}
