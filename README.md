# HealthApp - Sistema de Gestión de Citas Médicas 🏥

## 📖 Descripción del proyecto
HealthApp es una aplicación web desarrollada con el patrón MVC con Spring Boot, diseñada para gestionar citas médicas en un centro de salud. La aplicación permite a los clientes registrarse como pacientes, editar sus datos personales y pedir citas médicas de forma autónoma. Tambien cuenta con un panel Médico para gestionar las peticiones de pacientes

La aplicación diferencia el acceso y las funcionalidades según el rol del usuario:
* **Pacientes (`ROLE_PACIENTE`):** Pueden editar su perfil, pedir nuevas citas médicas, ver su historial, y editar o cancelar citas programadas.
* **Médicos (`ROLE_MEDICO`):** Tienen un panel donde pueden ver las peticiones de citas disponibles, añadirlas a su agenda y actualizar el estado delas citas (Realizada, Cancelada, Programada).

### Tecnologías utilizadas
* **Backend:** Java 17, Spring Boot 3, Spring Web, Spring Data JPA.
* **Seguridad:** Spring Security 6, JWT.
* **Base de Datos:** MySQL.
* **Frontend:** HTML5, CSS3, Thymeleaf.
* **Gestor de dependencias:** Maven.

---

## 🚀 Instrucciones de ejecución

Para ejecutar este proyecto en local, sigue estos pasos:

### 1. Requisitos previos
* Tener instalado **Java 17** o superior.
* Tener instalado **Maven**.
* Tener instalado y en ejecución un servidor **MySQL**.

### 2. Configuración de la Base de Datos
1. Abre tu gestor de base de datos (phpMyAdmin o MySQL Workbench).
2. Crea una base de datos vacía llamada `healthapp`:
   CREATE DATABASE healthapp;
3. En `src/main/resources/application.properties` tener esto:
spring.datasource.url=jdbc:mysql://localhost:3306/healthapp
spring.datasource.username=tu_usuario_mysql
spring.datasource.password=tu_contraseña_mysql
spring.jpa.hibernate.ddl-auto=update
server.port=8080
