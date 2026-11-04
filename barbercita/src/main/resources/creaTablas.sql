/*
  Script de creación de base de datos para BarberCita
  Este script crea el esquema, tablas, usuarios, y
  carga datos de ejemplo.
*/

-- Sección de administración (para ejecutar una vez)
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

create table usuario (
  id_usuario INT NOT NULL AUTO_INCREMENT,
  nombre VARCHAR(100) NOT NULL,
  correo VARCHAR(100) NOT NULL UNIQUE,
  telefono VARCHAR(20) NULL,
  activo boolean NOT NULL DEFAULT TRUE,
  rol ENUM('CLIENTE', 'BARBERO', 'ADMIN') NOT NULL DEFAULT 'CLIENTE',
  password varchar(512),
  foto varchar(1024),
  fecha_creacion TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
  fecha_modificacion TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (id_usuario))
  ENGINE = InnoDB;

-- Tabla de servicios
create table servicio (
  id_servicio INT NOT NULL AUTO_INCREMENT,
  nombre VARCHAR(50) NOT NULL,
  descripcion VARCHAR(255),
  precio decimal(12,2) NOT NULL CHECK (precio >= 0),
  duracion_min INT NOT NULL CHECK (duracion_min > 0),
  imagen_url varchar(1024),
  activo boolean NOT NULL DEFAULT TRUE,
  fecha_creacion TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
  fecha_modificacion TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (id_servicio))
  ENGINE = InnoDB;

-- Tabla de barberos (un barbero es un usuario con información adicional)
create table barbero (
  id_barbero INT NOT NULL AUTO_INCREMENT,
  id_usuario INT NOT NULL UNIQUE,
  especialidad VARCHAR(100),
  activo boolean NOT NULL DEFAULT TRUE,
  fecha_creacion TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
  fecha_modificacion TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (id_barbero),
  foreign key fk_barbero_usuario (id_usuario) references usuario(id_usuario))
  ENGINE = InnoDB;

-- Tabla de relación entre barberos y los servicios que realizan
create table barbero_servicio (
  id_barbero INT NOT NULL,
  id_servicio INT NOT NULL,
  fecha_creacion TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
  fecha_modificacion TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (id_barbero, id_servicio),
  foreign key fk_barberoServicio_barbero (id_barbero) references barbero(id_barbero),
  foreign key fk_barberoServicio_servicio (id_servicio) references servicio(id_servicio))
  ENGINE = InnoDB;

-- Tabla de horarios de trabajo de cada barbero (dia_semana: 1 = lunes ... 7 = domingo)
create table horario_barbero (
  id_horario INT NOT NULL AUTO_INCREMENT,
  id_barbero INT NOT NULL,
  dia_semana INT NOT NULL CHECK (dia_semana BETWEEN 1 AND 7),
  hora_inicio TIME NOT NULL,
  hora_fin TIME NOT NULL,
  fecha_creacion TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
  fecha_modificacion TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  CHECK (hora_fin > hora_inicio),
  PRIMARY KEY (id_horario),
  index ndx_barbero (id_barbero),
  foreign key fk_horario_barbero (id_barbero) references barbero(id_barbero))
  ENGINE = InnoDB;

-- Tabla de citas (el cliente es un usuario)
create table cita (
  id_cita INT NOT NULL AUTO_INCREMENT,
  id_cliente INT NOT NULL,
  id_barbero INT NOT NULL,
  id_servicio INT NOT NULL,
  fecha DATE NOT NULL,
  hora TIME NOT NULL,
  estado ENUM('Pendiente', 'Atendida', 'Cancelada') NOT NULL,
  observaciones VARCHAR(255),
  fecha_creacion TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
  fecha_modificacion TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (id_cita),
  index ndx_cliente (id_cliente),
  index ndx_barbero_fecha (id_barbero, fecha),
  foreign key fk_cita_usuario (id_cliente) references usuario(id_usuario),
  foreign key fk_cita_barbero (id_barbero) references barbero(id_barbero),
  foreign key fk_cita_servicio (id_servicio) references servicio(id_servicio))
  ENGINE = InnoDB;

-- Tabla de pagos (una cita solo puede tener un pago)
create table pago (
  id_pago INT NOT NULL AUTO_INCREMENT,
  id_cita INT NOT NULL UNIQUE,
  monto decimal(12,2) NOT NULL CHECK (monto > 0),
  metodo ENUM('Efectivo', 'SINPE', 'Tarjeta') NOT NULL,
  comprobante VARCHAR(50),
  descripcion VARCHAR(255),
  fecha DATE NOT NULL,
  fecha_creacion TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
  fecha_modificacion TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (id_pago),
  foreign key fk_pago_cita (id_cita) references cita(id_cita))
  ENGINE = InnoDB;


