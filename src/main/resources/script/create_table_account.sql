CREATE TABLE IF NOT EXISTS Account(
    id uuid PRIMARY KEY DEFAULT gen_random_uuid(),
    balance double precision default 0.0,
    creditAuthorization boolean default false,
    idClient uuid references Client(id),
    idBank int references Bank(id)
);