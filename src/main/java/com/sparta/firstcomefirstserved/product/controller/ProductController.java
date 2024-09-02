package com.sparta.firstcomefirstserved.product.controller;

import com.sparta.firstcomefirstserved.product.dto.ProductInfoResponseDto;
import com.sparta.firstcomefirstserved.product.service.ProductService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api")
public class ProductController {

    private final ProductService productService;

    public ProductController(ProductService productService) {
        this.productService = productService;
    }

    @GetMapping("/product/{id}")
    public ProductInfoResponseDto getProduct(@PathVariable Long id) {
        return productService.getProduct(id);
    }

    @GetMapping("/products")
    public List<ProductInfoResponseDto> getAllProducts() {
        return productService.getAllProducts();
    }
}
