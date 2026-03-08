package com.drip.store.controller;

import com.drip.store.model.Product;
import com.drip.store.repository.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/products")
public class ProductController {

    @Autowired
    private ProductRepository productRepository;

    // ── GET /api/products ──────────────────────────────────────────────
    // Supports ?category=men and ?search=hoodie
    @GetMapping
    public List<Product> getAllProducts(
            @RequestParam(required = false) String category,
            @RequestParam(required = false) String search
    ) {
        if (search != null && !search.isBlank()) {
            return productRepository
                    .findByNameContainingIgnoreCaseOrBrandContainingIgnoreCase(search, search);
        }
        if (category != null && !category.equals("all")) {
            return productRepository.findByCategory(category);
        }
        return productRepository.findAll();
    }

    // ── GET /api/products/{id} ─────────────────────────────────────────
    @GetMapping("/{id}")
    public ResponseEntity<Product> getProductById(@PathVariable Long id) {
        Optional<Product> product = productRepository.findById(id);
        return product.map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    // ── POST /api/products ─────────────────────────────────────────────
    // Admin: add a new product
    // Body: { "name": "...", "price": 2500, "category": "Women", "stock": 20, "emoji": "👗" }
    @PostMapping
    public ResponseEntity<Product> createProduct(@RequestBody Product product) {
        Product saved = productRepository.save(product);
        return ResponseEntity.status(HttpStatus.CREATED).body(saved);
    }

    // ── PUT /api/products/{id} ─────────────────────────────────────────
    // Admin: update an existing product
    @PutMapping("/{id}")
    public ResponseEntity<Product> updateProduct(
            @PathVariable Long id,
            @RequestBody Product updated
    ) {
        return productRepository.findById(id).map(existing -> {
            if (updated.getName()     != null) existing.setName(updated.getName());
            if (updated.getCategory() != null) existing.setCategory(updated.getCategory());
            if (updated.getBrand()    != null) existing.setBrand(updated.getBrand());
            if (updated.getBadge()    != null) existing.setBadge(updated.getBadge());
            if (updated.getPrice()    != 0)    existing.setPrice(updated.getPrice());
            if (updated.getOldPrice() != null) existing.setOldPrice(updated.getOldPrice());
            // Update stock if provided
            productRepository.save(existing);
            return ResponseEntity.ok(existing);
        }).orElse(ResponseEntity.notFound().build());
    }

    // ── DELETE /api/products/{id} ──────────────────────────────────────
    // Admin: delete a product
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteProduct(@PathVariable Long id) {
        if (!productRepository.existsById(id)) {
            return ResponseEntity.notFound().build();
        }
        productRepository.deleteById(id);
        return ResponseEntity.noContent().build();
    }
}