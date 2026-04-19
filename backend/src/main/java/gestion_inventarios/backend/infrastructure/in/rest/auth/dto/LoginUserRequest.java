package gestion_inventarios.backend.infrastructure.in.rest.auth.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotEmpty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class LoginUserRequest {
    @NotEmpty(message = "El email no puede estar vacío")
    @Email(message = "El email no es válido")
    private String email;
    @NotEmpty(message = "La contraseña no puede estar vacía")
    private String password;
}
