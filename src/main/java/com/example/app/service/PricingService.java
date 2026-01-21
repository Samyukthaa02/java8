package com.example.app.service;

import com.example.app.model.Order;
import com.example.app.model.OrderItem;
import com.example.app.model.Product;

import java.math.BigDecimal;
import java.math.RoundingMode;

import org.springframework.stereotype.Service;

@Service
public class PricingService {

    private static final BigDecimal BULK_THRESHOLD = BigDecimal.TEN;
    private static final BigDecimal BULK_RATE = new BigDecimal("0.10");
    private static final BigDecimal ORDER_THRESHOLD = new BigDecimal("1000");
    private static final BigDecimal ORDER_RATE = new BigDecimal("0.05");

    public BigDecimal calculateTotal(Order order, java.util.Map<String, Product> productMap) {
        BigDecimal total = BigDecimal.ZERO;
        for (OrderItem item : order.getItems()) {
            Product p = productMap.get(item.getProductId());
            BigDecimal itemSubtotal = p.getUnitPrice().multiply(BigDecimal.valueOf(item.getQuantity()));
            if (BigDecimal.valueOf(item.getQuantity()).compareTo(BULK_THRESHOLD) >= 0) {
                itemSubtotal = itemSubtotal.subtract(itemSubtotal.multiply(BULK_RATE));
            }
            total = total.add(itemSubtotal);
        }
        if (total.compareTo(ORDER_THRESHOLD) >= 0) {
            total = total.subtract(total.multiply(ORDER_RATE));
        }
        return total.setScale(2, RoundingMode.HALF_UP);
    }
}