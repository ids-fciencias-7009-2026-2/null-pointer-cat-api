-- ==============
--  AdoptionUser
-- ==============

CREATE TABLE AdoptionUser (
    IdAdoptionUser INTEGER,
    FirstName VARCHAR(20),
    LastName VARCHAR(50),
    DateOfBirth DATE,
    Gender VARCHAR(10),
    Email VARCHAR(256),
    Zipcode VARCHAR(10),
    RegisterDate TIMESTAMP,
    AdoptionUserPassword VARCHAR(128),
    Token VARCHAR(256) -- Create separate table?
);

-- Primary Key and Constraints
ALTER TABLE AdoptionUser ADD CONSTRAINT pk_AdoptionUser PRIMARY KEY (IdAdoptionUser);
ALTER TABLE AdoptionUser ADD CONSTRAINT CK_Zipcode CHECK (Zipcode BETWEEN 5 AND 10);
ALTER TABLE AdoptionUser ADD CONSTRAINT CK_Gender CHECK (Sexo IN ('M', 'F', 'Other'));
ALTER TABLE AdoptionUser ADD CONSTRAINT CK_BirthDate CHECK (BirthDate <= CURRENT_DATE);
ALTER TABLE AdoptionUser ADD CONSTRAINT CK_MaxAge CHECK (BirthDate >= CURRENT_DATE - INTERVAL '120 years');
ALTER TABLE AdoptionUser ADD CONSTRAINT CK_FirstName CHECK (FirstName <> '');
ALTER TABLE AdoptionUser ADD CONSTRAINT CK_LastName CHECK (LastName <> '');

-- Not Null Constraints
ALTER TABLE AdoptionUser ALTER COLUMN FirstName SET NOT NULL;
ALTER TABLE AdoptionUser ALTER COLUMN LastName SET NOT NULL;
ALTER TABLE AdoptionUser ALTER COLUMN DateOfBirth SET NOT NULL;
ALTER TABLE AdoptionUser ALTER COLUMN Gender SET NOT NULL;
ALTER TABLE AdoptionUser ALTER COLUMN Email SET NOT NULL;
ALTER TABLE AdoptionUser ALTER COLUMN Zipcode SET NOT NULL;
ALTER TABLE AdoptionUser ALTER COLUMN RegisterDate SET NOT NULL;
ALTER TABLE AdoptionUser ALTER COLUMN AdoptionUserPassword SET NOT NULL;

-- Sequence Setup
CREATE SEQUENCE seq_AdoptionUser
    START 1
    INCREMENT 1
    OWNED BY AdoptionUser.IdAdoptionUser;

ALTER TABLE AdoptionUser
ALTER COLUMN IdAdoptionUser SET DEFAULT nextval('seq_AdoptionUser');

-- DOCUMENTATION OF AdoptaUser
COMMENT ON TABLE AdoptionUser IS 'Table storing personal data for individuals registered in the adoption system.';

-- Column Comments
COMMENT ON COLUMN AdoptionUser.IdUser IS 'Unique primary identifier for the adoption user.';
COMMENT ON COLUMN AdoptionUser.FirstName IS 'First name of the user.';
COMMENT ON COLUMN AdoptionUser.LastName IS 'Last name of the user.';
COMMENT ON COLUMN AdoptionUser.BirthDate IS 'Birth date of the user (YYYY-MM-DD).';
COMMENT ON COLUMN AdoptionUser.Gender IS 'Gender identity of the user (M, F, or Other).';
COMMENT ON COLUMN AdoptionUser.Email IS 'Primary email address for system access and notifications.';
COMMENT ON COLUMN AdoptionUser.Zipcode IS 'Postal code of the user''s current residence.';
COMMENT ON COLUMN AdoptionUser.RegisterDate IS 'Timestamp recording when the user joined the adoption platform.';
COMMENT ON COLUMN AdoptionUser.UserPassword IS 'Encrypted hash of the user''s login password.';

