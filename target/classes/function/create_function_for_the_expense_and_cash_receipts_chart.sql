CREATE OR REPLACE FUNCTION SumIncomesAndExpenses(
    _startDate DATE,
    _endDate DATE,
    _accountId UUID
) RETURNS TABLE (
    month INTEGER,
    year INTEGER,
    Income DOUBLE PRECISION,
    Expense DOUBLE PRECISION
) AS $$
BEGIN
    RETURN QUERY
    SELECT
        CAST(EXTRACT(MONTH FROM transaction.effective_date) AS INTEGER) AS month,
        CAST(EXTRACT(YEAR FROM transaction.effective_date) AS INTEGER) AS year,
        SUM(CASE WHEN transaction.description = 'Credited account' THEN amount ELSE 0 END) as Income,
        SUM(CASE WHEN transaction.description = 'Debited account' THEN amount ELSE 0 END) as Expense
    FROM
        transaction
    INNER JOIN
        account ON account.id = transaction.id_account
    INNER JOIN
        category ON category.id = transaction.id_category
    WHERE
        account.id = _accountId
        AND (
            (_startDate IS NULL OR transaction.effective_date >= COALESCE(_startDate, '1900-01-01'))
            AND (_endDate IS NULL OR transaction.effective_date <= COALESCE(_endDate, '9999-12-31'))
        )
    GROUP BY
        month,
        year
    ORDER BY
        month;
END;
$$ LANGUAGE plpgsql;
