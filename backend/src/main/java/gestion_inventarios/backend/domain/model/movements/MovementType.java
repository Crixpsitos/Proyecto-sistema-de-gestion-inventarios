package gestion_inventarios.backend.domain.model.movements;

public enum MovementType {
    IN("Entrada"),
    OUT("Salida"),
    TRANSFER("Transferencia");

    private final String displayName;

    MovementType(String displayName) {
        this.displayName = displayName;
    }

    public String getDisplayName() {
        return displayName;
    }
}
