package gestion_inventarios.backend.infrastructure.out.persistence.inventory.mapper;
import org.springframework.stereotype.Component;

import gestion_inventarios.backend.domain.model.inventory.Inventory;
import gestion_inventarios.backend.infrastructure.out.persistence.inventory.entity.InventoryEntity;

@Component
public class InventoryMapper {

    public Inventory toDomain(InventoryEntity entity) {
        return new Inventory(
            entity.getId(),
            entity.getProductId(),
            entity.getLocationId(),
            entity.getQuantity(),
            entity.getCreatedAt(),
            entity.getUpdatedAt()
        );
    }

    public InventoryEntity toEntity(Inventory domain) {
        InventoryEntity entity = new InventoryEntity(
            domain.getId(),
            domain.getProductId(),
            domain.getLocationId(),
            domain.getQuantity(),
            domain.getCreatedAt(),
            domain.getUpdatedAt()
        );
        return entity;
    }
}