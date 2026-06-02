# RolRequest

Contrato usado para crear roles.

## Campos

| Campo | Tipo | Requerido | Validaciones | Descripcion |
| --- | --- | --- | --- | --- |
| `nombre` | `string` | Si | No vacio, maximo 50 caracteres, unico sin distinguir mayusculas/minusculas. | Nombre funcional del rol. Se guarda en minusculas y sin espacios externos. |
| `descripcion` | `string` | No | Maximo 255 caracteres. | Descripcion del rol. |

## Ejemplo

```json
{
  "nombre": "coordinador",
  "descripcion": "Usuario coordinador de actividades de innovacion"
}
```

