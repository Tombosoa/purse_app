
INSERT INTO Transaction (type, description, registration_date, effective_date, amount, status, reference, id_category, id_account, label, situation) VALUES
('Outgoing', 'Transaction 1', '2024-03-26 12:00:00', '2024-03-26 12:00:00', 100.00, true, 'Ref1', 1, '65cb7619-593e-4811-9725-9464a27cc9ed', 'Label1', 'Situation1'),
('Outgoing', 'Transaction 2', '2024-03-26 12:00:00', '2024-03-26 12:00:00', 150.00, true, 'Ref2', 1, 'f6bea828-9625-4a24-aecf-0daf0e970c30', 'Label2', 'Situation2'),
('Outgoing', 'Transaction 3','2024-03-27 12:00:00', '2024-03-27 12:00:00', 200.00, true, 'Ref3', 2, '948ba008-4531-43b3-bba4-a71dea2b40ae', 'Label3', 'Situation3');
SELECT * FROM Transaction;
