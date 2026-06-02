# API publica - Usuarios, roles y perfiles

Documentacion para probar en Postman el modulo base de usuarios, roles, perfil estudiante y perfil administrador.

## Configuracion inicial

Base URL local:

```text
http://localhost:8080
```

En Postman puedes crear estas variables:

| Variable | Valor inicial |
| --- | --- |
| `baseUrl` | `http://localhost:8080` |
| `idUsuario` | Se llena despues de crear un usuario |

Headers recomendados para requests con body:

| Key | Value |
| --- | --- |
| `Content-Type` | `application/json` |
| `Accept` | `application/json` |

Autenticacion: no aplica en esta fase. Los endpoints estan abiertos para pruebas.

## Flujo recomendado de prueba

1. `GET /api/roles` para verificar que los roles base fueron sembrados.
2. `POST /api/usuarios` para crear un usuario.
3. Guardar el `idUsuario` de la respuesta en la variable `idUsuario`.
4. `POST /api/usuarios/{{idUsuario}}/roles` para asignar `estudiante` o `administrador`.
5. Crear el perfil correspondiente:
   - estudiante: `POST /api/usuarios/{{idUsuario}}/perfiles/estudiante`
   - administrador: `POST /api/usuarios/{{idUsuario}}/perfiles/administrador`
6. `GET /api/usuarios/{{idUsuario}}` para verificar usuario, roles activos y perfiles.

## Resumen de endpoints

| Metodo | Endpoint | Uso |
| --- | --- | --- |
| `POST` | `/api/usuarios` | Crear usuario base |
| `GET` | `/api/usuarios` | Listar usuarios |
| `GET` | `/api/usuarios/{idUsuario}` | Obtener usuario por ID |
| `PUT` | `/api/usuarios/{idUsuario}` | Actualizar datos base del usuario |
| `PATCH` | `/api/usuarios/{idUsuario}/contrasena` | Cambiar contrasena |
| `PATCH` | `/api/usuarios/{idUsuario}/estado` | Cambiar estado |
| `GET` | `/api/roles` | Listar roles |
| `POST` | `/api/roles` | Crear rol |
| `POST` | `/api/usuarios/{idUsuario}/roles` | Asignar rol al usuario |
| `DELETE` | `/api/usuarios/{idUsuario}/roles/{nombreRol}` | Desactivar rol del usuario |
| `POST` | `/api/usuarios/{idUsuario}/perfiles/estudiante` | Crear perfil estudiante |
| `GET` | `/api/usuarios/{idUsuario}/perfiles/estudiante` | Obtener perfil estudiante |
| `POST` | `/api/usuarios/{idUsuario}/perfiles/administrador` | Crear perfil administrador |
| `GET` | `/api/usuarios/{idUsuario}/perfiles/administrador` | Obtener perfil administrador |

## Roles

### Listar roles

```http
GET {{baseUrl}}/api/roles
```

Respuesta esperada: `200 OK`

```json
[
  {
    "idRol": 1,
    "nombre": "estudiante",
    "descripcion": "Usuario estudiante del sistema"
  },
  {
    "idRol": 2,
    "nombre": "administrador",
    "descripcion": "Usuario con permisos administrativos"
  }
]
```

Roles base sembrados automaticamente:

```text
estudiante
administrador
docente
mentor
participante_externo
```

### Crear rol

```http
POST {{baseUrl}}/api/roles
```

Body:

```json
{
  "nombre": "coordinador",
  "descripcion": "Usuario coordinador de actividades"
}
```

Respuesta esperada: `201 Created`

```json
{
  "idRol": 6,
  "nombre": "coordinador",
  "descripcion": "Usuario coordinador de actividades"
}
```

Errores comunes:

| Status | Caso |
| --- | --- |
| `400 Bad Request` | `nombre` vacio o mayor a 50 caracteres |
| `409 Conflict` | Ya existe un rol con ese nombre |

## Usuarios

### Crear usuario

```http
POST {{baseUrl}}/api/usuarios
```

Body:

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

