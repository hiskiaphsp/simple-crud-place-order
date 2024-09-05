package org.hiskia.simplecrudplaceorder.repository;

import org.hiskia.simplecrudplaceorder.entity.CartDetail;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CartDetailRepository extends JpaRepository<CartDetail, Long> {
}
