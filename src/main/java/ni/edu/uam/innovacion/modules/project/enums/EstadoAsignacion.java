package ni.edu.uam.innovacion.modules.project.enums;

/**
 * Enum que representa el estado de una asignación de mentor a proyecto.
 *
 * Se utiliza en la entidad AsignacionMentorProyecto.
 *
 * Valores:
 * - activa: la mentoría está vigente.
 * - finalizada: la mentoría terminó correctamente.
 * - cancelada: la mentoría fue cancelada antes de finalizar.
 */
public enum EstadoAsignacion {

    ACTIVA("activa"),
    FINALIZADA("finalizada"),
    CANCELADA("cancelada");

    private final String valor;

    EstadoAsignacion(String valor) {
        this.valor = valor;
    }

    public String getValor() {
        return valor;
    }

    /**
     * Convierte un texto de base de datos o de request
     * en su valor correspondiente del enum.
     *
     * Ejemplo:
     * "activa" -> ACTIVA
     */
    public static EstadoAsignacion fromValue(String valor) {
        if (valor == null || valor.isBlank()) {
            return null;
        }

        String valorNormalizado = valor.trim();

        for (EstadoAsignacion estado : EstadoAsignacion.values()) {
            if (estado.valor.equalsIgnoreCase(valorNormalizado)) {
                return estado;
            }
        }

        throw new IllegalArgumentException(
                "Estado de asignación no válido: " + valor
        );
    }
}