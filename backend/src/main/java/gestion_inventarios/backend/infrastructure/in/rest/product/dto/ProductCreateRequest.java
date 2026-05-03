package gestion_inventarios.backend.infrastructure.in.rest.product.dto;

import java.math.BigDecimal;

import org.springframework.web.multipart.MultipartFile;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PositiveOrZero;

public record ProductCreateRequest(

    @NotBlank(message = "El SKU es obligatorio")
    String sku,
    
    @NotBlank(message = "El nombre es obligatorio")
    String name,
    
    String description,
    
    @NotNull(message = "El precio es obligatorio")
    @PositiveOrZero(message = "El precio no puede ser negativo")
    BigDecimal price,
    
    @NotNull(message = "La cantidad es obligatoria")
    @Min(value = 0, message = "La cantidad mínima es 0")
    Integer quantity,
    
    @NotBlank(message = "La marca es obligatoria")
    String brand,
    
    @NotBlank(message = "El modelo es obligatorio")
    String model,
    
    @NotNull(message = "La categoría es obligatoria")
    Long category,
    
    String color,
    String size,
    
    @NotBlank(message = "La moneda es obligatoria")
    String currency,

    MultipartFile image
) {}