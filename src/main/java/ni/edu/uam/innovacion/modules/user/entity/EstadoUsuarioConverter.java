package ni.edu.uam.innovacion.modules.user.entity;

import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;
import ni.edu.uam.innovacion.modules.user.enums.EstadoUsuario;

@Converter
public class EstadoUsuarioConverter implements AttributeConverter<EstadoUsuario, String> {

    @Override
    public String convertToDatabaseColumn(EstadoUsuario estadoUsuario) {
        return estadoUsuario == null ? null : estadoUsuario.getValor();
    }

    @Override
    public EstadoUsuario convertToEntityAttribute(String valor) {
        return EstadoUsuario.fromValue(valor);
    }
}
