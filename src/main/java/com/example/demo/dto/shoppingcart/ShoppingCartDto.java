package com.example.demo.dto.shoppingcart;

import com.example.demo.dto.cartitem.CartItemDto;
import java.util.Set;

public record ShoppingCartDto(
        Long id,
        Long userId,
        Set<CartItemDto> cartItems
) {
}
