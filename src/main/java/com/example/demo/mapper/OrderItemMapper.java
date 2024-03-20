package com.example.demo.mapper;

import com.example.demo.config.MapperConfig;
import com.example.demo.dto.orderitem.OrderItemDto;
import com.example.demo.model.CartItem;
import com.example.demo.model.OrderItem;
import java.math.BigDecimal;
import org.mapstruct.AfterMapping;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.Named;

@Mapper(config = MapperConfig.class)
public interface OrderItemMapper {
    @Mapping(target = "bookId", source = "book.id")
    @Mapping(target = "quantity", source = "quantity")
    @Named("orderItemsToDto")
    OrderItemDto toDto(OrderItem orderItem);

    @Mapping(target = "order", ignore = true)
    OrderItem toOrderItemModel(CartItem cartItem);

    @AfterMapping
    default void getPriceOfOrderItem(@MappingTarget OrderItem orderItem, CartItem cartItem) {
        orderItem.setPrice(cartItem.getBook().getPrice().multiply(
                BigDecimal.valueOf(cartItem.getQuantity())));
    }
}
