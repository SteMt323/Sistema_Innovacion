package ni.edu.uam.innovacion.modules.user.enums;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;

public enum EstadoUsuario {
    ACTIVO("activo"),
    INACTIVO("inactivo"),
    SUSPENDIDO("suspendido");

    private final String valor;

    EstadoUsuario(String valor) {
        this.valor = valor;
    }

    @JsonValue
    public String getValor() {
        return valor;
    }

    @JsonCreator
    public static EstadoUsuario fromValue(String valor) {
        if (valor == null) {
            return null;
        }

        for (EstadoUsuario estado : values()) {
            if (estado.valor.equalsIgnoreCase(valor.trim()) || estado.name().equalsIgnoreCase(valor.trim())) {
                return estado;
            }
        }

        throw new IllegalArgumentException("Estado de usuario no valido: " + valor);
    }
}
