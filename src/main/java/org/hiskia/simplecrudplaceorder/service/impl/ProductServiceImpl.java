package org.hiskia.simplecrudplaceorder.service.impl;

import org.hiskia.simplecrudplaceorder.entity.Product;
import org.hiskia.simplecrudplaceorder.repository.ProductRepository;
import org.hiskia.simplecrudplaceorder.service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class ProductServiceImpl implements ProductService {

    @Autowired
    private ProductRepository productRepository;

    @Override
    public Product addProduct(Product product) {
        return productRepository.save(product);
    }

    @Override
    public Optional<Product> findById(Long id) {
        return productRepository.findById(id);
    }

    @Override
    public Page<Product> getAllProducts(Pageable pageable) {
        return productRepository.findAll(pageable);
    }

    @Override
    public Product updateProduct(Long id, Product product) {
        Optional<Product> existingOpt = productRepository.findById(id);
        if (existingOpt.isPresent()) {
            Product existing = existingOpt.get();
            existing.setName(product.getName());
            existing.setDescription(product.getDescription());
            existing.setType(product.getType());
            existing.setPrice(product.getPrice());
            return productRepository.save(existing);
        } else {
            throw new RuntimeException("Product not found with id: " + id);
        }
    }

    @Override
    public void deleteProduct(Long id) {
        productRepository.deleteById(id);
    }
}
