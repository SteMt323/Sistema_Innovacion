# DELETE eliminar rol de usuario

## Resumen

Desactiva la asignacion de un rol a un usuario.

## Endpoint

| Propiedad | Valor |
| --- | --- |
| Metodo | `DELETE` |
| Ruta | `/api/usuarios/{idUsuario}/roles/{nombreRol}` |
| Controlador | `UsuarioController.desactivarRol` |
| Autenticacion | No definida en el backend actual. |

## Path params

| Parametro | Tipo | Requerido | Descripcion |
| --- | --- | --- | --- |
| `idUsuario` | `number` | Si | Identificador del usuario. |
| `nombreRol` | `string` | Si | Nombre del rol a desactivar. |

## Request

No requiere body.

### Headers

```http
Accept: application/json
```

## Response exitoso

| Codigo | Body |
| --- | --- |
| `204 No Content` | Sin body |

## Reglas de negocio

- El usuario debe existir.
- El rol debe estar asignado al usuario.
- No se puede desactivar un rol cuando el usuario ya tiene el perfil asociado a ese rol.
- No elimina fisicamente el registro en `usuario_roles`; solo marca la asignacion como `activo=false`.
- `nombreRol` se normaliza a minusculas y sin espacios externos.

## Errores

| Codigo | Causa |
| --- | --- |
| `400 Bad Request` | Existe un perfil asociado al rol que se intenta desactivar. |
| `404 Not Found` | No existe el usuario o el usuario no tiene asignado el rol. |
| `500 Internal Server Error` | Error inesperado del backend. |

Schema de error: [`ErrorResponse`](../../../schemas/common/ErrorResponse.md)

## Ejemplo cURL

```bash
curl -X DELETE "http://localhost:8080/api/usuarios/1/roles/estudiante" \
  -H "Accept: application/json"
```
