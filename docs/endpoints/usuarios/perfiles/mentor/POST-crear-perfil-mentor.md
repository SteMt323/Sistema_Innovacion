# POST crear perfil mentor

## Resumen

Crea el perfil mentor de un usuario.

## Endpoint

| Propiedad | Valor |
| --- | --- |
| Metodo | `POST` |
| Ruta | `/api/usuarios/{idUsuario}/perfiles/mentor` |
| Controlador | `UsuarioController.crearPerfilMentor` |
| Autenticacion | No definida en el backend actual. |

## Path params

| Parametro | Tipo | Requerido | Descripcion |
| --- | --- | --- | --- |
| `idUsuario` | `number` | Si | Identificador del usuario. |

## Request

Schema: [`PerfilMentor`](../../../../schemas/usuario/PerfilMentor.md)

### Headers

```http
Content-Type: application/json
Accept: application/json
```

### Body

```json
{
  "areaExperiencia": "Modelos de negocio",
  "especialidad": "Emprendimiento",
  "institucion": "UAM",
  "tipoAcompanamiento": "Mentoria grupal",
  "gradoAcademico": "doctorado",
  "tituloUniversitario": "Doctorado en innovacion"
}
```

## Response exitoso

| Codigo | Body | Headers |
| --- | --- | --- |
| `201 Created` | `PerfilMentor` | `Location: /api/usuarios/{idUsuario}/perfiles/mentor` |

### Ejemplo

```json
{
  "idUsuario": 1,
  "areaExperiencia": "Modelos de negocio",
  "especialidad": "Emprendimiento",
  "institucion": "UAM",
  "tipoAcompanamiento": "Mentoria grupal",
  "gradoAcademico": "doctorado",
  "tituloUniversitario": "Doctorado en innovacion"
}
```

## Reglas de negocio

- El usuario debe existir.
- El usuario debe tener rol activo `mentor`.
- El usuario solo puede tener un perfil mentor.
- Los campos de texto se guardan sin espacios externos.

## Errores

| Codigo | Causa |
| --- | --- |
| `400 Bad Request` | Campos invalidos, grado academico no valido, JSON mal formado o el usuario no tiene rol `mentor` activo. |
| `404 Not Found` | No existe el usuario. |
| `409 Conflict` | El usuario ya tiene perfil mentor. |
| `500 Internal Server Error` | Error inesperado del backend. |

Schema de error: [`ErrorResponse`](../../../../schemas/common/ErrorResponse.md)

## Ejemplo cURL

```bash
curl -X POST "http://localhost:8080/api/usuarios/1/perfiles/mentor" \
  -H "Content-Type: application/json" \
  -H "Accept: application/json" \
  -d '{"areaExperiencia":"Modelos de negocio","especialidad":"Emprendimiento","institucion":"UAM","tipoAcompanamiento":"Mentoria grupal","gradoAcademico":"doctorado","tituloUniversitario":"Doctorado en innovacion"}'
```
