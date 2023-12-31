CREATE TABLE IF NOT EXISTS currency_exchange.currency (
                       id INTEGER CONSTRAINT currency_pk PRIMARY KEY GENERATED ALWAYS AS IDENTITY,
                       code VARCHAR(3) NOT NULL,
                       full_name VARCHAR(50) NOT NULL,
                       sign VARCHAR(1) NOT NULL,
                       unique (code)
);

CREATE TABLE IF NOT EXISTS currency_exchange.exchange_rate (
                                        id INTEGER CONSTRAINT exchange_rate_pk PRIMARY KEY GENERATED ALWAYS AS IDENTITY,
                                        base_currency_id INTEGER NOT NULL,
                                        target_currency_id INTEGER NOT NULL,
                                        rate NUMERIC(10,4) NOT NULL,
                                        unique (base_currency_id,target_currency_id)
);