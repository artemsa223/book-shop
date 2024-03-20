package com.example.demo.repository;

import com.example.demo.model.Order;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface OrderRepository extends JpaRepository<Order, Long> {
    @Query("SELECT o FROM Order o "
            + "LEFT join fetch o.user u "
            + "LEFT join fetch o.orderItems i "
            + "WHERE u.id = :userId")
    List<Order> findAllByUserId(Long userId);
}
