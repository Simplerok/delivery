--liquibase formatted sql
--changeset ruaryb:1 logicalFilePath:1-init.sql

CREATE TABLE IF NOT EXISTS courier(
    id             UUID PRIMARY KEY,
    name           VARCHAR(255) NOT NULL,
    speed          INT NOT NULL,
    x              INT NOT NULL,
    y              INT NOT NULL
);

CREATE TABLE IF NOT EXISTS storage_place(
    id              UUID PRIMARY KEY,
    name            VARCHAR(255) NOT NULL,
    total_volume    INT NOT NULL,
    order_id         UUID,
    courier_id      UUID,

    CONSTRAINT storage_place_courier_id_fk
    FOREIGN KEY(courier_id) REFERENCES courier(id)
);

CREATE TABLE IF NOT EXISTS c_order(
    id             UUID PRIMARY KEY,
    x              INT NOT NULL,
    y              INT NOT NULL,
    volume         INT NOT NULL,
    status         VARCHAR(255) NOT NULL,
    courier_id     UUID
);