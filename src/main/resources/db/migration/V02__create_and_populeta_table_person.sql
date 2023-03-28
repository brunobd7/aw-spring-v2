CREATE TABLE person
(
    id                     BIGSERIAL PRIMARY KEY,
    name                   VARCHAR(50) NOT NULL,
    is_active              BOOLEAN,
    street                 VARCHAR(100),
    house_number           INTEGER,
    complement             VARCHAR(100),
    disctrict_neighborhood VARCHAR(100),
    postal_code            VARCHAR(100),
    city                   VARCHAR(100)
);



INSERT INTO person (name, is_active, street, house_number, complement, disctrict_neighborhood, postal_code, city)
    VALUES ('Bruno',true,'MAIN AVENUE',1,'MAIN BLOCK','DOWNTOWN','7777777','GYN');