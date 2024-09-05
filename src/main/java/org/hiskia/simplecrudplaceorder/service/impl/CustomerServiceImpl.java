package org.hiskia.simplecrudplaceorder.service.impl;

import org.hiskia.simplecrudplaceorder.entity.Customer;
import org.hiskia.simplecrudplaceorder.repository.CustomerRepository;
import org.hiskia.simplecrudplaceorder.service.CustomerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class CustomerServiceImpl implements CustomerService {

    @Autowired
    private CustomerRepository customerRepository;

    @Override
    public Customer addCustomer(Customer customer) {
        return customerRepository.save(customer);
    }

    @Override
    public Optional<Customer> findById(Long id) {
        return customerRepository.findById(id);
    }
}
