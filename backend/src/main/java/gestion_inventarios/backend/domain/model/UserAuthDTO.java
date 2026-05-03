package gestion_inventarios.backend.domain.model;

public record UserAuthDTO(
    Long id,
    String email,
    String passwordHash,
    Role role
) {}
