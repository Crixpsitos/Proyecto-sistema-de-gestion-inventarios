package gestion_inventarios.backend.domain.model.locations;

public final class LocationCode {

    private final String value;

    public LocationCode(String value) {
        if (value == null || value.isBlank()) {
            throw new IllegalArgumentException("Location code must not be blank");
        }
        if (!value.matches("^[A-Z0-9\\-]{2,20}$")) {
            throw new IllegalArgumentException("Location code format is invalid: " + value);
        }
        this.value = value.toUpperCase();
    }

    public String getValue() {
        return value;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof LocationCode)) return false;
        LocationCode that = (LocationCode) o;
        return value.equals(that.value);
    }

    @Override
    public int hashCode() {
        return value.hashCode();
    }

    @Override
    public String toString() {
        return value;
    }
}