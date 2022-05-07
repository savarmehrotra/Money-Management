INSERT INTO Bank.Account_Balances (account_number, account_holder_name, current_balance, currency)
VALUES ('10000', 'John Doe', 7000.00, 'USD');

INSERT INTO Bank.Account_Balances (account_number, account_holder_name, current_balance, currency)
VALUES ('10001', 'Jane Doe', 5000.00, 'USD');

INSERT INTO Bank.Transactions (transaction_id, source_account_id, destination_account_id, transaction_amount, transaction_type, last_updated, reference)
VALUES (1, '10000', '10001', 100.00, 'TRANSFER', '2022-05-04 10:30', 'Expenses');