# POST crear perfil administrador

## Resumen

Crea el perfil administrador de un usuario.

## Endpoint

| Propiedad | Valor |
| --- | --- |
| Metodo | `POST` |
| Ruta | `/api/usuarios/{idUsuario}/perfiles/administrador` |
| Controlador | `UsuarioController.crearPerfilAdministrador` |
| Autenticacion | No definida en el backend actual. |

## Path params

| Parametro | Tipo | Requerido | Descripcion |
| --- | --- | --- | --- |
| `idUsuario` | `number` | Si | Identificador del usuario. |

## Request

Schema: [`PerfilAdministrador`](../../../../schemas/usuario/PerfilAdministrador.md)

### Headers

```http
Content-Type: application/json
Accept: application/json
```

### Body

```json
{
  "cargo": "Coordinador de innovacion",
  "nivelAcceso": "total"
}
```

## Response exitoso

| Codigo | Body | Headers |
| --- | --- | --- |
| `201 Created` | `PerfilAdministrador` | `Location: /api/usuarios/{idUsuario}/perfiles/administrador` |

### Ejemplo

```json
{
  "idUsuario": 1,
  "cargo": "Coordinador de innovacion",
  "nivelAcceso": "total"
}
```

## Reglas de negocio

- El usuario debe existir.
- El usuario debe tener rol activo `administrador`.
- El usuario solo puede tener un perfil administrador.
- `cargo` y `nivelAcceso` se guardan sin espacios externos.

## Errores

| Codigo | Causa |
| --- | --- |
| `400 Bad Request` | Campos invalidos, JSON mal formado o el usuario no tiene rol `administrador` activo. |
| `404 Not Found` | No existe el usuario. |
| `409 Conflict` | El usuario ya tiene perfil administrador. |
| `500 Internal Server Error` | Error inesperado del backend. |

Schema de error: [`ErrorResponse`](../../../../schemas/common/ErrorResponse.md)

## Ejemplo cURL

```bash
curl -X POST "http://localhost:8080/api/usuarios/1/perfiles/administrador" \
  -H "Content-Type: application/json" \
  -H "Accept: application/json" \
  -d '{"cargo":"Coordinador de innovacion","nivelAcceso":"total"}'
```

