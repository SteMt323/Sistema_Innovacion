# Usuarios

APIs para gestionar usuarios y perfiles.

Base path:

```text
/api/usuarios
```

## Endpoints principales

| Metodo | Ruta | Archivo |
| --- | --- | --- |
| `GET` | `/api/usuarios` | [`GET-obtener-usuarios.md`](GET-obtener-usuarios.md) |
| `POST` | `/api/usuarios` | [`POST-crear-usuario.md`](POST-crear-usuario.md) |
| `GET` | `/api/usuarios/{idUsuario}` | [`GET-obtener-usuario-por-id.md`](GET-obtener-usuario-por-id.md) |
| `PUT` | `/api/usuarios/{idUsuario}` | [`PUT-actualizar-usuario.md`](PUT-actualizar-usuario.md) |
| `PATCH` | `/api/usuarios/{idUsuario}/contrasena` | [`PATCH-cambiar-contrasena.md`](PATCH-cambiar-contrasena.md) |

## Subrecursos

| Recurso | Documentacion |
| --- | --- |
| Perfiles | [`perfiles/README.md`](perfiles/README.md) |

## Schemas

- [`UsuarioRequest`](../../schemas/usuario/UsuarioRequest.md)
- [`UsuarioResponse`](../../schemas/usuario/UsuarioResponse.md)
- [`CambiarContrasenaRequest`](../../schemas/usuario/CambiarContrasenaRequest.md)
- [`PerfilEstudiante`](../../schemas/usuario/PerfilEstudiante.md)
- [`PerfilAdministrador`](../../schemas/usuario/PerfilAdministrador.md)
- [`PerfilDocente`](../../schemas/usuario/PerfilDocente.md)
- [`PerfilMentor`](../../schemas/usuario/PerfilMentor.md)
- [`PerfilParticipanteExterno`](../../schemas/usuario/PerfilParticipanteExterno.md)
- [`ErrorResponse`](../../schemas/common/ErrorResponse.md)
