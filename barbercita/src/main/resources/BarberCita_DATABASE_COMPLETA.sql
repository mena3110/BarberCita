/*
  BarberCita - Script COMPLETO de base de datos
  Navaja Barber Studio

  Este script crea el esquema desde cero, las tablas, los usuarios de MySQL
  y carga los datos de ejemplo.

  Esta basado en Barbercita.sql (trabajo original del grupo) y agrega:
    - Las columnas rol, password y foto de la tabla usuario (Persona 1: login).
    - Los roles y contrasenas cifradas (BCrypt) de los usuarios de prueba.
    - Un usuario inactivo para probar el bloqueo de acceso.
    - La correccion de los ids de barbero en barbero_servicio, horario_barbero y cita
      (en el original usaban 3 y 4, pero los barberos se crean con 1 y 2).

  OJO: la primera seccion BORRA la base barbercita si ya existe.
  Ejecutar con el usuario root desde MySQL Workbench (Query > Execute All).

  Contrasena de prueba de los usuarios de la aplicacion: Barber123*
  (en la base solo se guarda el hash BCrypt, nunca el texto plano)
*/

-- =========================================================
-- 1. Seccion de administracion (ejecutar una vez en un entorno de desarrollo)
-- =========================================================
drop database if exists barbercita;
drop user if exists usuario_barbercita;
drop user if exists usuario_reportes_barber;

-- Creacion del esquema
CREATE database barbercita
  DEFAULT CHARACTER SET utf8mb4
  DEFAULT COLLATE utf8mb4_unicode_ci;

-- Creacion de usuarios de MySQL
-- (son las claves de desarrollo del proyecto; la del usuario_barbercita
--  debe coincidir con spring.datasource.password de application.properties)
create user 'usuario_barbercita'@'%' identified by 'Barber_Clave.2026';
create user 'usuario_reportes_barber'@'%' identified by 'Reportes_Clave.2026';

-- Asignacion de permisos
grant select, insert, update, delete on barbercita.* to 'usuario_barbercita'@'%';
grant select on barbercita.* to 'usuario_reportes_barber'@'%';
flush privileges;

use barbercita;

-- =========================================================
-- 2. Creacion de tablas
--    (el orden respeta las llaves foraneas: primero las tablas padre)
-- =========================================================

-- Usuarios del sistema (clientes, barberos y administrador)
CREATE TABLE usuario (
    id_usuario INT AUTO_INCREMENT PRIMARY KEY,

    nombre VARCHAR(100) NOT NULL,

    correo VARCHAR(100) NOT NULL UNIQUE,

    telefono VARCHAR(20),

    activo BOOLEAN NOT NULL DEFAULT TRUE,

    -- Persona 1: campos para el login (entidad Usuario)
    -- rol: CLIENTE, BARBERO o ADMIN
    rol VARCHAR(20) DEFAULT 'CLIENTE',
    -- contrasena cifrada con BCrypt
    password VARCHAR(512),
    -- ruta de la foto del usuario (opcional)
    foto VARCHAR(1024)
);

CREATE TABLE servicio (
    id_servicio INT AUTO_INCREMENT PRIMARY KEY,
    nombre VARCHAR(50) NOT NULL,
    descripcion VARCHAR(255),
    precio DECIMAL(12,2) NOT NULL,
    duracion_min INT NOT NULL,
    imagen_url VARCHAR(1024),
    activo BOOLEAN NOT NULL DEFAULT TRUE
);

-- El barbero es un usuario con informacion adicional
CREATE TABLE barbero (
    id_barbero INT AUTO_INCREMENT PRIMARY KEY,

    id_usuario INT NOT NULL UNIQUE,

    especialidad VARCHAR(100),

    activo BOOLEAN NOT NULL DEFAULT TRUE,

    FOREIGN KEY(id_usuario)
        REFERENCES usuario(id_usuario)
);

