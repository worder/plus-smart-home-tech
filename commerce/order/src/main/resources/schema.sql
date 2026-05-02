CREATE TABLE IF NOT EXISTS orders (
    order_id UUID PRIMARY KEY,
    shopping_cart_id UUID NOT NULL,
    payment_id UUID,
    delivery_id UUID,
    state VARCHAR(255),
    delivery_weight numeric(10,3),
    delivery_volume numeric(10,4),
    fragile BOOLEAN,
    total_price numeric(10,2),
    delivery_price numeric(10,2),
    product_price numeric(10,2),
    created_on timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_on timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP
);

CREATE TABLE IF NOT EXISTS order_products (
    order_id UUID REFERENCES orders ON DELETE CASCADE,
    product_id UUID NOT NULL,
    quantity integer,
    PRIMARY KEY(order_id, product_id)
);