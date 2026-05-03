package gestion_inventarios.backend.domain.model.movements;

import java.time.LocalDateTime;

import gestion_inventarios.backend.domain.model.locations.Location;
import gestion_inventarios.backend.domain.model.products.Product;

public class Movement {

    private final Long id;
    private final MovementType type;
    private final Product product;
    private final Location source;       
    private final Location destination;   
    private final Integer quantity;
    private final String notes;
    private final String createdBy;
    private final LocalDateTime createdAt;

    public Movement(Long id, MovementType type, Product product, Location source,
                    Location destination, Integer quantity, String notes,
                    String createdBy, LocalDateTime createdAt) {
        this.id = id;
        this.type = type;
        this.product = product;
        this.source = source;
        this.destination = destination;
        this.quantity = quantity;
        this.notes = notes;
        this.createdBy = createdBy;
        this.createdAt = createdAt;
    }

    public Movement(MovementType type, Product product, Location source,
                    Location destination, Integer quantity, String notes,
                    String createdBy) {
        this(null, type, product, source, destination, quantity, notes, createdBy, null);
    }

    public Long getId() { return id; }
    public MovementType getType() { return type; }
    public Product getProduct() { return product; }
    public Location getSource() { return source; }
    public Location getDestination() { return destination; }
    public Integer getQuantity() { return quantity; }
    public String getNotes() { return notes; }
    public String getCreatedBy() { return createdBy; }
    public LocalDateTime getCreatedAt() { return createdAt; }
}