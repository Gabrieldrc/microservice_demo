INSERT INTO invoices (id, invoice_number, description, customer_id, create_at, state) VALUES(1, '0001', 'invoice office items', 1, NOW(),'CREATED');

INSERT INTO invoice_items ( id, product_id, quantity, price, invoice_id) VALUES(1, 1 , 1, 178.89, 1);
INSERT INTO invoice_items ( id, product_id, quantity, price, invoice_id)  VALUES(2, 2 , 2, 12.5, 1);
INSERT INTO invoice_items ( id, product_id, quantity, price, invoice_id)  VALUES(3, 3 , 1, 40.06, 1);