-- Tabla de notificaciones y pagos se trabajara en el avance 3.


-- --- Ingresamos datos de ejemplo ---

-- Inserción de usuarios (la contraseña de todos es Barber123*, guardada con BCrypt)
INSERT INTO usuario (nombre,correo,telefono,activo,rol,password) VALUES
('Carlos Barbero',   'carlos@barbercita.com',   '88881111', true,  'ADMIN',   '$2a$10$pli/uP9DE1op98NrpSVDq.Mm9E7xAbzKxZCf7vhXDL8OX5UbbAvVa'),
('Luis Barbero',     'luis@barbercita.com',     '88882222', true,  'BARBERO', '$2a$10$pNYtJEQVN4hELN7ccGWiM.guMpIdBqPXanEMmKleSuVTH4cU2vPwu'),
('Catalina Mora',    'catalina@barbercita.com', '88883333', true,  'BARBERO', '$2a$10$pNYtJEQVN4hELN7ccGWiM.guMpIdBqPXanEMmKleSuVTH4cU2vPwu'),
('Cliente Prueba',   'cliente@barbercita.com',  '88884444', true,  'CLIENTE', '$2a$10$xWtK6FU52oOq09vkLEhZUutzrRZZ2DIn.osB9EkEgpaFL.EDDpEq2'),
('Cliente Inactivo', 'inactivo@barbercita.com', '88885555', false, 'CLIENTE', '$2a$10$xWtK6FU52oOq09vkLEhZUutzrRZZ2DIn.osB9EkEgpaFL.EDDpEq2');

-- Inserción de servicios
INSERT INTO servicio (nombre,descripcion,precio,duracion_min,imagen_url,activo) VALUES
('Corte Clasico',      'Corte tradicional para caballero', 5000, 30, '/img/servicios/corte.jpg', true),
('Perfilado de Barba', 'Perfilado y arreglo de barba',     3500, 20, '/img/servicios/barba.jpg', true),
('Corte + Barba',      'Paquete completo',                 7500, 50, '/img/servicios/combo.jpg', true);

-- Inserción de barberos (Luis = barbero 1, Catalina = barbero 2)
INSERT INTO barbero (id_usuario,especialidad,activo) VALUES
(2, 'Cortes Clasicos',          true),
(3, 'Barba y estilos modernos', true);

-- Asignación de servicios a barberos (Luis hace los 3, Catalina barba y combo)
INSERT INTO barbero_servicio (id_barbero,id_servicio) VALUES
(1,1), (1,2), (1,3),
(2,2), (2,3);

-- Inserción de horarios de lunes (1) a miércoles (3)
INSERT INTO horario_barbero (id_barbero,dia_semana,hora_inicio,hora_fin) VALUES
(1, 1, '08:00:00', '17:00:00'),
(1, 2, '08:00:00', '17:00:00'),
(1, 3, '08:00:00', '17:00:00'),
(2, 1, '09:00:00', '18:00:00'),
(2, 2, '09:00:00', '18:00:00'),
(2, 3, '09:00:00', '18:00:00');

-- Inserción de citas (las fechas se calculan desde el día en que se corre el script)
INSERT INTO cita (id_cliente,id_barbero,id_servicio,fecha,hora,estado,observaciones) VALUES
(4, 1, 1, CURDATE(),                          '09:00:00', 'Pendiente', 'Primera cita'),
(4, 2, 3, DATE_ADD(CURDATE(), INTERVAL 2 DAY), '10:00:00', 'Pendiente', 'Corte y barba para evento'),
(4, 1, 1, DATE_SUB(CURDATE(), INTERVAL 7 DAY), '11:00:00', 'Atendida',  NULL),
(4, 2, 2, DATE_SUB(CURDATE(), INTERVAL 3 DAY), '15:00:00', 'Cancelada', NULL);

-- Inserción de pagos (solo la cita 3, que ya fue atendida)
INSERT INTO pago (id_cita,monto,metodo,comprobante,descripcion,fecha) VALUES
(3, 5000, 'Efectivo', NULL, 'Corte Clasico', DATE_SUB(CURDATE(), INTERVAL 7 DAY));
