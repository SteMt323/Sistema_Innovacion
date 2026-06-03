package ni.edu.uam.innovacion.modules.auth.dto;

public record LoginResponse(
    String accessToken,
    String tokenType,
    long expiresIn,
    AuthenticatedUserResponse usuario
) {
}
