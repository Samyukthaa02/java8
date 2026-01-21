```markdown
# Simple Order App - Java 8 + Spring Boot

This is a Spring Boot (2.3.x) application compiled for Java 8.

Endpoints:
- GET /api/products
- POST /api/products
- POST /api/orders

DB: in-memory H2 (configured in application.properties). Product entity contains stock.

Build:
mvn clean package

Run:
java -jar target/simple-order-app-java8-0.1.0.jar

Notes:
- Uses JPA entities (Product, Order, OrderItem).
- Inventory is persisted as `stock` on Product.
- OrderService is transactional and reserves stock during processing.
- Tests use JUnit 4.
```