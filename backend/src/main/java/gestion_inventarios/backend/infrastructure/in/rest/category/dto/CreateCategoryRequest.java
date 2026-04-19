package gestion_inventarios.backend.infrastructure.in.rest.category.dto;

import gestion_inventarios.backend.shared.validation.OptionalSize;
import jakarta.validation.constraints.NotEmpty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CreateCategoryRequest {

    @NotEmpty(message = "El nombre de la categoría es obligatorio")
    private String name;

    @OptionalSize(min = 10, message = "La descripción debe tener al menos 10 caracteres si se proporciona")
    private String description;
}
