# PerfilDocente

Contrato de request y response para el perfil docente.

## Request

Usado por `POST /api/usuarios/{idUsuario}/perfiles/docente`.

| Campo | Tipo | Requerido | Validaciones | Descripcion |
| --- | --- | --- | --- | --- |
| `areaAcademica` | `string` | No | Maximo 120 caracteres. | Area academica del docente. |
| `cargo` | `string` | No | Maximo 100 caracteres. | Cargo del docente. |
| `gradoAcademico` | `string` | No | Valores validos de `GradoAcademico`. | Grado academico. |
| `tituloUniversitario` | `string` | No | Maximo 150 caracteres. | Titulo universitario. |
| `idFacultad` | `number` | No | Sin validacion en el backend actual. | Identificador de facultad. |

### Valores validos de `gradoAcademico`

```text
bachiller
tecnico
licenciatura
ingenieria
maestria
doctorado
otro
```

El backend tambien acepta nombres del enum sin distinguir mayusculas/minusculas.

### Ejemplo request

```json
{
  "areaAcademica": "Ingenieria de software",
  "cargo": "Docente investigador",
  "gradoAcademico": "maestria",
  "tituloUniversitario": "Master en tecnologias de informacion",
  "idFacultad": 1
}
```

## Response

| Campo | Tipo | Requerido | Descripcion |
| --- | --- | --- | --- |
| `idUsuario` | `number` | Si | Identificador del usuario propietario del perfil. |
| `areaAcademica` | `string` | No | Area academica del docente. |
| `cargo` | `string` | No | Cargo del docente. |
| `gradoAcademico` | `string` | No | Grado academico o `null`. |
| `tituloUniversitario` | `string` | No | Titulo universitario o `null`. |
| `idFacultad` | `number` | No | Identificador de facultad o `null`. |

### Ejemplo response

```json
{
  "idUsuario": 1,
  "areaAcademica": "Ingenieria de software",
  "cargo": "Docente investigador",
  "gradoAcademico": "maestria",
  "tituloUniversitario": "Master en tecnologias de informacion",
  "idFacultad": 1
}
```

## Reglas

- El usuario debe existir.
- El usuario debe tener rol activo `docente`.
- El usuario solo puede tener un perfil docente.

