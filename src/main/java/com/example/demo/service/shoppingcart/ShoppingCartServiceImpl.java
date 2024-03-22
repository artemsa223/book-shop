package com.example.demo.service.shoppingcart;

import com.example.demo.dto.cartitem.AddItemToCartRequestDto;
import com.example.demo.dto.cartitem.UpdateCartItemById;
import com.example.demo.dto.shoppingcart.ShoppingCartDto;
import com.example.demo.exception.EntityNotFoundException;
import com.example.demo.mapper.CartItemMapper;
import com.example.demo.mapper.ShoppingCartMapper;
import com.example.demo.model.CartItem;
import com.example.demo.model.ShoppingCart;
import com.example.demo.model.User;
import com.example.demo.repository.CartItemRepository;
import com.example.demo.repository.ShoppingCartRepository;
import com.example.demo.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class ShoppingCartServiceImpl implements ShoppingCartService {
    private final ShoppingCartRepository shoppingCartRepository;
    private final CartItemRepository cartItemRepository;
    private final UserRepository userRepository;
    private final CartItemMapper cartItemMapper;
    private final ShoppingCartMapper cartMapper;

    @Override
    public ShoppingCart getShoppingCartByUserId(Long id) {
        return getCartByUserId(id);
    }

    @Override
    public ShoppingCartDto addBooksToCartByUserId(Long userId, AddItemToCartRequestDto requestDto) {
        ShoppingCart shoppingCart = getCartByUserId(userId);
        CartItem cartItem = cartItemMapper.toModel(requestDto);
        cartItem.setShoppingCart(shoppingCart);
        cartItemRepository.save(cartItem);
        return cartMapper.toDto(getCartByUserId(userId));
    }

    @Override
    public ShoppingCartDto delete(Long userId, Long cartItemId) {
        cartItemRepository.deleteById(cartItemId);
        return cartMapper.toDto(getCartByUserId(userId));
    }

    @Override
    public ShoppingCartDto updateCartItemById(Long userId, Long cartItemId,
                                              UpdateCartItemById requestDto) {
        CartItem cartItem = cartItemRepository.findById(cartItemId).orElseThrow(() ->
                new EntityNotFoundException("Can't find cart item by id=" + cartItemId));
        cartItemMapper.update(requestDto, cartItem);
        cartItemRepository.save(cartItem);
        return cartMapper.toDto(getCartByUserId(userId));
    }

    @Override
    public void clearShoppingCart(Long cartId) {
        cartItemRepository.deleteByShoppingCartId(cartId);
    }

    private ShoppingCart getCartByUserId(Long id) {
        return shoppingCartRepository.findByUserId(id)
                .orElseGet(() -> {
                    User user = userRepository.findById(id)
                            .orElseThrow(() -> new EntityNotFoundException(
                                    "Can't find user with id:" + id));
                    ShoppingCart shoppingCart = new ShoppingCart();
                    shoppingCart.setUser(user);
                    return shoppingCartRepository.save(shoppingCart);
                });
    }

}
