package org.hiskia.simplecrudplaceorder.service;

import org.hiskia.simplecrudplaceorder.entity.CartDetail;

import java.util.Optional;

public interface CartDetailService {
    public void addCartDetail(CartDetail cartDetail);
    public void deleteCartDetail(Long id);
    Optional <CartDetail> findById(Long id);
}
