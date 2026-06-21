package ni.edu.uam.innovacion.modules.project.enums;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;

/**
 * Enum que representa las fases del Programa PIA.
 *
 * Fases:
 * - PROSPECTO: proyecto identificado como posible candidato al programa.
 * - PREINCUBACION: fase inicial de estructuración y validación.
 * - INCUBACION: fase de desarrollo, acompañamiento y fortalecimiento.
 * - ACELERACION: fase de crecimiento o escalamiento del proyecto.
 * - SEGUIMIENTO: fase posterior para monitorear avances.
 * - GRADUADO: proyecto que completó el proceso del programa.
 */
public enum FasePIA {

    PROSPECTO("prospecto"),
    PREINCUBACION("preincubacion"),
    INCUBACION("incubacion"),
    ACELERACION("aceleracion"),
    SEGUIMIENTO("seguimiento"),
    GRADUADO("graduado");

    private final String valor;

    FasePIA(String valor) {
        this.valor = valor;
    }

    @JsonValue
    public String getValor() {
        return valor;
    }

    @JsonCreator
    public static FasePIA fromValue(String valor) {
        if (valor == null || valor.isBlank()) {
            return null;
        }

        for (FasePIA fase : values()) {
            if (
                    fase.valor.equalsIgnoreCase(valor.trim())
                            || fase.name().equalsIgnoreCase(valor.trim())
            ) {
                return fase;
            }
        }

        throw new IllegalArgumentException("Fase PIA no valida: " + valor);
    }
}