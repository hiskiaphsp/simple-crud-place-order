package org.hiskia.simplecrudplaceorder.controller;

import org.springframework.data.domain.Page;
import org.hiskia.simplecrudplaceorder.dto.ApiResponse;
import org.hiskia.simplecrudplaceorder.dto.PaginatedResponse;
import org.hiskia.simplecrudplaceorder.entity.Cart;
import org.hiskia.simplecrudplaceorder.entity.CartDetail;
import org.hiskia.simplecrudplaceorder.entity.Customer;
import org.hiskia.simplecrudplaceorder.entity.Product;
import org.hiskia.simplecrudplaceorder.service.CartService;
import org.hiskia.simplecrudplaceorder.service.CustomerService;
import org.hiskia.simplecrudplaceorder.service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/carts")
public class CartController {

    @Autowired
    private CartService cartService;

    @Autowired
    private CustomerService customerService;

    @Autowired
    private ProductService productService;

    @PostMapping
    public ResponseEntity<ApiResponse<Cart>> createCart(@RequestBody Cart cart) {
        try {
            Customer customer = cart.getCustomer();
            customer = customerService.addCustomer(customer);
            cart.setCustomer(customer);

            List<CartDetail> cartDetails = cart.getCartDetails();
            if (cartDetails == null || cartDetails.isEmpty()) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                        .body(ApiResponse.error(
                                HttpStatus.BAD_REQUEST.value(),
                                HttpStatus.BAD_REQUEST.getReasonPhrase(),
                                "CartDetails must not be empty."
                        ));
            }

            BigDecimal totalPrice = BigDecimal.ZERO;

            for (CartDetail detail : cartDetails) {
                if (detail.getProduct() == null || detail.getProduct().getId() == null) {
                    return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                            .body(ApiResponse.error(
                                    HttpStatus.BAD_REQUEST.value(),
                                    HttpStatus.BAD_REQUEST.getReasonPhrase(),
                                    "Product ID is required for each CartDetail."
                            ));
                }

                if (detail.getQuantity() == null || detail.getQuantity() <= 0) {
                    return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                            .body(ApiResponse.error(
                                    HttpStatus.BAD_REQUEST.value(),
                                    HttpStatus.BAD_REQUEST.getReasonPhrase(),
                                    "Quantity is required and must be greater than 0 for each CartDetail."
                            ));
                }

                Long productId = detail.getProduct().getId();
                Optional<Product> productOpt = productService.findById(productId);
                if (productOpt.isPresent()) {
                    Product product = productOpt.get();
                    detail.setProduct(product);
                    detail.setCart(cart);

                    BigDecimal itemTotal = product.getPrice().multiply(BigDecimal.valueOf(detail.getQuantity()));
                    detail.setPrice(itemTotal);
                    totalPrice = totalPrice.add(itemTotal); // Tambahkan itemTotal ke totalPrice
                } else {
                    return ResponseEntity.status(HttpStatus.NOT_FOUND)
                            .body(ApiResponse.error(
                                    HttpStatus.NOT_FOUND.value(),
                                    HttpStatus.NOT_FOUND.getReasonPhrase(),
                                    "Product not found with ID: " + productId));
                }
            }

            cart.setTotalPrice(totalPrice);
            cart.setStatus("PENDING");

            Cart savedCart = cartService.addCart(cart);

            return new ResponseEntity<>(
                    ApiResponse.success(HttpStatus.CREATED.value(), HttpStatus.CREATED.getReasonPhrase(), "Cart created successfully.", savedCart),
                    HttpStatus.CREATED
            );
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(ApiResponse.error(
                            HttpStatus.INTERNAL_SERVER_ERROR.value(),
                            HttpStatus.INTERNAL_SERVER_ERROR.getReasonPhrase(),
                            "Failed to create cart: " + e.getMessage()));
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<Cart>> getCartById(@PathVariable Long id) {
        return cartService.findById(id)
                .map(cart -> ResponseEntity.ok(
                        ApiResponse.success(HttpStatus.OK.value(), HttpStatus.OK.getReasonPhrase(), "Cart retrieved successfully.", cart)
                ))
                .orElseGet(() -> ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .body(ApiResponse.error(HttpStatus.NOT_FOUND.value(), HttpStatus.NOT_FOUND.getReasonPhrase(), "Cart not found.")));
    }

    @PostMapping("/{id}/place")
    public ResponseEntity<ApiResponse<Cart>> placeOrder(@PathVariable Long id) {
        Optional<Cart> cartOpt = cartService.findById(id);
        if (cartOpt.isPresent()) {
            Cart cart = cartOpt.get();
            cart.setStatus("PLACED");
            Cart updatedCart = cartService.updateCart(id, cart);
            return ResponseEntity.ok(
                    ApiResponse.success(HttpStatus.OK.value(), HttpStatus.OK.getReasonPhrase(), "Order placed successfully.", updatedCart)
            );
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(ApiResponse.error(HttpStatus.NOT_FOUND.value(), HttpStatus.NOT_FOUND.getReasonPhrase(), "Cart not found."));
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse<Cart>> updateCart(
            @PathVariable Long id,
            @RequestBody Cart cartDetailsUpdate) {
        try {
            Optional<Cart> existingCartOpt = cartService.findById(id);
            if (existingCartOpt.isPresent()) {
                Cart existingCart = existingCartOpt.get();

                Customer updatedCustomer = cartDetailsUpdate.getCustomer();
                if (updatedCustomer != null) {
                    if (updatedCustomer.getId() == null || !customerService.findById(updatedCustomer.getId()).isPresent()) {
                        updatedCustomer = customerService.addCustomer(updatedCustomer);
                    }
                    existingCart.setCustomer(updatedCustomer);
                }

                List<CartDetail> updatedDetails = cartDetailsUpdate.getCartDetails();
                List<CartDetail> existingDetails = existingCart.getCartDetails();

                existingDetails.removeIf(detail ->
                        updatedDetails.stream().noneMatch(updatedDetail -> updatedDetail.getId() != null && updatedDetail.getId().equals(detail.getId()))
                );

                BigDecimal newTotalPrice = BigDecimal.ZERO;
                for (CartDetail detail : updatedDetails) {
                    Long productId = detail.getProduct().getId();
                    Optional<Product> productOpt = productService.findById(productId);
                    if (productOpt.isPresent()) {
                        Product product = productOpt.get();
                        detail.setProduct(product);
                        detail.setCart(existingCart);
                        BigDecimal itemTotal = product.getPrice().multiply(BigDecimal.valueOf(detail.getQuantity()));
                        detail.setPrice(itemTotal);
                        newTotalPrice = newTotalPrice.add(itemTotal);

                        if (detail.getId() == null) {
                            existingDetails.add(detail);
                        }
                    } else {
                        return ResponseEntity.status(HttpStatus.NOT_FOUND)
                                .body(ApiResponse.error(
                                        HttpStatus.NOT_FOUND.value(),
                                        HttpStatus.NOT_FOUND.getReasonPhrase(),
                                        "Product not found with ID: " + productId));
                    }
                }

                existingCart.setTotalPrice(newTotalPrice);
                existingCart.setStatus(cartDetailsUpdate.getStatus());

                Cart updatedCart = cartService.updateCart(id, existingCart);

                return ResponseEntity.ok(
                        ApiResponse.success(HttpStatus.OK.value(), HttpStatus.OK.getReasonPhrase(), "Cart updated successfully.", updatedCart)
                );
            } else {
                return ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .body(ApiResponse.error(HttpStatus.NOT_FOUND.value(), HttpStatus.NOT_FOUND.getReasonPhrase(), "Cart not found."));
            }
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(ApiResponse.error(
                            HttpStatus.INTERNAL_SERVER_ERROR.value(),
                            HttpStatus.INTERNAL_SERVER_ERROR.getReasonPhrase(),
                            "Failed to update cart: " + e.getMessage()));
        }
    }

    @GetMapping
    public ResponseEntity<ApiResponse<PaginatedResponse<Cart>>> getAllCarts(Pageable pageable) {
        Page<Cart> cartsPage = cartService.getAllCarts(pageable);

        PaginatedResponse<Cart> response = new PaginatedResponse<>(
                cartsPage.getContent(),
                cartsPage.getNumber(),
                cartsPage.getSize(),
                cartsPage.getTotalElements(),
                cartsPage.getTotalPages()
        );

        return ResponseEntity.ok(
                ApiResponse.success(
                        HttpStatus.OK.value(),
                        HttpStatus.OK.getReasonPhrase(),
                        "Carts retrieved successfully.",
                        response
                )
        );
    }


    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<Void>> deleteCart(@PathVariable Long id) {
        Optional<Cart> cartOpt = cartService.findById(id);
        if (cartOpt.isPresent()) {
            cartService.deleteCart(id);
            return ResponseEntity.ok(
                    ApiResponse.success(HttpStatus.OK.value(), HttpStatus.OK.getReasonPhrase(), "Cart deleted successfully.")
            );
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(ApiResponse.error(HttpStatus.NOT_FOUND.value(), HttpStatus.NOT_FOUND.getReasonPhrase(), "Cart not found."));
        }
    }

}
