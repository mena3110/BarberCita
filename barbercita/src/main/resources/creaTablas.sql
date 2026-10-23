/*
  Script de creación de base de datos para BarberCita
  Este script crea el esquema, tablas, usuarios, y
  carga datos de ejemplo.
*/
-- Sección de administración (ejecutar una vez en un entorno de desarrollo)
drop database if exists barbercita;
drop user if exists usuario_barbercita;
drop user if exists usuario_reportes_barber;

-- Creación del esquema
CREATE database barbercita
  DEFAULT CHARACTER SET utf8mb4
  DEFAULT COLLATE utf8mb4_unicode_ci;

-- Creación de usuarios
create user 'usuario_barbercita'@'%' identified by 'Barber_Clave.2026';
create user 'usuario_reportes_barber'@'%' identified by 'Reportes_Clave.2026';

-- Asignación de permisos
grant select, insert, update, delete on barbercita.* to 'usuario_barbercita'@'%';
grant select on barbercita.* to 'usuario_reportes_barber'@'%';
flush privileges;

use barbercita;

-- --- Sección de Creación de Tablas ---