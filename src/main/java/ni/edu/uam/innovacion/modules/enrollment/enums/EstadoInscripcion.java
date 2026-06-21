package ni.edu.uam.innovacion.modules.enrollment.enums;

import java.util.Arrays;

/**
 * Estados posibles de una inscripción a una actividad.
 *
 * Estos estados permiten controlar el flujo desde que una persona se registra
 * en una actividad hasta que su inscripción es confirmada, cancelada o rechazada.
 */
public enum EstadoInscripcion {

    REGISTRADA("registrada"),
    PENDIENTE("pendiente"),
    CONFIRMADA("confirmada"),
    CANCELADA("cancelada"),
    RECHAZADA("rechazada");

    private final String valor;

    EstadoInscripcion(String valor) {
        this.valor = valor;
    }

    public String getValor() {
        return valor;
    }

    public static EstadoInscripcion fromValue(String valor) {
        if (valor == null || valor.isBlank()) {
            return null;
        }

        String valorNormalizado = valor.trim().toLowerCase();

        return Arrays.stream(values())
                .filter(estado -> estado.valor.equals(valorNormalizado))
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException(
                        "Estado de inscripcion no valido: " + valor
                ));
    }
}