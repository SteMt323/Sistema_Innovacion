package ni.edu.uam.innovacion.modules.activity.enums;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;

public enum ModalidadActividad {
    PRESENCIAL("presencial"),
    VIRTUAL("virtual"),
    HIBRIDA("hibrida");

    private final String valor;

    ModalidadActividad(String valor) {
        this.valor = valor;
    }

    @JsonValue
    public String getValor() {
        return valor;
    }

    @JsonCreator
    public static ModalidadActividad fromValue(String valor) {
        if (valor == null || valor.isBlank()) {
            return null;
        }

        for (ModalidadActividad modalidad : values()) {
            if (modalidad.valor.equalsIgnoreCase(valor.trim()) || modalidad.name().equalsIgnoreCase(valor.trim())) {
                return modalidad;
            }
        }

        throw new IllegalArgumentException("Modalidad de actividad no valida: " + valor);
    }
}
