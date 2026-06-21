package ni.edu.uam.innovacion.common.document;

import java.awt.Color;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDDocumentInformation;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.pdmodel.PDPageContentStream;
import org.apache.pdfbox.pdmodel.common.PDRectangle;
import org.apache.pdfbox.pdmodel.font.PDFont;
import org.apache.pdfbox.pdmodel.font.PDType1Font;
import org.apache.pdfbox.pdmodel.font.Standard14Fonts;
import org.springframework.stereotype.Service;

@Service
public class PdfDocumentService {

    private static final PDFont REGULAR =
        new PDType1Font(Standard14Fonts.FontName.HELVETICA);
    private static final PDFont BOLD =
        new PDType1Font(Standard14Fonts.FontName.HELVETICA_BOLD);
    private static final DateTimeFormatter DATE_FORMAT =
        DateTimeFormatter.ofPattern("dd/MM/yyyy");

    public byte[] crearReporte(
        String titulo,
        String subtitulo,
        List<String> encabezados,
        List<List<String>> filas
    ) {
        try (PDDocument document = new PDDocument()) {
            configurarMetadatos(document, titulo);
            PDRectangle formato = new PDRectangle(
                PDRectangle.A4.getHeight(),
                PDRectangle.A4.getWidth()
            );
            int pagina = 1;
            ReportPage reportPage = nuevaPaginaReporte(
                document,
                formato,
                titulo,
                subtitulo,
                encabezados,
                pagina
            );

            for (int indice = 0; indice < filas.size(); indice++) {
                if (reportPage.y() < 58) {
                    reportPage.stream().close();
                    pagina++;
                    reportPage = nuevaPaginaReporte(
                        document,
                        formato,
                        titulo,
                        subtitulo,
                        encabezados,
                        pagina
                    );
                }
                dibujarFila(
                    reportPage.stream(),
                    formato,
                    filas.get(indice),
                    encabezados.size(),
                    reportPage.y(),
                    indice % 2 == 0
                );
                reportPage = new ReportPage(reportPage.stream(), reportPage.y() - 20);
            }

            reportPage.stream().close();
            return guardar(document);
        } catch (IOException exception) {
            throw new IllegalStateException("No se pudo generar el reporte PDF", exception);
        }
    }

    public byte[] crearConstancia(
        boolean certificado,
        String nombreUsuario,
        String nombreActividad,
        String rolParticipacion,
        LocalDate fechaActividad,
        int puntos,
        String identificadorValidacion
    ) {
        String titulo = certificado
            ? "CERTIFICADO DE PARTICIPACION"
            : "CONSTANCIA DE PARTICIPACION";

        try (PDDocument document = new PDDocument()) {
            configurarMetadatos(document, titulo);
            PDPage page = new PDPage(PDRectangle.A4);
            document.addPage(page);

            try (PDPageContentStream stream = new PDPageContentStream(document, page)) {
                float ancho = page.getMediaBox().getWidth();
                dibujarFondoCertificado(stream, page.getMediaBox());

                dibujarTextoCentrado(stream, BOLD, 12, "UNIVERSIDAD AMERICANA", ancho, 755, 18, 57, 70);
                dibujarTextoCentrado(stream, REGULAR, 9, "SISTEMA DE INNOVACION", ancho, 738, 59, 83, 91);
                dibujarTextoCentrado(stream, BOLD, 24, titulo, ancho, 666, 15, 76, 92);

                stream.setStrokingColor(new Color(214, 154, 45));
                stream.setLineWidth(2);
                stream.moveTo(105, 640);
                stream.lineTo(ancho - 105, 640);
                stream.stroke();

                dibujarTextoCentrado(stream, REGULAR, 12, "Se hace constar que", ancho, 592, 55, 65, 81);
                dibujarTextoCentrado(stream, BOLD, 22, nombreUsuario, ancho, 548, 15, 76, 92);
                dibujarTextoCentrado(
                    stream,
                    REGULAR,
                    12,
                    certificado
                        ? "ha completado satisfactoriamente su participacion en"
                        : "participo en la actividad de innovacion",
                    ancho,
                    505,
                    55,
                    65,
                    81
                );
                dibujarTextoCentrado(stream, BOLD, 17, nombreActividad, ancho, 464, 22, 101, 52);

                float detalleY = 401;
                dibujarEtiquetaValor(stream, "Rol", rolParticipacion, 118, detalleY, 150);
                dibujarEtiquetaValor(
                    stream,
                    "Fecha",
                    fechaActividad == null ? "No registrada" : DATE_FORMAT.format(fechaActividad),
                    350,
                    detalleY,
                    120
                );
                if (certificado) {
                    dibujarEtiquetaValor(stream, "Puntos otorgados", String.valueOf(puntos), 238, 338, 120);
                }

                stream.setStrokingColor(new Color(100, 116, 139));
                stream.setLineWidth(0.8f);
                stream.moveTo(185, 220);
                stream.lineTo(410, 220);
                stream.stroke();
                dibujarTextoCentrado(stream, BOLD, 10, "COORDINACION DE INNOVACION", ancho, 202, 51, 65, 85);

                dibujarTextoCentrado(
                    stream,
                    REGULAR,
                    8,
                    "Identificador de validacion: " + identificadorValidacion,
                    ancho,
                    92,
                    71,
                    85,
                    105
                );
                dibujarTextoCentrado(
                    stream,
                    REGULAR,
                    8,
                    "Emitido el " + DATE_FORMAT.format(LocalDate.now()),
                    ancho,
                    76,
                    100,
                    116,
                    139
                );
            }
            return guardar(document);
        } catch (IOException exception) {
            throw new IllegalStateException("No se pudo generar el documento PDF", exception);
        }
    }

