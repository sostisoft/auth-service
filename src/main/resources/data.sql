-- Crear la tabla User
CREATE TABLE IF NOT EXISTS User (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    username VARCHAR(255) NOT NULL,
    email VARCHAR(255) NOT NULL,
    password VARCHAR(255) NOT NULL
);

-- Insertar valores iniciales en la tabla User
INSERT INTO User (username, email, password) VALUES ('usuario1', 'usuario1@example.com', 'password1');
INSERT INTO User (username, email, password) VALUES ('usuario2', 'usuario2@example.com', 'password2');
