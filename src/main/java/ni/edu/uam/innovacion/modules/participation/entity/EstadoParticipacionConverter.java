package ni.edu.uam.innovacion.modules.participation.entity;

import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;
import ni.edu.uam.innovacion.modules.participation.enums.EstadoParticipacion;

@Converter
public class EstadoParticipacionConverter implements AttributeConverter<EstadoParticipacion, String> {

    @Override
    public String convertToDatabaseColumn(EstadoParticipacion estadoParticipacion) {
        return estadoParticipacion == null ? null : estadoParticipacion.getValor();
    }

    @Override
    public EstadoParticipacion convertToEntityAttribute(String valor) {
        return EstadoParticipacion.fromValue(valor);
    }
}