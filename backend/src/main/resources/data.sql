INSERT INTO tb_customer (id, name, email, password, birthday, CPF, phone_number) VALUES (1, 'João Silva', 'joao@email.com', 'senhaSegura123', '1990-01-15', '12345678900', '11999998888');
INSERT INTO tb_customer (id, name, email, password, birthday, CPF, phone_number) VALUES (2, 'Maria Oliveira', 'maria.o@email.com', 'maria123', '1985-06-22', '98765432100', '11988887777');
INSERT INTO tb_customer (id, name, email, password, birthday, CPF, phone_number) VALUES (3, 'Carlos Pereira', 'carlos.p@email.com', 'carlos456', '1992-03-10', '45678912300', '11977776666');
INSERT INTO tb_customer (id, name, email, password, birthday, CPF, phone_number) VALUES (4, 'Ana Costa', 'ana.costa@email.com', 'ana789', '1995-12-05', '32165498700', '11966665555');
INSERT INTO tb_customer (id, name, email, password, birthday, CPF, phone_number) VALUES (5, 'Pedro Santos', 'pedro@email.com', 'pedro321', '1988-08-19', '74185296300', '11955554444');
INSERT INTO tb_customer (id, name, email, password, birthday, CPF, phone_number) VALUES (6, 'Juliana Rocha', 'juliana@email.com', 'juliana654', '1993-07-30', '36925814700', '11944443333');
INSERT INTO tb_customer (id, name, email, password, birthday, CPF, phone_number) VALUES (7, 'Bruno Lima', 'bruno@email.com', 'bruno111', '1991-09-17', '85274196300', '11933332222');
INSERT INTO tb_customer (id, name, email, password, birthday, CPF, phone_number) VALUES (8, 'Fernanda Souza', 'fernanda@email.com', 'fernanda222', '1987-11-23', '96385274100', '11922221111');
INSERT INTO tb_customer (id, name, email, password, birthday, CPF, phone_number) VALUES (9, 'Lucas Martins', 'lucas@email.com', 'lucas333', '1996-02-14', '14725836900', '11911110000');
INSERT INTO tb_customer (id, name, email, password, birthday, CPF, phone_number) VALUES (10, 'Patrícia Mendes', 'patricia@email.com', 'patricia444', '1994-04-09', '25836914700', '11900009999');
INSERT INTO tb_customer (id, name, email, password, birthday, CPF, phone_number) VALUES (11, 'André Barbosa', 'andre@email.com', 'andre555', '1990-10-01', '15975348600', '11888887777');

INSERT INTO tb_account (account_number, customer_id, account_type, current_balance, date_opened) VALUES ('000001', 1, 'CHECKING', 2500.00, '2022-01-10');
INSERT INTO tb_account (account_number, customer_id, account_type, current_balance, date_opened) VALUES ('000002', 2, 'SAVINGS', 3800.75, '2023-02-15');
INSERT INTO tb_account (account_number, customer_id, account_type, current_balance, date_opened) VALUES ('000003', 3, 'CHECKING', 1200.50, '2021-11-05');
INSERT INTO tb_account (account_number, customer_id, account_type, current_balance, date_opened) VALUES ('000004', 4, 'SAVINGS', 5400.20, '2023-04-01');
INSERT INTO tb_account (account_number, customer_id, account_type, current_balance, date_opened) VALUES ('000005', 5, 'CHECKING', 700.00, '2020-08-12');
INSERT INTO tb_account (account_number, customer_id, account_type, current_balance, date_opened) VALUES ('000006', 6, 'SAVINGS', 9200.90, '2023-07-30');
INSERT INTO tb_account (account_number, customer_id, account_type, current_balance, date_opened) VALUES ('000007', 7, 'CHECKING', 1580.10, '2022-12-05');
INSERT INTO tb_account (account_number, customer_id, account_type, current_balance, date_opened) VALUES ('000008', 8, 'SAVINGS', 300.00, '2021-09-20');
INSERT INTO tb_account (account_number, customer_id, account_type, current_balance, date_opened) VALUES ('000009', 9, 'CHECKING', 4320.45, '2022-06-15');
INSERT INTO tb_account (account_number, customer_id, account_type, current_balance, date_opened) VALUES ('000010', 10, 'SAVINGS', 1890.30, '2023-03-08');
INSERT INTO tb_account (account_number, customer_id, account_type, current_balance, date_opened) VALUES ('000011', 11, 'CHECKING', 2750.00, '2022-10-22');

