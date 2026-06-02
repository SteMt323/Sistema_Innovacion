# POST crear rol

## Resumen

Crea un nuevo rol del sistema.

## Endpoint

| Propiedad | Valor |
| --- | --- |
| Metodo | `POST` |
| Ruta | `/api/roles` |
| Controlador | `RolController.crearRol` |
| Autenticacion | No definida en el backend actual. |

## Request

Schema: [`RolRequest`](../../schemas/rol/RolRequest.md)

### Headers

```http
Content-Type: application/json
Accept: application/json
```

### Body

```json
{
  "nombre": "coordinador",
  "descripcion": "Usuario coordinador de actividades de innovacion"
}
```

## Response exitoso

| Codigo | Body | Headers |
| --- | --- | --- |
| `201 Created` | `RolResponse` | `Location: /api/roles/{idRol}` |

Schema: [`RolResponse`](../../schemas/rol/RolResponse.md)

### Ejemplo

```json
{
  "idRol": 6,
  "nombre": "coordinador",
  "descripcion": "Usuario coordinador de actividades de innovacion"
}
```

## Reglas de negocio

- `nombre` se guarda en minusculas y sin espacios externos.
- `nombre` debe ser unico sin distinguir mayusculas/minusculas.
- `descripcion` se guarda sin espacios externos cuando se envia.

## Errores

| Codigo | Causa |
| --- | --- |
| `400 Bad Request` | Campos invalidos o JSON mal formado. |
| `409 Conflict` | Ya existe un rol con el mismo nombre. |
| `500 Internal Server Error` | Error inesperado del backend. |

Schema de error: [`ErrorResponse`](../../schemas/common/ErrorResponse.md)

## Ejemplo cURL

```bash
curl -X POST "http://localhost:8080/api/roles" \
  -H "Content-Type: application/json" \
  -H "Accept: application/json" \
  -d '{"nombre":"coordinador","descripcion":"Usuario coordinador de actividades de innovacion"}'
```

