-- Update expense categories constraint to match domain model
-- Changes old categories to new standardized categories

-- Drop old constraint
ALTER TABLE expenses DROP CONSTRAINT chk_expenses_category_valid;

-- Add new constraint with updated categories
ALTER TABLE expenses
    ADD CONSTRAINT chk_expenses_category_valid CHECK (
        category IN (
            'FOOD_DINING',
            'TRANSPORTATION',
            'HOUSING',
            'HEALTHCARE',
            'ENTERTAINMENT',
            'SHOPPING',
            'EDUCATION',
            'TRAVEL',
            'OTHER'
        )
    );

-- Update existing data to match new categories (if any exists)
UPDATE expenses SET category = 'FOOD_DINING' WHERE category = 'FOOD';
UPDATE expenses SET category = 'TRANSPORTATION' WHERE category = 'TRANSPORT';
UPDATE expenses SET category = 'HEALTHCARE' WHERE category = 'HEALTH';
