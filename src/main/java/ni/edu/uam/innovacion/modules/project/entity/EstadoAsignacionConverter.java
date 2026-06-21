package ni.edu.uam.innovacion.modules.project.entity;

import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;
import ni.edu.uam.innovacion.modules.project.enums.EstadoAsignacion;

/**
 * Converter JPA para guardar EstadoAsignacion como texto en la base de datos.
 *
 * En Java se trabaja con:
 * EstadoAsignacion.ACTIVA
 *
 * En la base de datos se guarda como:
 * activa
 */
@Converter
public class EstadoAsignacionConverter implements AttributeConverter<EstadoAsignacion, String> {

    @Override
    public String convertToDatabaseColumn(EstadoAsignacion estadoAsignacion) {
        return estadoAsignacion == null ? null : estadoAsignacion.getValor();
    }

    @Override
    public EstadoAsignacion convertToEntityAttribute(String valor) {
        return EstadoAsignacion.fromValue(valor);
    }
}