package org.hiskia.simplecrudplaceorder.service.impl;

import org.hiskia.simplecrudplaceorder.entity.Order;
import org.hiskia.simplecrudplaceorder.entity.OrderDetail;
import org.hiskia.simplecrudplaceorder.entity.Cart;
import org.hiskia.simplecrudplaceorder.entity.CartDetail;
import org.hiskia.simplecrudplaceorder.repository.OrderRepository;
import org.hiskia.simplecrudplaceorder.service.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class OrderServiceImpl implements OrderService {

    @Autowired
    private OrderRepository orderRepository;

    public Order placeOrder(Cart cart) {
        Order order = new Order();
        order.setCustomer(cart.getCustomer());
        order.setStatus("PLACED");

        BigDecimal totalPrice = BigDecimal.ZERO;
        List<OrderDetail> orderDetails = new ArrayList<>();

        for (CartDetail cartDetail : cart.getCartDetails()) {
            OrderDetail orderDetail = new OrderDetail();
            orderDetail.setOrder(order);
            orderDetail.setProduct(cartDetail.getProduct());
            orderDetail.setQuantity(cartDetail.getQuantity());
            orderDetail.setPrice(cartDetail.getPrice());

            totalPrice = totalPrice.add(cartDetail.getPrice());
            orderDetails.add(orderDetail);
        }

        order.setTotalPrice(totalPrice);
        order.setOrderDetails(orderDetails);

        return orderRepository.save(order);
    }

    @Override
    public Page<Order> getAllOrders(Pageable pageable) {
        return orderRepository.findAll(pageable);
    }

    @Override
    public Optional<Order> findById(Long id) {
        return orderRepository.findById(id);
    }

    @Override
    public void deleteOrder(Long id) {
        orderRepository.deleteById(id);
    }
}
