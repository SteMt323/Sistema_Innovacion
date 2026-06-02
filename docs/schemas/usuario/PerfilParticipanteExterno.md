# PerfilParticipanteExterno

Contrato de request y response para el perfil participante externo.

## Request

Usado por `POST /api/usuarios/{idUsuario}/perfiles/participante-externo`.

| Campo | Tipo | Requerido | Validaciones | Descripcion |
| --- | --- | --- | --- | --- |
| `ocupacion` | `string` | No | Maximo 100 caracteres. | Ocupacion del participante externo. |
| `institucionProcedencia` | `string` | No | Maximo 150 caracteres. | Institucion de procedencia. |

### Ejemplo request

```json
{
  "ocupacion": "Consultor",
  "institucionProcedencia": "Empresa externa"
}
```

## Response

| Campo | Tipo | Requerido | Descripcion |
| --- | --- | --- | --- |
| `idUsuario` | `number` | Si | Identificador del usuario propietario del perfil. |
| `ocupacion` | `string` | No | Ocupacion. |
| `institucionProcedencia` | `string` | No | Institucion de procedencia. |

### Ejemplo response

```json
{
  "idUsuario": 1,
  "ocupacion": "Consultor",
  "institucionProcedencia": "Empresa externa"
}
```

## Reglas

- El usuario debe existir.
- El usuario debe tener rol activo `participante_externo`.
- El usuario solo puede tener un perfil participante externo.