-- Servicios que puede hacer cada barbero
CREATE TABLE barbero_servicio (
    id_barbero INT NOT NULL,
    id_servicio INT NOT NULL,

    PRIMARY KEY(id_barbero,id_servicio),

    FOREIGN KEY (id_barbero)
        REFERENCES barbero(id_barbero),

    FOREIGN KEY (id_servicio)
        REFERENCES servicio(id_servicio)
);

-- Horario de trabajo de cada barbero (1 = lunes ... 7 = domingo)
CREATE TABLE horario_barbero (
    id_horario INT AUTO_INCREMENT PRIMARY KEY,

    id_barbero INT NOT NULL,

    dia_semana INT NOT NULL,

    hora_inicio TIME NOT NULL,
    hora_fin TIME NOT NULL,

    FOREIGN KEY (id_barbero)
        REFERENCES barbero(id_barbero)
);

-- Citas: el cliente es un usuario (id_cliente -> usuario.id_usuario)
CREATE TABLE cita (
    id_cita INT AUTO_INCREMENT PRIMARY KEY,

    id_cliente INT NOT NULL,
    id_barbero INT NOT NULL,
    id_servicio INT NOT NULL,

    fecha DATE NOT NULL,
    hora TIME NOT NULL,

    -- Pendiente, Atendida o Cancelada
    estado VARCHAR(20) NOT NULL,
    observaciones VARCHAR(255),

    FOREIGN KEY (id_cliente)
        REFERENCES usuario(id_usuario),

    FOREIGN KEY (id_barbero)
        REFERENCES barbero(id_barbero),

    FOREIGN KEY (id_servicio)
        REFERENCES servicio(id_servicio)
);

-- =========================================================
-- 3. Datos de ejemplo originales
--    Para no depender de que los AUTO_INCREMENT empiecen en 1, los ids
--    se buscan por el correo del usuario o el nombre del servicio.
-- =========================================================

-- Usuarios originales
INSERT INTO usuario
(nombre, correo, telefono, activo)
VALUES

('Carlos Barbero',
 'carlos@barbercita.com',
 '88881111',
 TRUE),

('Luis Barbero',
 'luis@barbercita.com',
 '88882222',
 TRUE),

('Catalina Mora',
 'catalina@barbercita.com',
 '88883333',
 TRUE),

('Cliente Prueba',
 'cliente@barbercita.com',
 '88884444',
 TRUE);

-- Ids reales de los usuarios que se usan en las relaciones
SET @usuario_luis     = (SELECT id_usuario FROM usuario WHERE correo = 'luis@barbercita.com');
SET @usuario_catalina = (SELECT id_usuario FROM usuario WHERE correo = 'catalina@barbercita.com');
SET @usuario_cliente  = (SELECT id_usuario FROM usuario WHERE correo = 'cliente@barbercita.com');

-- Barberos (igual que el original: Luis y Catalina)
INSERT INTO barbero
(id_usuario, especialidad, activo)
VALUES
(@usuario_luis,'Cortes Clasicos',true),
(@usuario_catalina,'Barba y estilos modernos',true);

-- Ids reales de los barberos creados
-- (en el original se usaban 3 y 4, pero en realidad quedan con 1 y 2)
SET @barbero_luis     = (SELECT id_barbero FROM barbero WHERE id_usuario = @usuario_luis);
SET @barbero_catalina = (SELECT id_barbero FROM barbero WHERE id_usuario = @usuario_catalina);

-- Servicios originales
INSERT INTO servicio
(nombre, descripcion, precio, duracion_min, imagen_url, activo)
VALUES
('Corte Clasico',
 'Corte tradicional para caballero',
 5000,
 30,
 '/img/servicios/corte.jpg',
 true),

('Perfilado de Barba',
 'Perfilado y arreglo de barba',
 3500,
 20,
 '/img/servicios/barba.jpg',
 true),

('Corte + Barba',
 'Paquete completo',
 7500,
 50,
 '/img/servicios/combo.jpg',
 true);

