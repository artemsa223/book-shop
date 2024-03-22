package com.example.demo.service.order;

import com.example.demo.dto.order.CreateOrderRequestDto;
import com.example.demo.dto.order.OrderDto;
import com.example.demo.dto.order.UpdateOrderStatusDto;
import com.example.demo.dto.orderitem.OrderItemDto;
import java.util.List;

public interface OrderService {
    OrderDto create(Long userId, CreateOrderRequestDto requestDto);

    List<OrderDto> findAll(Long userId);

    List<OrderItemDto> findAllOrderItemsByOrderId(Long userId, Long orderId);

    OrderItemDto findOrderItemById(Long userId, Long itemId, Long orderId);

    OrderDto update(UpdateOrderStatusDto requestDto, Long orderId);
}
