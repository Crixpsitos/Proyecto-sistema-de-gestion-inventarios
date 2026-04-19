package gestion_inventarios.backend.infrastructure.in.security;

import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Service;

import gestion_inventarios.backend.application.ports.in.AuthUseCase;
import gestion_inventarios.backend.application.ports.in.FindUserCase;
import gestion_inventarios.backend.infrastructure.in.rest.auth.dto.LoginResponse;
import gestion_inventarios.backend.infrastructure.in.rest.auth.dto.RefreshTokenResponse;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class AuthService implements AuthUseCase {

    private final AuthenticationManager authenticationManager;
    private final JwtService jwtService;
    private final FindUserCase findUserCase;
    private final UserDetailsService userDetailsService;

    @Override
    public LoginResponse login(String email, String password) {
        authenticationManager.authenticate(
            new UsernamePasswordAuthenticationToken(email, password)
        );

        

        UserDetails userDetails = new UserDetailsAdapter(findUserCase.findByEmail(email));
        String accessToken = jwtService.generateToken(userDetails);
        String refreshToken = jwtService.generateRefreshToken(userDetails);

        return LoginResponse.of(accessToken, refreshToken, jwtService.getExpirationSeconds());
    }

    @Override
    public RefreshTokenResponse refreshToken(String refreshToken) {
        String email = jwtService.extractUsername(refreshToken);
        UserDetails userDetails = userDetailsService.loadUserByUsername(email);

        if (!jwtService.isRefreshTokenValid(refreshToken, userDetails)) {
            throw new IllegalArgumentException("Refresh token inválido o expirado");
        }

        String newAccessToken = jwtService.generateToken(userDetails);
        String newRefreshToken = jwtService.generateRefreshToken(userDetails);

        return RefreshTokenResponse.of(newAccessToken, newRefreshToken, jwtService.getExpirationSeconds());
    }


}
