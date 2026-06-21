package ni.edu.uam.innovacion.modules.points.enums;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;
import java.util.Arrays;

public enum EstadoPuntos {
    ACTIVO("activo"),
    ANULADO("anulado");

    private final String valor;

    EstadoPuntos(String valor) {
        this.valor = valor;
    }

    @JsonValue
    public String getValor() {
        return valor;
    }

    @JsonCreator
    public static EstadoPuntos fromValue(String valor) {
        if (valor == null || valor.isBlank()) {
            return null;
        }
        return Arrays.stream(values())
            .filter(estado -> estado.valor.equalsIgnoreCase(valor.trim())
                || estado.name().equalsIgnoreCase(valor.trim()))
            .findFirst()
            .orElseThrow(() -> new IllegalArgumentException(
                "Estado de puntos no valido: " + valor
            ));
    }
}
