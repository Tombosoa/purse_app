CREATE TABLE IF NOT EXISTS Account(
    id uuid PRIMARY KEY DEFAULT gen_random_uuid(),
    balance double precision default 0.0,
    credit_authorization boolean default false,
    id_client uuid references Client(id),
    id_bank int references Bank(id)
);
CREATE INDEX account_id_client_index ON Account (id_Client);