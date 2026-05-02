CREATE TABLE IF NOT EXISTS payments (
    payment_id UUID PRIMARY KEY,
    order_id UUID NOT NULL,
    total_payment numeric(10, 2),
    delivery_total numeric(10, 2),
    product_total numeric(10, 2),
    fee_total numeric(10, 2),
    payment_status varchar(50)
);