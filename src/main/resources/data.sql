-- =====================
-- USERS
-- =====================
INSERT INTO users (id, email, password, role) VALUES
(1, 'admin@example.com', '$2a$10$7Xyf0x0uq1V6/lb0ldVQLeRiJpL6eQf8YtJ1n5fOZsH8QPMcWb8Ti', 'ADMIN'),
(2, 'user@example.com', '$2a$10$7Xyf0x0uq1V6/lb0ldVQLeRiJpL6eQf8YtJ1n5fOZsH8QPMcWb8Ti', 'USER');
-- Password for both: password123

-- =====================
-- CATEGORIES
-- =====================
INSERT INTO category (id, name) VALUES
(1, 'Electronics'),
(2, 'Books');

-- =====================
-- PRODUCTS
-- =====================
INSERT INTO product (id, name, description, brand, category_id, price, release_date, available, quantity) VALUES
(1, 'Laptop', 'High-end gaming laptop', 'BrandX', 1, 1200.0, '2025-01-01', TRUE, 10),
(2, 'Headphones', 'Noise-cancelling headphones', 'BrandY', 1, 250.0, '2025-02-01', TRUE, 20),
(3, 'Novel', 'Best-selling fiction novel', 'AuthorZ', 2, 15.0, '2024-12-15', TRUE, 50);

-- =====================
-- ORDERS
-- =====================
INSERT INTO orders (id, created_at, total_price, user_id) VALUES
(1, CURRENT_TIMESTAMP, 1450.0, 2);

-- =====================
-- ORDER ITEMS
-- =====================
INSERT INTO order_item (id, product_id, quantity, order_id) VALUES
(1, 1, 1, 1),
(2, 2, 1, 1);
