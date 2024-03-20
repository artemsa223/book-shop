package com.example.demo.repository;

import com.example.demo.model.OrderItem;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface OrderItemRepository extends JpaRepository<OrderItem, Long> {
    @Query("SELECT item FROM OrderItem item "
            + "LEFT JOIN FETCH item.order order "
            + " WHERE item.order.id = :orderId AND order.user.id = :userId")
    List<OrderItem> findAllByOrderIdAndUserId(Long orderId, Long userId);
}