Respuesta esperada: `201 Created`

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
  "fechaRegistro": "2026-06-01T15:00:00.000000",
  "ultimoAcceso": null,
  "roles": [],
  "perfilEstudiante": null,
  "perfilAdministrador": null
}
```

Notas:

- La contrasena no se devuelve en la respuesta.
- Internamente se guarda como hash BCrypt en `contrasena_hash`.
- `correo` y `documento` deben ser unicos.

Errores comunes:

| Status | Caso |
| --- | --- |
| `400 Bad Request` | Campos requeridos vacios o correo invalido |
| `409 Conflict` | Correo o documento duplicado |

### Listar usuarios

```http
GET {{baseUrl}}/api/usuarios
```

Respuesta esperada: `200 OK`

```json
[
  {
    "idUsuario": 1,
    "nombreCompleto": "Maria Fernanda Lopez",
    "documento": "001-010101-0001A",
    "telefono": "88880000",
    "correo": "maria.lopez@example.com",
    "sexo": "F",
    "tallaCamisa": "M",
    "estado": "activo",
    "fechaRegistro": "2026-06-01T15:00:00.000000",
    "ultimoAcceso": null,
    "roles": [],
    "perfilEstudiante": null,
    "perfilAdministrador": null
  }
]
```

### Obtener usuario por ID

```http
GET {{baseUrl}}/api/usuarios/{{idUsuario}}
```

Respuesta esperada: `200 OK`

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
  "fechaRegistro": "2026-06-01T15:00:00.000000",
  "ultimoAcceso": null,
  "roles": [],
  "perfilEstudiante": null,
  "perfilAdministrador": null
}
```

Errores comunes:

| Status | Caso |
| --- | --- |
| `404 Not Found` | No existe usuario con ese ID |

### Actualizar usuario

```http
PUT {{baseUrl}}/api/usuarios/{{idUsuario}}
```

Body:

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

Respuesta esperada: `200 OK`

```json
{
  "idUsuario": 1,
  "nombreCompleto": "Maria Fernanda Lopez Actualizada",
  "documento": "001-010101-0001A",
  "telefono": "88881111",
  "correo": "maria.actualizada@example.com",
  "sexo": "F",
  "tallaCamisa": "L",
  "estado": "activo",
  "fechaRegistro": "2026-06-01T15:00:00.000000",
  "ultimoAcceso": null,
  "roles": [],
  "perfilEstudiante": null,
  "perfilAdministrador": null
}
```

Notas:

- Este endpoint no cambia contrasena.
- Este endpoint no cambia roles.

### Cambiar contrasena

```http
PATCH {{baseUrl}}/api/usuarios/{{idUsuario}}/contrasena
```

Body:

```json
{
  "contrasena": "nuevoSecreto123"
}
```

Respuesta esperada: `200 OK`

Devuelve el usuario actualizado sin mostrar la contrasena.

Errores comunes:

| Status | Caso |
| --- | --- |
| `400 Bad Request` | Contrasena vacia o menor a 6 caracteres |
| `404 Not Found` | No existe usuario con ese ID |

### Cambiar estado

```http
PATCH {{baseUrl}}/api/usuarios/{{idUsuario}}/estado
```

Body:

```json
{
  "estado": "inactivo"
}
```

Valores permitidos:

```text
activo
inactivo
suspendido
```

Respuesta esperada: `200 OK`

```json
{
  "idUsuario": 1,
  "nombreCompleto": "Maria Fernanda Lopez",
  "documento": "001-010101-0001A",
  "telefono": "88880000",
  "correo": "maria.lopez@example.com",
  "sexo": "F",
  "tallaCamisa": "M",
  "estado": "inactivo",
  "fechaRegistro": "2026-06-01T15:00:00.000000",
  "ultimoAcceso": null,
  "roles": [],
  "perfilEstudiante": null,
  "perfilAdministrador": null
}
```

## Asignacion de roles a usuarios

### Asignar rol

```http
POST {{baseUrl}}/api/usuarios/{{idUsuario}}/roles
```

Body para estudiante:

```json
{
  "nombreRol": "estudiante"
}
```

Body para administrador:

```json
{
  "nombreRol": "administrador"
}
```

