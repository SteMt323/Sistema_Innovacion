package ni.edu.uam.innovacion.modules.activity.entity;

import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;
import ni.edu.uam.innovacion.modules.activity.enums.EstadoActividad;

@Converter
public class EstadoActividadConverter implements AttributeConverter<EstadoActividad, String> {

    @Override
    public String convertToDatabaseColumn(EstadoActividad estadoActividad) {
        return estadoActividad == null ? null : estadoActividad.getValor();
    }

    @Override
    public EstadoActividad convertToEntityAttribute(String valor) {
        return EstadoActividad.fromValue(valor);
    }
}
