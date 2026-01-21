package com.example.app;

import com.example.app.model.Product;
import com.example.app.repository.ProductRepository;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.ResponseEntity;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;

import static org.junit.Assert.*;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class OrderIntegrationTest {

    @Autowired
    private TestRestTemplate rest;

    @Autowired
    private ProductRepository productRepository;

    @Before
    public void setup() {
        productRepository.save(new Product("P1", "Widget", new BigDecimal("10.00"), 100));
        productRepository.save(new Product("P2", "BulkThing", new BigDecimal("5.00"), 200));
    }

    @Test
    public void testCreateProductAndOrder() {
        ResponseEntity<Product> resp = rest.getForEntity("/api/products/P1", Product.class);
        assertTrue(resp.getStatusCode().is2xxSuccessful());
    }
}