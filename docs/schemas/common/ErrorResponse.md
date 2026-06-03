# ErrorResponse

Schema comun de errores devuelto por `GlobalExceptionHandler`.

## Campos

| Campo | Tipo | Requerido | Descripcion |
| --- | --- | --- | --- |
| `timestamp` | `string` | Si | Fecha y hora local en formato ISO-8601. |
| `status` | `number` | Si | Codigo HTTP numerico. |
| `error` | `string` | Si | Nombre HTTP del error. |
| `message` | `string` | Si | Mensaje funcional del error. |
| `details` | `object` | Si | Detalle por campo. Puede ser objeto vacio. |

## Ejemplo

```json
{
  "timestamp": "2026-06-01T15:00:00",
  "status": 400,
  "error": "Bad Request",
  "message": "La solicitud tiene campos invalidos",
  "details": {
    "correo": "must be a well-formed email address"
  }
}
```

## Codigos frecuentes

| Codigo | Origen |
| --- | --- |
| `400 Bad Request` | Validacion de campos, JSON invalido, enum invalido o regla de negocio incumplida. |
| `404 Not Found` | Recurso inexistente. |
| `409 Conflict` | Recurso duplicado. |
| `500 Internal Server Error` | Error inesperado. |

