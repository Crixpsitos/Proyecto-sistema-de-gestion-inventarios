package gestion_inventarios.backend.infrastructure.in.rest.auth.dto;

public record LoginResponse(String accessToken, String refreshToken, long expiresIn) {

    public static LoginResponse of(String accessToken, String refreshToken, long expiresIn) {
        return new LoginResponse(accessToken, refreshToken, expiresIn);
    }
}
