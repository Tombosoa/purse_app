CREATE TABLE IF NOT EXISTS ApplyInterest(
    id SERIAL PRIMARY KEY,
    id_interest int references Interest(id),
    id_account uuid references Account(id)
);

CREATE INDEX ApplyInterest_id_interest_index ON ApplyInterest (id_interest);


ALTER TABLE applyinterest add column start_date timestamp with time zone DEFAULT current_timestamp;
ALTER TABLE applyinterest add column first_due double precision;
ALTER TABLE applyinterest add column actual_due double precision;
