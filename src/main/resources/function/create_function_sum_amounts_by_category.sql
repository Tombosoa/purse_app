CREATE OR REPLACE FUNCTION SumAmountsByCategory(
    _startDate DATE,
    _endDate DATE,
    _accountId UUID
) RETURNS TABLE (
    category_id INTEGER,
    category_name category_enum,
    total_amount DOUBLE PRECISION
) AS $$
BEGIN
    RETURN QUERY
    SELECT
        c.id AS category_id,
        c.name AS category_name,
        SUM(t.amount) AS total_amount
    FROM Transaction t
    JOIN category c ON t.id_category = c.id
    WHERE t.registration_date >= _startDate
    AND t.registration_date <= _endDate
    AND t.id_account = _accountId
    GROUP BY c.id, c.name
    ORDER BY total_amount DESC;
END;
$$ LANGUAGE plpgsql;


// permission postgres
GRANT SELECT ON TABLE category TO postgres;
GRANT SELECT ON TABLE Transaction TO postgres;
GRANT EXECUTE ON FUNCTION SumAmountsByCategory(DATE, DATE) TO postgres;

