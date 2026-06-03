# PerfilEstudiante

Contrato de request y response para el perfil estudiante.

## Request

Usado por `POST /api/usuarios/{idUsuario}/perfiles/estudiante`.

| Campo | Tipo | Requerido | Validaciones | Descripcion |
| --- | --- | --- | --- | --- |
| `cif` | `string` | Si | No vacio, maximo 30 caracteres, unico sin distinguir mayusculas/minusculas. | Codigo CIF del estudiante. |
| `correoInstitucional` | `string` | No | Email valido, maximo 150 caracteres, unico si se envia. | Correo institucional. Se guarda en minusculas y sin espacios externos. |
| `idCarreraPrincipal` | `number` | No | Sin validacion en el backend actual. | Identificador de carrera principal. Puede ser `null` mientras catalogos academicos no esten implementados. |
| `dobleTitular` | `boolean` | No | Si se omite o se envia `null`, se guarda `false`. | Indica si el estudiante tiene doble titulacion. |

### Ejemplo request

```json
{
  "cif": "2026-0001",
  "correoInstitucional": "maria.lopez@uam.edu.ni",
  "idCarreraPrincipal": null,
  "dobleTitular": false
}
```

## Response

| Campo | Tipo | Requerido | Descripcion |
| --- | --- | --- | --- |
| `idUsuario` | `number` | Si | Identificador del usuario propietario del perfil. |
| `cif` | `string` | Si | Codigo CIF. |
| `correoInstitucional` | `string` | No | Correo institucional o `null`. |
| `idCarreraPrincipal` | `number` | No | Identificador de carrera principal o `null`. |
| `dobleTitular` | `boolean` | Si | Indicador de doble titulacion. |

### Ejemplo response

```json
{
  "idUsuario": 1,
  "cif": "2026-0001",
  "correoInstitucional": "maria.lopez@uam.edu.ni",
  "idCarreraPrincipal": null,
  "dobleTitular": false
}
```

## Reglas

- El usuario debe existir.
- El usuario debe tener rol activo `estudiante`.
- El usuario solo puede tener un perfil estudiante.
- `cif` debe ser unico.
- `correoInstitucional` debe ser unico cuando no es `null`.

