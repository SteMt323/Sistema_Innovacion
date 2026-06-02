# CambiarContrasenaRequest

Contrato usado para cambiar la contrasena de un usuario.

## Campos

| Campo | Tipo | Requerido | Validaciones | Descripcion |
| --- | --- | --- | --- | --- |
| `contrasena` | `string` | Si | No vacia, minimo 6 y maximo 100 caracteres. | Nueva contrasena. Se guarda hasheada y no se devuelve. |

## Ejemplo

```json
{
  "contrasena": "nuevoSecreto123"
}
```

