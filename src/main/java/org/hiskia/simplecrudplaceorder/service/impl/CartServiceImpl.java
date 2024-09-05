package org.hiskia.simplecrudplaceorder.service.impl;

import org.hiskia.simplecrudplaceorder.entity.Cart;
import org.hiskia.simplecrudplaceorder.repository.CartRepository;
import org.hiskia.simplecrudplaceorder.service.CartService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class CartServiceImpl implements CartService {

    @Autowired
    private CartRepository cartRepository;

    @Override
    public Cart addCart(Cart cart) {
        return cartRepository.save(cart);
    }

    @Override
    public Page<Cart> getAllCarts(Pageable pageable) {
        return cartRepository.findAll(pageable);
    }

    @Override
    public Optional<Cart> findById(Long id) {
        return cartRepository.findById(id);
    }

    @Override
    public Cart updateCart(Long id, Cart cart) {
        Optional<Cart> existingOpt = cartRepository.findById(id);
        if (existingOpt.isPresent()) {
            Cart existing = existingOpt.get();
            existing.setStatus(cart.getStatus());
            existing.setTotalPrice(cart.getTotalPrice());

            return cartRepository.save(existing);
        } else {
            throw new RuntimeException("Cart not found with id: " + id);
        }
    }

    @Override
    public void deleteCart(Long id) {
        cartRepository.deleteById(id);
    }
}
