package com.example.app.model;

import javax.persistence.*;
import java.math.BigDecimal;

@Entity
@Table(name = "order_items")
public class OrderItem {
    @Id
    @GeneratedValue
    private Long id;

    private String productId;

    private int quantity;

    @ManyToOne(fetch = FetchType.LAZY)
    private Order order;

    protected OrderItem() {}

    public OrderItem(String productId, int quantity) {
        this.productId = productId;
        this.quantity = quantity;
    }

    public BigDecimal subtotal(BigDecimal unitPrice) {
        return unitPrice.multiply(java.math.BigDecimal.valueOf(quantity));
    }

    public Long getId() {
        return id;
    }

    public String getProductId() {
        return productId;
    }

    public int getQuantity() {
        return quantity;
    }

    public Order getOrder() {
        return order;
    }

    void setOrder(Order order) {
        this.order = order;
    }
}