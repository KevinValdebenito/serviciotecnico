# Servicio Técnico

Sistema de gestión para un servicio técnico desarrollado con **Java 25 y Spring Boot 4.1.0**, organizado con una arquitectura de **microservicios**. La solución permite administrar clientes, empleados, tickets, equipos, diagnósticos, repuestos, cotizaciones y notificaciones, además de incorporar autenticación mediante JWT y un BFF para centralizar consultas.

## Objetivo del proyecto

El proyecto busca resolver la gestión fragmentada de un servicio técnico. Cada microservicio tiene una responsabilidad concreta y, cuando corresponde, utiliza una base de datos PostgreSQL independiente para respetar el patrón **Database per Service**.

La solución está compuesta por:

- **1 BFF**.
- **9 microservicios de negocio y soporte**.
- **4 microservicios nuevos con base de datos propia**: equipo, repuesto, diagnóstico y cotización.
- **1 microservicio sin base de datos**: notificación.

## Arquitectura general

| Módulo | Responsabilidad | Base de datos |
|---|---|---|
| **bff** | Centraliza el acceso del cliente y combina información de otros microservicios. | No |
| **auth** | Registro, inicio de sesión y generación de tokens JWT. | `auth_db` |
| **empleado** | Gestiona empleados o técnicos y consulta tickets asociados. | `employee_db` |
| **ticket** | Administra solicitudes de atención y sus estados. | `ticket` |
| **cliente** | Administra los datos de los clientes. | `cliente_db` |
| **equipo** | Registra los equipos de los clientes. | `equipo_db` |
| **repuesto** | Administra inventario, precios y stock de repuestos. | `repuesto_db` |
| **diagnostico** | Registra diagnósticos técnicos asociados a tickets y empleados. | `diagnostico_db` |
| **notificacion** | Simula el envío de notificaciones cuando cambia el estado de un ticket. | No |
| **cotizacion** | Calcula y guarda cotizaciones utilizando tickets, mano de obra y repuestos. | `cotizacion_db` |

## Comunicación entre microservicios

La comunicación principal implementada es:

```text
BFF ───────► auth
BFF ───────► ticket
BFF ───────► cliente
BFF ───────► empleado

empleado ──► ticket
cotizacion ► ticket
cotizacion ► repuesto
```

El microservicio `cotizacion` reenvía el encabezado `Authorization` a `ticket` y `repuesto` para mantener la autenticación JWT durante las llamadas internas.

## Tecnologías utilizadas

- Java 25
- Spring Boot 4.1.0
- Spring Web MVC / REST
- Spring Security
- Spring Data JPA
- JWT
- PostgreSQL
- Flyway
- Gradle
- Docker y Docker Compose
- JUnit y Mockito
- JaCoCo
- Springdoc OpenAPI / Swagger
- Spring Boot Actuator

## Puertos del sistema

### Aplicaciones

| Servicio | Puerto |
|---|---:|
| BFF | 8080 |
| Empleado | 8081 |
| Ticket | 8082 |
| Cliente | 8083 |
| Equipo | 8084 |
| Repuesto | 8085 |
| Diagnóstico | 8086 |
| Notificación | 8087 |
| Cotización | 8088 |
| Auth | 7778 |

### PostgreSQL en ejecución local

| Base de datos | Puerto local | Puerto interno Docker |
|---|---:|---:|
| Ticket | 5433 | 5432 |
| Cliente | 5434 | 5432 |
| Auth | 5435 | 5432 |
| Empleado | 5436 | 5432 |
| Cotización | 5437 | 5432 |
| Equipo | 5438 | 5432 |
| Repuesto | 5439 | 5432 |
| Diagnóstico | 5440 | 5432 |

`notificacion` no utiliza PostgreSQL porque solamente simula el envío y registra la operación mediante logs.

## Funcionalidades y endpoints

### Auth

Gestiona el registro y el inicio de sesión. El JWT funciona como sesión **stateless** y tiene una duración configurada de 24 horas.

```http
POST /register
POST /login
```

### Cliente

CRUD de clientes utilizando el correo electrónico como identificador de consulta.

```http
GET    /api/clientes
GET    /api/clientes/{email}
POST   /api/clientes
PUT    /api/clientes/{email}
DELETE /api/clientes/{email}
```

### Empleado

