package ni.edu.uam.innovacion.common.enums;


/**
 * Enum que representa el estado general de un registro dentro del sistema.
 *
 * Se usa principalmente en catálogos y tablas de configuración, como:
 * facultades, carreras, roles, categorías, ámbitos de actividad, fuentes de proyecto, etc.
 *
 * La idea es no eliminar físicamente los registros de la base de datos,
 * sino cambiar su estado para conservar el historial del sistema.
 */

public enum EstadoRegistro {
    ACTIVO,
    INACTIVO,
    ARCHIVADO
}