/*
  Persona 1: login y autenticacion de usuarios
  Este script NO borra datos. Solo agrega las columnas rol, password y foto
  a la tabla usuario y les pone rol y contrasena a los usuarios de prueba.

  Se ejecuta UNA sola vez, despues de Barbercita.sql, con el usuario root
  (usuario_barbercita no tiene permiso para hacer ALTER TABLE).

  Contrasena de prueba de todos los usuarios: Barber123*
  (se guarda encriptada con BCrypt, nunca en texto plano)
*/
use barbercita;

-- Columnas nuevas para el login
ALTER TABLE usuario
    ADD COLUMN rol VARCHAR(20) NOT NULL DEFAULT 'CLIENTE',
    ADD COLUMN password VARCHAR(512),
    ADD COLUMN foto VARCHAR(1024);

-- Roles de los usuarios de prueba (CLIENTE, BARBERO o ADMIN)
UPDATE usuario SET rol = 'ADMIN'   WHERE correo = 'carlos@barbercita.com';
UPDATE usuario SET rol = 'BARBERO' WHERE correo IN ('luis@barbercita.com', 'catalina@barbercita.com');
UPDATE usuario SET rol = 'CLIENTE' WHERE correo = 'cliente@barbercita.com';

-- Contrasena Barber123* encriptada con BCrypt
UPDATE usuario SET password = '$2a$10$pli/uP9DE1op98NrpSVDq.Mm9E7xAbzKxZCf7vhXDL8OX5UbbAvVa'
    WHERE correo = 'carlos@barbercita.com';
UPDATE usuario SET password = '$2a$10$pNYtJEQVN4hELN7ccGWiM.guMpIdBqPXanEMmKleSuVTH4cU2vPwu'
    WHERE correo IN ('luis@barbercita.com', 'catalina@barbercita.com');
UPDATE usuario SET password = '$2a$10$xWtK6FU52oOq09vkLEhZUutzrRZZ2DIn.osB9EkEgpaFL.EDDpEq2'
    WHERE correo = 'cliente@barbercita.com';

-- Usuario inactivo para probar que no pueda iniciar sesion (si ya existe no se agrega)
INSERT IGNORE INTO usuario (nombre, correo, telefono, activo, rol, password)
VALUES ('Cliente Inactivo', 'inactivo@barbercita.com', '88885555', FALSE, 'CLIENTE',
        '$2a$10$xWtK6FU52oOq09vkLEhZUutzrRZZ2DIn.osB9EkEgpaFL.EDDpEq2');
