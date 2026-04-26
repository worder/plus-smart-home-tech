CREATE TABLE IF NOT EXISTS products (
    product_id UUID PRIMARY KEY,
    product_name VARCHAR(255),
    description VARCHAR,
    image_src VARCHAR(255),
    quantity_state VARCHAR(64),
    product_state VARCHAR(64),
    product_category VARCHAR(64),
    price numeric(10,2)
);