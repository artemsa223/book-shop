package com.example.demo.controller;

import com.example.demo.dto.cartitem.AddItemToCartRequestDto;
import com.example.demo.dto.cartitem.UpdateCartItemById;
import com.example.demo.dto.shoppingcart.ShoppingCartDto;
import com.example.demo.model.User;
import com.example.demo.service.shoppingcart.ShoppingCartService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "Shopping cart management",
        description = "The Shopping Cart API")
@RestController
@RequestMapping("/cart")
@RequiredArgsConstructor
public class ShoppingCartController {
    private final ShoppingCartService shoppingCartService;

    @Operation(summary = "Get shopping cart",
            description = "Show user`s shopping cart")
    @PreAuthorize("hasRole('ROLE_USER')")
    @GetMapping
    ShoppingCartDto getCart(Authentication authentication) {
        User user = (User) authentication.getPrincipal();
        return shoppingCartService.getShoppingCartDtoByUserId(user.getId());
    }

    @Operation(summary = "Add books to shopping cart")
    @PreAuthorize("hasRole('ROLE_USER')")
    @PostMapping
    ShoppingCartDto addBooks(Authentication authentication,
                             @RequestBody @Valid AddItemToCartRequestDto requestDto) {
        User user = (User) authentication.getPrincipal();
        return shoppingCartService.addBooksToCartByUserId(user.getId(), requestDto);
    }

    @Operation(summary = "Update cart item")
    @PreAuthorize("hasRole('ROLE_USER')")
    @PutMapping("/cart-items/{cartItemId}")
    ShoppingCartDto updateItems(Authentication authentication,
                                @PathVariable Long cartItemId,
                                @RequestBody @Valid UpdateCartItemById requestDto) {
        User user = (User) authentication.getPrincipal();
        return shoppingCartService.updateCartItemById(user.getId(), cartItemId, requestDto);
    }

    @Operation(summary = "Delete a cart item")
    @PreAuthorize("hasRole('ROLE_USER')")
    @DeleteMapping("/cart-items/{cartItemId}")
    ShoppingCartDto deleteItems(Authentication authentication,
                                @PathVariable Long cartItemId) {
        User user = (User) authentication.getPrincipal();
        return shoppingCartService.delete(user.getId(), cartItemId);
    }
}
