# GET obtener perfil estudiante

## Resumen

Obtiene el perfil estudiante asociado a un usuario.

## Endpoint

| Propiedad | Valor |
| --- | --- |
| Metodo | `GET` |
| Ruta | `/api/usuarios/{idUsuario}/perfiles/estudiante` |
| Controlador | `UsuarioController.obtenerPerfilEstudiante` |
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
| `200 OK` | `PerfilEstudiante` |

Schema: [`PerfilEstudiante`](../../../../schemas/usuario/PerfilEstudiante.md)

### Ejemplo

```json
{
  "idUsuario": 1,
  "cif": "2026-0001",
  "correoInstitucional": "maria.lopez@uam.edu.ni",
  "idCarreraPrincipal": null,
  "dobleTitular": false
}
```

## Errores

| Codigo | Causa |
| --- | --- |
| `404 Not Found` | El usuario no tiene perfil estudiante. |
| `500 Internal Server Error` | Error inesperado del backend. |

Schema de error: [`ErrorResponse`](../../../../schemas/common/ErrorResponse.md)

## Ejemplo cURL

```bash
curl -X GET "http://localhost:8080/api/usuarios/1/perfiles/estudiante" \
  -H "Accept: application/json"
```