Gestiona empleados y consulta los tickets relacionados.

```http
POST /api/empleados
GET  /api/empleados/{id}
GET  /api/empleados/tickets
```

### Ticket

CRUD de solicitudes de atención. El listado permite aplicar filtros por título, estado, prioridad y empleado asignado.

```http
GET    /api/tickets
GET    /api/tickets/{id}
POST   /api/tickets
PUT    /api/tickets/{id}
DELETE /api/tickets/{id}
```

### Equipo

CRUD de equipos asociados a clientes. Registra correo del cliente, tipo, marca, modelo y número de serie.

```http
GET    /api/equipos
GET    /api/equipos/{id}
POST   /api/equipos
PUT    /api/equipos/{id}
DELETE /api/equipos/{id}
```

### Repuesto

CRUD de repuestos e inventario. Incluye operaciones para aumentar o disminuir stock y evita que el stock quede negativo.

```http
GET    /api/repuestos
GET    /api/repuestos/{id}
POST   /api/repuestos
PUT    /api/repuestos/{id}
DELETE /api/repuestos/{id}
POST   /api/repuestos/{id}/reducir-stock
POST   /api/repuestos/{id}/aumentar-stock
```

### Diagnóstico

CRUD de diagnósticos técnicos asociados a un ticket y a un empleado.

```http
GET    /api/diagnosticos
GET    /api/diagnosticos/{id}
POST   /api/diagnosticos
PUT    /api/diagnosticos/{id}
DELETE /api/diagnosticos/{id}
```

### Notificación

Simula el envío de una notificación por correo cuando un ticket cambia de estado. No guarda información en una base de datos; el envío queda registrado en los logs.

```http
POST /api/notificaciones
```

Ejemplo de solicitud:

```json
{
  "ticketId": "550e8400-e29b-41d4-a716-446655440000",
  "destinatario": "cliente@correo.cl",
  "nuevoEstado": "EN_REPARACION"
}
```

### Cotización

CRUD de cotizaciones e integración con los microservicios `ticket` y `repuesto`.

```http
GET    /api/cotizaciones
GET    /api/cotizaciones/{id}
GET    /api/cotizaciones/ticket/{ticketId}
POST   /api/cotizaciones
PUT    /api/cotizaciones/{id}
POST   /api/cotizaciones/{id}/aprobar
POST   /api/cotizaciones/{id}/rechazar
DELETE /api/cotizaciones/{id}
```

Al crear o actualizar una cotización, el servicio:

1. Comprueba que el ticket exista y pueda ser cotizado.
2. Consulta cada repuesto en `repuesto`.
3. Obtiene el precio actual y valida el stock disponible.
4. Calcula el costo de repuestos, mano de obra, descuento, IVA y total.
5. Guarda la cotización inicialmente con estado `PENDIENTE`.
6. Al aprobarla, descuenta el stock de los repuestos utilizados.

Cálculo aplicado:

```text
subtotal = manoObra + costoRepuestos
neto     = subtotal - descuento
iva      = neto × 0,19
total    = neto + iva
```

Ejemplo de solicitud:

```json
{
  "ticketId": "550e8400-e29b-41d4-a716-446655440000",
  "descripcion": "Cambio de pantalla y revisión general",
  "manoObra": 15000,
  "descuento": 0,
  "repuestos": [
    {
      "repuestoId": "11111111-1111-1111-1111-111111111111",
      "cantidad": 1
    }
  ]
}
```

### BFF

El BFF funciona como capa intermedia y combina información de distintos servicios.

```http
POST /login
POST /register
GET  /api/bff/tickets/resumen/{idTicket}
```

El endpoint de resumen obtiene la información base de un ticket y la complementa con datos del cliente y del empleado.

## Base de datos y Flyway

Cada microservicio que necesita persistencia tiene su propia base de datos y sus propias migraciones Flyway.

```text
auth/src/main/resources/db/migration
cliente/src/main/resources/db/migration
empleado/src/main/resources/db/migration
ticket/src/main/resources/db/migration
equipo/src/main/resources/db/migration
repuesto/src/main/resources/db/migration
diagnostico/src/main/resources/db/migration
cotizacion/src/main/resources/db/migration
```

Al iniciar cada aplicación, Flyway valida el historial y ejecuta las migraciones pendientes. Hibernate utiliza `ddl-auto=validate`, por lo que no crea las tablas automáticamente: solamente comprueba que coincidan con las entidades.

