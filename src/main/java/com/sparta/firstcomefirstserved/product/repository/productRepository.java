package com.sparta.firstcomefirstserved.product.repository;

import com.sparta.firstcomefirstserved.product.entity.Product;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface productRepository extends JpaRepository<Product, Long> {
    List<Product> findAllByDisplayedTrue();
}
