package gestion_inventarios.backend.domain.model.locations;

public enum LocationType {

    WAREHOUSE("Bodega"),
    STORE("Tienda"),
    VIRTUAL("Virtual");

    private final String displayName;

    LocationType(String displayName) {
        this.displayName = displayName;
    }

    public String getDisplayName() {
        return displayName;
    }
}