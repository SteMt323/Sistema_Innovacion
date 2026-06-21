package ni.edu.uam.innovacion.modules.points.enums;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;
import java.util.Arrays;

public enum TipoMovimientoPuntos {
    OTORGAMIENTO("otorgamiento"),
    AJUSTE_MANUAL("ajuste_manual"),
    CORRECCION("correccion"),
    PENALIZACION("penalizacion");

    private final String valor;

    TipoMovimientoPuntos(String valor) {
        this.valor = valor;
    }

    @JsonValue
    public String getValor() {
        return valor;
    }

    @JsonCreator
    public static TipoMovimientoPuntos fromValue(String valor) {
        if (valor == null || valor.isBlank()) {
            return null;
        }
        return Arrays.stream(values())
            .filter(tipo -> tipo.valor.equalsIgnoreCase(valor.trim())
                || tipo.name().equalsIgnoreCase(valor.trim()))
            .findFirst()
            .orElseThrow(() -> new IllegalArgumentException(
                "Tipo de movimiento de puntos no valido: " + valor
            ));
    }

    public boolean esManual() {
        return !OTORGAMIENTO.equals(this);
    }
}
