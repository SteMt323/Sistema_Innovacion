# GET obtener perfil mentor

## Resumen

Obtiene el perfil mentor de un usuario.

## Endpoint

| Propiedad | Valor |
| --- | --- |
| Metodo | `GET` |
| Ruta | `/api/usuarios/{idUsuario}/perfiles/mentor` |
| Controlador | `UsuarioController.obtenerPerfilMentor` |
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
| `200 OK` | `PerfilMentor` |

Schema: [`PerfilMentor`](../../../../schemas/usuario/PerfilMentor.md)

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

## Errores

| Codigo | Causa |
| --- | --- |
| `404 Not Found` | El usuario no tiene perfil mentor. |
| `500 Internal Server Error` | Error inesperado del backend. |

Schema de error: [`ErrorResponse`](../../../../schemas/common/ErrorResponse.md)

## Ejemplo cURL

```bash
curl -X GET "http://localhost:8080/api/usuarios/1/perfiles/mentor" \
  -H "Accept: application/json"
```
