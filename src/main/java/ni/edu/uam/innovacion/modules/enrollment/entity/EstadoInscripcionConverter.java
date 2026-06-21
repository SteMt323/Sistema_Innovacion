package ni.edu.uam.innovacion.modules.enrollment.entity;

import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;
import ni.edu.uam.innovacion.modules.enrollment.enums.EstadoInscripcion;

@Converter
public class EstadoInscripcionConverter implements AttributeConverter<EstadoInscripcion, String> {

    @Override
    public String convertToDatabaseColumn(EstadoInscripcion estadoInscripcion) {
        return estadoInscripcion == null ? null : estadoInscripcion.getValor();
    }

    @Override
    public EstadoInscripcion convertToEntityAttribute(String valor) {
        return EstadoInscripcion.fromValue(valor);
    }
}