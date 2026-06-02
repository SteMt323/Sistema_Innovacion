# CambiarEstadoRequest

Contrato usado para cambiar el estado de un usuario.

## Campos

| Campo | Tipo | Requerido | Validaciones | Descripcion |
| --- | --- | --- | --- | --- |
| `estado` | `string` | Si | Valores validos: `activo`, `inactivo`, `suspendido`. Tambien acepta nombres del enum sin distinguir mayusculas/minusculas. | Nuevo estado del usuario. |

## Ejemplo

```json
{
  "estado": "inactivo"
}
```