INSERT INTO tb_transaction (amount, origin_account_id, destiny_account_id, transaction_date) VALUES (150.00, '000001', '000002', '2023-01-15');
INSERT INTO tb_transaction (amount, origin_account_id, destiny_account_id, transaction_date) VALUES (300.50, '000003', '000004', '2023-03-22');
INSERT INTO tb_transaction (amount, origin_account_id, destiny_account_id, transaction_date) VALUES (75.25, '000005', '000006', '2023-05-10');
INSERT INTO tb_transaction (amount, origin_account_id, destiny_account_id, transaction_date) VALUES (500.00, '000007', '000008', '2023-06-18');
INSERT INTO tb_transaction (amount, origin_account_id, destiny_account_id, transaction_date) VALUES (1200.00, '000009', '000010', '2023-07-01');
INSERT INTO tb_transaction (amount, origin_account_id, destiny_account_id, transaction_date) VALUES (60.75, '000011', '000001', '2023-08-05');
INSERT INTO tb_transaction (amount, origin_account_id, destiny_account_id, transaction_date) VALUES (220.10, '000002', '000003', '2023-09-12');
INSERT INTO tb_transaction (amount, origin_account_id, destiny_account_id, transaction_date) VALUES (45.90, '000004', '000005', '2023-10-25');
INSERT INTO tb_transaction (amount, origin_account_id, destiny_account_id, transaction_date) VALUES (800.00, '000006', '000007', '2023-11-11');
INSERT INTO tb_transaction (amount, origin_account_id, destiny_account_id, transaction_date) VALUES (130.00, '000008', '000009', '2023-12-20');
-- Para conta 000001 (envia e recebe)
INSERT INTO tb_transaction (amount, destiny_account_id, origin_account_id, transaction_date) VALUES (100.00, '000002', '000001', '2025-06-01');
INSERT INTO tb_transaction (amount, destiny_account_id, origin_account_id, transaction_date) VALUES (50.00, '000003', '000001', '2025-06-02');
INSERT INTO tb_transaction (amount, destiny_account_id, origin_account_id, transaction_date) VALUES (70.00, '000004', '000001', '2025-06-03');
INSERT INTO tb_transaction (amount, destiny_account_id, origin_account_id, transaction_date) VALUES (20.00, '000001', '000005', '2025-06-04');
INSERT INTO tb_transaction (amount, destiny_account_id, origin_account_id, transaction_date) VALUES (10.00, '000001', '000006', '2025-06-05');

-- Para conta 000002
INSERT INTO tb_transaction (amount, destiny_account_id, origin_account_id, transaction_date) VALUES (60.00, '000003', '000002', '2025-06-01');
INSERT INTO tb_transaction (amount, destiny_account_id, origin_account_id, transaction_date) VALUES (30.00, '000004', '000002', '2025-06-02');
INSERT INTO tb_transaction (amount, destiny_account_id, origin_account_id, transaction_date) VALUES (15.00, '000002', '000001', '2025-06-03');
INSERT INTO tb_transaction (amount, destiny_account_id, origin_account_id, transaction_date) VALUES (25.00, '000002', '000007', '2025-06-04');
INSERT INTO tb_transaction (amount, destiny_account_id, origin_account_id, transaction_date) VALUES (10.00, '000008', '000002', '2025-06-05');

-- Para conta 000003
INSERT INTO tb_transaction (amount, destiny_account_id, origin_account_id, transaction_date) VALUES (40.00, '000004', '000003', '2025-06-01');
INSERT INTO tb_transaction (amount, destiny_account_id, origin_account_id, transaction_date) VALUES (25.00, '000005', '000003', '2025-06-02');
INSERT INTO tb_transaction (amount, destiny_account_id, origin_account_id, transaction_date) VALUES (15.00, '000003', '000002', '2025-06-03');
INSERT INTO tb_transaction (amount, destiny_account_id, origin_account_id, transaction_date) VALUES (50.00, '000006', '000003', '2025-06-04');
INSERT INTO tb_transaction (amount, destiny_account_id, origin_account_id, transaction_date) VALUES (30.00, '000003', '000001', '2025-06-05');

-- Para conta 000004
INSERT INTO tb_transaction (amount, destiny_account_id, origin_account_id, transaction_date) VALUES (70.00, '000005', '000004', '2025-06-01');
INSERT INTO tb_transaction (amount, destiny_account_id, origin_account_id, transaction_date) VALUES (10.00, '000006', '000004', '2025-06-02');
INSERT INTO tb_transaction (amount, destiny_account_id, origin_account_id, transaction_date) VALUES (20.00, '000004', '000003', '2025-06-03');
INSERT INTO tb_transaction (amount, destiny_account_id, origin_account_id, transaction_date) VALUES (35.00, '000004', '000007', '2025-06-04');
INSERT INTO tb_transaction (amount, destiny_account_id, origin_account_id, transaction_date) VALUES (15.00, '000008', '000004', '2025-06-05');

