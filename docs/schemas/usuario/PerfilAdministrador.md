# PerfilAdministrador

Contrato de request y response para el perfil administrador.

## Request

Usado por `POST /api/usuarios/{idUsuario}/perfiles/administrador`.

| Campo | Tipo | Requerido | Validaciones | Descripcion |
| --- | --- | --- | --- | --- |
| `cargo` | `string` | Si | No vacio, maximo 100 caracteres. | Cargo administrativo del usuario. |
| `nivelAcceso` | `string` | Si | No vacio, maximo 50 caracteres. | Nivel de acceso funcional. |

### Ejemplo request

```json
{
  "cargo": "Coordinador de innovacion",
  "nivelAcceso": "total"
}
```

## Response

| Campo | Tipo | Requerido | Descripcion |
| --- | --- | --- | --- |
| `idUsuario` | `number` | Si | Identificador del usuario propietario del perfil. |
| `cargo` | `string` | Si | Cargo administrativo. |
| `nivelAcceso` | `string` | Si | Nivel de acceso. |

### Ejemplo response

```json
{
  "idUsuario": 1,
  "cargo": "Coordinador de innovacion",
  "nivelAcceso": "total"
}
```

## Reglas

- El usuario debe existir.
- El usuario debe tener rol activo `administrador`.
- El usuario solo puede tener un perfil administrador.

