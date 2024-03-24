CREATE TABLE IF NOT EXISTS Interest (
    id SERIAL PRIMARY KEY,
    counts double precision,
    day_gone int
);
CREATE INDEX Interest_counts_index ON Interest (counts);