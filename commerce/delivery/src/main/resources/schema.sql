CREATE TABLE IF NOT EXISTS addresses (
    address_id UUID PRIMARY KEY,
    country VARCHAR(255),
    city VARCHAR(255),
    street VARCHAR(255),
    house VARCHAR(255),
    flat VARCHAR(255)
);

CREATE TABLE IF NOT EXISTS deliveries (
    delivery_id UUID PRIMARY KEY,
    order_id UUID NOT NULL,
    from_address_id UUID NOT NULL REFERENCES addresses,
    to_address_id UUID NOT NULL REFERENCES addresses,
    delivery_state varchar(50) NOT NULL
);

