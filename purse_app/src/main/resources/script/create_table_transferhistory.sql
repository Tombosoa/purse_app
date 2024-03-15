CREATE TABLE IF NOT EXISTS TransferHistory(
  id SERIAL PRIMARY KEY,
  idTransactionDebited int references Transaction(id),
  idTransactionCredited int references Transaction(id)
);