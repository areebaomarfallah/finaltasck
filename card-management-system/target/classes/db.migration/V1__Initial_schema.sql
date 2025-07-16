

CREATE TABLE account (
    id UUID PRIMARY KEY,
    status VARCHAR(20) NOT NULL,
    balance DECIMAL(19,2) NOT NULL,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

CREATE TABLE card (
    id UUID PRIMARY KEY,
    card_number_hash VARCHAR(255) NOT NULL UNIQUE,
    expiry DATE NOT NULL,
    status VARCHAR(20) NOT NULL,
    account_id UUID NOT NULL,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (account_id) REFERENCES account(id)
);

CREATE TABLE transaction (
    id UUID PRIMARY KEY,
    transaction_amount DECIMAL(19,2) NOT NULL,
    transaction_date TIMESTAMP NOT NULL,
    transaction_type VARCHAR(10) NOT NULL,
    status VARCHAR(20) NOT NULL,
    card_id UUID NOT NULL,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (card_id) REFERENCES card(id)
);

