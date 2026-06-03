Claro. En base al análisis, esta sería la lista de pendientes que deberías resolver para que el módulo de usuarios quede alineado con los PDFs, el SVG y el SQL.

**1. Corregir la relación SQL entre usuarios y perfiles**

En el SQL actual, las llaves foráneas de perfiles parecen estar invertidas.

Ahora aparece algo como:

```sql
ALTER TABLE "usuarios" ADD FOREIGN KEY ("id_usuario") REFERENCES "perfiles_estudiante" ("id_usuario");
```

Pero lo correcto debería ser que cada perfil apunte al usuario:

```sql
ALTER TABLE "perfiles_estudiante"
ADD FOREIGN KEY ("id_usuario") REFERENCES "usuarios" ("id_usuario");
```

Debes corregir esto para:

- `perfiles_administrador`
- `perfiles_estudiante`
- `perfiles_docente`
- `perfiles_mentor`
- `perfiles_participante_externo`

La implementación JPA ya va en la dirección correcta con `@MapsId`, pero el SQL visual/propuesto debe quedar consistente.

**2. Definir si el registro será en un solo paso o en varios pasos**

Los casos de uso dicen que el usuario se registra seleccionando su tipo de perfil desde el inicio.

Hoy el backend hace esto en pasos separados:

1. Crear usuario base.
2. Asignar rol.
3. Crear perfil estudiante o administrador.

Eso funciona técnicamente, pero no cumple completamente el flujo propuesto. Debes decidir una de estas rutas:

- Mantener flujo separado, pero documentarlo como decisión técnica.
- Crear un endpoint de registro completo, por ejemplo:

```http
POST /api/usuarios/registro
```

Con un body que incluya:

```json
{
  "tipoPerfil": "estudiante",
  "usuario": {},
  "perfilEstudiante": {}
}
```

Esto se alinea mejor con el PDF.

**3. Completar los perfiles faltantes**

Actualmente solo están implementados:

- `PerfilEstudiante`
- `PerfilAdministrador`

Faltan implementar entidades, DTOs, repositorios, servicios y endpoints para:

- `PerfilDocente`
- `PerfilMentor`
- `PerfilParticipanteExterno`

Según el SQL, deberían cubrir:

Para docente:

- `area_academica`
- `cargo`
- `grado_academico`
- `titulo_universitario`
- `id_facultad`

Para mentor:

- `area_experiencia`
- `especialidad`
- `institucion`
- `tipo_acompanamiento`
- `grado_academico`
- `titulo_universitario`

Para participante externo:

- `ocupacion`
- `institucion_procedencia`

**4. Agregar endpoints para esos perfiles**

Así como existen:

```http
POST /api/usuarios/{idUsuario}/perfiles/estudiante
GET  /api/usuarios/{idUsuario}/perfiles/estudiante
```

También deberían existir:

```http
POST /api/usuarios/{idUsuario}/perfiles/docente
GET  /api/usuarios/{idUsuario}/perfiles/docente

POST /api/usuarios/{idUsuario}/perfiles/mentor
GET  /api/usuarios/{idUsuario}/perfiles/mentor

POST /api/usuarios/{idUsuario}/perfiles/participante-externo
GET  /api/usuarios/{idUsuario}/perfiles/participante-externo
```

Y cada uno debería validar que el usuario tenga el rol activo correspondiente antes de crear el perfil.

**5. Resolver el tema de validación administrativa de usuarios**

Los casos de uso piden que un administrador valide la identidad del usuario.

Hoy solo existe:

```java
ACTIVO
INACTIVO
SUSPENDIDO
```

Pero eso no representa bien estados como:

- Pendiente de validación
- Validado
- Rechazado
- Corrección requerida

Tienes dos opciones limpias:

Opción A: ampliar `EstadoUsuario`:

```java
PENDIENTE_VALIDACION
VALIDADO
RECHAZADO
ACTIVO
INACTIVO
SUSPENDIDO
```

Opción B: dejar `estado` para activación general y agregar otro campo:

```java
estadoValidacion
```

Con valores:

```text
pendiente
validado
rechazado
correccion_requerida
```

Yo preferiría la opción B porque separa “si la cuenta está activa” de “si la identidad ya fue validada”.

**6. Agregar endpoint para validar identidad**

Hace falta un endpoint administrativo similar a:

```http
PATCH /api/usuarios/{idUsuario}/validacion
```

Body:

```json
{
  "estadoValidacion": "validado",
  "observacion": "Documento revisado correctamente"
}
```

También sería útil guardar:

- quién validó
- fecha de validación
- observaciones
- motivo de rechazo, si aplica

**7. Completar la relación con catálogos académicos**

