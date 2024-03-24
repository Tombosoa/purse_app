CREATE TABLE IF NOT EXISTS Client(
    id uuid PRIMARY KEY DEFAULT gen_random_uuid(),
    firstname varchar(255),
    lastname varchar(255),
    birthdate date,
    monthlyPay double precision
);
CREATE INDEX Client_firstname_index ON Client (firstname);
