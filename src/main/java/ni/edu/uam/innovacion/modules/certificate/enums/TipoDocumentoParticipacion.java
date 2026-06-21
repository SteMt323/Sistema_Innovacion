package ni.edu.uam.innovacion.modules.certificate.enums;

import java.util.Arrays;
import ni.edu.uam.innovacion.common.exception.BadRequestException;

public enum TipoDocumentoParticipacion {
    CONSTANCIA("constancia"),
    CERTIFICADO("certificado");

    private final String valor;

    TipoDocumentoParticipacion(String valor) {
        this.valor = valor;
    }

    public String getValor() {
        return valor;
    }

    public static TipoDocumentoParticipacion fromValue(String valor) {
        if (valor == null || valor.isBlank()) {
            return CONSTANCIA;
        }
        return Arrays.stream(values())
            .filter(tipo -> tipo.valor.equalsIgnoreCase(valor.trim()))
            .findFirst()
            .orElseThrow(() -> new BadRequestException(
                "Tipo de documento no valido. Use constancia o certificado"
            ));
    }
}
