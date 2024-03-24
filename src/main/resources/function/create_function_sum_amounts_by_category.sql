CREATE OR REPLACE FUNCTION SumAmountsByCategory(
    _startDate DATE,
    _endDate DATE
) RETURNS TABLE (
    category_id UUID,
    category_name VARCHAR(255),
    total_amount DOUBLE PRECISION
) AS $$
BEGIN
    RETURN QUERY
    SELECT
        c.id AS category_id,
        c.name AS category_name,
        SUM(t.amount) AS total_amount
    FROM Transaction t
    JOIN Category c ON t.idCategory = c.id
    WHERE t.registrationDate >= _startDate
    AND t.registrationDate <= _endDate
    GROUP BY c.id, c.name
    ORDER BY total_amount DESC;
END;

