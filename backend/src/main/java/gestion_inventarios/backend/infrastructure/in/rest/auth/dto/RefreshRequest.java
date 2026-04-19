package gestion_inventarios.backend.infrastructure.in.rest.auth.dto;

import jakarta.validation.constraints.NotEmpty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class RefreshRequest {

    @NotEmpty(message = "El refresh token es obligatorio")
    private String refreshToken;
}