-- Para conta 000005
INSERT INTO tb_transaction (amount, destiny_account_id, origin_account_id, transaction_date) VALUES (50.00, '000006', '000005', '2025-06-01');
INSERT INTO tb_transaction (amount, destiny_account_id, origin_account_id, transaction_date) VALUES (40.00, '000007', '000005', '2025-06-02');
INSERT INTO tb_transaction (amount, destiny_account_id, origin_account_id, transaction_date) VALUES (30.00, '000005', '000004', '2025-06-03');
INSERT INTO tb_transaction (amount, destiny_account_id, origin_account_id, transaction_date) VALUES (20.00, '000001', '000005', '2025-06-04');
INSERT INTO tb_transaction (amount, destiny_account_id, origin_account_id, transaction_date) VALUES (10.00, '000002', '000005', '2025-06-05');

-- Para conta 000006
INSERT INTO tb_transaction (amount, destiny_account_id, origin_account_id, transaction_date) VALUES (60.00, '000007', '000006', '2025-06-01');
INSERT INTO tb_transaction (amount, destiny_account_id, origin_account_id, transaction_date) VALUES (25.00, '000008', '000006', '2025-06-02');
INSERT INTO tb_transaction (amount, destiny_account_id, origin_account_id, transaction_date) VALUES (35.00, '000006', '000005', '2025-06-03');
INSERT INTO tb_transaction (amount, destiny_account_id, origin_account_id, transaction_date) VALUES (40.00, '000001', '000006', '2025-06-04');
INSERT INTO tb_transaction (amount, destiny_account_id, origin_account_id, transaction_date) VALUES (10.00, '000002', '000006', '2025-06-05');

-- Para conta 000007
INSERT INTO tb_transaction (amount, destiny_account_id, origin_account_id, transaction_date) VALUES (80.00, '000008', '000007', '2025-06-01');
INSERT INTO tb_transaction (amount, destiny_account_id, origin_account_id, transaction_date) VALUES (20.00, '000009', '000007', '2025-06-02');
INSERT INTO tb_transaction (amount, destiny_account_id, origin_account_id, transaction_date) VALUES (15.00, '000007', '000006', '2025-06-03');
INSERT INTO tb_transaction (amount, destiny_account_id, origin_account_id, transaction_date) VALUES (25.00, '000001', '000007', '2025-06-04');
INSERT INTO tb_transaction (amount, destiny_account_id, origin_account_id, transaction_date) VALUES (30.00, '000002', '000007', '2025-06-05');

-- Para conta 000008
INSERT INTO tb_transaction (amount, destiny_account_id, origin_account_id, transaction_date) VALUES (50.00, '000009', '000008', '2025-06-01');
INSERT INTO tb_transaction (amount, destiny_account_id, origin_account_id, transaction_date) VALUES (40.00, '000010', '000008', '2025-06-02');
INSERT INTO tb_transaction (amount, destiny_account_id, origin_account_id, transaction_date) VALUES (30.00, '000008', '000007', '2025-06-03');
INSERT INTO tb_transaction (amount, destiny_account_id, origin_account_id, transaction_date) VALUES (20.00, '000001', '000008', '2025-06-04');
INSERT INTO tb_transaction (amount, destiny_account_id, origin_account_id, transaction_date) VALUES (10.00, '000002', '000008', '2025-06-05');

-- Para conta 000009
INSERT INTO tb_transaction (amount, destiny_account_id, origin_account_id, transaction_date) VALUES (70.00, '000010', '000009', '2025-06-01');
INSERT INTO tb_transaction (amount, destiny_account_id, origin_account_id, transaction_date) VALUES (30.00, '000011', '000009', '2025-06-02');
INSERT INTO tb_transaction (amount, destiny_account_id, origin_account_id, transaction_date) VALUES (15.00, '000009', '000008', '2025-06-03');
INSERT INTO tb_transaction (amount, destiny_account_id, origin_account_id, transaction_date) VALUES (25.00, '000001', '000009', '2025-06-04');
INSERT INTO tb_transaction (amount, destiny_account_id, origin_account_id, transaction_date) VALUES (10.00, '000002', '000009', '2025-06-05');

