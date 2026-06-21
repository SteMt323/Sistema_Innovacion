package ni.edu.uam.innovacion.modules.project.entity;

import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;
import ni.edu.uam.innovacion.modules.project.enums.EstadoProyectoPIA;

/**
 * Converter encargado de transformar el enum EstadoProyectoPIA
 * al valor que se guarda en base de datos y viceversa.
 *
 * Esto permite guardar valores como:
 * - activo
 * - pausado
 * - finalizado
 * - retirado
 */
@Converter
public class EstadoProyectoPIAConverter implements AttributeConverter<EstadoProyectoPIA, String> {

    @Override
    public String convertToDatabaseColumn(EstadoProyectoPIA estadoProyectoPIA) {
        return estadoProyectoPIA == null ? null : estadoProyectoPIA.getValor();
    }

    @Override
    public EstadoProyectoPIA convertToEntityAttribute(String valor) {
        return EstadoProyectoPIA.fromValue(valor);
    }
}