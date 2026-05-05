package gestion_inventarios.backend.infrastructure.in.rest.auth.rest;

import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import gestion_inventarios.backend.application.ports.in.auth.AuthUseCase;
import gestion_inventarios.backend.infrastructure.in.rest.auth.dto.LoginResponse;
import gestion_inventarios.backend.infrastructure.in.rest.auth.dto.LoginUserRequest;
import gestion_inventarios.backend.infrastructure.in.rest.auth.dto.RefreshRequest;
import gestion_inventarios.backend.infrastructure.in.rest.auth.dto.RefreshTokenResponse;
import gestion_inventarios.backend.infrastructure.in.rest.auth.dto.UserProfileResponse;
import gestion_inventarios.backend.infrastructure.in.security.UserDetailsAdapter;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthUseCase authUseCase;

    @PostMapping("/login")
    public ResponseEntity<LoginResponse> login(@RequestBody @Valid LoginUserRequest request) {
        return ResponseEntity.ok(authUseCase.login(request.getEmail(), request.getPassword()));
    }

    @PostMapping("/refresh")
    public ResponseEntity<RefreshTokenResponse> refresh(@RequestBody @Valid RefreshRequest request) {
        return ResponseEntity.ok(authUseCase.refreshToken(request.getRefreshToken()));
    }

    @GetMapping("/me")
    public ResponseEntity<UserProfileResponse> me(@AuthenticationPrincipal UserDetailsAdapter userDetails) {
        return ResponseEntity.ok(UserProfileResponse.from(userDetails.getDomainUser()));
    }
}
