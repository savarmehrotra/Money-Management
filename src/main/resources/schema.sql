CREATE SCHEMA Bank;

CREATE TABLE Bank.Account_Balances (
    account_number CHAR(5) NOT NULL PRIMARY KEY,
    account_holder_name VARCHAR(255) NOT NULL,
    current_balance NUMERIC(12,2) NOT NULL,
    currency VARCHAR(255) NOT NULL
);

--CREATE TABLE Bank.Transaction_Status_Enum_Table (
--    transaction_status VARCHAR(255) check (transaction_status in ('PROCESSING', 'PROCESSED', 'CANCELLED'))
--);

CREATE SEQUENCE Bank.transaction_sequence START WITH 10;

CREATE TABLE Bank.Transactions (
    transaction_id bigint NOT NULL PRIMARY KEY,
    source_account_id CHAR(5) NOT NULL REFERENCES Bank.Account_Balances(account_number),
    destination_account_id CHAR(5) REFERENCES Bank.Account_Balances(account_number),
    transaction_amount NUMERIC(10,3) NOT NULL,
    transaction_type VARCHAR(255),
    last_updated TIMESTAMP NOT NULL,
    reference VARCHAR(255)
);

--REFERENCES Bank.Transaction_Status_Enum_Table(transaction_status)