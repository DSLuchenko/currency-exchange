INSERT INTO currency_exchange.currency (code, full_name, sign)
VALUES ('USD', 'US Dollar', '$'),
       ('EUR', 'Euro', '€'),
       ('TRY', 'Turkish Lira', '₺'),
       ('CNY', 'Yuan Renminbi', '¥');

INSERT INTO currency_exchange.exchange_rate (base_currency_id, target_currency_id, rate)
VALUES (1, 2, 0.9100),
       (1, 3, 26.9700),
       (1, 4, 7.1700);