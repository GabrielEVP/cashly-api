-- Create transactions table
CREATE TABLE transactions (
    id VARCHAR(36) PRIMARY KEY,
    user_id VARCHAR(36) NOT NULL,
    transaction_type VARCHAR(20) NOT NULL,
    transaction_status VARCHAR(20) NOT NULL,
    amount DECIMAL(19,2) NOT NULL,
    currency VARCHAR(3) NOT NULL,
    description VARCHAR(255) NOT NULL,
    transaction_date DATE NOT NULL,
    source_account_id VARCHAR(36),
    destination_account_id VARCHAR(36),
    expense_id VARCHAR(36),
    income_id VARCHAR(36),
    created_at TIMESTAMP NOT NULL,
    updated_at TIMESTAMP NOT NULL,
    INDEX idx_user_id (user_id),
    INDEX idx_source_account_id (source_account_id),
    INDEX idx_destination_account_id (destination_account_id),
    INDEX idx_transaction_status (transaction_status),
    INDEX idx_transaction_date (transaction_date)
);

-- Add comments for documentation
ALTER TABLE transactions COMMENT = 'Stores financial transactions between accounts and external sources';
