# PATCH cambiar contrasena

## Resumen

Cambia la contrasena de un usuario existente.

## Endpoint

| Propiedad | Valor |
| --- | --- |
| Metodo | `PATCH` |
| Ruta | `/api/usuarios/{idUsuario}/contrasena` |
| Controlador | `UsuarioController.cambiarContrasena` |
| Autenticacion | No definida en el backend actual. |

## Path params

| Parametro | Tipo | Requerido | Descripcion |
| --- | --- | --- | --- |
| `idUsuario` | `number` | Si | Identificador del usuario. |

## Request

Schema: [`CambiarContrasenaRequest`](../../schemas/usuario/CambiarContrasenaRequest.md)

### Headers

```http
Content-Type: application/json
Accept: application/json
```

### Body

```json
{
  "contrasena": "nuevoSecreto123"
}
```

## Response exitoso

| Codigo | Body |
| --- | --- |
| `200 OK` | `UsuarioResponse` |

Schema: [`UsuarioResponse`](../../schemas/usuario/UsuarioResponse.md)

## Reglas de negocio

- El usuario debe existir.
- La nueva contrasena se guarda hasheada.
- La contrasena no se devuelve en la respuesta.

## Errores

| Codigo | Causa |
| --- | --- |
| `400 Bad Request` | `contrasena` vacia, menor a 6 caracteres, mayor a 100 caracteres o JSON mal formado. |
| `404 Not Found` | No existe el usuario con el `idUsuario` enviado. |
| `500 Internal Server Error` | Error inesperado del backend. |

Schema de error: [`ErrorResponse`](../../schemas/common/ErrorResponse.md)

## Ejemplo cURL

```bash
curl -X PATCH "http://localhost:8080/api/usuarios/1/contrasena" \
  -H "Content-Type: application/json" \
  -H "Accept: application/json" \
  -d '{"contrasena":"nuevoSecreto123"}'
```

