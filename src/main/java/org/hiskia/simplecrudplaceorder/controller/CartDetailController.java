package org.hiskia.simplecrudplaceorder.controller;

import org.hiskia.simplecrudplaceorder.dto.ApiResponse;
import org.hiskia.simplecrudplaceorder.entity.Cart;
import org.hiskia.simplecrudplaceorder.entity.CartDetail;
import org.hiskia.simplecrudplaceorder.service.CartDetailService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RestController
@RequestMapping("api/cart-detail")
public class CartDetailController {

    @Autowired
    private CartDetailService cartDetailService;

    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<Void>> deleteCart(@PathVariable Long id) {
        Optional<CartDetail> cartOpt = cartDetailService.findById(id);
        if (cartOpt.isPresent()) {
            cartDetailService.deleteCartDetail(id);
            return ResponseEntity.ok(
                    ApiResponse.success(HttpStatus.OK.value(), HttpStatus.OK.getReasonPhrase(), "Cart deleted successfully.")
            );
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(ApiResponse.error(HttpStatus.NOT_FOUND.value(), HttpStatus.NOT_FOUND.getReasonPhrase(), "Cart not found."));
        }
    }
}
