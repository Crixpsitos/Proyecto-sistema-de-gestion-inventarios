package gestion_inventarios.backend.infrastructure.in.rest.auth.dto;

import java.time.LocalDateTime;
import java.util.Set;
import java.util.stream.Collectors;

import gestion_inventarios.backend.domain.model.User;

public record UserProfileResponse(
    Long id,
    String name,
    String lastName,
    String email,
    String phone,
    String role,
    Set<String> permissions,
    LocalDateTime createdAt
) {
    public static UserProfileResponse from(User user) {
        Set<String> allPermissions = user.getRole().getPermissions().stream()
            .map(p -> p.getName())
            .collect(Collectors.toSet());

        user.getExtraPermissions().stream()
            .map(p -> p.getName())
            .forEach(allPermissions::add);

        user.getDeniedPermissions().stream()
            .map(p -> p.getName())
            .forEach(allPermissions::remove);

        return new UserProfileResponse(
            user.getId(),
            user.getName(),
            user.getLastName(),
            user.getEmail(),
            user.getPhone(),
            user.getRole().getName(),
            allPermissions,
            user.getCreatedAt()
        );
    }
}
