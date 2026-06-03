package ni.edu.uam.innovacion.modules.user.enums;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;

public enum GradoAcademico {
    BACHILLER("bachiller"),
    TECNICO("tecnico"),
    LICENCIATURA("licenciatura"),
    INGENIERIA("ingenieria"),
    MAESTRIA("maestria"),
    DOCTORADO("doctorado"),
    OTRO("otro");

    private final String valor;

    GradoAcademico(String valor) {
        this.valor = valor;
    }

    @JsonValue
    public String getValor() {
        return valor;
    }

    @JsonCreator
    public static GradoAcademico fromValue(String valor) {
        if (valor == null || valor.isBlank()) {
            return null;
        }

        for (GradoAcademico grado : values()) {
            if (grado.valor.equalsIgnoreCase(valor.trim()) || grado.name().equalsIgnoreCase(valor.trim())) {
                return grado;
            }
        }

        throw new IllegalArgumentException("Grado academico no valido: " + valor);
    }
}
