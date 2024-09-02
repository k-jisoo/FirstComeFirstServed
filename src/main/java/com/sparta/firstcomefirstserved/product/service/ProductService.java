package com.sparta.firstcomefirstserved.product.service;

import com.sparta.firstcomefirstserved.product.dto.ProductInfoResponseDto;
import com.sparta.firstcomefirstserved.product.entity.Product;
import com.sparta.firstcomefirstserved.product.repository.productRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

@Slf4j
@Service
public class ProductService {
    private final com.sparta.firstcomefirstserved.product.repository.productRepository productRepository;

    public ProductService(productRepository productRepository) {
        this.productRepository = productRepository;
    }

    /**
     * 상품 상세 정보
     * @param id productId
     * @return ProductInfoResponseDto
     */
    public ProductInfoResponseDto getProduct(Long id) {
        Product product = productRepository.findById(id).orElse(null);
        if (product == null) {
            log.error("Product not found");
            return null;
        }

        log.error("Product found");

        return new ProductInfoResponseDto(product);
    }

    /**
     * bIsDesplayed가 true인 전체 Product list로 반환
     * @return bIsDesplayed가 true인 전체 Product list
     */
    public List<ProductInfoResponseDto> getAllProducts() {
        return productRepository.findAllByDisplayedTrue().stream().map(ProductInfoResponseDto::new).toList();
    }
}
