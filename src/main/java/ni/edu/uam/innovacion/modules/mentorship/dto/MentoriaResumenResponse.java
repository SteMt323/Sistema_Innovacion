package ni.edu.uam.innovacion.modules.mentorship.dto;

public record MentoriaResumenResponse(
    long mentoresRegistrados,
    long mentoresActivos,
    long mentoriasActivas,
    long mentoriasInactivas,
    long mentoriasArchivadas,
    long actividadesConMentoriasActivas
) {
}
