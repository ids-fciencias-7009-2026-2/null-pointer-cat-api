-- =================
--  adoption_user
-- =================
CREATE DATABASE IF NOT EXISTS adoption_db;
USE adoption_db;

DROP TABLE IF EXISTS adoption_user;

CREATE TABLE adoption_user (
    id_user       SERIAL PRIMARY KEY,
    username      VARCHAR(30)  NOT NULL,
    first_name    VARCHAR(20)  NOT NULL,
    last_name     VARCHAR(50)  NOT NULL,
    birth_date    DATE         NOT NULL,
    gender        VARCHAR(10)  NOT NULL,
    email         VARCHAR(256) NOT NULL,
    phone_number  VARCHAR(15),
    zipcode       VARCHAR(10)  NOT NULL,
    register_date TIMESTAMP    NOT NULL DEFAULT CURRENT_TIMESTAMP,
    user_password VARCHAR(128) NOT NULL,
    token         VARCHAR(256)
);

-- Constraints
ALTER TABLE adoption_user ADD CONSTRAINT uq_user_email    UNIQUE (email);
ALTER TABLE adoption_user ADD CONSTRAINT uq_user_username UNIQUE (username);

ALTER TABLE adoption_user ADD CONSTRAINT ck_user_zipcode   CHECK (LENGTH(zipcode) BETWEEN 5 AND 10);
ALTER TABLE adoption_user ADD CONSTRAINT ck_user_birthdate CHECK (birth_date <= CURRENT_DATE);
ALTER TABLE adoption_user ADD CONSTRAINT ck_user_max_age   CHECK (birth_date >= CURRENT_DATE - INTERVAL '120 years');
ALTER TABLE adoption_user ADD CONSTRAINT ck_user_firstname CHECK (first_name <> '');
ALTER TABLE adoption_user ADD CONSTRAINT ck_user_lastname  CHECK (last_name <> '');
ALTER TABLE adoption_user ADD CONSTRAINT ck_user_gender    CHECK (gender IN ('male', 'female', 'other', 'prefer_not_to_say'));
ALTER TABLE adoption_user ADD CONSTRAINT ck_user_phone     CHECK (phone_number ~ '^\+?[0-9]{7,15}$');

-- adoption_user stores every registered person on the platform.
-- Any user can act as publisher (post animals) or adopter (mark favorites),
-- there is no role separation at the table level.


-- =================
--  breed
-- =================

CREATE TABLE breed (
    id_breed    SERIAL       PRIMARY KEY,
    breed_name  VARCHAR(60)  NOT NULL,
    origin      VARCHAR(60),
    temperament VARCHAR(120),
    life_span   VARCHAR(30)
);

-- Constraints
ALTER TABLE breed ADD CONSTRAINT uq_breed_name CHECK (breed_name <> '');

-- breed holds the catalogue of known breeds for both dogs and cats.
-- life_span is stored as text (e.g. '10-14 years') to match common registry formats.


-- =================
--  animal
-- =================

CREATE TABLE animal (
    id_animal     SERIAL       PRIMARY KEY,
    id_user       INT          NOT NULL,
    id_breed      INT,
    animal_name   VARCHAR(40)  NOT NULL,
    species       VARCHAR(3)   NOT NULL,
    date_of_birth DATE,
    description   TEXT,
    size          VARCHAR(10),
    animal_zipcode VARCHAR(10) NOT NULL,
    published_at  TIMESTAMP    NOT NULL DEFAULT CURRENT_TIMESTAMP
);

-- Constraints
ALTER TABLE animal ADD CONSTRAINT fk_animal_user  FOREIGN KEY (id_user)  REFERENCES adoption_user (id_user) ON DELETE CASCADE;
ALTER TABLE animal ADD CONSTRAINT fk_animal_breed FOREIGN KEY (id_breed) REFERENCES breed (id_breed) ON DELETE SET NULL;

ALTER TABLE animal ADD CONSTRAINT uq_animal_name        CHECK (animal_name <> '');
ALTER TABLE animal ADD CONSTRAINT ck_animal_species     CHECK (species IN ('DOG', 'CAT'));
ALTER TABLE animal ADD CONSTRAINT ck_animal_size        CHECK (size IN ('small', 'medium', 'large', 'extra_large'));
ALTER TABLE animal ADD CONSTRAINT ck_animal_dob         CHECK (date_of_birth <= CURRENT_DATE);
ALTER TABLE animal ADD CONSTRAINT ck_animal_zipcode     CHECK (LENGTH(animal_zipcode) BETWEEN 5 AND 10);

-- animal represents a pet posted for adoption.
-- id_user references the publisher (the user who created the post).
-- id_breed is nullable: a mixed-breed or unknown animal can be registered without a breed.
-- species enforces the Dog/Cat disjoint specialisation from the ER diagram.
-- age is a derived attribute (calculated from date_of_birth) and is NOT stored.


-- =================
--  photo
-- =================

CREATE TABLE photo (
    id_photo  SERIAL        PRIMARY KEY,
    id_animal INT           NOT NULL,
    url       VARCHAR(512)  NOT NULL,
    width     INT,
    height    INT
);

-- Constraints
ALTER TABLE photo ADD CONSTRAINT fk_photo_animal FOREIGN KEY (id_animal) REFERENCES animal (id_animal) ON DELETE CASCADE;

ALTER TABLE photo ADD CONSTRAINT ck_photo_url    CHECK (url <> '');
ALTER TABLE photo ADD CONSTRAINT ck_photo_width  CHECK (width  > 0);
ALTER TABLE photo ADD CONSTRAINT ck_photo_height CHECK (height > 0);

-- photo stores the images associated with an animal post.
-- One animal can have many photos (1:M as shown in the ER diagram).
-- ON DELETE CASCADE ensures orphaned photos are removed when the animal post is deleted.


-- =================
--  animal_favorite
-- =================

CREATE TABLE animal_favorite (
    id_user    INT       NOT NULL,
    id_animal  INT       NOT NULL,
    saved_at   TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP
);

-- Constraints
ALTER TABLE animal_favorite ADD CONSTRAINT pk_animal_favorite PRIMARY KEY (id_user, id_animal);

ALTER TABLE animal_favorite ADD CONSTRAINT fk_favorite_user   FOREIGN KEY (id_user)   REFERENCES adoption_user (id_user) ON DELETE CASCADE;
ALTER TABLE animal_favorite ADD CONSTRAINT fk_favorite_animal FOREIGN KEY (id_animal) REFERENCES animal (id_animal)       ON DELETE CASCADE;

-- animal_favorite is the join table for the interest relationship (Adopter M:M Animal).
-- The composite primary key (id_user, id_animal) prevents duplicate favorites.
-- saved_at records when the user marked the animal as a favorite.


-- =================
--  post
-- =================

CREATE TABLE post(
    id_post       SERIAL      PRIMARY KEY,
    id_animal     INT         NOT NULL,
    description   TEXT,
    status        VARCHAR(20) NOT NULL DEFAULT 'ACTIVE',
    created_at    TIMESTAMP   NOT NULL DEFAULT CURRENT_TIMESTAMP
);

ALTER TABLE post ADD CONSTRAINT fk_post_animal FOREIGN KEY (id_animal)  REFERENCES animal (id_animal) ON DELETE CASCADE;

