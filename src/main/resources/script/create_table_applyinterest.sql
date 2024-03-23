CREATE TABLE IF NOT EXISTS ApplyInterest(
    id SERIAL PRIMARY KEY,
    idInterest int references Interest(id),
    idAccount uuid references Account(id)
);

ALTER TABLE applyinterest add column startDate timestamp with time zone DEFAULT current_timestamp;
ALTER TABLE applyinterest add column firstDue double precision;
ALTER TABLE applyinterest add column actualDue double precision;