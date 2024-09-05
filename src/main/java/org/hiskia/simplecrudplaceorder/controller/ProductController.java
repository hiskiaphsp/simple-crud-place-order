package org.hiskia.simplecrudplaceorder.controller;

import org.hiskia.simplecrudplaceorder.dto.ApiResponse;
import org.hiskia.simplecrudplaceorder.dto.PaginatedResponse;
import org.hiskia.simplecrudplaceorder.entity.Product;
import org.hiskia.simplecrudplaceorder.service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/products")
public class ProductController {

    @Autowired
    private ProductService productService;

    @PostMapping
    public ResponseEntity<ApiResponse<Product>> createProduct(@RequestBody Product product) {
        Product created = productService.addProduct(product);
        return new ResponseEntity<>(
                ApiResponse.success(HttpStatus.CREATED.value(), HttpStatus.CREATED.getReasonPhrase(), "Product created successfully.", created),
                HttpStatus.CREATED
        );
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<Product>> getProductById(@PathVariable Long id) {
        return productService.findById(id)
                .map(product -> ResponseEntity.ok(
                        ApiResponse.success(HttpStatus.OK.value(), HttpStatus.OK.getReasonPhrase(), "Product retrieved successfully.", product)
                ))
                .orElseGet(() -> ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .body(ApiResponse.error(HttpStatus.NOT_FOUND.value(), HttpStatus.NOT_FOUND.getReasonPhrase(), "Product not found.")));
    }

    @GetMapping
    public ResponseEntity<ApiResponse<PaginatedResponse<Product>>> getProducts(Pageable pageable) {
        Page<Product> productsPage = productService.getAllProducts(pageable);

        PaginatedResponse<Product> response = new PaginatedResponse<>(
                productsPage.getContent(),
                productsPage.getNumber(),
                productsPage.getSize(),
                productsPage.getTotalElements(),
                productsPage.getTotalPages()
        );

        return ResponseEntity.ok(
                ApiResponse.success(
                        HttpStatus.OK.value(),
                        HttpStatus.OK.getReasonPhrase(),
                        "Products retrieved successfully.",
                        response
                )
        );
    }
    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse<Product>> updateProduct(@PathVariable Long id, @RequestBody Product product) {
        try {
            Product updated = productService.updateProduct(id, product);
            return ResponseEntity.ok(
                    ApiResponse.success(HttpStatus.OK.value(), HttpStatus.OK.getReasonPhrase(), "Product updated successfully.", updated)
            );
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(ApiResponse.error(HttpStatus.NOT_FOUND.value(), HttpStatus.NOT_FOUND.getReasonPhrase(), e.getMessage()));
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<Void>> deleteProduct(@PathVariable Long id) {
        productService.deleteProduct(id);
        return ResponseEntity.ok(
                ApiResponse.success(HttpStatus.OK.value(), HttpStatus.OK.getReasonPhrase(), "Product deleted successfully.")
        );
    }
}
