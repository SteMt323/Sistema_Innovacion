package ni.edu.uam.innovacion.modules.project.enums;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;

/**
 * Enum que representa el tipo de vínculo entre un proyecto
 * y una actividad registrada en el sistema.
 *
 * Ejemplos:
 * - ORIGEN: actividad donde nació el proyecto.
 * - SEGUIMIENTO: actividad utilizada para dar continuidad al proyecto.
 * - PRESENTACION: actividad donde el proyecto fue presentado.
 * - FORMACION: actividad formativa relacionada al proyecto.
 * - CONCURSO: actividad competitiva donde participó el proyecto.
 * - MENTORIA: actividad de acompañamiento o asesoría.
 * - OTRO: otro tipo de relación.
 */
public enum TipoVinculoProyectoActividad {

    ORIGEN("origen"),
    SEGUIMIENTO("seguimiento"),
    PRESENTACION("presentacion"),
    FORMACION("formacion"),
    CONCURSO("concurso"),
    MENTORIA("mentoria"),
    OTRO("otro");

    private final String valor;

    TipoVinculoProyectoActividad(String valor) {
        this.valor = valor;
    }

    @JsonValue
    public String getValor() {
        return valor;
    }

    @JsonCreator
    public static TipoVinculoProyectoActividad fromValue(String valor) {
        if (valor == null || valor.isBlank()) {
            return null;
        }

        for (TipoVinculoProyectoActividad tipo : values()) {
            if (
                    tipo.valor.equalsIgnoreCase(valor.trim())
                            || tipo.name().equalsIgnoreCase(valor.trim())
            ) {
                return tipo;
            }
        }

        throw new IllegalArgumentException("Tipo de vinculo de proyecto y actividad no valido: " + valor);
    }
}