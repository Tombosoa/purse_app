CREATE TABLE IF NOT EXISTS Bank(
    id SERIAL PRIMARY KEY,
    name varchar(255)
);
CREATE INDEX Bank_name_index ON Bank (name);