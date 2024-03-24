CREATE OR REPLACE FUNCTION SumIncomesAndExpenses(
    _startDate DATE,
    _endDate DATE
) RETURNS TABLE (
    operation_type VARCHAR(255),
    total_amount DOUBLE PRECISION
) AS $$
BEGIN
    RETURN QUERY
    SELECT
        CASE WHEN t.amount < 0 THEN 'Dépenses' ELSE 'Rentrées' END AS operation_type,
        SUM(t.amount) AS total_amount
    FROM Transaction t
    WHERE t.registrationDate >= _startDate
    AND t.registrationDate <= _endDate
    GROUP BY operation_type
    ORDER BY operation_type;
END;

