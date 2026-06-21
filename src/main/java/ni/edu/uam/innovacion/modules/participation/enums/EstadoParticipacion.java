package ni.edu.uam.innovacion.modules.participation.enums;

import java.util.Arrays;

public enum EstadoParticipacion {

    PENDIENTE("pendiente"),
    VALIDADA("validada"),
    NO_VALIDADA("no_validada"),
    ANULADA("anulada");

    private final String valor;

    EstadoParticipacion(String valor) {
        this.valor = valor;
    }

    public String getValor() {
        return valor;
    }

    public static EstadoParticipacion fromValue(String valor) {
        if (valor == null || valor.isBlank()) {
            return null;
        }

        String valorNormalizado = valor.trim().toLowerCase();

        return Arrays.stream(values())
                .filter(estado -> estado.valor.equals(valorNormalizado))
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException(
                        "Estado de participación no válido: " + valor
                ));
    }
}