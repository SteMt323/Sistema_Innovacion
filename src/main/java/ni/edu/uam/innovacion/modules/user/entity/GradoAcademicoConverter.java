package ni.edu.uam.innovacion.modules.user.entity;

import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;
import ni.edu.uam.innovacion.modules.user.enums.GradoAcademico;

@Converter
public class GradoAcademicoConverter implements AttributeConverter<GradoAcademico, String> {

    @Override
    public String convertToDatabaseColumn(GradoAcademico gradoAcademico) {
        return gradoAcademico == null ? null : gradoAcademico.getValor();
    }

    @Override
    public GradoAcademico convertToEntityAttribute(String valor) {
        return GradoAcademico.fromValue(valor);
    }
}
