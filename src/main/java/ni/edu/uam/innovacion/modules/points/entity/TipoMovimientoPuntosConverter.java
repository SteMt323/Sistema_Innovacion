package ni.edu.uam.innovacion.modules.points.entity;

import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;
import ni.edu.uam.innovacion.modules.points.enums.TipoMovimientoPuntos;

@Converter(autoApply = false)
public class TipoMovimientoPuntosConverter
        implements AttributeConverter<TipoMovimientoPuntos, String> {

    @Override
    public String convertToDatabaseColumn(TipoMovimientoPuntos attribute) {
        return attribute == null ? null : attribute.getValor();
    }

    @Override
    public TipoMovimientoPuntos convertToEntityAttribute(String dbData) {
        return TipoMovimientoPuntos.fromValue(dbData);
    }
}
