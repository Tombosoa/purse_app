CREATE TABLE IF NOT EXISTS Transaction(
    id SERIAL PRIMARY KEY,
    type varchar(255),
    description varchar(500),
    registrationDate timestamp with time zone DEFAULT current_timestamp,
    effectiveDate timestamp with time zone,
    amount double precision,
    status boolean,
    reference varchar(255),
    idCategory int references Category(id),
    idAccount uuid references Account(id)
);