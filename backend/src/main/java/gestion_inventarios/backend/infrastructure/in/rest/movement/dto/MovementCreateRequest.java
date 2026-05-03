package gestion_inventarios.backend.infrastructure.in.rest.movement.dto;

import jakarta.validation.constraints.AssertTrue;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;

public record MovementCreateRequest(
    
    @NotBlank(message = "El tipo es obligatorio")
    @Pattern(regexp = "IN|OUT|TRANSFER", message = "El tipo debe ser Entrada, Salida o Transferencia")
    String type,
    
    @NotBlank(message = "El producto es obligatorio")
    String productId,

    @NotNull(message = "La cantidad es obligatoria")
    @Min(value = 1, message = "La cantidad debe ser mayor a cero")
    Integer quantity,

    String sourceId,

    String destinationId,

    String notes
) {
    
    
    @AssertTrue(message = "La bodega origen es obligatoria para Salida y Transferencia")
    public boolean isSourceValid() {
        if (type == null) return true; 
        
        return switch (type) {
            case "OUT", "TRANSFER" -> sourceId != null && !sourceId.isBlank();
            default -> true;
        };
    }

    @AssertTrue(message = "La bodega destino es obligatoria para Entrada y Transferencia")
    public boolean isDestinationValid() {
        if (type == null) return true;
        
        return switch (type) {
            case "IN", "TRANSFER" -> destinationId != null && !destinationId.isBlank();
            default -> true;
        };
    }
}