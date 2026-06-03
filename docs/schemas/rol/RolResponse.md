# RolResponse

Contrato devuelto por las APIs de roles y por el arreglo `roles` de `UsuarioResponse`.

## Campos

| Campo | Tipo | Requerido | Descripcion |
| --- | --- | --- | --- |
| `idRol` | `number` | Si | Identificador del rol. |
| `nombre` | `string` | Si | Nombre del rol. |
| `descripcion` | `string` | No | Descripcion del rol. |

## Ejemplo

```json
{
  "idRol": 1,
  "nombre": "estudiante",
  "descripcion": "Usuario estudiante del sistema"
}
```

## Roles base sembrados

El backend crea estos roles si no existen:

| Rol | Descripcion |
| --- | --- |
| `estudiante` | Usuario estudiante del sistema |
| `administrador` | Usuario con permisos administrativos |
| `docente` | Usuario docente |
| `mentor` | Usuario mentor |
| `participante_externo` | Usuario externo participante |

