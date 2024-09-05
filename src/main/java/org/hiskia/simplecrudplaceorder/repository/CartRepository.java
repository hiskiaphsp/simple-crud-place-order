package org.hiskia.simplecrudplaceorder.repository;

import org.hiskia.simplecrudplaceorder.entity.Cart;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CartRepository extends JpaRepository<Cart, Long> {
}
