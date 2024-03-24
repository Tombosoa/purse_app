CREATE TABLE IF NOT EXISTS TransferHistory(
  id SERIAL PRIMARY KEY,
  id_transaction_debited int references Transaction(id),
  id_transaction_credited int references Transaction(id)
);
CREATE INDEX TransferHistory_id_transaction_debited_index ON TransferHistory (id_transaction_debited);