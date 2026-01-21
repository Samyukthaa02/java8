package com.example.app.service;

import com.example.app.model.Product;
import com.example.app.repository.ProductRepository;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.Optional;

@Service
public class InventoryService {
    private final ProductRepository productRepository;

    public InventoryService(ProductRepository productRepository) {
        this.productRepository = productRepository;
    }

    public Optional<Product> findProduct(String productId) {
        return productRepository.findById(productId);
    }

    @Transactional
    public boolean canFulfill(com.example.app.model.Order order) {
        for (com.example.app.model.OrderItem item : order.getItems()) {
            Product p = productRepository.findById(item.getProductId()).orElse(null);
            if (p == null || p.getStock() < item.getQuantity()) {
                return false;
            }
        }
        return true;
    }

    @Transactional
    public boolean reserve(com.example.app.model.Order order) {
        if (!canFulfill(order)) {
            return false;
        }
        for (com.example.app.model.OrderItem item : order.getItems()) {
            Product p = productRepository.findById(item.getProductId()).get();
            p.setStock(p.getStock() - item.getQuantity());
            productRepository.save(p);
        }
        return true;
    }
}