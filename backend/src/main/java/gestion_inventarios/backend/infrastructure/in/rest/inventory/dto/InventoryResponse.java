package gestion_inventarios.backend.infrastructure.in.rest.inventory.dto;

import java.time.LocalDateTime;
import java.util.UUID;

import gestion_inventarios.backend.domain.model.inventory.Inventory;

public record InventoryResponse(
    Long id,
    UUID productId,
    String productSku,
    String productName,
    Long locationId,
    String locationCode,
    String locationName,
    Integer quantity,
    LocalDateTime createdAt,
    LocalDateTime updatedAt
) {
    public static InventoryResponse from(
        Inventory inventory,
        String productSku,
        String productName,
        String locationCode,
        String locationName
    ) {
        return new InventoryResponse(
            inventory.getId(),
            inventory.getProductId(),
            productSku,
            productName,
            inventory.getLocationId(),
            locationCode,
            locationName,
            inventory.getQuantity(),
            inventory.getCreatedAt(),
            inventory.getUpdatedAt()
        );
    }
}
