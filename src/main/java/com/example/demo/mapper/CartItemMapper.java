package com.example.demo.mapper;

import com.example.demo.config.MapperConfig;
import com.example.demo.dto.cartitem.AddItemToCartRequestDto;
import com.example.demo.dto.cartitem.CartItemDto;
import com.example.demo.dto.cartitem.UpdateCartItemById;
import com.example.demo.model.CartItem;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

@Mapper(config = MapperConfig.class, uses = BookMapper.class)
public interface CartItemMapper {
    @Mapping(target = "bookId", source = "book.id")
    @Mapping(target = "bookTitle", source = "book.title")
    CartItemDto toDto(CartItem cartItem);

    @Mapping(target = "book", source = "bookId", qualifiedByName = "bookFromId")
    CartItem toModel(AddItemToCartRequestDto requestDto);

    @Mapping(target = "book", ignore = true)
    void update(UpdateCartItemById requestDto, @MappingTarget CartItem itemFromDb);
}
