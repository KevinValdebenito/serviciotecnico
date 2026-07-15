# Microservicio cotización

Este módulo implementa el punto 5: CRUD de cotizaciones conectado al microservicio `repuesto`, conservando también la validación del `ticket` existente.

## Flujo

1. Al crear o actualizar, `cotizacion` valida el ticket mediante `GET /api/tickets/{id}`.
2. Consulta cada repuesto mediante `GET /api/repuestos/{id}`.
3. El precio no se escribe manualmente: se obtiene desde `repuesto`.
4. Valida que exista stock suficiente.
5. Calcula costo de repuestos, subtotal, descuento, neto, IVA 19 % y total.
6. Guarda la cotización como `PENDIENTE`.
7. Al aprobar, llama a `POST /api/repuestos/{id}/reducir-stock`.
8. Si falla uno de varios descuentos, intenta devolver el stock ya descontado.

## Puertos

- ticket: 8082
- repuesto: 8085
- cotizacion: 8088
- PostgreSQL cotizacion: 5437

## Reemplazo

Reemplaza por completo la carpeta `cotizacion`. No pegues los archivos encima de la carpeta vieja, porque deben eliminarse las versiones anteriores que calculaban `costoRepuestos` manualmente.

No cambies `repuesto`, `ticket` ni `docker-compose.yml` todavía.

## Base de datos local

```powershell
docker run --name postgres-cotizacion `
  -e POSTGRES_USER=postgres `
  -e POSTGRES_PASSWORD=root `
  -e POSTGRES_DB=cotizacion_db `
  -p 5437:5432 `
  -d postgres:15-alpine
```

Si ya existe:

```powershell
docker start postgres-cotizacion
```

## Ejecución

Primero deben estar ejecutándose `ticket` en 8082 y `repuesto` en 8085.

```powershell
cd cotizacion
.\gradlew.bat clean build
.\gradlew.bat bootRun
```

Comprobación:

```powershell
Invoke-RestMethod http://localhost:8088/actuator/health
```

## Token

`ticket` y `repuesto` están protegidos. Envía a cotización:

```text
Authorization: Bearer TU_TOKEN
```

Cotización reenvía ese token a ambos microservicios.

## Endpoints

```text
GET    /api/cotizaciones
GET    /api/cotizaciones/{id}
GET    /api/cotizaciones/ticket/{ticketId}
POST   /api/cotizaciones
PUT    /api/cotizaciones/{id}
POST   /api/cotizaciones/{id}/aprobar
POST   /api/cotizaciones/{id}/rechazar
DELETE /api/cotizaciones/{id}
```

## Crear cotización

```json
{
  "ticketId": "REEMPLAZAR_POR_ID_DE_TICKET_REAL",
  "descripcion": "Cambio de pantalla y revisión general",
  "manoObra": 15000,
  "descuento": 0,
  "repuestos": [
    {
      "repuestoId": "REEMPLAZAR_POR_ID_DE_REPUESTO_REAL",
      "cantidad": 1
    }
  ]
}
```

## Aprobar cotización

```text
POST http://localhost:8088/api/cotizaciones/{idCotizacion}/aprobar
Authorization: Bearer TU_TOKEN
```

Al aprobar, el stock del repuesto se reduce.
