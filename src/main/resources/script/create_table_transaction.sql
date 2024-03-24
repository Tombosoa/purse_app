CREATE TABLE IF NOT EXISTS Transaction(
    id SERIAL PRIMARY KEY,
    type varchar(255),
    description varchar(500),
    registration_date timestamp with time zone DEFAULT current_timestamp,
    effective_date timestamp with time zone,
    amount double precision,
    status boolean,
    reference varchar(255),
    id_category int references Category(id),
    id_account uuid references Account(id)
);
ALTER TABLE transaction add column label varchar(255);
ALTER TABLE transaction add column situation varchar(255);
CREATE INDEX Transaction_reference_index ON Transaction (reference);