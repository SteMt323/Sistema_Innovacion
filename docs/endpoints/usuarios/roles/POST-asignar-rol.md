# POST asignar rol

## Resumen

Asigna un rol existente a un usuario. Si el rol ya estaba asignado pero inactivo, lo reactiva.

## Endpoint

| Propiedad | Valor |
| --- | --- |
| Metodo | `POST` |
| Ruta | `/api/usuarios/{idUsuario}/roles` |
| Controlador | `UsuarioController.asignarRol` |
| Autenticacion | No definida en el backend actual. |

## Path params

| Parametro | Tipo | Requerido | Descripcion |
| --- | --- | --- | --- |
| `idUsuario` | `number` | Si | Identificador del usuario. |

## Request

Schema: [`AsignarRolRequest`](../../../schemas/usuario/AsignarRolRequest.md)

### Headers

```http
Content-Type: application/json
Accept: application/json
```

### Body

```json
{
  "nombreRol": "estudiante"
}
```

## Response exitoso

| Codigo | Body |
| --- | --- |
| `200 OK` | `UsuarioResponse` |

Schema: [`UsuarioResponse`](../../../schemas/usuario/UsuarioResponse.md)

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
  "roles": [
    {
      "idRol": 1,
      "nombre": "estudiante",
      "descripcion": "Usuario estudiante del sistema"
    }
  ],
  "perfilEstudiante": null,
  "perfilAdministrador": null,
  "perfilDocente": null,
  "perfilMentor": null,
  "perfilParticipanteExterno": null
}
```

## Reglas de negocio

- El usuario debe existir.
- El rol debe existir en `/api/roles`.
- `nombreRol` se normaliza a minusculas y sin espacios externos.
- Si ya existe una asignacion inactiva entre el usuario y el rol, se reactiva.
- La fecha de asignacion se actualiza al momento actual.

## Errores

| Codigo | Causa |
| --- | --- |
| `400 Bad Request` | `nombreRol` vacio, mayor a 50 caracteres o JSON mal formado. |
| `404 Not Found` | No existe el usuario o no existe el rol solicitado. |
| `500 Internal Server Error` | Error inesperado del backend. |

Schema de error: [`ErrorResponse`](../../../schemas/common/ErrorResponse.md)

## Ejemplo cURL

```bash
curl -X POST "http://localhost:8080/api/usuarios/1/roles" \
  -H "Content-Type: application/json" \
  -H "Accept: application/json" \
  -d '{"nombreRol":"estudiante"}'
```
