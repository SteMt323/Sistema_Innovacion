# GET obtener usuario por id

## Resumen

Obtiene un usuario especifico por su identificador.

## Endpoint

| Propiedad | Valor |
| --- | --- |
| Metodo | `GET` |
| Ruta | `/api/usuarios/{idUsuario}` |
| Controlador | `UsuarioController.obtenerUsuario` |
| Autenticacion | No definida en el backend actual. |

## Path params

| Parametro | Tipo | Requerido | Descripcion |
| --- | --- | --- | --- |
| `idUsuario` | `number` | Si | Identificador del usuario. |

## Request

No requiere body.

### Headers

```http
Accept: application/json
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

## Errores

| Codigo | Causa |
| --- | --- |
| `404 Not Found` | No existe el usuario con el `idUsuario` enviado. |
| `500 Internal Server Error` | Error inesperado del backend. |

Schema de error: [`ErrorResponse`](../../schemas/common/ErrorResponse.md)

## Ejemplo cURL

```bash
curl -X GET "http://localhost:8080/api/usuarios/1" \
  -H "Accept: application/json"
```
