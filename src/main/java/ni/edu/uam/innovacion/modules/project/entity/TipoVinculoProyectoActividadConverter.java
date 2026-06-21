package ni.edu.uam.innovacion.modules.project.entity;

import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;
import ni.edu.uam.innovacion.modules.project.enums.TipoVinculoProyectoActividad;

/**
 * Converter encargado de transformar el enum TipoVinculoProyectoActividad
 * al valor que se guarda en base de datos y viceversa.
 *
 * Esto permite guardar valores como:
 * - origen
 * - seguimiento
 * - presentacion
 * - formacion
 * - concurso
 * - mentoria
 * - otro
 */
@Converter
public class TipoVinculoProyectoActividadConverter
        implements AttributeConverter<TipoVinculoProyectoActividad, String> {

    @Override
    public String convertToDatabaseColumn(TipoVinculoProyectoActividad tipoVinculo) {
        return tipoVinculo == null ? null : tipoVinculo.getValor();
    }

    @Override
    public TipoVinculoProyectoActividad convertToEntityAttribute(String valor) {
        return TipoVinculoProyectoActividad.fromValue(valor);
    }
}