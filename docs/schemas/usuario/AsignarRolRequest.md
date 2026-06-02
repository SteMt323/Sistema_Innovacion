# AsignarRolRequest

Contrato usado para asignar o reactivar un rol a un usuario.

## Campos

| Campo | Tipo | Requerido | Validaciones | Descripcion |
| --- | --- | --- | --- | --- |
| `nombreRol` | `string` | Si | No vacio, maximo 50 caracteres. Debe existir en `roles`. | Nombre del rol a asignar. Se normaliza a minusculas y sin espacios externos. |

## Ejemplo

```json
{
  "nombreRol": "estudiante"
}
```

