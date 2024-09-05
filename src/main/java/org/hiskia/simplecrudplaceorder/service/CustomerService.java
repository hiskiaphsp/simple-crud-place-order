package org.hiskia.simplecrudplaceorder.service;

import org.hiskia.simplecrudplaceorder.entity.Customer;

import java.util.Optional;

public interface CustomerService {
    Customer addCustomer(Customer customer);

    Optional<Customer> findById(Long id);
}
