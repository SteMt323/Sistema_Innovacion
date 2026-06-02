# GET obtener usuarios

## Resumen

Obtiene todos los usuarios registrados en el sistema.

## Endpoint

| Propiedad | Valor |
| --- | --- |
| Metodo | `GET` |
| Ruta | `/api/usuarios` |
| Controlador | `UsuarioController.listarUsuarios` |
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
| `200 OK` | `array<UsuarioResponse>` |

Schema: [`UsuarioResponse`](../../schemas/usuario/UsuarioResponse.md)

### Ejemplo

```json
[
  {
    "idUsuario": 1,
    "nombreCompleto": "Maria Fernanda Lopez",
    "documento": "001-010101-0001A",
    "telefono": "88880000",
    "correo": "maria.lopez@example.com",
    "sexo": "F",
    "tallaCamisa": "M",
    "estado": "activo",
    "fechaRegistro": "2026-06-01T15:00:00",
    "ultimoAcceso": null,
    "roles": [],
    "perfilEstudiante": null,
    "perfilAdministrador": null,
    "perfilDocente": null,
    "perfilMentor": null,
    "perfilParticipanteExterno": null
  }
]
```

## Notas

- El backend actual no aplica paginacion ni filtros.
- Los roles incluidos son solo asignaciones activas.

## Errores

| Codigo | Causa |
| --- | --- |
| `500 Internal Server Error` | Error inesperado del backend. |

Schema de error: [`ErrorResponse`](../../schemas/common/ErrorResponse.md)

## Ejemplo cURL

```bash
curl -X GET "http://localhost:8080/api/usuarios" \
  -H "Accept: application/json"
```
