package gestion_inventarios.backend.infrastructure.in.rest.employee.dto;

import java.time.LocalDateTime;

import gestion_inventarios.backend.domain.model.DocumentType;
import gestion_inventarios.backend.domain.model.User;

public record EmployeeResponse(
    Long id,
    String name,
    String lastName,
    String email,
    String phone,
    DocumentType documentType,
    String documentNumber,
    boolean enabled,
    String role,
    LocalDateTime createdAt,
    LocalDateTime updatedAt
) {
    public static EmployeeResponse from(User user) {
        return new EmployeeResponse(
            user.getId(),
            user.getName(),
            user.getLastName(),
            user.getEmail(),
            user.getPhone(),
            user.getDocumentIdentity() == null ? null : user.getDocumentIdentity().getDocumentType(),
            user.getDocumentIdentity() == null ? null : user.getDocumentIdentity().getDocumentNumber(),
            user.isEnabled(),
            user.getRole().getName(),
            user.getCreatedAt(),
            user.getUpdatedAt()
        );
    }
}
