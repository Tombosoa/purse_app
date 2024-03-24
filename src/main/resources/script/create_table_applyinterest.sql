CREATE TABLE IF NOT EXISTS ApplyInterest(
    id SERIAL PRIMARY KEY,
    id_interest int references Interest(id),
    id_account uuid references Account(id)
);
CREATE INDEX ApplyInterest_id_interest_index ON ApplyInterest (id_interest);