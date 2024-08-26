package com.sparta.firstcomefirstserved.userProduct.repository;

import com.sparta.firstcomefirstserved.userProduct.entity.ProductStatus;
import com.sparta.firstcomefirstserved.userProduct.entity.UserProduct;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface UserProductRepository extends JpaRepository<UserProduct, Long> {
    List<UserProduct> findALlByProductStatus(ProductStatus status);

    Optional<UserProduct> findByUserIdAndProductId(Long userId, Long productId);
}
