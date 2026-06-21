package ni.edu.uam.innovacion.modules.report.enums;

import java.util.Arrays;
import ni.edu.uam.innovacion.common.exception.BadRequestException;

public enum FormatoReporte {
    CSV("csv", "text/csv"),
    PDF("pdf", "application/pdf");

    private final String extension;
    private final String contentType;

    FormatoReporte(String extension, String contentType) {
        this.extension = extension;
        this.contentType = contentType;
    }

    public String getExtension() {
        return extension;
    }

    public String getContentType() {
        return contentType;
    }

    public static FormatoReporte fromValue(String valor) {
        if (valor == null || valor.isBlank()) {
            return PDF;
        }
        return Arrays.stream(values())
            .filter(formato -> formato.extension.equalsIgnoreCase(valor.trim()))
            .findFirst()
            .orElseThrow(() -> new BadRequestException(
                "Formato de reporte no valido. Use csv o pdf"
            ));
    }
}
