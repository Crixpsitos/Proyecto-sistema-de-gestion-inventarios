package gestion_inventarios.backend.application.ports.in;

import gestion_inventarios.backend.infrastructure.in.rest.auth.dto.LoginResponse;
import gestion_inventarios.backend.infrastructure.in.rest.auth.dto.RefreshTokenResponse;

public interface AuthUseCase {
    LoginResponse login(String email, String password);
    RefreshTokenResponse refreshToken(String refreshToken);

}
