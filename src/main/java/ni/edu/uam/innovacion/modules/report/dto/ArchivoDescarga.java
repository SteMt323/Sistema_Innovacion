package ni.edu.uam.innovacion.modules.report.dto;

public record ArchivoDescarga(
    byte[] contenido,
    String contentType,
    String nombreArchivo
) {
}
