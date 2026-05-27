# Servicio Técnico

## Descripción

Sistema desarrollado con Spring Boot para la gestión de empleados y procesos de un servicio técnico.

# Problemática

En los servicios técnicos, la administración de empleados y la gestión de solicitudes suelen realizarse mediante procesos manuales o sistemas poco integrados, lo que genera dificultades en la organización y control de la información.

Entre los principales problemas identificados se encuentran:

- Falta de centralización de datos.
- Dificultad para gestionar empleados y sus funciones.
- Retrasos en la atención y seguimiento de tickets.

Estas situaciones afectan directamente la eficiencia operativa del servicio técnico y la calidad de atención entregada a los usuarios.

# Solución

Para abordar esta problemática, se desarrolló una aplicación utilizando Spring Boot y arquitectura REST, orientada a la gestión de empleados dentro de un sistema de servicio técnico.

La solución implementada permite:

- Gestionar empleados de manera centralizada.
- Integrar comunicación con otros módulos del sistema mediante clientes HTTP.
- Implementar filtros y mecanismos de seguridad.
- Manejar excepciones globales de forma estructurada.
- Facilitar la escalabilidad y mantenimiento gracias a una arquitectura por capas.

El sistema contribuye a optimizar los procesos internos, aumentar la eficiencia en la administración del servicio técnico.

# Tecnologías Utilizadas

- Java
- Spring Boot
- Spring Security
- Spring Data JPA
- Gradle
- PostgreSQL


# Estructura del Proyecto

src/main/java/com/serviciotecnico/empleado
├── config
├── controller
├── dto
├── entity
├── exception
├── filter
├── repository
├── service
└── util

# Características Implementadas

    # Gestión de Empleados
    - Administración de empleados
    - Organización de información

    # Seguridad
    - Filtros de autenticación
    - Manejo de excepciones
    - Validación de acceso

    # Arquitectura
    - Separación por capas
    - Escalabilidad
    - Integración entre componentes

    # Testing
    - Tests unitarios
    - Tests de integración
    - Validación de servicios

