package ni.edu.uam.innovacion.modules.points.entity;

import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;
import ni.edu.uam.innovacion.modules.points.enums.EstadoPuntos;

@Converter(autoApply = false)
public class EstadoPuntosConverter implements AttributeConverter<EstadoPuntos, String> {

    @Override
    public String convertToDatabaseColumn(EstadoPuntos attribute) {
        return attribute == null ? null : attribute.getValor();
    }

    @Override
    public EstadoPuntos convertToEntityAttribute(String dbData) {
        return EstadoPuntos.fromValue(dbData);
    }
}
