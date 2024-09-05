package org.hiskia.simplecrudplaceorder.service.impl;

import org.hiskia.simplecrudplaceorder.entity.CartDetail;
import org.hiskia.simplecrudplaceorder.repository.CartDetailRepository;
import org.hiskia.simplecrudplaceorder.repository.CartRepository;
import org.hiskia.simplecrudplaceorder.service.CartDetailService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class CartDetailServiceImpl implements CartDetailService {

    @Autowired
    private CartDetailRepository cartDetailRepository;

    @Override
    public void addCartDetail(CartDetail cartDetail) {
        cartDetailRepository.save(cartDetail);
    }

    @Override
    public Optional<CartDetail> findById(Long id) {
        return cartDetailRepository.findById(id);
    }

    @Override
    public void deleteCartDetail(Long id) {
        cartDetailRepository.deleteById(id);
    }

}
