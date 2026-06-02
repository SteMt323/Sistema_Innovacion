# GET obtener perfil participante externo

## Resumen

Obtiene el perfil participante externo de un usuario.

## Endpoint

| Propiedad | Valor |
| --- | --- |
| Metodo | `GET` |
| Ruta | `/api/usuarios/{idUsuario}/perfiles/participante-externo` |
| Controlador | `UsuarioController.obtenerPerfilParticipanteExterno` |
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
| `200 OK` | `PerfilParticipanteExterno` |

Schema: [`PerfilParticipanteExterno`](../../../../schemas/usuario/PerfilParticipanteExterno.md)

### Ejemplo

```json
{
  "idUsuario": 1,
  "ocupacion": "Emprendedor",
  "institucionProcedencia": "Empresa externa"
}
```

## Errores

| Codigo | Causa |
| --- | --- |
| `404 Not Found` | El usuario no tiene perfil participante externo. |
| `500 Internal Server Error` | Error inesperado del backend. |

Schema de error: [`ErrorResponse`](../../../../schemas/common/ErrorResponse.md)

## Ejemplo cURL

```bash
curl -X GET "http://localhost:8080/api/usuarios/1/perfiles/participante-externo" \
  -H "Accept: application/json"
```
