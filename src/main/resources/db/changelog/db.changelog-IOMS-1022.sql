--liquibase formatted sql
--changeset Abhishek :2
-- IOMS-1022 : updated category and product table

-- Step 1: Drop the existing foreign key constraint from the product table
ALTER TABLE product
DROP FOREIGN KEY fk_product_category;

-- Step 2: Modify category_id in the category table to INT and AUTO_INCREMENT
ALTER TABLE category
MODIFY COLUMN category_id INT AUTO_INCREMENT;

-- Step 3: Modify category_id in the product table to INT
ALTER TABLE product
MODIFY COLUMN category_id INT;

-- Step 4: Add the new foreign key constraint to the product table
ALTER TABLE product
ADD CONSTRAINT fk_product_category
FOREIGN KEY (category_id) REFERENCES category(category_id);

-- Step 5: Modify tags in the product table to VARCHAR
ALTER TABLE product
MODIFY COLUMN tags VARCHAR(255);
