package org.hiskia.simplecrudplaceorder.repository;

import org.hiskia.simplecrudplaceorder.entity.Customer;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CustomerRepository extends JpaRepository<Customer, Long> {
}
