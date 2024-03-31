CREATE OR REPLACE FUNCTION SumIncomesAndExpenses(
    _startDate DATE,
    _endDate DATE,
    _accountId UUID
) RETURNS TABLE (
    category_id INTEGER,
    category_name category_enum,
    operation_type TEXT,
    total_amount DOUBLE PRECISION
) AS $$
BEGIN
    RETURN QUERY
    SELECT
        c.id AS category_id,
        c.name AS category_name,
        CASE WHEN SUM(CASE WHEN t.amount < 0 THEN t.amount ELSE 0 END) < 0 THEN 'Income' ELSE 'Expenses' END AS operation_type,
        SUM(t.amount) AS total_amount
    FROM Transaction t
    JOIN Category c ON t.id_category = c.id
    WHERE t.registration_date >= _startDate
    AND t.registration_date <= _endDate
    AND t.id_account = _accountId
    GROUP BY c.id, c.name;
END;
$$ LANGUAGE plpgsql;
GRANT SELECT ON TABLE Transaction TO postgres;
GRANT EXECUTE ON FUNCTION SumIncomesAndExpenses(DATE, DATE, UUID) TO postgres;
