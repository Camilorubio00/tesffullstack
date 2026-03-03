CREATE DATABASE IF NOT EXISTS bd_rubio; 
USE bd_rubio;

CREATE TABLE IF NOT EXISTS person (
    id INT AUTO_INCREMENT PRIMARY KEY,
    nombre VARCHAR(100),
    apellido VARCHAR(100),
    fechaNacimiento DATE,
    puesto VARCHAR(100),
    sueldo DECIMAL(10, 2)
);

CREATE USER IF NOT EXISTS 'conexión'@'localhost' IDENTIFIED BY 'Password123*';
GRANT ALL PRIVILEGES ON bd_rubio.* TO 'conexión'@'localhost';
FLUSH PRIVILEGES;