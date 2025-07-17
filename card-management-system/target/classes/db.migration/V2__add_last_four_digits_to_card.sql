ALTER TABLE card ADD COLUMN last_four_digits VARCHAR(4);

UPDATE card SET last_four_digits = '0000' WHERE last_four_digits IS NULL;

ALTER TABLE card ALTER COLUMN last_four_digits SET NOT NULL;