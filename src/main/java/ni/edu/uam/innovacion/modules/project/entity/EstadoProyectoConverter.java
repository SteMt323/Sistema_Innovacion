package ni.edu.uam.innovacion.modules.project.entity;

import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;
import ni.edu.uam.innovacion.modules.project.enums.EstadoProyecto;

/**
 * Converter encargado de transformar el enum EstadoProyecto
 * al valor que se guarda en base de datos y viceversa.
 *
 * Esto permite guardar valores como:
 * - activo
 * - pausado
 * - finalizado
 * - cancelado
 * - archivado
 *
 * En lugar de guardar directamente el nombre del enum en mayúsculas.
 */
@Converter
public class EstadoProyectoConverter implements AttributeConverter<EstadoProyecto, String> {

    @Override
    public String convertToDatabaseColumn(EstadoProyecto estadoProyecto) {
        return estadoProyecto == null ? null : estadoProyecto.getValor();
    }

    @Override
    public EstadoProyecto convertToEntityAttribute(String valor) {
        return EstadoProyecto.fromValue(valor);
    }
}