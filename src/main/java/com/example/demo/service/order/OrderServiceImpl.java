package com.example.demo.service.order;

import com.example.demo.dto.order.CreateOrderRequestDto;
import com.example.demo.dto.order.OrderDto;
import com.example.demo.dto.order.UpdateOrderStatusDto;
import com.example.demo.dto.orderitem.OrderItemDto;
import com.example.demo.exception.EntityNotFoundException;
import com.example.demo.mapper.OrderItemMapper;
import com.example.demo.mapper.OrderMapper;
import com.example.demo.model.Order;
import com.example.demo.model.OrderItem;
import com.example.demo.model.ShoppingCart;
import com.example.demo.model.Status;
import com.example.demo.repository.OrderItemRepository;
import com.example.demo.repository.OrderRepository;
import com.example.demo.service.shoppingcart.ShoppingCartService;
import com.example.demo.service.user.UserService;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class OrderServiceImpl implements OrderService {
    private final OrderRepository orderRepository;
    private final OrderMapper orderMapper;
    private final ShoppingCartService shoppingCartService;
    private final OrderItemRepository orderItemRepository;
    private final UserService userService;
    private final OrderItemMapper orderItemMapper;

    @Transactional
    @Override
    public OrderDto create(Long userId, CreateOrderRequestDto requestDto) {
        ShoppingCart shoppingCart = shoppingCartService.getShoppingCartByUserId(userId);
        if (shoppingCart.getCartItems().isEmpty()) {
            throw new IllegalStateException("Empty shopping cart!");
        }
        Order order = createOrder(userId, requestDto);
        order = orderRepository.save(order);
        Set<OrderItem> orderItems = createOrderItemsSet(shoppingCart, order);
        order.setOrderItems(orderItems);
        order.setTotal(order.getOrderItems().stream()
                .map(OrderItem::getPrice)
                .reduce(BigDecimal.ZERO, BigDecimal::add));
        shoppingCartService.clearShoppingCart(shoppingCart.getId());
        orderRepository.save(order);
        return orderMapper.toDto(order);
    }

    @Override
    public List<OrderDto> findAll(Long userId) {
        return orderRepository.findAllByUserId(userId).stream()
                .map(orderMapper::toDto)
                .toList();
    }

    @Override
    public List<OrderItemDto> findAllOrderItemsByOrderId(Long userId, Long orderId) {
        List<OrderItem> orderItems = orderItemRepository.findAllByOrderIdAndUserId(orderId, userId);
        return orderItems.stream()
                .map(orderItemMapper::toDto)
                .toList();
    }

    @Override
    public OrderItemDto findOrderItemById(Long userId, Long itemId, Long orderId) {
        List<OrderItem> orderItems = orderItemRepository.findAllByOrderIdAndUserId(orderId, userId);
        OrderItem requiredItem = orderItems.stream()
                .filter(orderItem -> orderItem.getId().equals(itemId))
                .findFirst()
                .orElseThrow(() -> new EntityNotFoundException("Can't find order"
                        + " with orderId: " + orderId));
        return orderItemMapper.toDto(requiredItem);
    }

    @Override
    public OrderDto update(UpdateOrderStatusDto requestDto, Long orderId) {
        Order order = orderRepository.findById(orderId)
                .orElseThrow(
                        () -> new EntityNotFoundException("Can't find order by id=" + orderId));
        order.setStatus(requestDto.status());
        orderRepository.save(order);
        return orderMapper.toDto(order);
    }

    private Set<OrderItem> createOrderItemsSet(ShoppingCart shoppingCart, Order order) {
        return shoppingCart.getCartItems().stream()
                .map(cartItem -> {
                    OrderItem orderItem = orderItemMapper.toOrderItemModel(cartItem);
                    orderItem.setOrder(order);
                    return orderItem;
                })
                .peek(orderItemRepository::save)
                .collect(Collectors.toSet());
    }

    private Order createOrder(Long userId, CreateOrderRequestDto requestDto) {
        Order order = new Order();
        order.setUser(userService.getById(userId));
        order.setStatus(Status.PENDING);
        order.setOrderDate(LocalDateTime.now());
        order.setShippingAddress(requestDto.shippingAddress());
        order.setTotal(BigDecimal.ZERO);
        return order;
    }
}
