package com.example.demo.controller;

import com.example.demo.dto.order.CreateOrderRequestDto;
import com.example.demo.dto.order.OrderDto;
import com.example.demo.dto.order.UpdateOrderStatusDto;
import com.example.demo.dto.orderitem.OrderItemDto;
import com.example.demo.model.User;
import com.example.demo.service.order.OrderService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "Order manager", description = "Endpoints for managing orders")
@RequiredArgsConstructor
@RestController
@RequestMapping("/orders")
public class OrderController {
    private final OrderService orderService;

    @Operation(summary = "Create order")
    @PreAuthorize("hasRole('ROLE_USER')")
    @PostMapping
    OrderDto placeOrder(Authentication authentication,
                        @RequestBody @Valid CreateOrderRequestDto requestDto) {
        User user = (User) authentication.getPrincipal();
        return orderService.create(user.getId(), requestDto);
    }

    @Operation(summary = "Get all orders",
            description = "Return all orders for the current user")
    @PreAuthorize("hasRole('ROLE_USER')")
    @GetMapping
    List<OrderDto> getAllOrders(Authentication authentication) {
        User user = (User) authentication.getPrincipal();
        return orderService.findAll(user.getId());
    }

    @Operation(summary = "Get a specific order item")
    @PreAuthorize("hasRole('ROLE_USER')")
    @GetMapping("{orderId}/items/{itemId}")
    OrderItemDto getOrderItem(Authentication authentication,
                              @PathVariable Long itemId,
                              @PathVariable Long orderId) {
        User user = (User) authentication.getPrincipal();
        return orderService.findOrderItemById(user.getId(), itemId, orderId);
    }

    @Operation(summary = "Get all items for a specific order")
    @PreAuthorize("hasRole('ROLE_USER')")
    @GetMapping("/{orderId}/items")
    List<OrderItemDto> getAllOrderItems(Authentication authentication,
                                        @PathVariable Long orderId) {
        User user = (User) authentication.getPrincipal();
        return orderService.findAllOrderItemsByOrderId(user.getId(), orderId);
    }

    @Operation(summary = "Update order status")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @PatchMapping("/{id}")
    OrderDto updateOrderStatus(@RequestBody @Valid UpdateOrderStatusDto requestDto,
                               @PathVariable Long id) {
        return orderService.update(requestDto, id);
    }
}
