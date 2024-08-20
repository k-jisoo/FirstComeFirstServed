package com.sparta.firstcomefirstserved.wishList.repository;

import com.sparta.firstcomefirstserved.wishList.entity.WishList;
import org.springframework.data.jpa.repository.JpaRepository;

public interface WishListRepository extends JpaRepository<WishList, Long> {
}
