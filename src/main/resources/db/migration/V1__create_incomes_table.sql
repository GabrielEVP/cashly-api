-- Create incomes table
-- This table stores income records for users with proper indexing for query performance

CREATE TABLE incomes (
    id VARCHAR(36) PRIMARY KEY COMMENT 'UUID of the income record',
    amount DECIMAL(19,2) NOT NULL COMMENT 'Income amount with 2 decimal precision',
    description VARCHAR(255) NOT NULL COMMENT 'Description of the income source',
    category VARCHAR(50) NOT NULL COMMENT 'Category of income (SALARY, BUSINESS, INVESTMENT, OTHER)',
    date DATE NOT NULL COMMENT 'Date when the income was received',
    user_id VARCHAR(36) NOT NULL COMMENT 'UUID of the user who owns this income',
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT 'Record creation timestamp',
    updated_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT 'Record last update timestamp'
);

-- Create indexes for optimal query performance
CREATE INDEX idx_user_id ON incomes (user_id);
CREATE INDEX idx_user_category ON incomes (user_id, category);
CREATE INDEX idx_user_created ON incomes (user_id, created_at);

-- Add constraints
ALTER TABLE incomes 
    ADD CONSTRAINT chk_amount_positive CHECK (amount >= 0),
    ADD CONSTRAINT chk_category_valid CHECK (category IN ('SALARY', 'BUSINESS', 'INVESTMENT', 'OTHER')),
    ADD CONSTRAINT chk_description_not_empty CHECK (TRIM(description) != ''),
    ADD CONSTRAINT chk_user_id_not_empty CHECK (TRIM(user_id) != ''),
    ADD CONSTRAINT chk_date_not_future CHECK (date <= CURDATE());