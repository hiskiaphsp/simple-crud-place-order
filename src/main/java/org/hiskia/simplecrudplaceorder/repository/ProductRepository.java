package org.hiskia.simplecrudplaceorder.repository;

import org.hiskia.simplecrudplaceorder.entity.Product;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProductRepository extends JpaRepository<Product, Long> {
}
