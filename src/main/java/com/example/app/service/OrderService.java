package com.example.app.service;

import com.example.app.model.Order;
import com.example.app.repository.OrderRepository;
import com.example.app.repository.ProductRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;

@Service
public class OrderService {
    private final InventoryService inventoryService;
    private final PricingService pricingService;
    private final OrderRepository orderRepository;
    private final ProductRepository productRepository;

    public OrderService(InventoryService inventoryService,
                        PricingService pricingService,
                        OrderRepository orderRepository,
                        ProductRepository productRepository) {
        this.inventoryService = inventoryService;
        this.pricingService = pricingService;
        this.orderRepository = orderRepository;
        this.productRepository = productRepository;
    }

    @Transactional
    public String processOrder(Order order) {
        if (order == null || order.getItems().isEmpty()) {
            if (order != null) order.setStatus(Order.Status.FAILED);
            return "Order empty or null";
        }
        order.setStatus(Order.Status.PROCESSING);

        if (!inventoryService.canFulfill(order)) {
            order.setStatus(Order.Status.FAILED);
            return "Insufficient stock";
        }

        boolean reserved = inventoryService.reserve(order);
        if (!reserved) {
            order.setStatus(Order.Status.FAILED);
            return "Failed to reserve";
        }

        // build product map for pricing
        Map<String, com.example.app.model.Product> productMap = new HashMap<>();
        for (com.example.app.model.OrderItem it : order.getItems()) {
            productMap.put(it.getProductId(), productRepository.findById(it.getProductId()).get());
        }
        BigDecimal total = pricingService.calculateTotal(order, productMap);
        order.setStatus(Order.Status.COMPLETED);
        orderRepository.save(order);
        return "Processed order " + order.getId() + ", total=" + total;
    }
}