Tú ya mencionaste que faltan los catálogos, y aquí impactan directo.

El SQL dice que `perfiles_estudiante.id_carrera_principal` es `NOT NULL`, pero en el backend está temporalmente nullable.

Cuando existan catálogos, debes completar:

- `facultades`
- `carreras`
- `doble_titulaciones`

Y luego hacer que estudiante valide:

- carrera principal obligatoria
- carrera existente
- facultad derivada o asociada correctamente
- doble titulación registrada en tabla aparte, no solo con boolean

**8. Ajustar el perfil estudiante**

Actualmente el perfil estudiante tiene:

- `cif`
- `correoInstitucional`
- `idCarreraPrincipal`
- `dobleTitular`

Pero la propuesta pide también reflejar facultad/carrera y doble carrera.

Pendientes:

- Hacer obligatorio `idCarreraPrincipal` cuando existan catálogos.
- Validar que el CIF sea único.
- Validar correo institucional único.
- Crear endpoints para doble titulación:

```http
POST /api/usuarios/{idUsuario}/doble-titulaciones
GET  /api/usuarios/{idUsuario}/doble-titulaciones
DELETE /api/usuarios/{idUsuario}/doble-titulaciones/{id}
```

**9. Definir reglas para roles múltiples**

El SQL permite que una persona tenga varios roles. Eso está bien.

Pero debes definir reglas como:

- ¿Un usuario puede ser estudiante y mentor al mismo tiempo?
- ¿Un administrador puede tener también perfil docente?
- ¿Un participante externo puede luego ser mentor?
- ¿Se permite desactivar un rol si todavía existe un perfil asociado?

Hoy el backend permite varios roles, pero todavía no hay reglas de consistencia entre rol y perfil.

**10. Agregar endpoints de actualización de perfiles**

Actualmente se pueden crear y consultar perfiles, pero no actualizar.

Deberían existir:

```http
PUT /api/usuarios/{idUsuario}/perfiles/estudiante
PUT /api/usuarios/{idUsuario}/perfiles/administrador
PUT /api/usuarios/{idUsuario}/perfiles/docente
PUT /api/usuarios/{idUsuario}/perfiles/mentor
PUT /api/usuarios/{idUsuario}/perfiles/participante-externo
```

Esto es necesario porque los PDFs piden gestionar información personal, académica e institucional.

**11. Mejorar búsqueda/listado de usuarios**

Ahora `GET /api/usuarios` lista todo.

Más adelante conviene agregar filtros:

```http
GET /api/usuarios?rol=estudiante
GET /api/usuarios?estado=activo
GET /api/usuarios?estadoValidacion=pendiente
GET /api/usuarios?correo=...
GET /api/usuarios?documento=...
```

Esto es importante para el caso de uso donde el administrador revisa usuarios registrados o pendientes de validación.

**12. Agregar seguridad/autenticación**

La documentación dice que los endpoints están abiertos “en esta fase”. Para cumplir la propuesta real, faltaría:

- login
- autenticación
- autorización por rol
- endpoints administrativos protegidos
- coordinar con el modulo de catalogos la gestion de roles y estados

Especialmente deben quedar fuera del modulo de usuarios los cambios directos de roles, estados y validacion administrativa.

**13. Actualizar la documentación Postman**

El archivo `API_PRUEBAS_USUARIOS.md` esta bien para lo que existe, pero luego debe incluir:

- perfiles docente, mentor y externo
- validación administrativa
- filtros de usuarios
- catálogos académicos
- doble titulación
- flujo recomendado completo según tipo de usuario

**14. Ampliar pruebas**

Ya hay buenas pruebas base. Faltaría agregar tests para:

- crear perfil docente
- crear perfil mentor
- crear perfil participante externo
- impedir crear perfil sin rol correspondiente
- validar usuario como administrador
- rechazar duplicados de CIF/documento/correo
- actualizar perfiles
- filtros de listado
- doble titulación
- reglas de roles múltiples

**Prioridad Recomendada**

1. Corregir SQL de llaves foráneas.
2. Definir flujo de registro: separado o atómico.
3. Agregar `estadoValidacion`.
4. Implementar perfiles faltantes: docente, mentor, externo.
5. Completar endpoints de perfiles.
6. Agregar catálogos académicos.
7. Ajustar estudiante con carrera obligatoria y doble titulación.
8. Agregar seguridad por roles.
9. Mejorar documentación y pruebas.

En resumen: el módulo actual está sano como base, pero todavía representa solo el primer bloque del modelo. Lo siguiente importante es cerrar la brecha entre “usuario técnico creado en backend” y “persona registrada, clasificada y validada según la propuesta del sistema”.
