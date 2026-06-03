package ni.edu.uam.innovacion.modules.activity.entity;

import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;
import ni.edu.uam.innovacion.modules.activity.enums.ModalidadActividad;

@Converter
public class ModalidadActividadConverter implements AttributeConverter<ModalidadActividad, String> {

    @Override
    public String convertToDatabaseColumn(ModalidadActividad modalidadActividad) {
        return modalidadActividad == null ? null : modalidadActividad.getValor();
    }

    @Override
    public ModalidadActividad convertToEntityAttribute(String valor) {
        return ModalidadActividad.fromValue(valor);
    }
}
