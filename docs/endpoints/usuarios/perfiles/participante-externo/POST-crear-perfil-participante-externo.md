# POST crear perfil participante externo

## Resumen

Crea el perfil participante externo de un usuario.

## Endpoint

| Propiedad | Valor |
| --- | --- |
| Metodo | `POST` |
| Ruta | `/api/usuarios/{idUsuario}/perfiles/participante-externo` |
| Controlador | `UsuarioController.crearPerfilParticipanteExterno` |
| Autenticacion | No definida en el backend actual. |

## Path params

| Parametro | Tipo | Requerido | Descripcion |
| --- | --- | --- | --- |
| `idUsuario` | `number` | Si | Identificador del usuario. |

## Request

Schema: [`PerfilParticipanteExterno`](../../../../schemas/usuario/PerfilParticipanteExterno.md)

### Headers

```http
Content-Type: application/json
Accept: application/json
```

### Body

```json
{
  "ocupacion": "Emprendedor",
  "institucionProcedencia": "Empresa externa"
}
```

## Response exitoso

| Codigo | Body | Headers |
| --- | --- | --- |
| `201 Created` | `PerfilParticipanteExterno` | `Location: /api/usuarios/{idUsuario}/perfiles/participante-externo` |

### Ejemplo

```json
{
  "idUsuario": 1,
  "ocupacion": "Emprendedor",
  "institucionProcedencia": "Empresa externa"
}
```

## Reglas de negocio

- El usuario debe existir.
- El usuario debe tener rol activo `participante_externo`.
- El usuario solo puede tener un perfil participante externo.
- Los campos de texto se guardan sin espacios externos.

## Errores

| Codigo | Causa |
| --- | --- |
| `400 Bad Request` | Campos invalidos, JSON mal formado o el usuario no tiene rol `participante_externo` activo. |
| `404 Not Found` | No existe el usuario. |
| `409 Conflict` | El usuario ya tiene perfil participante externo. |
| `500 Internal Server Error` | Error inesperado del backend. |

Schema de error: [`ErrorResponse`](../../../../schemas/common/ErrorResponse.md)

## Ejemplo cURL

```bash
curl -X POST "http://localhost:8080/api/usuarios/1/perfiles/participante-externo" \
  -H "Content-Type: application/json" \
  -H "Accept: application/json" \
  -d '{"ocupacion":"Emprendedor","institucionProcedencia":"Empresa externa"}'
```
