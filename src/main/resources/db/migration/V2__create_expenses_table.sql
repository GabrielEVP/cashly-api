-- Create expenses table
-- This table stores expense records for users with proper indexing for query performance

CREATE TABLE expenses (
    id VARCHAR(36) PRIMARY KEY COMMENT 'UUID of the expense record',
    amount DECIMAL(19,2) NOT NULL COMMENT 'Expense amount with 2 decimal precision',
    description VARCHAR(255) NOT NULL COMMENT 'Description of the expense',
    category VARCHAR(50) NOT NULL COMMENT 'Category of expense (FOOD, TRANSPORT, ENTERTAINMENT, UTILITIES, HEALTH, SHOPPING, OTHER)',
    date DATE NOT NULL COMMENT 'Date when the expense was incurred',
    user_id VARCHAR(36) NOT NULL COMMENT 'UUID of the user who owns this expense',
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT 'Record creation timestamp',
    updated_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT 'Record last update timestamp'
);

-- Create indexes for optimal query performance
CREATE INDEX idx_user_id ON expenses (user_id);
CREATE INDEX idx_user_category ON expenses (user_id, category);
CREATE INDEX idx_user_created ON expenses (user_id, created_at);

-- Add constraints
ALTER TABLE expenses 
    ADD CONSTRAINT chk_amount_positive CHECK (amount >= 0),
    ADD CONSTRAINT chk_category_valid CHECK (category IN ('FOOD', 'TRANSPORT', 'ENTERTAINMENT', 'UTILITIES', 'HEALTH', 'SHOPPING', 'OTHER')),
    ADD CONSTRAINT chk_description_not_empty CHECK (TRIM(description) != ''),
    ADD CONSTRAINT chk_user_id_not_empty CHECK (TRIM(user_id) != ''),
    ADD CONSTRAINT chk_date_not_future CHECK (date <= CURDATE());