-- Constraints Comments
COMMENT ON CONSTRAINT pk_AdoptionUser ON AdoptionUser IS 'Primary key identifying each unique user in the adoption system.';
COMMENT ON CONSTRAINT CK_Zipcode_Range ON AdoptionUser IS 'Ensures the Zipcode is within a valid numeric range.';
COMMENT ON CONSTRAINT CK_Gender ON AdoptionUser IS 'Restricts gender values to M, F, or Other.';
COMMENT ON CONSTRAINT CK_BirthDate ON AdoptionUser IS 'Validates that the birth date is not set in the future.';
COMMENT ON CONSTRAINT CK_MaxAge ON AdoptionUser IS 'Enforces a logical age limit (maximum 120 years).';
COMMENT ON CONSTRAINT CK_FirstName ON AdoptionUser IS 'Ensures the first name is not left as an empty string.';
COMMENT ON CONSTRAINT CK_LastName ON AdoptionUser IS 'Ensures the last name is not left as an empty string.';

-- Sequence Comment
COMMENT ON SEQUENCE seq_AdoptionUser IS 'Sequence for generating auto-incrementing IDs for the AdoptionUser table.';


-- ==============
--  UserPhone
-- ==============

CREATE TABLE UserPhone (
    IdPhone INT,
    IdUser INT,
    PhoneNumber VARCHAR(20),
    PhoneType VARCHAR(20),
    IsPrimary BOOLEAN,
);

-- Primary Key
ALTER TABLE UserPhone ADD CONSTRAINT pk_UserPhone PRIMARY KEY (IdPhone);

-- Foreign Keys
ALTER TABLE UserPhone ADD CONSTRAINT fk_Phone_AdoptionUser 
    FOREIGN KEY (IdUser) REFERENCES AdoptionUser(IdUser) 
    ON DELETE CASCADE;

-- UserPhone Checks & Uniqueness
ALTER TABLE UserPhone ADD CONSTRAINT CK_Phone_Length CHECK (LENGTH(PhoneNumber) >= 7);
ALTER TABLE UserPhone ADD CONSTRAINT UQ_User_Phone_Pair UNIQUE (IdUser, PhoneNumber);
ALTER TABLE UserPhone ALTER COLUMN PhoneType SET DEFAULT 'Mobile';
ALTER TABLE UserPhone ALTER COLUMN IsPrimary SET DEFAULT TRUE;

-- Not Null Constraints
ALTER TABLE UserPhone ALTER COLUMN IdUser SET NOT NULL;
ALTER TABLE UserPhone ALTER COLUMN PhoneNumber SET NOT NULL;

-- UserPhone Sequence
CREATE SEQUENCE seq_UserPhone START 1 INCREMENT 1 OWNED BY UserPhone.IdPhone;
ALTER TABLE UserPhone ALTER COLUMN IdPhone SET DEFAULT nextval('seq_UserPhone');

-- DOCUMENTATION OF UserPhone
COMMENT ON TABLE UserPhone IS 'Stores contact numbers associated with users.';

-- Column Comments
COMMENT ON COLUMN UserPhone.IdPhone IS 'Unique primary identifier for the phone record.';
COMMENT ON COLUMN UserPhone.IdUser IS 'Foreign key linking the phone number to a specific AdoptionUser.';
COMMENT ON COLUMN UserPhone.PhoneNumber IS 'The actual telephone string, stored in E.164 format (e.g., +1234567890) to support international dialing.';
COMMENT ON COLUMN UserPhone.PhoneType IS 'The category of the phone number, such as "Mobile", "Home", "Work", or "Emergency". Set to Mobile by default.';
COMMENT ON COLUMN UserPhone.IsPrimary IS 'A boolean flag indicating if this is the user''s preferred or default contact number. Set to TRUE by default.';

-- Constraints Comments
COMMENT ON CONSTRAINT fk_Phone_AdoptionUser ON UserPhone IS 'Links phone records to a specific AdoptionUser.';
COMMENT ON CONSTRAINT UQ_User_Phone_Pair ON UserPhone IS 'Prevents duplicate phone numbers for the same user.';

-- Sequence Comment
COMMENT ON SEQUENCE seq_UserPhone IS 'Sequence for generating auto-incrementing IDs for the UserPhone table.';