-- Para conta 000010
INSERT INTO tb_transaction (amount, destiny_account_id, origin_account_id, transaction_date) VALUES (90.00, '000011', '000010', '2025-06-01');
INSERT INTO tb_transaction (amount, destiny_account_id, origin_account_id, transaction_date) VALUES (40.00, '000001', '000010', '2025-06-02');
INSERT INTO tb_transaction (amount, destiny_account_id, origin_account_id, transaction_date) VALUES (35.00, '000010', '000009', '2025-06-03');
INSERT INTO tb_transaction (amount, destiny_account_id, origin_account_id, transaction_date) VALUES (25.00, '000002', '000010', '2025-06-04');
INSERT INTO tb_transaction (amount, destiny_account_id, origin_account_id, transaction_date) VALUES (10.00, '000003', '000010', '2025-06-05');

-- Para conta 000011
INSERT INTO tb_transaction (amount, destiny_account_id, origin_account_id, transaction_date) VALUES (70.00, '000001', '000011', '2025-06-01');
INSERT INTO tb_transaction (amount, destiny_account_id, origin_account_id, transaction_date) VALUES (30.00, '000002', '000011', '2025-06-02');
INSERT INTO tb_transaction (amount, destiny_account_id, origin_account_id, transaction_date) VALUES (15.00, '000011', '000010', '2025-06-03');
INSERT INTO tb_transaction (amount, destiny_account_id, origin_account_id, transaction_date) VALUES (25.00, '000003', '000011', '2025-06-04');
INSERT INTO tb_transaction (amount, destiny_account_id, origin_account_id, transaction_date) VALUES (10.00, '000004', '000011', '2025-06-05');


UPDATE tb_account SET current_balance = 910.75 WHERE account_number = '000001';  -- 1000 -150 +60.75
UPDATE tb_account SET current_balance = 1530.00 WHERE account_number = '000002'; -- 1500 +150 -220.10
UPDATE tb_account SET current_balance = 2000.40 WHERE account_number = '000003'; -- 2000 +220.10 -300.50
UPDATE tb_account SET current_balance = 2545.40 WHERE account_number = '000004'; -- 2500 +300.50 -45.90
UPDATE tb_account SET current_balance = 1770.65 WHERE account_number = '000005'; -- 1800 +45.90 -75.25
UPDATE tb_account SET current_balance = 2324.25 WHERE account_number = '000006'; -- 2200 +75.25 -800.00
UPDATE tb_account SET current_balance = 1300.00 WHERE account_number = '000007'; -- 1600 +800 -500
UPDATE tb_account SET current_balance = 2270.00 WHERE account_number = '000008'; -- 1900 +500 -130
UPDATE tb_account SET current_balance = 2530.00 WHERE account_number = '000009'; -- 2100 +130 -1200
UPDATE tb_account SET current_balance = 3500.00 WHERE account_number = '000010'; -- 2300 +1200
UPDATE tb_account SET current_balance = 1639.25 WHERE account_number = '000011'; -- 1700 -60.75

UPDATE tb_account SET current_balance = 930.00 WHERE account_number = '000001';  -- saldo inicial 1000 - envio 240 + recebimento 170
UPDATE tb_account SET current_balance = 1520.00 WHERE account_number = '000002';  -- 1500 - envio 155 + recebimento 175
UPDATE tb_account SET current_balance = 2010.00 WHERE account_number = '000003';  -- 2000 - envio 120 + recebimento 130
UPDATE tb_account SET current_balance = 2560.00 WHERE account_number = '000004';  -- 2500 - envio 130 + recebimento 190
UPDATE tb_account SET current_balance = 1805.00 WHERE account_number = '000005';  -- 1800 - envio 140 + recebimento 145
UPDATE tb_account SET current_balance = 2245.00 WHERE account_number = '000006';  -- 2200 - envio 170 + recebimento 215
UPDATE tb_account SET current_balance = 1540.00 WHERE account_number = '000007';  -- 1600 - envio 90 + recebimento 30
UPDATE tb_account SET current_balance = 1950.00 WHERE account_number = '000008';  -- 1900 - envio 130 + recebimento 180
UPDATE tb_account SET current_balance = 2085.00 WHERE account_number = '000009';  -- 2100 - envio 135 + recebimento 120
UPDATE tb_account SET current_balance = 2305.00 WHERE account_number = '000010';  -- 2300 - envio 155 + recebimento 160
UPDATE tb_account SET current_balance = 1700.00 WHERE account_number = '000011';  -- 1700 - envio 135 + recebimento 135

