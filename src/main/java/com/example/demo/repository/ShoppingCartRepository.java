package com.example.demo.repository;

import com.example.demo.model.ShoppingCart;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface ShoppingCartRepository extends JpaRepository<ShoppingCart, Long> {
    @Query("SELECT c FROM ShoppingCart c "
            + "LEFT JOIN FETCH c.user u "
            + "LEFT JOIN FETCH c.cartItems i "
            + "LEFT JOIN FETCH i.book "
            + "WHERE :userId = u.id ")
    Optional<ShoppingCart> findByUserId(Long userId);
}
