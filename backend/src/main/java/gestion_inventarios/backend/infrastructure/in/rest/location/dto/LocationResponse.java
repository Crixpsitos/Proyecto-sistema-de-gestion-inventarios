package gestion_inventarios.backend.infrastructure.in.rest.location.dto;

import gestion_inventarios.backend.domain.model.locations.Location;

public record LocationResponse(
    Long id,
    String name,
    String code,
    String type,
    String address,
    boolean active,
    Long movementCount,
    Long currentStock,
    String createdAt,
    String updatedAt
) {
    public static LocationResponse from(Location location, Long movementCount, Long currentStock) {
        return new LocationResponse(
                location.getId(),
                location.getName(),
                location.getCode().getValue(),
                location.getType().name(),
                location.getAddress(),
                location.isActive(),
                movementCount,
                currentStock,
                location.getCreatedAt().toString(),
                location.getUpdatedAt().toString()
        );
    }
}
