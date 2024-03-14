package com.example.demo.service.shoppingcart;

import com.example.demo.dto.cartitem.AddItemToCartRequestDto;
import com.example.demo.dto.cartitem.UpdateCartItemById;
import com.example.demo.dto.shoppingcart.ShoppingCartDto;

public interface ShoppingCartService {
    ShoppingCartDto getShoppingCartByUserId(Long id);

    ShoppingCartDto addBooksToCartByUserId(Long userId, AddItemToCartRequestDto requestDto);

    ShoppingCartDto delete(Long userId, Long cartItemId);

    ShoppingCartDto updateCartItemById(
            Long userId, Long cartItemId, UpdateCartItemById requestDto);

}
