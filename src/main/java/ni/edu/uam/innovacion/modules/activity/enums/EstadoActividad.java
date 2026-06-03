package ni.edu.uam.innovacion.modules.activity.enums;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;

public enum EstadoActividad {
    BORRADOR("borrador"),
    PUBLICADA("publicada"),
    EN_CURSO("en_curso"),
    FINALIZADA("finalizada"),
    CANCELADA("cancelada"),
    ARCHIVADA("archivada");

    private final String valor;

    EstadoActividad(String valor) {
        this.valor = valor;
    }

    @JsonValue
    public String getValor() {
        return valor;
    }

    @JsonCreator
    public static EstadoActividad fromValue(String valor) {
        if (valor == null || valor.isBlank()) {
            return null;
        }

        for (EstadoActividad estado : values()) {
            if (estado.valor.equalsIgnoreCase(valor.trim()) || estado.name().equalsIgnoreCase(valor.trim())) {
                return estado;
            }
        }

        throw new IllegalArgumentException("Estado de actividad no valido: " + valor);
    }
}