-- Ids reales de los servicios creados
SET @servicio_corte = (SELECT id_servicio FROM servicio WHERE nombre = 'Corte Clasico');
SET @servicio_barba = (SELECT id_servicio FROM servicio WHERE nombre = 'Perfilado de Barba');
SET @servicio_combo = (SELECT id_servicio FROM servicio WHERE nombre = 'Corte + Barba');

-- Servicios de cada barbero
-- original: (3,1),(3,2),(3,3) -> Luis hace los 3 servicios
--           (4,2),(4,3)       -> Catalina hace barba y combo
INSERT INTO barbero_servicio
(id_barbero,id_servicio)
VALUES

(@barbero_luis,@servicio_corte),
(@barbero_luis,@servicio_barba),
(@barbero_luis,@servicio_combo),

(@barbero_catalina,@servicio_barba),
(@barbero_catalina,@servicio_combo);

-- Horarios de los barberos (lunes, martes y miercoles)
INSERT INTO horario_barbero
(id_barbero,dia_semana,hora_inicio,hora_fin)
VALUES

(@barbero_luis,1,'08:00:00','17:00:00'),
(@barbero_luis,2,'08:00:00','17:00:00'),
(@barbero_luis,3,'08:00:00','17:00:00'),

(@barbero_catalina,1,'09:00:00','18:00:00'),
(@barbero_catalina,2,'09:00:00','18:00:00'),
(@barbero_catalina,3,'09:00:00','18:00:00');

-- Cita de prueba: Cliente Prueba con Luis, Corte Clasico
-- (original: cliente 4, barbero 3, servicio 1)
INSERT INTO cita
(id_cliente,id_barbero,id_servicio,fecha,hora,estado,observaciones)
VALUES
(
@usuario_cliente,
@barbero_luis,
@servicio_corte,
CURDATE(),
'09:00:00',
'Pendiente',
'Primera cita'
);

-- =========================================================
-- 4. Persona 1: datos para el login
-- =========================================================

-- Roles de los usuarios de prueba (CLIENTE, BARBERO o ADMIN)
UPDATE usuario SET rol = 'ADMIN'   WHERE correo = 'carlos@barbercita.com';
UPDATE usuario SET rol = 'BARBERO' WHERE correo IN ('luis@barbercita.com', 'catalina@barbercita.com');
UPDATE usuario SET rol = 'CLIENTE' WHERE correo = 'cliente@barbercita.com';

-- Contrasena Barber123* cifrada con BCrypt
UPDATE usuario SET password = '$2a$10$pli/uP9DE1op98NrpSVDq.Mm9E7xAbzKxZCf7vhXDL8OX5UbbAvVa'
    WHERE correo = 'carlos@barbercita.com';
UPDATE usuario SET password = '$2a$10$pNYtJEQVN4hELN7ccGWiM.guMpIdBqPXanEMmKleSuVTH4cU2vPwu'
    WHERE correo IN ('luis@barbercita.com', 'catalina@barbercita.com');
UPDATE usuario SET password = '$2a$10$xWtK6FU52oOq09vkLEhZUutzrRZZ2DIn.osB9EkEgpaFL.EDDpEq2'
    WHERE correo = 'cliente@barbercita.com';

-- Usuario inactivo para probar que no pueda iniciar sesion
INSERT INTO usuario
(nombre, correo, telefono, activo, rol, password)
VALUES
('Cliente Inactivo',
 'inactivo@barbercita.com',
 '88885555',
 FALSE,
 'CLIENTE',
 '$2a$10$xWtK6FU52oOq09vkLEhZUutzrRZZ2DIn.osB9EkEgpaFL.EDDpEq2');

-- =========================================================
-- 5. Revision rapida (se puede ejecutar aparte)
-- =========================================================
SELECT u.id_usuario, u.nombre, u.correo, u.rol, u.activo,
       (u.password LIKE '$2a$10$%') AS password_bcrypt
FROM usuario u
ORDER BY u.id_usuario;