    private ReportPage nuevaPaginaReporte(
        PDDocument document,
        PDRectangle formato,
        String titulo,
        String subtitulo,
        List<String> encabezados,
        int numeroPagina
    ) throws IOException {
        PDPage page = new PDPage(formato);
        document.addPage(page);
        PDPageContentStream stream = new PDPageContentStream(document, page);

        stream.setNonStrokingColor(new Color(15, 118, 110));
        stream.addRect(0, formato.getHeight() - 74, formato.getWidth(), 74);
        stream.fill();
        dibujarTexto(stream, BOLD, 20, titulo, 38, formato.getHeight() - 36, 255, 255, 255);
        dibujarTexto(stream, REGULAR, 9, subtitulo, 38, formato.getHeight() - 55, 204, 251, 241);
        dibujarTexto(
            stream,
            REGULAR,
            8,
            "Pagina " + numeroPagina,
            formato.getWidth() - 76,
            24,
            100,
            116,
            139
        );

        float y = formato.getHeight() - 100;
        dibujarEncabezadoTabla(stream, formato, encabezados, y);
        return new ReportPage(stream, y - 22);
    }

    private void dibujarEncabezadoTabla(
        PDPageContentStream stream,
        PDRectangle formato,
        List<String> encabezados,
        float y
    ) throws IOException {
        float margen = 32;
        float anchoTabla = formato.getWidth() - (margen * 2);
        float anchoColumna = anchoTabla / encabezados.size();

        stream.setNonStrokingColor(new Color(30, 41, 59));
        stream.addRect(margen, y - 4, anchoTabla, 20);
        stream.fill();

        for (int i = 0; i < encabezados.size(); i++) {
            dibujarTexto(
                stream,
                BOLD,
                7.5f,
                ajustarTexto(encabezados.get(i), BOLD, 7.5f, anchoColumna - 8),
                margen + (i * anchoColumna) + 4,
                y + 3,
                255,
                255,
                255
            );
        }
    }

    private void dibujarFila(
        PDPageContentStream stream,
        PDRectangle formato,
        List<String> valores,
        int columnas,
        float y,
        boolean alterna
    ) throws IOException {
        float margen = 32;
        float anchoTabla = formato.getWidth() - (margen * 2);
        float anchoColumna = anchoTabla / columnas;

        if (alterna) {
            stream.setNonStrokingColor(new Color(241, 245, 249));
            stream.addRect(margen, y - 5, anchoTabla, 20);
            stream.fill();
        }

        for (int i = 0; i < columnas; i++) {
            String valor = i < valores.size() ? valores.get(i) : "";
            dibujarTexto(
                stream,
                REGULAR,
                7.2f,
                ajustarTexto(valor, REGULAR, 7.2f, anchoColumna - 8),
                margen + (i * anchoColumna) + 4,
                y + 2,
                30,
                41,
                59
            );
        }
    }

