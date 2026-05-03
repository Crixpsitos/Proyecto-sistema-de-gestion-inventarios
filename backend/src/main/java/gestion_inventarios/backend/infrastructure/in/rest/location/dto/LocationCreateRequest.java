package gestion_inventarios.backend.infrastructure.in.rest.location.dto;

import gestion_inventarios.backend.domain.model.locations.LocationType;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record LocationCreateRequest(

    @NotBlank(message = "El nombre es obligatorio")
     String name,
    
    @NotBlank(message = "El código es obligatorio")
     String code,
    
    @NotNull(message = "El tipo es obligatorio")
     LocationType type,
    
     String address,
    
    @NotNull(message = "El estado es obligatorio")
     Boolean active
) {}
