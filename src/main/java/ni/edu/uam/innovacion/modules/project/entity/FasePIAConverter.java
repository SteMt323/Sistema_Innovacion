package ni.edu.uam.innovacion.modules.project.entity;

import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;
import ni.edu.uam.innovacion.modules.project.enums.FasePIA;

/**
 * Converter encargado de transformar el enum FasePIA
 * al valor que se guarda en base de datos y viceversa.
 *
 * Esto permite guardar valores como:
 * - prospecto
 * - preincubacion
 * - incubacion
 * - aceleracion
 * - seguimiento
 * - graduado
 */
@Converter
public class FasePIAConverter implements AttributeConverter<FasePIA, String> {

    @Override
    public String convertToDatabaseColumn(FasePIA fasePIA) {
        return fasePIA == null ? null : fasePIA.getValor();
    }

    @Override
    public FasePIA convertToEntityAttribute(String valor) {
        return FasePIA.fromValue(valor);
    }
}