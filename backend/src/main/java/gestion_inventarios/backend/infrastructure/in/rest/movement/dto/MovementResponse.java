package gestion_inventarios.backend.infrastructure.in.rest.movement.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

import gestion_inventarios.backend.domain.model.locations.Location;
import gestion_inventarios.backend.domain.model.movements.Movement;
import gestion_inventarios.backend.domain.model.products.Product;

public record MovementResponse(Long id, @JsonProperty("type") MovementTypeDTO type, Product product, Location source, Location destination, Integer quantity, String notes, String createdBy, String createdAt) {   
    public static MovementResponse from(Movement movement) {
        return new MovementResponse(
            movement.getId(),
            MovementTypeDTO.from(movement.getType().getDisplayName(), movement.getType().name()),
            movement.getProduct(),
            movement.getSource(),
            movement.getDestination(),
            movement.getQuantity(),
            movement.getNotes(),
            movement.getCreatedBy(),
            movement.getCreatedAt().toString()
        );
    }
}
