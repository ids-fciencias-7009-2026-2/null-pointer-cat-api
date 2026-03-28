-- =================
--  adoption_user
-- =================

CREATE TABLE adoption_user (
    id_user SERIAL PRIMARY KEY,
    first_name VARCHAR(20) NOT NULL,
    last_name VARCHAR(50) NOT NULL,
    birth_date DATE NOT NULL,
    gender VARCHAR(10) NOT NULL,
    email VARCHAR(256) NOT NULL,
    zipcode VARCHAR(10) NOT NULL,
    register_date TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    user_password VARCHAR(128) NOT NULL,
    token VARCHAR(256)
);

-- Constraints
ALTER TABLE adoption_user
ADD CONSTRAINT ck_zipcode CHECK (LENGTH(zipcode) BETWEEN 5 AND 10);

ALTER TABLE adoption_user
ADD CONSTRAINT ck_birthdate CHECK (birth_date <= CURRENT_DATE);

ALTER TABLE adoption_user
ADD CONSTRAINT ck_max_age CHECK (birth_date >= CURRENT_DATE - INTERVAL '120 years');

ALTER TABLE adoption_user
ADD CONSTRAINT ck_firstname CHECK (first_name <> '');

ALTER TABLE adoption_user
ADD CONSTRAINT ck_lastname CHECK (last_name <> '');
