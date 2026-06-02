# PATCH cambiar estado

## Resumen

Cambia el estado de un usuario existente.

## Endpoint

| Propiedad | Valor |
| --- | --- |
| Metodo | `PATCH` |
| Ruta | `/api/usuarios/{idUsuario}/estado` |
| Controlador | `UsuarioController.cambiarEstado` |
| Autenticacion | No definida en el backend actual. |

## Path params

| Parametro | Tipo | Requerido | Descripcion |
| --- | --- | --- | --- |
| `idUsuario` | `number` | Si | Identificador del usuario. |

## Request

Schema: [`CambiarEstadoRequest`](../../schemas/usuario/CambiarEstadoRequest.md)

### Headers

```http
Content-Type: application/json
Accept: application/json
```

### Body

```json
{
  "estado": "inactivo"
}
```

## Response exitoso

| Codigo | Body |
| --- | --- |
| `200 OK` | `UsuarioResponse` |

Schema: [`UsuarioResponse`](../../schemas/usuario/UsuarioResponse.md)

### Valores validos

```text
activo
inactivo
suspendido
```

## Reglas de negocio

- El usuario debe existir.
- El backend tambien acepta nombres del enum (`ACTIVO`, `INACTIVO`, `SUSPENDIDO`) sin distinguir mayusculas/minusculas.

## Errores

| Codigo | Causa |
| --- | --- |
| `400 Bad Request` | `estado` nulo, valor no valido o JSON mal formado. |
| `404 Not Found` | No existe el usuario con el `idUsuario` enviado. |
| `500 Internal Server Error` | Error inesperado del backend. |

Schema de error: [`ErrorResponse`](../../schemas/common/ErrorResponse.md)

## Ejemplo cURL

```bash
curl -X PATCH "http://localhost:8080/api/usuarios/1/estado" \
  -H "Content-Type: application/json" \
  -H "Accept: application/json" \
  -d '{"estado":"inactivo"}'
```

