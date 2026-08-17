-- ============================================
-- STOCK TRADING SIMULATOR
-- DATABASE SCHEMA
-- ============================================


-- ============================================
-- 1. USERS
-- ============================================

CREATE TABLE users
(
    user_id SERIAL PRIMARY KEY,
    username VARCHAR(50) UNIQUE NOT NULL,
    email VARCHAR(100) UNIQUE NOT NULL,
    password VARCHAR(100) NOT NULL,
    balance DECIMAL(15,2) DEFAULT 0.00,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);


-- ============================================
-- 2. STOCKS
-- ============================================

CREATE TABLE stocks
(
    stock_id SERIAL PRIMARY KEY,
    symbol VARCHAR(20) UNIQUE NOT NULL,
    company_name VARCHAR(100) NOT NULL,
    current_price DECIMAL(15,2) NOT NULL,
    available_quantity INT DEFAULT 0
);


-- ============================================
-- 3. PORTFOLIOS
-- ============================================

CREATE TABLE portfolios
(
    portfolio_id SERIAL PRIMARY KEY,
    user_id INT UNIQUE NOT NULL,

    CONSTRAINT fk_portfolio_user
        FOREIGN KEY (user_id)
            REFERENCES users(user_id)
            ON DELETE CASCADE
);


-- ============================================
-- 4. HOLDINGS
-- ============================================

CREATE TABLE holdings
(
    holding_id SERIAL PRIMARY KEY,

    portfolio_id INT NOT NULL,
    stock_id INT NOT NULL,

    quantity INT NOT NULL,
    average_price DECIMAL(15,2) NOT NULL,

    CONSTRAINT fk_holding_portfolio
        FOREIGN KEY (portfolio_id)
            REFERENCES portfolios(portfolio_id)
            ON DELETE CASCADE,

    CONSTRAINT fk_holding_stock
        FOREIGN KEY (stock_id)
            REFERENCES stocks(stock_id)
            ON DELETE CASCADE,

    CONSTRAINT unique_portfolio_stock
        UNIQUE (portfolio_id, stock_id)
);


-- ============================================
-- 5. ORDERS
-- ============================================

CREATE TABLE orders
(
    order_id SERIAL PRIMARY KEY,

    user_id INT NOT NULL,
    stock_id INT NOT NULL,

    order_type VARCHAR(10) NOT NULL,
    quantity INT NOT NULL,
    price DECIMAL(15,2) NOT NULL,

    status VARCHAR(20) DEFAULT 'PENDING',

    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,

    CONSTRAINT fk_order_user
        FOREIGN KEY (user_id)
            REFERENCES users(user_id),

    CONSTRAINT fk_order_stock
        FOREIGN KEY (stock_id)
            REFERENCES stocks(stock_id)
);


-- ============================================
-- 6. TRANSACTIONS
-- ============================================

CREATE TABLE transactions
(
    transaction_id SERIAL PRIMARY KEY,

    user_id INT NOT NULL,
    stock_id INT NOT NULL,

    transaction_type VARCHAR(10) NOT NULL,
    quantity INT NOT NULL,
    price DECIMAL(15,2) NOT NULL,

    total_amount DECIMAL(15,2) NOT NULL,

    transaction_time TIMESTAMP DEFAULT CURRENT_TIMESTAMP,

    CONSTRAINT fk_transaction_user
        FOREIGN KEY (user_id)
            REFERENCES users(user_id),

    CONSTRAINT fk_transaction_stock
        FOREIGN KEY (stock_id)
            REFERENCES stocks(stock_id)
);