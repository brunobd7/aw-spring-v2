CREATE TABLE "user" (
	id BIGINT PRIMARY KEY,
	name VARCHAR(50) NOT NULL,
	email VARCHAR(50) NOT NULL,
	password VARCHAR(150) NOT NULL
);

CREATE TABLE role (
	id BIGINT PRIMARY KEY,
	description VARCHAR(50) NOT NULL
);

CREATE TABLE user_role (
	id_user BIGINT NOT NULL,
	id_role BIGINT NOT NULL,
	PRIMARY KEY (id_user, id_role),
	FOREIGN KEY (id_user) REFERENCES "user"(id),
	FOREIGN KEY (id_role) REFERENCES role(id)
);

INSERT INTO "user" (id, name, email, password) values (1, 'Administrador', 'admin@algamoney.com', '$2a$10$X607ZPhQ4EgGNaYKt3n4SONjIv9zc.VMWdEuhCuba7oLAL5IvcL5.');
INSERT INTO "user" (id, name, email, password) values (2, 'Maria Silva', 'maria@algamoney.com', '$2a$10$Zc3w6HyuPOPXamaMhh.PQOXvDnEsadztbfi6/RyZWJDzimE8WQjaq');

INSERT INTO role (id, description) values (1, 'ROLE_CADASTRAR_CATEGORIA');
INSERT INTO role (id, description) values (2, 'ROLE_PESQUISAR_CATEGORIA');

INSERT INTO role (id, description) values (3, 'ROLE_CADASTRAR_PESSOA');
INSERT INTO role (id, description) values (4, 'ROLE_REMOVER_PESSOA');
INSERT INTO role (id, description) values (5, 'ROLE_PESQUISAR_PESSOA');

INSERT INTO role (id, description) values (6, 'ROLE_CADASTRAR_LANCAMENTO');
INSERT INTO role (id, description) values (7, 'ROLE_REMOVER_LANCAMENTO');
INSERT INTO role (id, description) values (8, 'ROLE_PESQUISAR_LANCAMENTO');

-- admin
INSERT INTO user_role (id_user, id_role) values (1, 1);
INSERT INTO user_role (id_user, id_role) values (1, 2);
INSERT INTO user_role (id_user, id_role) values (1, 3);
INSERT INTO user_role (id_user, id_role) values (1, 4);
INSERT INTO user_role (id_user, id_role) values (1, 5);
INSERT INTO user_role (id_user, id_role) values (1, 6);
INSERT INTO user_role (id_user, id_role) values (1, 7);
INSERT INTO user_role (id_user, id_role) values (1, 8);

-- maria
INSERT INTO user_role (id_user, id_role) values (2, 2);
INSERT INTO user_role (id_user, id_role) values (2, 5);
INSERT INTO user_role (id_user, id_role) values (2, 8);