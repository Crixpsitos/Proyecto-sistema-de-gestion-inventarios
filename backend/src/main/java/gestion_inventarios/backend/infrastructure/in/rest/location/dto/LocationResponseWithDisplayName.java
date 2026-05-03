package gestion_inventarios.backend.infrastructure.in.rest.location.dto;
import gestion_inventarios.backend.domain.model.locations.Location;

public record LocationResponseWithDisplayName(
        Long id,
        String name,
        String code,
        String type,
        String address,
        boolean active,
        Long movementCount,
        Long currentStock,
        String createdAt,
        String updatedAt) {
    public static LocationResponseWithDisplayName from(Location location, Long movementCount, Long currentStock) {
        return new LocationResponseWithDisplayName(location.getId(),
                location.getName(),
                location.getCode().getValue(),
                location.getType().getDisplayName(),
                location.getAddress(),
                location.isActive(),
                movementCount,
                currentStock,
                location.getCreatedAt().toString(),
                location.getUpdatedAt().toString());
    }
}
