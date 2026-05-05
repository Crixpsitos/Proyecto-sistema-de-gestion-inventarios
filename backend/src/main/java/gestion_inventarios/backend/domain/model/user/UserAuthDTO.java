package gestion_inventarios.backend.domain.model.user;

public record UserAuthDTO(
    Long id,
    String email,
    String passwordHash,
    Role role
) {}
