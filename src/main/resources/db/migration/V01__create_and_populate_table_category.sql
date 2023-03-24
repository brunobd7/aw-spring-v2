CREATE TABLE category(
    id BIGSERIAL PRIMARY KEY, description VARCHAR(50) NOT NULL
);

INSERT INTO category (description) VALUES ('Leasure');
INSERT INTO category (description) VALUES ('Food');
INSERT INTO category (description) VALUES ('Shopping');
INSERT INTO category (description) VALUES ('Medicines');
INSERT INTO category (description) VALUES ('Others');