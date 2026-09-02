CREATE TABLE accounts (
      account_id UUID PRIMARY KEY,
      customer_id UUID NOT NULL,
      account_number VARCHAR(50) NOT NULL UNIQUE,
      account_type VARCHAR(30) NOT NULL,
      status VARCHAR(20) NOT NULL,
      created_at TIMESTAMP NOT NULL,
      updated_at TIMESTAMP NOT NULL
);

CREATE INDEX idx_accounts_customer_id
    ON accounts(customer_id);