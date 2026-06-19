package ni.edu.uam.innovacion.modules.project.enums;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;

/**
 * Enum que representa los estados posibles de un proyecto
 * dentro del sistema de innovación y emprendimiento.
 *
 * Estados:
 * - ACTIVO: proyecto registrado y en desarrollo.
 * - PAUSADO: proyecto temporalmente detenido.
 * - FINALIZADO: proyecto concluido.
 * - CANCELADO: proyecto cancelado antes de finalizar.
 * - ARCHIVADO: proyecto conservado como historial.
 */
public enum EstadoProyecto {

    ACTIVO("activo"),
    PAUSADO("pausado"),
    FINALIZADO("finalizado"),
    CANCELADO("cancelado"),
    ARCHIVADO("archivado");

    private final String valor;

    EstadoProyecto(String valor) {
        this.valor = valor;
    }

    @JsonValue
    public String getValor() {
        return valor;
    }

    @JsonCreator
    public static EstadoProyecto fromValue(String valor) {
        if (valor == null || valor.isBlank()) {
            return null;
        }

        for (EstadoProyecto estado : values()) {
            if (
                    estado.valor.equalsIgnoreCase(valor.trim())
                            || estado.name().equalsIgnoreCase(valor.trim())
            ) {
                return estado;
            }
        }

        throw new IllegalArgumentException("Estado de proyecto no valido: " + valor);
    }
}