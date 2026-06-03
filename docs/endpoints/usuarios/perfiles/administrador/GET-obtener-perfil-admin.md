# GET obtener perfil administrador

## Resumen

Obtiene el perfil administrador asociado a un usuario.

## Endpoint

| Propiedad | Valor |
| --- | --- |
| Metodo | `GET` |
| Ruta | `/api/usuarios/{idUsuario}/perfiles/administrador` |
| Controlador | `UsuarioController.obtenerPerfilAdministrador` |
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
| `200 OK` | `PerfilAdministrador` |

Schema: [`PerfilAdministrador`](../../../../schemas/usuario/PerfilAdministrador.md)

### Ejemplo

```json
{
  "idUsuario": 1,
  "cargo": "Coordinador de innovacion",
  "nivelAcceso": "total"
}
```

## Errores

| Codigo | Causa |
| --- | --- |
| `404 Not Found` | El usuario no tiene perfil administrador. |
| `500 Internal Server Error` | Error inesperado del backend. |

Schema de error: [`ErrorResponse`](../../../../schemas/common/ErrorResponse.md)

## Ejemplo cURL

```bash
curl -X GET "http://localhost:8080/api/usuarios/1/perfiles/administrador" \
  -H "Accept: application/json"
```