Respuesta esperada: `200 OK`

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
  "fechaRegistro": "2026-06-01T15:00:00.000000",
  "ultimoAcceso": null,
  "roles": [
    {
      "idRol": 1,
      "nombre": "estudiante",
      "descripcion": "Usuario estudiante del sistema"
    }
  ],
  "perfilEstudiante": null,
  "perfilAdministrador": null
}
```

Notas:

- Si el rol estaba desactivado para el usuario, este endpoint lo reactiva.
- El nombre del rol se normaliza a minusculas.

Errores comunes:

| Status | Caso |
| --- | --- |
| `404 Not Found` | Usuario no existe o rol no existe |
| `400 Bad Request` | `nombreRol` vacio |

### Desactivar rol

```http
DELETE {{baseUrl}}/api/usuarios/{{idUsuario}}/roles/estudiante
```

Respuesta esperada: `204 No Content`

Notas:

- No elimina fisicamente el registro de `usuario_roles`.
- Solo marca la asignacion como `activo=false`.

Errores comunes:

| Status | Caso |
| --- | --- |
| `404 Not Found` | Usuario no existe o no tiene asignado ese rol |

## Perfil estudiante

### Crear perfil estudiante

Antes de usar este endpoint, el usuario debe tener rol activo `estudiante`.

```http
POST {{baseUrl}}/api/usuarios/{{idUsuario}}/perfiles/estudiante
```

Body:

```json
{
  "cif": "2026-0001",
  "correoInstitucional": "maria.lopez@uam.edu.ni",
  "idCarreraPrincipal": null,
  "dobleTitular": false
}
```

Respuesta esperada: `201 Created`

```json
{
  "idUsuario": 1,
  "cif": "2026-0001",
  "correoInstitucional": "maria.lopez@uam.edu.ni",
  "idCarreraPrincipal": null,
  "dobleTitular": false
}
```

Notas:

- `idCarreraPrincipal` es temporalmente nullable.
- Todavia no valida contra catalogos academicos.
- `cif` debe ser unico.
- `correoInstitucional` puede ser null, pero si se envia debe ser unico.

Errores comunes:

| Status | Caso |
| --- | --- |
| `400 Bad Request` | Usuario no tiene rol `estudiante` activo o datos invalidos |
| `404 Not Found` | Usuario no existe |
| `409 Conflict` | Usuario ya tiene perfil estudiante, CIF duplicado o correo institucional duplicado |

### Obtener perfil estudiante

```http
GET {{baseUrl}}/api/usuarios/{{idUsuario}}/perfiles/estudiante
```

Respuesta esperada: `200 OK`

```json
{
  "idUsuario": 1,
  "cif": "2026-0001",
  "correoInstitucional": "maria.lopez@uam.edu.ni",
  "idCarreraPrincipal": null,
  "dobleTitular": false
}
```

Errores comunes:

| Status | Caso |
| --- | --- |
| `404 Not Found` | El usuario no tiene perfil estudiante |

## Perfil administrador

### Crear perfil administrador

Antes de usar este endpoint, el usuario debe tener rol activo `administrador`.

```http
POST {{baseUrl}}/api/usuarios/{{idUsuario}}/perfiles/administrador
```

Body:

```json
{
  "cargo": "Coordinador de innovacion",
  "nivelAcceso": "total"
}
```

Respuesta esperada: `201 Created`

```json
{
  "idUsuario": 1,
  "cargo": "Coordinador de innovacion",
  "nivelAcceso": "total"
}
```

Errores comunes:

| Status | Caso |
| --- | --- |
| `400 Bad Request` | Usuario no tiene rol `administrador` activo o datos invalidos |
| `404 Not Found` | Usuario no existe |
| `409 Conflict` | Usuario ya tiene perfil administrador |

### Obtener perfil administrador

```http
GET {{baseUrl}}/api/usuarios/{{idUsuario}}/perfiles/administrador
```

Respuesta esperada: `200 OK`

```json
{
  "idUsuario": 1,
  "cargo": "Coordinador de innovacion",
  "nivelAcceso": "total"
}
```

Errores comunes:

| Status | Caso |
| --- | --- |
| `404 Not Found` | El usuario no tiene perfil administrador |

## Formato de errores

Cuando ocurre un error, la API responde con este formato general:

```json
{
  "timestamp": "2026-06-01T15:00:00.000000",
  "status": 409,
  "error": "Conflict",
  "message": "Ya existe un usuario con el correo maria.lopez@example.com",
  "details": {}
}
```

En errores de validacion, `details` contiene los campos invalidos:

```json
{
  "timestamp": "2026-06-01T15:00:00.000000",
  "status": 400,
  "error": "Bad Request",
  "message": "La solicitud tiene campos invalidos",
  "details": {
    "correo": "must be a well-formed email address",
    "contrasena": "size must be between 6 and 100"
  }
}
```

## Checklist rapido para Postman

### Usuario estudiante

1. `GET {{baseUrl}}/api/roles`
2. `POST {{baseUrl}}/api/usuarios`
3. Copiar `idUsuario` de la respuesta.
4. `POST {{baseUrl}}/api/usuarios/{{idUsuario}}/roles`

```json
{
  "nombreRol": "estudiante"
}
```

5. `POST {{baseUrl}}/api/usuarios/{{idUsuario}}/perfiles/estudiante`

```json
{
  "cif": "2026-0001",
  "correoInstitucional": "maria.lopez@uam.edu.ni",
  "idCarreraPrincipal": null,
  "dobleTitular": false
}
```

6. `GET {{baseUrl}}/api/usuarios/{{idUsuario}}`

### Usuario administrador

1. `POST {{baseUrl}}/api/usuarios`
2. Copiar `idUsuario` de la respuesta.
3. `POST {{baseUrl}}/api/usuarios/{{idUsuario}}/roles`

```json
{
  "nombreRol": "administrador"
}
```

4. `POST {{baseUrl}}/api/usuarios/{{idUsuario}}/perfiles/administrador`

```json
{
  "cargo": "Coordinador de innovacion",
  "nivelAcceso": "total"
}
```

5. `GET {{baseUrl}}/api/usuarios/{{idUsuario}}`
