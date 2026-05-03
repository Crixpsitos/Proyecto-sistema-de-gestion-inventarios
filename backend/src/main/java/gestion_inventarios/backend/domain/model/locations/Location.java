package gestion_inventarios.backend.domain.model.locations;

import java.time.LocalDateTime;

public class Location {
    private final Long id;
    private String name;
    private final LocationCode code;
    private LocationType type;
    private String address;
    private boolean active;
    private final LocalDateTime createdAt;
    private LocalDateTime updatedAt;



    public Location(String name, LocationCode code, LocationType type, String address, boolean active, LocalDateTime createdAt, LocalDateTime updatedAt) {
        this.id = null;
        setName(name);
        this.code = code;
        this.type = type;
        this.address = address;
        this.active = active;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
    }

    public Location(Long id, String name, LocationCode code, LocationType type, String address, boolean active, LocalDateTime createdAt, LocalDateTime updatedAt) {
        this.id = id;
        setName(name);
        this.code = code;
        this.type = type;
        this.address = address;
        this.active = active;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
    }




       public void update(String name, LocationType type, String address) {
        setName(name);
        this.type = type;
        this.address = address;
        this.updatedAt = LocalDateTime.now();
    }

    public void deactivate() {
        if (!this.active) {
            throw new IllegalStateException("Location is already inactive");
        }
        this.active = false;
        this.updatedAt = LocalDateTime.now();
    }

    public void activate() {
        if (this.active) {
            throw new IllegalStateException("Location is already active");
        }
        this.active = true;
        this.updatedAt = LocalDateTime.now();
    }

    private void setName(String name) {
        if (name == null || name.isBlank()) {
            throw new IllegalArgumentException("Location name must not be blank");
        }
        this.name = name;
    }

    public Long getId() { return id; }
    public String getName() { return name; }
    public LocationCode getCode() { return code; }
    public LocationType getType() { return type; }
    public String getAddress() { return address; }
    public boolean isActive() { return active; }
    public LocalDateTime getCreatedAt() { return createdAt; }
    public LocalDateTime getUpdatedAt() { return updatedAt; }
}




