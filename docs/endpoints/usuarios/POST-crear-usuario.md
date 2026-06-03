# POST crear usuario

## Resumen

Crea un usuario con estado inicial `activo`.

## Endpoint

| Propiedad | Valor |
| --- | --- |
| Metodo | `POST` |
| Ruta | `/api/usuarios` |
| Controlador | `UsuarioController.crearUsuario` |
| Autenticacion | No definida en el backend actual. |

## Request

Schema: [`UsuarioRequest`](../../schemas/usuario/UsuarioRequest.md)

### Headers

```http
Content-Type: application/json
Accept: application/json
```

### Body

```json
{
  "nombreCompleto": "Maria Fernanda Lopez",
  "documento": "001-010101-0001A",
  "telefono": "88880000",
  "correo": "maria.lopez@example.com",
  "contrasena": "secreto123",
  "sexo": "F",
  "tallaCamisa": "M"
}
```

## Response exitoso

| Codigo | Body | Headers |
| --- | --- | --- |
| `201 Created` | `UsuarioResponse` | `Location: /api/usuarios/{idUsuario}` |

Schema: [`UsuarioResponse`](../../schemas/usuario/UsuarioResponse.md)

### Ejemplo

```json
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
```

## Reglas de negocio

- `correo` se guarda en minusculas y sin espacios externos.
- `documento` se guarda sin espacios externos.
- `correo` debe ser unico sin distinguir mayusculas/minusculas.
- `documento` debe ser unico.
- `contrasena` se guarda hasheada y no se devuelve.
- El usuario se crea con `estado` igual a `activo`.

## Errores

| Codigo | Causa |
| --- | --- |
| `400 Bad Request` | Campos invalidos o JSON mal formado. |
| `409 Conflict` | Ya existe un usuario con el correo o documento enviado. |
| `500 Internal Server Error` | Error inesperado del backend. |

Schema de error: [`ErrorResponse`](../../schemas/common/ErrorResponse.md)

## Ejemplo cURL

```bash
curl -X POST "http://localhost:8080/api/usuarios" \
  -H "Content-Type: application/json" \
  -H "Accept: application/json" \
  -d '{"nombreCompleto":"Maria Fernanda Lopez","documento":"001-010101-0001A","telefono":"88880000","correo":"maria.lopez@example.com","contrasena":"secreto123","sexo":"F","tallaCamisa":"M"}'
```
