# POST crear perfil estudiante

## Resumen

Crea el perfil estudiante de un usuario.

## Endpoint

| Propiedad | Valor |
| --- | --- |
| Metodo | `POST` |
| Ruta | `/api/usuarios/{idUsuario}/perfiles/estudiante` |
| Controlador | `UsuarioController.crearPerfilEstudiante` |
| Autenticacion | No definida en el backend actual. |

## Path params

| Parametro | Tipo | Requerido | Descripcion |
| --- | --- | --- | --- |
| `idUsuario` | `number` | Si | Identificador del usuario. |

## Request

Schema: [`PerfilEstudiante`](../../../../schemas/usuario/PerfilEstudiante.md)

### Headers

```http
Content-Type: application/json
Accept: application/json
```

### Body

```json
{
  "cif": "2026-0001",
  "correoInstitucional": "maria.lopez@uam.edu.ni",
  "idCarreraPrincipal": null,
  "dobleTitular": false
}
```

## Response exitoso

| Codigo | Body | Headers |
| --- | --- | --- |
| `201 Created` | `PerfilEstudiante` | `Location: /api/usuarios/{idUsuario}/perfiles/estudiante` |

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

## Reglas de negocio

- El usuario debe existir.
- El usuario debe tener rol activo `estudiante`.
- El usuario solo puede tener un perfil estudiante.
- `cif` debe ser unico sin distinguir mayusculas/minusculas.
- `correoInstitucional` se normaliza a minusculas y debe ser unico cuando no es `null`.
- Si `dobleTitular` se omite o se envia `null`, se guarda `false`.
- `idCarreraPrincipal` puede ser `null` en el backend actual.

## Errores

| Codigo | Causa |
| --- | --- |
| `400 Bad Request` | Campos invalidos, JSON mal formado o el usuario no tiene rol `estudiante` activo. |
| `404 Not Found` | No existe el usuario. |
| `409 Conflict` | El usuario ya tiene perfil estudiante, el CIF ya existe o el correo institucional ya existe. |
| `500 Internal Server Error` | Error inesperado del backend. |

Schema de error: [`ErrorResponse`](../../../../schemas/common/ErrorResponse.md)

## Ejemplo cURL

```bash
curl -X POST "http://localhost:8080/api/usuarios/1/perfiles/estudiante" \
  -H "Content-Type: application/json" \
  -H "Accept: application/json" \
  -d '{"cif":"2026-0001","correoInstitucional":"maria.lopez@uam.edu.ni","idCarreraPrincipal":null,"dobleTitular":false}'
```

