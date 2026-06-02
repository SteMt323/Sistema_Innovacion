# Documentacion de APIs

Documentacion funcional del backend de Sistema Innovacion.

Base URL local:

```text
http://localhost:8080
```

## Estructura

- `endpoints/`: documentacion por API, separada por modulo y recurso.
- `schemas/`: contratos de request/response usados por los endpoints.

## Convenciones

- Los endpoints con body reciben y devuelven `application/json`.
- Los errores usan el schema comun [`ErrorResponse`](schemas/common/ErrorResponse.md).
- En el backend actual no hay autenticacion declarada en los controladores documentados.

