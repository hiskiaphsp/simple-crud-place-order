package org.hiskia.simplecrudplaceorder.service;

import org.hiskia.simplecrudplaceorder.entity.Product;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Optional;

public interface ProductService {
    Product addProduct(Product product);

    Optional<Product> findById(Long id);

    Page<Product> getAllProducts(Pageable pageable);

    Product updateProduct(Long id, Product product);

    void deleteProduct(Long id);
}
