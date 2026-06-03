# UsuarioResponse

Contrato devuelto por las APIs de usuarios.

## Campos

| Campo | Tipo | Requerido | Descripcion |
| --- | --- | --- | --- |
| `idUsuario` | `number` | Si | Identificador del usuario. |
| `nombreCompleto` | `string` | Si | Nombre completo. |
| `documento` | `string` | Si | Documento de identidad. |
| `telefono` | `string` | No | Telefono. |
| `correo` | `string` | Si | Correo personal. |
| `sexo` | `string` | No | Sexo o valor equivalente usado por el sistema. |
| `tallaCamisa` | `string` | No | Talla de camisa. |
| `estado` | `string` | Si | Estado actual: `activo`, `inactivo` o `suspendido`. |
| `fechaRegistro` | `string` | Si | Fecha de registro en formato ISO-8601 local. |
| `ultimoAcceso` | `string` | No | Ultimo acceso. Actualmente puede ser `null`. |
| `roles` | `array<RolResponse>` | Si | Roles activos del usuario, ordenados por nombre. |
| `perfilEstudiante` | `PerfilEstudiante` | No | Perfil estudiante o `null`. |
| `perfilAdministrador` | `PerfilAdministrador` | No | Perfil administrador o `null`. |
| `perfilDocente` | `PerfilDocente` | No | Perfil docente o `null`. |
| `perfilMentor` | `PerfilMentor` | No | Perfil mentor o `null`. |
| `perfilParticipanteExterno` | `PerfilParticipanteExterno` | No | Perfil participante externo o `null`. |

## Ejemplo

```json
{
  "idUsuario": 1,
  "nombreCompleto": "Maria Fernanda Lopez",
  "documento": "001-010101-0001A",
  "telefono": "88880000",
  "correo": "maria.lopez@example.com",
  "sexo": "F",
  "tallaCamisa": "M",
  "estado": "activo",
  "fechaRegistro": "2026-06-01T15:00:00",
  "ultimoAcceso": null,
  "roles": [
    {
      "idRol": 1,
      "nombre": "estudiante",
      "descripcion": "Usuario estudiante del sistema"
    }
  ],
  "perfilEstudiante": null,
  "perfilAdministrador": null,
  "perfilDocente": null,
  "perfilMentor": null,
  "perfilParticipanteExterno": null
}
```

## Referencias

- [`RolResponse`](../rol/RolResponse.md)
- [`PerfilEstudiante`](PerfilEstudiante.md)
- [`PerfilAdministrador`](PerfilAdministrador.md)
- [`PerfilDocente`](PerfilDocente.md)
- [`PerfilMentor`](PerfilMentor.md)
- [`PerfilParticipanteExterno`](PerfilParticipanteExterno.md)

