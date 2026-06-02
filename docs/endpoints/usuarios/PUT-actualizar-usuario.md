# PUT actualizar usuario

## Resumen

Actualiza los datos principales de un usuario. No actualiza contrasena, roles ni perfiles.

## Endpoint

| Propiedad | Valor |
| --- | --- |
| Metodo | `PUT` |
| Ruta | `/api/usuarios/{idUsuario}` |
| Controlador | `UsuarioController.actualizarUsuario` |
| Autenticacion | No definida en el backend actual. |

## Path params

| Parametro | Tipo | Requerido | Descripcion |
| --- | --- | --- | --- |
| `idUsuario` | `number` | Si | Identificador del usuario a actualizar. |

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
  "nombreCompleto": "Maria Fernanda Lopez Actualizada",
  "documento": "001-010101-0001A",
  "telefono": "88881111",
  "correo": "maria.actualizada@example.com",
  "sexo": "F",
  "tallaCamisa": "L"
}
```

## Response exitoso

| Codigo | Body |
| --- | --- |
| `200 OK` | `UsuarioResponse` |

Schema: [`UsuarioResponse`](../../schemas/usuario/UsuarioResponse.md)

### Ejemplo

```json
{
  "idUsuario": 1,
  "nombreCompleto": "Maria Fernanda Lopez Actualizada",
  "documento": "001-010101-0001A",
  "telefono": "88881111",
  "correo": "maria.actualizada@example.com",
  "sexo": "F",
  "tallaCamisa": "L",
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

- El usuario debe existir.
- `correo` se guarda en minusculas y sin espacios externos.
- `documento` se guarda sin espacios externos.
- `correo` no puede pertenecer a otro usuario.
- `documento` no puede pertenecer a otro usuario.
- La contrasena no se modifica en este endpoint.

## Errores

| Codigo | Causa |
| --- | --- |
| `400 Bad Request` | Campos invalidos o JSON mal formado. |
| `404 Not Found` | No existe el usuario con el `idUsuario` enviado. |
| `409 Conflict` | El correo o documento ya pertenece a otro usuario. |
| `500 Internal Server Error` | Error inesperado del backend. |

Schema de error: [`ErrorResponse`](../../schemas/common/ErrorResponse.md)

## Ejemplo cURL

```bash
curl -X PUT "http://localhost:8080/api/usuarios/1" \
  -H "Content-Type: application/json" \
  -H "Accept: application/json" \
  -d '{"nombreCompleto":"Maria Fernanda Lopez Actualizada","documento":"001-010101-0001A","telefono":"88881111","correo":"maria.actualizada@example.com","sexo":"F","tallaCamisa":"L"}'
```
