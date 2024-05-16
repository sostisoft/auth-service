-- Crear la tabla User
CREATE TABLE IF NOT EXISTS User (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    username VARCHAR(255) NOT NULL,
    email VARCHAR(255) NOT NULL,
    encrypted_password VARCHAR(255) NOT NULL,
    role Varchar(20) NOT NULL
);

-- Contraseña password1
INSERT INTO User (username, email, encrypted_password, role)
SELECT 'usuario1', 'usuario1@example.com', '$2a$10$tLrtdPnjQtaqViSG/WcUPu7VDYta95IU647Ebwu4SOaD1GwF0Pygm', 'admin'
WHERE NOT EXISTS (SELECT 1 FROM User WHERE username = 'usuario1');

-- Contraseña password2
INSERT INTO User (username, email, encrypted_password, role)
SELECT 'usuario2', 'usuario2@example.com', '$2a$10$VA19GVqalNf72jh2OR7yduI4k5DJWmj7uqiZmZlXOHZCH9PfFGAM.', 'admin'
WHERE NOT EXISTS (SELECT 1 FROM User WHERE username = 'usuario2');

INSERT INTO User (username, email, encrypted_password, role)
SELECT 'usuario3', 'usuario3@example.com', '$2a$10$VA19GVqalNf72jh2OR7yduI4k5DJWmj7uqiZmZlXOHZCH9PfFGAM.', 'user'
WHERE NOT EXISTS (SELECT 1 FROM User WHERE username = 'usuario3');

INSERT INTO User (username, email, encrypted_password, role)
SELECT 'usuario4', 'usuario4@example.com', '$2a$10$VA19GVqalNf72jh2OR7yduI4k5DJWmj7uqiZmZlXOHZCH9PfFGAM.', 'user'
WHERE NOT EXISTS (SELECT 1 FROM User WHERE username = 'usuario4');