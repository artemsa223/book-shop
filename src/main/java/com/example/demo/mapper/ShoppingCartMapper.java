package com.example.demo.mapper;

import com.example.demo.config.MapperConfig;
import com.example.demo.dto.shoppingcart.ShoppingCartDto;
import com.example.demo.model.ShoppingCart;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Mappings;

@Mapper(config = MapperConfig.class, uses = CartItemMapper.class)
public interface ShoppingCartMapper {
    @Mappings({
            @Mapping(source = "user.id", target = "userId"),
            @Mapping(source = "cartItems", target = "cartItems")
    })
    ShoppingCartDto toDto(ShoppingCart shoppingCart);

    @Mappings({
            @Mapping(source = "userId", target = "user.id"),
            @Mapping(source = "cartItems", target = "cartItems")
    })
    ShoppingCart toModel(ShoppingCartDto shoppingCartResponseDto);
}
