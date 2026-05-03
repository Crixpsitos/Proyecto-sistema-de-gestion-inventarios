package gestion_inventarios.backend.domain.model.inventory;

import java.time.LocalDateTime;
import java.util.UUID;

public class Inventory {
    
    Long id;
    UUID productId;
    Long locationId;
    Integer quantity;
    LocalDateTime createdAt;
    LocalDateTime updatedAt;


    public Inventory(Long id, UUID productId, Long locationId, Integer quantity, LocalDateTime createdAt, LocalDateTime updatedAt) {
        this.id = id;
        this.productId = productId;
        this.locationId = locationId;
        this.quantity = quantity;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
    }

    public Inventory(UUID productId, Long locationId, Integer quantity) {
        this.productId = productId;
        this.locationId = locationId;
        this.quantity = quantity;
        this.createdAt = LocalDateTime.now();
        this.updatedAt = LocalDateTime.now();
    }
    
    public void updateQuantity(Integer quantity) {
        if (quantity < 0) {
            throw new IllegalArgumentException("La cantidad debe ser mayor o igual a 0");
        }
        this.quantity = quantity;
        this.updatedAt = LocalDateTime.now();
    }
    
        public Long getId() {
            return id;
        }
    
    
        public UUID getProductId() {
            return productId;
        }
    
        
    
        public Long getLocationId() {
            return locationId;
        }
    
    
        public Integer getQuantity() {
            return quantity;
        }

        public LocalDateTime getCreatedAt() {
            return createdAt;
        }

        public LocalDateTime getUpdatedAt() {
            return updatedAt;
        }


}