## Variables de entorno

Crea un archivo `.env` en la raíz del proyecto:

```env
DB_PASSWORD=root
JWT_SECRET=una-clave-larga-y-segura-compartida-por-todos
```

No se deben subir secretos reales al repositorio. El archivo `.env.example` puede utilizarse como plantilla.

## Ejecución local con Gradle

Primero levanta las bases de datos necesarias con Docker Compose. Por ejemplo, para cotización:

```powershell
docker compose up -d db-cotizacion
```

Después entra al microservicio y ejecútalo:

```powershell
cd cotizacion
.\gradlew.bat bootRun
```

Comprobación de salud:

```powershell
Invoke-RestMethod http://localhost:8088/actuator/health
```

Respuesta esperada:

```text
status
------
UP
```

Para detener una aplicación iniciada con `bootRun`, utiliza `Ctrl + C`.

## Ejecución con Docker Compose

Desde la raíz del proyecto:

```powershell
docker compose config
docker compose up -d --build
```

Revisar contenedores:

```powershell
docker compose ps
```

Revisar logs:

```powershell
docker compose logs -f
```

Dentro de Docker, los servicios se comunican utilizando el nombre definido en Compose y no `localhost`. Ejemplos:

```text
http://app-ticket:8082
http://app-repuesto:8085
http://app-auth:7778
```

## Compilación y pruebas

Para compilar un microservicio:

```powershell
.\gradlew.bat clean build
```

Para ejecutar solamente las pruebas:

```powershell
.\gradlew.bat test
```

Para generar el informe de cobertura:

```powershell
.\gradlew.bat test jacocoTestReport
```

El informe HTML se genera normalmente en:

```text
MICROSERVICIO/build/reports/jacoco/test/html/index.html
```

La meta mínima solicitada para el proyecto es una cobertura de **40 %**.

## Swagger y Actuator

Cuando un microservicio incluye Springdoc, la documentación puede consultarse en:

```text
http://localhost:PUERTO/swagger-ui/index.html
http://localhost:PUERTO/v3/api-docs
```

Los servicios que incluyen Actuator exponen su estado en:

```text
http://localhost:PUERTO/actuator/health
```

## Estructura general

```text
serviciotecnico/
├── auth/
├── bff/
├── cliente/
├── empleado/
├── ticket/
├── equipo/
├── repuesto/
├── diagnostico/
├── notificacion/
├── cotizacion/
├── docs/
│   ├── architecture/
│   └── diagrams/
├── docker-compose.yml
├── .env.example
└── README.md
```

Cada microservicio sigue, según corresponda, una estructura por capas:

```text
src/main/java
├── client
├── config
├── controller
├── dto
├── entity o model
├── exception
├── filter
├── repository
├── service
└── util
```

## Estado actual

Implementado hasta el punto 5:

- CRUD de equipos con base de datos propia.
- CRUD de repuestos con control de stock.
- CRUD de diagnósticos con base de datos propia.
- Notificaciones simuladas sin base de datos.
- CRUD de cotizaciones con cálculo automático.
- Comunicación de `cotizacion` con `ticket` y `repuesto`.
- Validación de stock y descuento al aprobar una cotización.
- Migraciones Flyway para los servicios que utilizan PostgreSQL.

Pendiente para completar todos los requisitos finales del examen:

- Ampliar el BFF para consumir los microservicios nuevos.
- Configurar el API Gateway como puerta de entrada única.
- Completar Javadoc, seguridad y manejo de errores donde corresponda.
- Completar pruebas y comprobar el porcentaje de cobertura.
- Integrar todos los microservicios en Docker Compose.
- Completar la integración centralizada de logs.

## Diagramas

### Diagrama de contenedores C2

![Arquitectura C2](docs/diagrams/DiagramaC2.png)

### Diagrama de componentes C3

![Arquitectura C3](docs/diagrams/DiagramaC3.png)

## Conclusión

El proyecto aplica una arquitectura distribuida con responsabilidades separadas, persistencia independiente por servicio, autenticación JWT, comunicación REST y despliegue mediante Docker. La integración entre cotizaciones, tickets y repuestos demuestra comunicación real entre microservicios y permite calcular reparaciones utilizando precios y stock administrados por otro servicio.