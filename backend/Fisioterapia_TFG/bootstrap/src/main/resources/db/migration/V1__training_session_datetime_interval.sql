ALTER TABLE training_sessions
    DROP COLUMN date,
    ADD COLUMN start_date_time TIMESTAMP NOT NULL DEFAULT NOW(),
    ADD COLUMN end_date_time   TIMESTAMP NOT NULL DEFAULT NOW() + INTERVAL '1 hour';

ALTER TABLE training_sessions
    ALTER COLUMN start_date_time DROP DEFAULT,
    ALTER COLUMN end_date_time   DROP DEFAULT;
