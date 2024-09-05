package org.hiskia.simplecrudplaceorder.service;

import org.hiskia.simplecrudplaceorder.entity.Cart;
import org.hiskia.simplecrudplaceorder.entity.Order;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Optional;

public interface OrderService {
    Order placeOrder(Cart cart);
    Page<Order> getAllOrders(Pageable pageable);
    Optional<Order> findById(Long id);
    void deleteOrder(Long id);
}
