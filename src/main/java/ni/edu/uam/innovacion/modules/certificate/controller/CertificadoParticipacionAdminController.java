package ni.edu.uam.innovacion.modules.certificate.controller;

import java.nio.charset.StandardCharsets;
import ni.edu.uam.innovacion.modules.certificate.enums.TipoDocumentoParticipacion;
import ni.edu.uam.innovacion.modules.certificate.service.CertificadoParticipacionService;
import ni.edu.uam.innovacion.modules.report.dto.ArchivoDescarga;
import org.springframework.http.ContentDisposition;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/admin/certificados")
public class CertificadoParticipacionAdminController {

    private final CertificadoParticipacionService certificadoService;

    public CertificadoParticipacionAdminController(
        CertificadoParticipacionService certificadoService
    ) {
        this.certificadoService = certificadoService;
    }

    @GetMapping("/participacion/{idParticipacion}")
    public ResponseEntity<byte[]> generar(
        @PathVariable Long idParticipacion,
        @RequestParam(defaultValue = "constancia") String tipo
    ) {
        ArchivoDescarga archivo = certificadoService.generar(
            idParticipacion,
            TipoDocumentoParticipacion.fromValue(tipo)
        );
        ContentDisposition disposition = ContentDisposition.attachment()
            .filename(archivo.nombreArchivo(), StandardCharsets.UTF_8)
            .build();

        return ResponseEntity.ok()
            .header(HttpHeaders.CONTENT_DISPOSITION, disposition.toString())
            .contentType(MediaType.APPLICATION_PDF)
            .contentLength(archivo.contenido().length)
            .body(archivo.contenido());
    }
}
