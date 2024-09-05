package org.hiskia.simplecrudplaceorder.repository;

import org.hiskia.simplecrudplaceorder.entity.Order;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OrderRepository extends JpaRepository<Order, Long> {
}
