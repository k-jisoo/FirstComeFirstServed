package com.sparta.firstcomefirstserved.wishList.entity;

import com.sparta.firstcomefirstserved.product.entity.Product;
import com.sparta.firstcomefirstserved.user.entity.User;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Entity
@NoArgsConstructor
public class WishList {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "userId", nullable = false)
    private User user;

    @ManyToOne
    @JoinColumn(name = "productId", nullable = false)
    private Product product;

    public WishList(User user, Product product) {
        this.user = user;
        this.product = product;
    }
}
