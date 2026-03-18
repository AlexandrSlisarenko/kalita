--liquibase formatted sql

--changeset aslisarenko:1
CREATE SCHEMA IF NOT EXISTS kalita;
--rollback DROP SCHEMA IF EXISTS document_db;

--changeset aslisarenko:2
CREATE TABLE IF NOT EXISTS kalita.wallet
(
    walletId UUID PRIMARY KEY,
    operationType VARCHAR(10) NOT NULL,
    amount NUMERIC(8,2) NOT NULL
);
--rollback DROP TABLE IF EXISTS kalita.wallet;
