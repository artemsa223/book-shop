package com.example.demo.mapper;

import com.example.demo.config.MapperConfig;
import com.example.demo.dto.order.OrderDto;
import com.example.demo.model.Order;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(config = MapperConfig.class, uses = OrderItemMapper.class)
public interface OrderMapper {
    @Mapping(target = "userId", source = "user.id")
    @Mapping(target = "orderDate", source = "orderDate")
    @Mapping(target = "total", source = "total")
    @Mapping(target = "status", source = "status")
    @Mapping(target = "orderItems", source = "orderItems",
            qualifiedByName = "orderItemsToDto")
    OrderDto toDto(Order order);

}
