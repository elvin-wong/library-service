
-- populate checkouts_history

CREATE OR REPLACE FUNCTION audit_checkouts()
    RETURNS TRIGGER AS $$
BEGIN
    IF TG_OP = 'DELETE' THEN
        INSERT INTO checkouts_history
        (member_id, book_id, checkout_date, due_date, created_at, updated_at, operation, changed_at)
        VALUES
            (OLD.member_id, OLD.book_id, OLD.checkout_date, OLD.due_date, OLD.created_at, OLD.updated_at, 'DELETE', CURRENT_TIMESTAMP);
        RETURN OLD;
    ELSIF TG_OP = 'UPDATE' THEN
        INSERT INTO checkouts_history
        (member_id, book_id, checkout_date, due_date, created_at, updated_at, operation, changed_at)
        VALUES
            (OLD.member_id, OLD.book_id, OLD.checkout_date, OLD.due_date, OLD.created_at, OLD.updated_at, 'UPDATE', CURRENT_TIMESTAMP);
        RETURN NEW;
    ELSIF TG_OP = 'INSERT' THEN
        INSERT INTO checkouts_history
        (member_id, book_id, checkout_date, due_date, created_at, updated_at, operation, changed_at)
        VALUES
            (NEW.member_id, NEW.book_id, NEW.checkout_date, NEW.due_date, NEW.created_at, NEW.updated_at, 'INSERT', CURRENT_TIMESTAMP);
        RETURN NEW;
    END IF;
    RETURN NULL;
END;
$$ LANGUAGE plpgsql;

-- ^^^ END OF SCRIPT ^^^
