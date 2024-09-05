package org.hiskia.simplecrudplaceorder.service;

import org.springframework.data.domain.Page;
import org.hiskia.simplecrudplaceorder.entity.Cart;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Optional;

public interface CartService {
    Cart addCart(Cart cart);

    Page<Cart> getAllCarts(Pageable pageable);

    Optional<Cart> findById(Long id);

    Cart updateCart(Long id, Cart cart);

    void deleteCart(Long id);
}
