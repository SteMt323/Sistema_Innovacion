package ni.edu.uam.innovacion.modules.project.enums;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;

/**
 * Enum que representa los estados posibles de un proyecto
 * dentro del Programa PIA.
 *
 * Estados:
 * - ACTIVO: proyecto en proceso dentro del programa.
 * - PAUSADO: proyecto detenido temporalmente.
 * - FINALIZADO: proyecto que concluyó su proceso.
 * - RETIRADO: proyecto que salió del programa antes de finalizar.
 */
public enum EstadoProyectoPIA {

    ACTIVO("activo"),
    PAUSADO("pausado"),
    FINALIZADO("finalizado"),
    RETIRADO("retirado");

    private final String valor;

    EstadoProyectoPIA(String valor) {
        this.valor = valor;
    }

    @JsonValue
    public String getValor() {
        return valor;
    }

    @JsonCreator
    public static EstadoProyectoPIA fromValue(String valor) {
        if (valor == null || valor.isBlank()) {
            return null;
        }

        for (EstadoProyectoPIA estado : values()) {
            if (
                    estado.valor.equalsIgnoreCase(valor.trim())
                            || estado.name().equalsIgnoreCase(valor.trim())
            ) {
                return estado;
            }
        }

        throw new IllegalArgumentException("Estado de proyecto PIA no valido: " + valor);
    }
}