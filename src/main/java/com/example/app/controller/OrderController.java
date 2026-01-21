package com.example.app.controller;

import com.example.app.model.Order;
import com.example.app.model.OrderItem;
import com.example.app.repository.OrderRepository;
import com.example.app.service.OrderService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;

@RestController
@RequestMapping("/api/orders")
public class OrderController {
    private final OrderService orderService;
    private final OrderRepository orderRepository;

    public OrderController(OrderService orderService, OrderRepository orderRepository) {
        this.orderService = orderService;
        this.orderRepository = orderRepository;
    }

    @PostMapping
    public ResponseEntity<?> createOrder(@RequestBody OrderRequest req) {
        Order order = new Order(req.getOrderId());
        for (OrderRequest.Item it : req.getItems()) {
            order.addItem(new OrderItem(it.getProductId(), it.getQuantity()));
        }
        String result = orderService.processOrder(order);
        return ResponseEntity.ok(result);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Order> get(@PathVariable String id) {
        return orderRepository.findById(id).map(ResponseEntity::ok).orElse(ResponseEntity.notFound().build());
    }

    public static class OrderRequest {
        private String orderId;
        private java.util.List<Item> items = new ArrayList<>();

        public static class Item {
            private String productId;
            private int quantity;
            // getters/setters
            public String getProductId() { return productId; }
            public void setProductId(String productId) { this.productId = productId; }
            public int getQuantity() { return quantity; }
            public void setQuantity(int quantity) { this.quantity = quantity; }
        }

        // getters/setters
        public String getOrderId() { return orderId; }
        public void setOrderId(String orderId) { this.orderId = orderId; }
        public java.util.List<Item> getItems() { return items; }
        public void setItems(java.util.List<Item> items) { this.items = items; }
    }
}