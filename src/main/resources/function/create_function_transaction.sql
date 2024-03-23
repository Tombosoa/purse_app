CREATE OR REPLACE FUNCTION CreateTransaction(
    _type VARCHAR(255),
    _description VARCHAR(500),
    _effectiveDate TIMESTAMP WITH TIME ZONE,
    _amount DOUBLE PRECISION,
    _reference VARCHAR(255),
    _idCategory UUID,
    _idAccount UUID,
    _label VARCHAR(255),
    _isPlanified BOOLEAN
) RETURNS VOID AS $$
DECLARE
    _currentBalance DOUBLE PRECISION;
BEGIN
    -- Vérifier le solde du compte
    SELECT balance INTO _currentBalance FROM Account WHERE id = _idAccount;
    IF _currentBalance < _amount THEN
        RAISE EXCEPTION 'Solde insuffisant pour le virement.';
    END IF;

    -- Générer une référence unique pour la transaction
    IF _reference IS NULL THEN
        _reference := CONCAT('TR', TO_CHAR(NOW(), 'YYYYMMDDHH24MISS'), LPAD(FLOOR(RANDOM() * 1000)::TEXT, 3, '0'));
    END IF;

    -- Insérer la transaction
    INSERT INTO Transaction(type, description, registrationDate, effectiveDate, amount, status, reference, idCategory, idAccount, label, situation)
    VALUES (_type, _description, CURRENT_TIMESTAMP, _effectiveDate, _amount, FALSE, _reference, _idCategory, _idAccount, _label, CASE WHEN _isPlanified THEN 'Planifié' ELSE 'En attente' END);

    -- Si le virement n'est pas planifié, effectuer le débit et le crédit immédiatement
    IF NOT _isPlanified THEN
        -- Débiter le compte
        UPDATE Account SET balance = balance - _amount WHERE id = _idAccount;
    END IF;
END;