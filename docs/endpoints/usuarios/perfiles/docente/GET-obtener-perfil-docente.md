# GET obtener perfil docente

## Resumen

Obtiene el perfil docente de un usuario.

## Endpoint

| Propiedad | Valor |
| --- | --- |
| Metodo | `GET` |
| Ruta | `/api/usuarios/{idUsuario}/perfiles/docente` |
| Controlador | `UsuarioController.obtenerPerfilDocente` |
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
| `200 OK` | `PerfilDocente` |

Schema: [`PerfilDocente`](../../../../schemas/usuario/PerfilDocente.md)

### Ejemplo

```json
{
  "idUsuario": 1,
  "areaAcademica": "Innovacion educativa",
  "cargo": "Docente investigador",
  "gradoAcademico": "maestria",
  "tituloUniversitario": "Maestria en educacion",
  "idFacultad": null
}
```

## Errores

| Codigo | Causa |
| --- | --- |
| `404 Not Found` | El usuario no tiene perfil docente. |
| `500 Internal Server Error` | Error inesperado del backend. |

Schema de error: [`ErrorResponse`](../../../../schemas/common/ErrorResponse.md)

## Ejemplo cURL

```bash
curl -X GET "http://localhost:8080/api/usuarios/1/perfiles/docente" \
  -H "Accept: application/json"
```
