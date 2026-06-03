# PerfilMentor

Contrato de request y response para el perfil mentor.

## Request

Usado por `POST /api/usuarios/{idUsuario}/perfiles/mentor`.

| Campo | Tipo | Requerido | Validaciones | Descripcion |
| --- | --- | --- | --- | --- |
| `areaExperiencia` | `string` | No | Maximo 120 caracteres. | Area de experiencia del mentor. |
| `especialidad` | `string` | No | Maximo 120 caracteres. | Especialidad principal. |
| `institucion` | `string` | No | Maximo 150 caracteres. | Institucion asociada. |
| `tipoAcompanamiento` | `string` | No | Maximo 100 caracteres. | Tipo de acompanamiento que brinda. |
| `gradoAcademico` | `string` | No | Valores validos de `GradoAcademico`. | Grado academico. |
| `tituloUniversitario` | `string` | No | Maximo 150 caracteres. | Titulo universitario. |

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
  "areaExperiencia": "Emprendimiento",
  "especialidad": "Modelos de negocio",
  "institucion": "UAM",
  "tipoAcompanamiento": "mentoria",
  "gradoAcademico": "maestria",
  "tituloUniversitario": "Master en administracion"
}
```

## Response

| Campo | Tipo | Requerido | Descripcion |
| --- | --- | --- | --- |
| `idUsuario` | `number` | Si | Identificador del usuario propietario del perfil. |
| `areaExperiencia` | `string` | No | Area de experiencia. |
| `especialidad` | `string` | No | Especialidad. |
| `institucion` | `string` | No | Institucion asociada. |
| `tipoAcompanamiento` | `string` | No | Tipo de acompanamiento. |
| `gradoAcademico` | `string` | No | Grado academico o `null`. |
| `tituloUniversitario` | `string` | No | Titulo universitario o `null`. |

### Ejemplo response

```json
{
  "idUsuario": 1,
  "areaExperiencia": "Emprendimiento",
  "especialidad": "Modelos de negocio",
  "institucion": "UAM",
  "tipoAcompanamiento": "mentoria",
  "gradoAcademico": "maestria",
  "tituloUniversitario": "Master en administracion"
}
```

## Reglas

- El usuario debe existir.
- El usuario debe tener rol activo `mentor`.
- El usuario solo puede tener un perfil mentor.

