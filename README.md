# Proyecto Grupo 7 Gym Manager

Integrante:
- Brayan Ruiz Valverde

Gym Manager es un sistema de gestion web orientado a la gestion
de Gimnacios. Permite centralizar y automatizar la administracion 
integral de un gimnasio: control de inventario, gestion de miembros 
e instructores, registro depagos mensuales, asignacion de rutinas 
de entrenamiento y mucho mas.

TECNOLOGIAS UTILIZADAS
----------------------------------------------------------------
- Java / Spring Boot (arquitectura MVC)
- Thymeleaf
- Bootstrap 5.3.7 (via WebJars)
- Spring Data JPA
- MySQL
- Lombok
- Spring Validation
- Autenticacion manual por sesion (sin Spring Security)

- ROLES DEL SISTEMA
----------------------------------------------------------------
- Administrador: control total sobre miembros, instructores,
  inventario, pagos y activacion/desactivacion de cuentas.
- Instructor: creacion y gestion de rutinas de entrenamiento
  asignadas a miembros.
- Miembro: consulta de sus rutinas y de su informacion personal.

VIDEO EXPLICATIVO DE LA APLICACION
-----------------------------------------------------------------
https://youtu.be/v1ZRuCswLcc 

HISTORIAS DE USUARIO COMPLETAS
-----------------------------------------------------------------
HU-01 - Iniciar sesion - Prioridad: Alta
 
HU-04 - Restriccion de acceso por rol - Prioridad: Alta
 
HU-05 - Registrar miembros - Prioridad: Alta
 
HU-06 - Registrar y gestionar instructores - Prioridad: Alta
