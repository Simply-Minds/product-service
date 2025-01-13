--liquibase formatted sql
--changeset Jeet :1
-- IOMS-007 : Create Basic Database Schema for product

CREATE TABLE category (
category_id VARCHAR(36) PRIMARY KEY,
name VARCHAR(100),
description VARCHAR(250)
);

CREATE TABLE product_unit (
product_unit_id BIGINT AUTO_INCREMENT PRIMARY KEY,
unit_name VARCHAR(100),
unit_spec VARCHAR(50)
);

CREATE TABLE product (
product_id BIGINT AUTO_INCREMENT PRIMARY KEY,
category_id VARCHAR(36),
product_unit_id BIGINT,
name VARCHAR(100),
sku VARCHAR(250),
price DECIMAL(19,2),
quantity_in_stock INTEGER,
status VARCHAR(100),
description VARCHAR(250),
reorder_level INTEGER,
image_url VARCHAR(500),
brand VARCHAR(200),
weight DOUBLE,
dimensions VARCHAR(300),
color VARCHAR(200),
tags VARBINARY(255),
CONSTRAINT fk_product_category FOREIGN KEY (category_id) REFERENCES category(category_id),
CONSTRAINT fk_product_product_unit FOREIGN KEY (product_unit_id) REFERENCES product_unit(product_unit_id)
);