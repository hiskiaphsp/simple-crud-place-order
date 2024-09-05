package org.hiskia.simplecrudplaceorder.controller;

import org.hiskia.simplecrudplaceorder.dto.ApiResponse;
import org.hiskia.simplecrudplaceorder.dto.PaginatedResponse;
import org.hiskia.simplecrudplaceorder.entity.Cart;
import org.hiskia.simplecrudplaceorder.entity.Order;
import org.hiskia.simplecrudplaceorder.service.CartService;
import org.hiskia.simplecrudplaceorder.service.CustomerService;
import org.hiskia.simplecrudplaceorder.service.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RestController
@RequestMapping("/api/orders")
public class OrderController {

    @Autowired
    private OrderService orderService;

    @Autowired
    private CartService cartService;

    @PostMapping("/{id}/place")
    public ResponseEntity<ApiResponse<Order>> placeOrder(@PathVariable Long id) {
        Optional<Cart> cartOpt = cartService.findById(id);
        if (cartOpt.isPresent()) {
            Cart cart = cartOpt.get();

            Order order = orderService.placeOrder(cart);

            cartService.deleteCart(cart.getId());

            return ResponseEntity.ok(
                    ApiResponse.success(HttpStatus.OK.value(), HttpStatus.OK.getReasonPhrase(), "Order placed successfully.", order)
            );
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(ApiResponse.error(HttpStatus.NOT_FOUND.value(), HttpStatus.NOT_FOUND.getReasonPhrase(), "Cart not found."));
        }
    }

    @GetMapping
    public ResponseEntity<ApiResponse<PaginatedResponse<Order>>> getAllOrders(Pageable pageable) {
        Page<Order> ordersPage = orderService.getAllOrders(pageable);

        PaginatedResponse<Order> response = new PaginatedResponse<>(
                ordersPage.getContent(),
                ordersPage.getNumber(),
                ordersPage.getSize(),
                ordersPage.getTotalElements(),
                ordersPage.getTotalPages()
        );

        return ResponseEntity.ok(
                ApiResponse.success(
                        HttpStatus.OK.value(),
                        HttpStatus.OK.getReasonPhrase(),
                        "Orders retrieved successfully.",
                        response
                )
        );
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<Order>> getOrderById(@PathVariable Long id) {
        return orderService.findById(id)
                .map(cart -> ResponseEntity.ok(
                        ApiResponse.success(HttpStatus.OK.value(), HttpStatus.OK.getReasonPhrase(), "Order retrieved successfully.", cart)
                ))
                .orElseGet(() -> ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .body(ApiResponse.error(HttpStatus.NOT_FOUND.value(), HttpStatus.NOT_FOUND.getReasonPhrase(), "Order not found.")));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<Void>> deleteOrder(@PathVariable Long id) {
        Optional<Order> cartOpt = orderService.findById(id);
        if (cartOpt.isPresent()) {
            orderService.deleteOrder(id);
            return ResponseEntity.ok(
                    ApiResponse.success(HttpStatus.OK.value(), HttpStatus.OK.getReasonPhrase(), "Order deleted successfully.")
            );
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(ApiResponse.error(HttpStatus.NOT_FOUND.value(), HttpStatus.NOT_FOUND.getReasonPhrase(), "Order not found."));
        }
    }

}
