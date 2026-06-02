# UsuarioRequest

Contrato de entrada para crear y actualizar usuarios.

## Crear usuario

Usado por `POST /api/usuarios`.

| Campo | Tipo | Requerido | Validaciones | Descripcion |
| --- | --- | --- | --- | --- |
| `nombreCompleto` | `string` | Si | No vacio, maximo 150 caracteres. | Nombre completo del usuario. |
| `documento` | `string` | Si | No vacio, maximo 40 caracteres, unico. | Documento de identidad. |
| `telefono` | `string` | No | Maximo 30 caracteres. | Telefono del usuario. |
| `correo` | `string` | Si | No vacio, email valido, maximo 150 caracteres, unico sin distinguir mayusculas/minusculas. | Correo personal. Se guarda en minusculas y sin espacios externos. |
| `contrasena` | `string` | Si | No vacia, minimo 6 y maximo 100 caracteres. | Contrasena inicial. Se guarda hasheada y nunca se devuelve. |
| `sexo` | `string` | No | Maximo 30 caracteres. | Sexo o valor equivalente usado por el sistema. |
| `tallaCamisa` | `string` | No | Maximo 20 caracteres. | Talla de camisa. |

### Ejemplo

```json
{
  "nombreCompleto": "Maria Fernanda Lopez",
  "documento": "001-010101-0001A",
  "telefono": "88880000",
  "correo": "maria.lopez@example.com",
  "contrasena": "secreto123",
  "sexo": "F",
  "tallaCamisa": "M"
}
```

## Actualizar usuario

Usado por `PUT /api/usuarios/{idUsuario}`.

| Campo | Tipo | Requerido | Validaciones | Descripcion |
| --- | --- | --- | --- | --- |
| `nombreCompleto` | `string` | Si | No vacio, maximo 150 caracteres. | Nombre completo actualizado. |
| `documento` | `string` | Si | No vacio, maximo 40 caracteres, unico para otro usuario. | Documento actualizado. |
| `telefono` | `string` | No | Maximo 30 caracteres. | Telefono actualizado. |
| `correo` | `string` | Si | No vacio, email valido, maximo 150 caracteres, unico para otro usuario. | Correo actualizado. |
| `sexo` | `string` | No | Maximo 30 caracteres. | Sexo actualizado. |
| `tallaCamisa` | `string` | No | Maximo 20 caracteres. | Talla actualizada. |

### Ejemplo

```json
{
  "nombreCompleto": "Maria Fernanda Lopez Actualizada",
  "documento": "001-010101-0001A",
  "telefono": "88881111",
  "correo": "maria.actualizada@example.com",
  "sexo": "F",
  "tallaCamisa": "L"
}
```

