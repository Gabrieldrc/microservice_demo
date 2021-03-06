CREATE DATABASE "shopping-service";

CREATE TABLE IF NOT EXISTS INVOICES (
  "id" SERIAL NOT NULL,
  "invoice_number" VARCHAR(45) NULL,
  "description" TEXT NULL,
  "customer_id" SERIAL NOT NULL,
  "create_at" DATE NULL,
  "state" VARCHAR(45) NOT NULL,
  PRIMARY KEY ("id"));

CREATE TABLE IF NOT EXISTS INVOICE_ITEMS (
  "id" SERIAL NOT NULL,
  "quantity" DOUBLE PRECISION NOT NULL,
  "price" DOUBLE PRECISION NULL,
  "product_id" SERIAL NOT NULL,
  "invoice_id" SERIAL NOT NULL,
  PRIMARY KEY ("id"),
  CONSTRAINT "fk_INVOICE_ITEMS_INVOICES"
    FOREIGN KEY ("invoice_id")
    REFERENCES INVOICES ("id") MATCH SIMPLE
    ON UPDATE CASCADE
    ON DELETE CASCADE
    NOT VALID
);