CREATE TABLE IF NOT EXISTS ApplyInterest(
    id SERIAL PRIMARY KEY,
    idInterest int references Interest(id),
    idAccount uuid references Account(id)
);