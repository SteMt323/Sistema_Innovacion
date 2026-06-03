# POST crear perfil docente

## Resumen

Crea el perfil docente de un usuario.

## Endpoint

| Propiedad | Valor |
| --- | --- |
| Metodo | `POST` |
| Ruta | `/api/usuarios/{idUsuario}/perfiles/docente` |
| Controlador | `UsuarioController.crearPerfilDocente` |
| Autenticacion | No definida en el backend actual. |

## Path params

| Parametro | Tipo | Requerido | Descripcion |
| --- | --- | --- | --- |
| `idUsuario` | `number` | Si | Identificador del usuario. |

## Request

Schema: [`PerfilDocente`](../../../../schemas/usuario/PerfilDocente.md)

### Headers

```http
Content-Type: application/json
Accept: application/json
```

### Body

```json
{
  "areaAcademica": "Innovacion educativa",
  "cargo": "Docente investigador",
  "gradoAcademico": "maestria",
  "tituloUniversitario": "Maestria en educacion",
  "idFacultad": null
}
```

## Response exitoso

| Codigo | Body | Headers |
| --- | --- | --- |
| `201 Created` | `PerfilDocente` | `Location: /api/usuarios/{idUsuario}/perfiles/docente` |

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

## Reglas de negocio

- El usuario debe existir.
- El usuario debe tener rol activo `docente`.
- El usuario solo puede tener un perfil docente.
- Los campos de texto se guardan sin espacios externos.
- `idFacultad` queda pendiente de catalogos academicos; enviar `null` en esta fase.

## Errores

| Codigo | Causa |
| --- | --- |
| `400 Bad Request` | Campos invalidos, grado academico no valido, JSON mal formado o el usuario no tiene rol `docente` activo. |
| `404 Not Found` | No existe el usuario. |
| `409 Conflict` | El usuario ya tiene perfil docente. |
| `500 Internal Server Error` | Error inesperado del backend. |

Schema de error: [`ErrorResponse`](../../../../schemas/common/ErrorResponse.md)

## Ejemplo cURL

```bash
curl -X POST "http://localhost:8080/api/usuarios/1/perfiles/docente" \
  -H "Content-Type: application/json" \
  -H "Accept: application/json" \
  -d '{"areaAcademica":"Innovacion educativa","cargo":"Docente investigador","gradoAcademico":"maestria","tituloUniversitario":"Maestria en educacion","idFacultad":null}'
```
