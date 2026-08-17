-- ============================================
-- STOCK TRADING SIMULATOR
-- SAMPLE DATA
-- ============================================

-- ============================================
-- USERS
-- ============================================

INSERT INTO users (username, email, password, balance)
VALUES
    ('salman', 'salman@gmail.com', '1234', 100000.00),
    ('rahul', 'rahul@gmail.com', '1234', 75000.00),
    ('aman', 'aman@gmail.com', '1234', 50000.00);


-- ============================================
-- STOCKS
-- ============================================

INSERT INTO stocks (symbol, company_name, current_price, available_quantity)
VALUES
    ('TCS', 'Tata Consultancy Services', 3500.00, 1000),
    ('INFY', 'Infosys', 1800.00, 1500),
    ('RELIANCE', 'Reliance Industries', 2900.00, 2000),
    ('HDFCBANK', 'HDFC Bank', 1700.00, 1200),
    ('ITC', 'ITC Limited', 450.00, 3000);


-- ============================================
-- PORTFOLIOS
-- ============================================

INSERT INTO portfolios (user_id)
SELECT user_id
FROM users;