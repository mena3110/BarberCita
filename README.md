# BarberCita
Aplicación web de reservas para Navaja Barber Studio.

## Descripción
BarberCita es una aplicación web transaccional desarrollada para facilitar la gestión de citas de una barbería.

La aplicación permitirá a los clientes consultar los servicios disponibles, seleccionar un barbero, consultar horarios disponibles y reservar citas en línea.

Los barberos podrán consultar su agenda y gestionar el estado de sus citas, mientras que el administrador podrá gestionar servicios, barberos, horarios y consultar información del negocio.

## Objetivo
Desarrollar una aplicación web que permita organizar las reservas de Navaja Barber Studio, reducir los choques de horario y mejorar la experiencia de los clientes y la administración del negocio.

## Usuarios
* Cliente
* Barbero
* Administrador

## Tecnologías
* Java
* Spring Boot
* Spring MVC
* Thymeleaf
* Bootstrap
* Hibernate / JPA
* Base de datos relacional
* GitHub

## Arquitectura
El proyecto utilizará la arquitectura MVC (Model-View-Controller).

## Funcionalidades principales

### Cliente
* Registro e inicio de sesión
* Consulta de servicios
* Consulta de disponibilidad
* Reserva de citas
* Consulta de próximas citas
* Cancelación de citas
* Consulta del historial

### Barbero
* Consulta de agenda
* Gestión del estado de las citas
* Consulta de información relacionada con sus servicios

### Administrador
* Gestión de barberos
* Gestión de servicios
* Gestión de horarios
* Consulta de reportes

## Estructura de ramas
El proyecto utilizará una estrategia basada en ramas para organizar el desarrollo.

* `main`: versión estable del proyecto.
* `develop`: rama de integración de las funcionalidades.
* `feature/cuentas`: desarrollo relacionado con las cuentas de usuario.
* `feature/reservas`: desarrollo relacionado con las reservas y funcionalidades del cliente.
* `feature/barbero-admin`: desarrollo relacionado con las funciones del barbero y administrador.
* `feature/pagos`: desarrollo relacionado con pagos, notificaciones y funcionalidades de datos.

Las nuevas funcionalidades se desarrollaron en ramas `feature/` y posteriormente se integraron a `develop` mediante Pull Requests.

Cuando tuvimos la version estable, los cambios de `develop` se movieron  a `main`.

## Integrantes
* Catalina Mora Duran
* Jimena Rivera Sancho
* Olman Daniel Serrano González
* Valeria Ledezma Calvo


# Requisitos
JDK 21
NetBeans
MySQL 8 y MySQL Workbench
Archivo JSON de Firebase (se entrega en Moodle)
Instalación y ejecución
Base de datos

Se debe ejecutar el script creaTablas.sql que está en barbercita/src/main/resources con el usuario root en MySQL Workbench.

El script crea la base de datos barbercita, las tablas, los datos de prueba y el usuario que utiliza la aplicación:

Usuario: usuario_barbercita
Contraseña: Barber_Clave.2026

# Firebase
El archivo JSON de Firebase no se sube al repositorio por seguridad. Se debe crear la carpeta barbercita/src/main/resources/firebase y copiar ahí el archivo JSON.

# Ejecución

Abrir la carpeta barbercita en NetBeans, hacer Clean and Build y ejecutar el proyecto.

La aplicación se abre en http://localhost.

# Usuarios de prueba

Todos los usuarios tienen la contraseña Barber123*

Administrador: carlos@barbercita.com
Barbero: luis@barbercita.com
Barbero: catalina@barbercita.com
Cliente: cliente@barbercita.com
Cliente inactivo: inactivo@barbercita.com


# Estado del avance

Historias implementadas
HU-02 Inicio de sesión
HU-06 Consultar servicios
HU-07 Reservar una cita
HU-08 Consultar próximas citas
HU-09 Cancelar una cita
HU-11 Consultar agenda diaria
HU-12 Actualizar estado de cita
HU-13 Gestionar servicios
HU-16 Registrar pago de una cita

Historias pendientes
HU-01 Registro de cliente
HU-03 Editar perfil
HU-04 Recuperar contraseña
HU-05 Gestionar cuentas de barberos
HU-10 Consultar historial de citas
HU-14 Consultar agenda general
HU-15 Generar reportes de citas
HU-17 Consultar historial de pagos
HU-18 Consultar reporte de ingresos
HU-19 Recibir confirmación por correo
HU-20 Recibir recordatorio de cita