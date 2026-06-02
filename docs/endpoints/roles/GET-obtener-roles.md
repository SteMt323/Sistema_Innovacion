# GET obtener roles

## Resumen

Obtiene todos los roles registrados en el sistema.

## Endpoint

| Propiedad | Valor |
| --- | --- |
| Metodo | `GET` |
| Ruta | `/api/roles` |
| Controlador | `RolController.listarRoles` |
| Autenticacion | No definida en el backend actual. |

## Request

No requiere body.

### Headers

```http
Accept: application/json
```

## Response exitoso

| Codigo | Body |
| --- | --- |
| `200 OK` | `array<RolResponse>` |

Schema: [`RolResponse`](../../schemas/rol/RolResponse.md)

### Ejemplo

```json
[
  {
    "idRol": 1,
    "nombre": "estudiante",
    "descripcion": "Usuario estudiante del sistema"
  },
  {
    "idRol": 2,
    "nombre": "administrador",
    "descripcion": "Usuario con permisos administrativos"
  }
]
```

## Errores

| Codigo | Causa |
| --- | --- |
| `500 Internal Server Error` | Error inesperado del backend. |

Schema de error: [`ErrorResponse`](../../schemas/common/ErrorResponse.md)

## Ejemplo cURL

```bash
curl -X GET "http://localhost:8080/api/roles" \
  -H "Accept: application/json"
```