    private void dibujarFondoCertificado(
        PDPageContentStream stream,
        PDRectangle formato
    ) throws IOException {
        stream.setNonStrokingColor(new Color(248, 250, 252));
        stream.addRect(0, 0, formato.getWidth(), formato.getHeight());
        stream.fill();

        stream.setStrokingColor(new Color(15, 118, 110));
        stream.setLineWidth(8);
        stream.addRect(22, 22, formato.getWidth() - 44, formato.getHeight() - 44);
        stream.stroke();

        stream.setStrokingColor(new Color(214, 154, 45));
        stream.setLineWidth(1.5f);
        stream.addRect(31, 31, formato.getWidth() - 62, formato.getHeight() - 62);
        stream.stroke();
    }

    private void dibujarEtiquetaValor(
        PDPageContentStream stream,
        String etiqueta,
        String valor,
        float x,
        float y,
        float anchoMaximo
    ) throws IOException {
        dibujarTexto(stream, BOLD, 9, etiqueta.toUpperCase(), x, y + 24, 15, 118, 110);
        dibujarTexto(stream, REGULAR, 12, ajustarTexto(valor, REGULAR, 12, anchoMaximo), x, y, 30, 41, 59);
    }

    private void dibujarTextoCentrado(
        PDPageContentStream stream,
        PDFont font,
        float tamano,
        String texto,
        float anchoPagina,
        float y,
        int rojo,
        int verde,
        int azul
    ) throws IOException {
        String seguro = ajustarTexto(texto, font, tamano, anchoPagina - 110);
        float anchoTexto = font.getStringWidth(seguro) / 1000 * tamano;
        dibujarTexto(
            stream,
            font,
            tamano,
            seguro,
            Math.max(42, (anchoPagina - anchoTexto) / 2),
            y,
            rojo,
            verde,
            azul
        );
    }

    private void dibujarTexto(
        PDPageContentStream stream,
        PDFont font,
        float tamano,
        String texto,
        float x,
        float y,
        int rojo,
        int verde,
        int azul
    ) throws IOException {
        stream.beginText();
        stream.setFont(font, tamano);
        stream.setNonStrokingColor(new Color(rojo, verde, azul));
        stream.newLineAtOffset(x, y);
        stream.showText(textoSeguro(texto, font));
        stream.endText();
    }

    private String ajustarTexto(
        String valor,
        PDFont font,
        float tamano,
        float anchoMaximo
    ) throws IOException {
        String texto = textoSeguro(valor, font);
        if (font.getStringWidth(texto) / 1000 * tamano <= anchoMaximo) {
            return texto;
        }
        String sufijo = "...";
        while (!texto.isEmpty()
            && font.getStringWidth(texto + sufijo) / 1000 * tamano > anchoMaximo) {
            texto = texto.substring(0, texto.length() - 1);
        }
        return texto + sufijo;
    }

    private String textoSeguro(String valor, PDFont font) throws IOException {
        if (valor == null) {
            return "";
        }
        String limpio = valor.replaceAll("[\\r\\n\\t]+", " ").trim();
        StringBuilder seguro = new StringBuilder();
        for (char caracter : limpio.toCharArray()) {
            try {
                font.encode(String.valueOf(caracter));
                seguro.append(caracter);
            } catch (IllegalArgumentException exception) {
                seguro.append('?');
            }
        }
        return seguro.toString();
    }

    private void configurarMetadatos(PDDocument document, String titulo) {
        PDDocumentInformation informacion = new PDDocumentInformation();
        informacion.setTitle(titulo);
        informacion.setAuthor("Sistema de Innovacion UAM");
        informacion.setCreator("Backend Sistema Innovacion");
        document.setDocumentInformation(informacion);
    }

    private byte[] guardar(PDDocument document) throws IOException {
        try (ByteArrayOutputStream salida = new ByteArrayOutputStream()) {
            document.save(salida);
            return salida.toByteArray();
        }
    }

    private record ReportPage(PDPageContentStream stream, float y) {
    }
}
