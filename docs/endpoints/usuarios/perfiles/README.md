# Perfiles de usuario

APIs para crear y consultar perfiles asociados a un usuario.

Base path:

```text
/api/usuarios/{idUsuario}/perfiles
```

## Endpoints

| Perfil | Metodo | Ruta | Archivo |
| --- | --- | --- | --- |
| Administrador | `GET` | `/api/usuarios/{idUsuario}/perfiles/administrador` | [`administrador/GET-obtener-perfil-admin.md`](administrador/GET-obtener-perfil-admin.md) |
| Administrador | `POST` | `/api/usuarios/{idUsuario}/perfiles/administrador` | [`administrador/POST-crear-perfil-admin.md`](administrador/POST-crear-perfil-admin.md) |
| Docente | `GET` | `/api/usuarios/{idUsuario}/perfiles/docente` | [`docente/GET-obtener-perfil-docente.md`](docente/GET-obtener-perfil-docente.md) |
| Docente | `POST` | `/api/usuarios/{idUsuario}/perfiles/docente` | [`docente/POST-crear-perfil-docente.md`](docente/POST-crear-perfil-docente.md) |
| Estudiante | `GET` | `/api/usuarios/{idUsuario}/perfiles/estudiante` | [`estudiante/GET-obtener-perfil-estudiante.md`](estudiante/GET-obtener-perfil-estudiante.md) |
| Estudiante | `POST` | `/api/usuarios/{idUsuario}/perfiles/estudiante` | [`estudiante/POST-crear-perfil-estudiante.md`](estudiante/POST-crear-perfil-estudiante.md) |
| Mentor | `GET` | `/api/usuarios/{idUsuario}/perfiles/mentor` | [`mentor/GET-obtener-perfil-mentor.md`](mentor/GET-obtener-perfil-mentor.md) |
| Mentor | `POST` | `/api/usuarios/{idUsuario}/perfiles/mentor` | [`mentor/POST-crear-perfil-mentor.md`](mentor/POST-crear-perfil-mentor.md) |
| Participante externo | `GET` | `/api/usuarios/{idUsuario}/perfiles/participante-externo` | [`participante-externo/GET-obtener-perfil-participante-externo.md`](participante-externo/GET-obtener-perfil-participante-externo.md) |
| Participante externo | `POST` | `/api/usuarios/{idUsuario}/perfiles/participante-externo` | [`participante-externo/POST-crear-perfil-participante-externo.md`](participante-externo/POST-crear-perfil-participante-externo.md) |

## Reglas generales

- Cada perfil se identifica por `idUsuario`.
- Para crear perfil estudiante, el usuario debe tener rol activo `estudiante`.
- Para crear perfil administrador, el usuario debe tener rol activo `administrador`.
- Para crear perfil docente, el usuario debe tener rol activo `docente`.
- Para crear perfil mentor, el usuario debe tener rol activo `mentor`.
- Para crear perfil participante externo, el usuario debe tener rol activo `participante_externo`.
- No se puede desactivar un rol si ya existe el perfil asociado.

## Schemas

- [`PerfilEstudiante`](../../../schemas/usuario/PerfilEstudiante.md)
- [`PerfilAdministrador`](../../../schemas/usuario/PerfilAdministrador.md)
- [`PerfilDocente`](../../../schemas/usuario/PerfilDocente.md)
- [`PerfilMentor`](../../../schemas/usuario/PerfilMentor.md)
- [`PerfilParticipanteExterno`](../../../schemas/usuario/PerfilParticipanteExterno.md)
- [`ErrorResponse`](../../../schemas/common/ErrorResponse.md)
