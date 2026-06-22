# Backend Sistema Innovacion

## Primer arranque

1. Copia `.env.example` a `.env` en esta misma carpeta.
2. Revisa `DB_URL`, `DB_USER` y `DB_PASSWORD` para que apunten a tu Postgres local.
3. Si quieres usar un secreto distinto en local, cambia `JWT_SECRET` por un valor de al menos 32 caracteres.
4. Inicia el backend con `mvnw.cmd spring-boot:run` o desde tu IDE.

## Como funciona la configuracion

- El proyecto importa `.env` de forma explicita con `spring.config.import=optional:file:.env[.properties]`.
- Si nadie define perfil, Spring usa `local` por defecto.
- En `local`, el backend tiene defaults seguros para desarrollo, incluyendo un secreto JWT local.
- En `prod`, `JWT_SECRET` debe existir como variable de entorno y no puede usar el secreto local por defecto.

## Cierre administrativo

El backend incluye:

- administrador activo unico y transferencia controlada;
- historial de puntos, ajustes, anulaciones e insignias;
- gestion administrativa de mentores y mentorias por actividad;
- dashboard agregado y ranking;
- reportes CSV/PDF bajo demanda;
- constancias y certificados PDF para participaciones validadas.

Ejecuta la verificacion con `mvnw.cmd test`.

## Referencias

- La documentacion funcional de APIs sigue en `../docs/README.md`.
