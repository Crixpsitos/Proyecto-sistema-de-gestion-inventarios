package gestion_inventarios.backend.infrastructure.in.rest.employee.dto;

import jakarta.validation.constraints.NotBlank;

public record EmployeePermissionsRequest(
    @NotBlank(message = "El rol es obligatorio")
    String role
) {}
