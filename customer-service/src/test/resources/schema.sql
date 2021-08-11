CREATE DATABASE "customer-service";

CREATE TABLE IF NOT EXISTS REGIONS (
  "id" SERIAL NOT NULL,
  "name" VARCHAR(45) NOT NULL,
  PRIMARY KEY ("id"));

CREATE TABLE IF NOT EXISTS CUSTOMERS (
  "id" SERIAL NOT NULL,
  "id_number" VARCHAR(8) NULL,
  "first_name" VARCHAR(45) NOT NULL,
  "last_name" VARCHAR(45) NOT NULL,
  "email" VARCHAR(45) NOT NULL,
  "photo_url" TEXT NOT NULL,
  "region_id" SERIAL NOT NULL,
  "state" VARCHAR(45) NULL,
  PRIMARY KEY ("id"),
  CONSTRAINT "fk_CUSTOMERS_REGIONS"
    FOREIGN KEY ("region_id")
    REFERENCES REGIONS ("id")
    ON DELETE NO ACTION
    ON UPDATE NO ACTION);