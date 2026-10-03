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

Las nuevas funcionalidades se desarrollarán en ramas `feature/` y posteriormente se integrarán a `develop` mediante Pull Requests.

Cuando una versión estable esté lista, los cambios de `develop` se integrarán a `main`.

## Integrantes
* Catalina Mora Duran
* Jimena Rivera Sancho
* Olman Daniel Serrano González
* Valeria Ledezma Calvo
