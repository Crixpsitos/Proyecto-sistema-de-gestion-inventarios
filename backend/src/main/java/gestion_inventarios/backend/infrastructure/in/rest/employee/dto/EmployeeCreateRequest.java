package gestion_inventarios.backend.infrastructure.in.rest.employee.dto;

import gestion_inventarios.backend.domain.model.DocumentType;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public record EmployeeCreateRequest(
    @NotBlank(message = "El nombre es obligatorio")
    String name,

    @NotBlank(message = "El apellido es obligatorio")
    String lastName,

    @NotBlank(message = "El email es obligatorio")
    @Email(message = "El email no es válido")
    String email,

    @NotBlank(message = "La contraseña es obligatoria")
    @Size(min = 8, message = "La contraseña debe tener mínimo 8 caracteres")
    String password,

    String phone,

    @NotNull(message = "El tipo de documento es obligatorio")
    DocumentType documentType,

    @NotBlank(message = "El número de documento es obligatorio")
    String documentNumber,

    @NotBlank(message = "El rol es obligatorio")
    String role
) {}
