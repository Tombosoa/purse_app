CREATE TYPE category_enum AS ENUM(
    'Food_and_Drinks',
    'Online_Shopping',
    'Housing',
    'Transportation',
    'Vehicle',
    'Leisure',
    'Multimedia_and_Computers',
    'Financial_Expenses',
    'Investments',
    'Income',
    'Other',
    'Unknown'
);
CREATE TYPE type_enum AS ENUM(
    'Incoming',
    'Outgoing',
    'Loan'
);
CREATE TABLE IF NOT EXISTS category (
    id SERIAL PRIMARY KEY,
    type type_enum,
    name category_enum,
    description VARCHAR(255)
);
CREATE INDEX category_name_index ON category (name);
