package gestion_inventarios.backend.infrastructure.in.rest.auth.dto;

public record RefreshTokenResponse(String accessToken, String refreshToken, long expiresIn) {

    public static RefreshTokenResponse of(String accessToken, String refreshToken, long expiresIn) {
        return new RefreshTokenResponse(accessToken, refreshToken, expiresIn);
    }
}