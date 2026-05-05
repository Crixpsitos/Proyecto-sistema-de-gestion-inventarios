package gestion_inventarios.backend.infrastructure.in.rest.employee.dto;

import gestion_inventarios.backend.domain.model.user.DocumentType;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record EmployeeUpdateRequest(
    @NotBlank(message = "El nombre es obligatorio")
    String name,

    @NotBlank(message = "El apellido es obligatorio")
    String lastName,

    @NotBlank(message = "El email es obligatorio")
    @Email(message = "El email no es válido")
    String email,

    String phone,

    @NotNull(message = "El tipo de documento es obligatorio")
    DocumentType documentType,

    @NotBlank(message = "El número de documento es obligatorio")
    String documentNumber
) {}
